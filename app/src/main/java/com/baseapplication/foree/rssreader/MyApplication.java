package com.baseapplication.foree.rssreader;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Environment;
import android.util.Log;

import com.rssreader.foree.rssreader.MainActivity;
import com.utils.foree.rssreader.NetworkUtils;

import java.io.File;
import java.util.jar.Manifest;

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

    //Sdcard的路径
    public static final String SdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    //Sdcard的状态（用于判断是否挂载）
    public static final String SdcardState = Environment.getExternalStorageState();

    //应用程序名称
    public static final String myApplicationName = "RSSReader";
    //应用程序的网络状态
    public static int mNetworkState = NetworkUtils.NETWORK_NONE;
    //应用程序的sdcard的目录路径
    public static String mySdcardDataDir;
    //应用程序的缓存目录路径
    public static String mySdcardCacheDir;

    public MyApplication(MainActivity context) {
        mContext = context;
    }

    //初始化环境
    public void initEnv() {
        //判断当前网络状况
        // NetworkUtils networkUtils = new NetworkUtils();
        mNetworkState = NetworkUtils.getNetworkState(mContext);

        //如果当前Sdcard已经挂载，应用程序目录与缓存目录是否建立完成
        if (SdcardState.equals(Environment.MEDIA_MOUNTED)) {
            //应用程序目录
            File myDateDir = new File(SdcardPath + "/RSSReader/");
            if (!myDateDir.exists())
                myDateDir.mkdir();
            mySdcardDataDir = SdcardPath + "/RSSReader";
            //缓存目录
            File myCacheDir = new File(mySdcardDataDir + "/" + "cache/");
            if (!myCacheDir.exists())
                myCacheDir.mkdir();
            mySdcardCacheDir = mySdcardDataDir + "/" + "cache";
        }
        Log.v(TAG, "初始化环境变量成功");
    }

   /* //初始化一些系统设置
    public void initSettings(Context context) {
        //获取系统设置文件
        File file = new File(context.getFilesDir() + "/../shared_prefs" + "/settings.xml");
        if (!file.exists()) {
            SharedPreferences sp = context.getSharedPreferences("settings", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("darktheme", false);
            editor.putBoolean("wificache", false);
            editor.putBoolean("cleancache", false);
            editor.apply();
        } else
            Log.v(TAG, "设置文件存在，不进行初始化");
    }*/

    //清除列表缓存文件
    public void cleanListCache() {
        File file = new File(MyApplication.mySdcardCacheDir + "/listcache");

        if (file.exists()) {
            if (file.delete())
                Log.v(TAG, "cache cleand");
        }
    }
}
