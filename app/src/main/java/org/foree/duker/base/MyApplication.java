package org.foree.duker.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.preference.PreferenceManager;

import org.foree.duker.db.RssDao;
import org.foree.duker.rssinfo.RssFeedInfo;
import org.foree.duker.ui.fragment.NavigationDrawerFragment;
import org.foree.duker.ui.activity.SettingsActivity;
import org.foree.duker.utils.FileUtils;
import org.foree.duker.utils.LogUtils;
import org.foree.duker.utils.NetworkUtils;
import com.rssreader.foree.rssreader.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by foree on 3/7/15.
 * 包含自己app的一些信息：
 * 1）当前网络状况
 * 2）应用程序目录和缓存目录
 * 3)Sdcard路径
 * <p/>
 * 以及启动时需要做的初始化
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private Context mContext;
    public static boolean mFirstRun;

    /**
     * 数据库信息
     */
    //数据库名称
    public static final String dbName = "rss.db";
    //存放用户个人订阅信息的表
    public static final String userFeedTable = "rss";
    //存放所有订阅信息的表
    public static final String allFeedTable = "feedlist";
    //应用程序的数据库版本
    public static int myDataBaseVersion = 2;
    //数据库的存放路径
    public static String myDataBasePath;

    /**
     * Sdcard信息
     */
    //Sdcard的路径
    public static final String SdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    //Sdcard的状态（用于判断是否挂载）
    public static final String SdcardState = Environment.getExternalStorageState();

    /**
     * 应用程序信息
     */
    //应用程序名称
    public static String myApplicationName;
    //应用程序sdcard目录名
    public static String myApplicationDir = "Duker";
    //应用程序包名
    public static String myApplicationPackageName;
    //应用程序版本名称
    public static String myVersionName;
    //应用程序版本序号(应用程序用来判断是否升级的,例如:17)
    public static int myVersionCode;
    //应用程序版本号(开发者自定义,例如:1.7.3
    public static String myApplicationVersion;
    //应用程序的网络状态
    public static int mNetworkState = NetworkUtils.NETWORK_NONE;
    //应用程序的sdcard的目录路径
    public static String mySdcardDataDir;
    //应用程序的缓存目录路径
    public static String mySdcardCacheDir;
    //应用程序来源的缓存目录
    public static String MYSDCARDSOURCEDIR;
    //程序当前运行的最新日期
    public static String myDate;

    public MyApplication(Activity context) {
        mContext = context;
    }

    //初始化环境(loading...时候使用)
    public void initEnv() {

        //初始化log打印级别,默认为VERBOSE
        //LogUtils.logLevel = Log.INFO;

        //初始化应用程序名
        myApplicationName = mContext.getString(R.string.app_name);
        //初始化应用程序的包名
        myApplicationPackageName = mContext.getPackageName();
        //判断当前网络状况
        mNetworkState = NetworkUtils.getNetworkState(mContext);
        //初始化当前日期
        myDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(new Date());
        // Log.v(TAG, myDate);

        //如果当前Sdcard已经挂载，应用程序目录与缓存目录是否建立完成
        if (SdcardState.equals(Environment.MEDIA_MOUNTED)) {
            //应用程序目录
            File myDateDir = new File(SdcardPath + "/" + myApplicationDir + "/");
            if (!myDateDir.exists())
                if (!myDateDir.mkdir()) {
                    LogUtils.e(TAG, "创建应用程序目录失败");
                }
            mySdcardDataDir = SdcardPath + "/" + myApplicationDir;
            //缓存目录
            File myCacheDir = new File(mySdcardDataDir + "/" + "cache/");
            if (!myCacheDir.exists())
                if (!myCacheDir.mkdir()) {
                    LogUtils.e(TAG, "创建缓存目录失败");
                }
            mySdcardCacheDir = mySdcardDataDir + "/" + "cache";
            //source目录
            File mySourceDir = new File(mySdcardDataDir + File.separator + "source/");
            if (!mySourceDir.exists())
                if (!mySourceDir.mkdir()) {
                    LogUtils.e(TAG, "创建source目录失败");
                }
            MYSDCARDSOURCEDIR = mySdcardDataDir + File.separator + "source";
        }

        //初始化数据库文件所在的路径
        myDataBasePath = mContext.getFilesDir().getPath() + "/.." + File.separator + "databases";

        /**
         * if the application is first run, you can init opml, else skip it
         */

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        mFirstRun = sp.getBoolean(SettingsActivity.KEY_FIRST_RUN, true);
        if (mFirstRun) {
            //copy opml file and parse it
            for (int i = 111111; i < 111116; i++) {
                try {

                    InputStream in = mContext.getAssets().open(i + "");
                    String filePath = MyApplication.MYSDCARDSOURCEDIR + File.separator + i;
                    FileUtils.copyFile(in, filePath);
                    in.close();

                    //start parse opml file
                    FileUtils.parseOpml(mContext);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            sp.edit().putBoolean(SettingsActivity.KEY_FIRST_RUN, false).apply();
        }

        //获取当前应用程序的版本号和版本名称
        initApplicationVersionInfo(mContext);

        if (LogUtils.isCompilerLog) LogUtils.v(TAG, "环境变量初始化成功");

    }

    //用于进行mainActivity的初始化
    public void mainInitEnv() {
        //进行数据库的初始化
        RssDao rssDao = new RssDao(mContext);
        List<RssFeedInfo> rssFeedInfoList = rssDao.findAll();
        for (RssFeedInfo rssFeedInfo : rssFeedInfoList) {
            NavigationDrawerFragment.FeedInfos.add(rssFeedInfo.getTitle());
        }
        if (LogUtils.isCompilerLog) LogUtils.v(TAG, "数据库初始化成功");
    }

    //获取应用程序的版本信息
    public void initApplicationVersionInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            //获取当前包的信息
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            //获取应用程序版本号
            myApplicationVersion = packageInfo.versionName;
            //获取版本序号
            myVersionCode = packageInfo.versionCode;
            //获取版本名称
            myVersionName = myApplicationName + " v" + packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
