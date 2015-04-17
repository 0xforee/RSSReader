package com.foree.rssreader.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.foree.rssreader.rssinfo.RssItemInfo;
import com.foree.rssreader.ui.activity.SettingsActivity;
import com.foree.rssreader.utils.ColorUtils;
import com.foree.rssreader.utils.ImageDownLoader;
import com.foree.rssreader.utils.LogUtils;
import com.foree.rssreader.utils.NetworkUtils;
import com.foree.rssreader.utils.WindowUtils;
import com.rssreader.foree.rssreader.R;
import com.foree.rssreader.xmlparse.XmlParseHandler;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by foree on 15-3-10.
 * 所有activity的基类，用于实现一些所有activity共有的方法，比如设置主题
 */
public class BaseActivity extends ActionBarActivity implements ListView.OnScrollListener {
    private static final String TAG = "BaseActivity";
    private Handler mHandler;
    private RssAdapter mRssAdapter;
    public ImageDownLoader mImageDownLoader;
    private boolean isFirstEnter = true;
    private int mFirstVisibleItem;
    private int mVisibleItemCount;
    public static ListView listView;
    protected SystemBarTintManager mTintManager;
    public boolean isDarkTheme = false;
    public int themeColor = 0;

    public List<RssItemInfo> getRssItemInfos() {
        return rssItemInfos;
    }

    List<RssItemInfo> rssItemInfos;

    public Handler getmHandler() {
        return mHandler;
    }

    public RssAdapter getmRssAdapter() {
        return mRssAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //新建一个Handler用于传递数据
        mHandler = new Handler();
        //新构建rssAdapter,用于解析处理listView
        rssItemInfos = new ArrayList<>();
        mRssAdapter = new RssAdapter(this, rssItemInfos);

        mImageDownLoader = new ImageDownLoader(this);

        //在api大于19时候才使用状态栏透明模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        mTintManager = new SystemBarTintManager(this);

    }

    //sdk大于19使用透明状态栏模式
    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //设置content的状态的偏移,只适用于大于api19
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View view = getWindow().findViewById(android.R.id.content);
            view.setPadding(0, WindowUtils.getInstance().getPadding(this), 0, 0);
        }
    }

    public void setContentView(int layoutResID) {
        //设置状态栏沉浸模式
        mTintManager.setStatusBarTintEnabled(true);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        themeColor = sp.getInt(SettingsActivity.KEY_CHANGE_THEME, ColorUtils.AQUAMARINE);
        if (sp.getBoolean(SettingsActivity.KEY_DARK_THEME, false)) {
            isDarkTheme = true;
            mTintManager.setStatusBarTintResource(R.color.dark_my_actionbar_background);
            setTheme(R.style.NightTheme);
        } else {
            isDarkTheme = false;
            switch (themeColor) {
                case ColorUtils.AQUAMARINE:
                    mTintManager.setStatusBarTintResource(R.color.aquamarine_my_actionbar_background);
                    setTheme(R.style.DayTheme);
                    break;
                case ColorUtils.BLUE:
                    mTintManager.setStatusBarTintResource(R.color.blue_my_actionbar_background);
                    setTheme(R.style.BlueTheme);
                    break;
                case ColorUtils.GREEN:
                    mTintManager.setStatusBarTintResource(R.color.green_my_actionbar_background);
                    setTheme(R.style.GreenTheme);
                    break;
                case ColorUtils.ORANGE:
                    mTintManager.setStatusBarTintResource(R.color.orange_my_actionbar_background);
                    setTheme(R.style.OrangeTheme);
                    break;
                case ColorUtils.RED:
                    mTintManager.setStatusBarTintResource(R.color.red_my_actionbar_background);
                    setTheme(R.style.RedTheme);
                    break;
                case ColorUtils.GRAY:
                    mTintManager.setStatusBarTintResource(R.color.gray_my_actionbar_background);
                    setTheme(R.style.GrayTheme);
                    break;
            }
        }
        super.setContentView(layoutResID);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //仅当ListView静止时才去下载图片，ListView滑动时取消所有正在下载的任务
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            showImage(mFirstVisibleItem, mVisibleItemCount);
        } else {
            if (LogUtils.isCompilerLog) Log.v(TAG, "cancelTask");
            cancelTask();
        }

    }

    //在滑动过程中,取得可见的数据和第一个项目
    //ListView滑动的时候调用的方法
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
        if (isFirstEnter && visibleItemCount > 0) {
            showImage(mFirstVisibleItem, mVisibleItemCount);
            isFirstEnter = false;
        }
    }

    /**
     * 显示当前屏幕的图片，先会去查找LruCache，LruCache没有就去sd卡或者手机目录查找，再没有就开启线程去下载
     *
     * @param firstVisibleItem 第一个可见的item
     * @param visibleItemCount 总可见的item数量
     */
    private void showImage(int firstVisibleItem, int visibleItemCount) {
        Bitmap bitmap;
        for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
            String mImageUrl = getRssItemInfos().get(i).getImageUrl();
            if (mImageUrl == null) {
                if (LogUtils.isCompilerLog) Log.v(TAG, "download showImage is null");
            }
            final ImageView mImageView = (ImageView) listView.findViewWithTag(mImageUrl);
            bitmap = this.mImageDownLoader.downloadImage(mImageUrl, new ImageDownLoader.onImageLoaderListener() {

                @Override
                public void onImageLoader(Bitmap bitmap, String url) {
                    if (mImageView != null && bitmap != null) {
                        mImageView.setImageBitmap(bitmap);
                    }
                }
            });

            if (bitmap != null) {
                mImageView.setVisibility(View.VISIBLE);
                mImageView.setImageBitmap(bitmap);
            }
        }
    }

    /**
     */
    public void cancelTask() {
        mImageDownLoader.cancelTask();
    }

    /**
     * rss数据适配器,专为title_list_layout布局文件打造,如果有其他的布局文件想使用
     * 可以考虑将view对象单独摘出来,通过子类来重构
     */
    public class RssAdapter extends ArrayAdapter<RssItemInfo> {

        private class ViewHolder {
            TextView tv_title;
            TextView tv_rssfeed;
            TextView tv_time;
            ImageView iv_title_image;
        }

        private LayoutInflater mLayoutInflater;

        public RssAdapter(Context context, List<RssItemInfo> objects) {
            super(context, 0, objects);
            //获取系统服务layoutInfalter
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return rssItemInfos.size();
        }

        @Override
        public RssItemInfo getItem(int position) {
            return rssItemInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //重写getView方法
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            //获取系统的view控件
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.listview_item_layout, null);
                viewHolder = new ViewHolder();
                //将rssItemInfo数据设置到viewHolder中
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.tv_rssfeed = (TextView) convertView.findViewById(R.id.rssfeed);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.time);
                viewHolder.iv_title_image = (ImageView) convertView.findViewById(R.id.title_image);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //获取RssItemInfo,从ArrayList中
            RssItemInfo rssItemInfo = this.getItem(position);

            //  Log.v(TAG, rssItemInfo.getTitle());

            viewHolder.tv_title.setText(rssItemInfo.getTitle());
            //rssItem中所对应的中的channel信息
            viewHolder.tv_rssfeed.setText(rssItemInfo.getFeedTitle());
            viewHolder.tv_time.setText(rssItemInfo.getpubDate());
            // iv_title_image.setImageBitmap(rssItemInfo.getImage());

            viewHolder.iv_title_image.setTag(rssItemInfo.getImageUrl());

            //显示缓存中的bitmap
            Bitmap bitmap = mImageDownLoader.showCacheBitmap(rssItemInfo.getImageUrl().replaceAll("[^\\w]", ""));
            if (bitmap != null) {
                viewHolder.iv_title_image.setVisibility(View.VISIBLE);
                viewHolder.iv_title_image.setImageBitmap(bitmap);
            }

            return convertView;
        }
    }

    /**
     * rss解析线程类,用来打开url链接,并调用rss解析器
     */
    public static class RssParse extends Thread {
        private String mUrl;
        private BaseActivity mActivity;

        public RssParse(BaseActivity activity, String urlName) {
            this.mActivity = activity;
            mUrl = urlName;
        }

        @Override
        public void run() {
            try {
                //打开url连接
                URL url = new URL(mUrl);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setConnectTimeout(10000);
                urlConnection.connect();
                //获取url的输入流
                InputStream in = urlConnection.getInputStream();
                //解析Rss文档
                parseRss(in);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void parseRss(InputStream in) {
            try {
                //调用rss解析工厂构造SAX解析器
                SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
                //使用自定义的事件处理器(传入当前对象用于获取所对应的Handler和adapter
                XmlParseHandler xmlParseHandler = new XmlParseHandler(mActivity);
                //开始解析
                saxParser.parse(in, xmlParseHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //开一个线程来解析Rss
    public void doRss(String urlName) {
        //根据是否只在wifi下下载来决定是否开始解析
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if ((sp.getBoolean(SettingsActivity.KEY_DOWNLOAD_ON_WIFI, false)) && MyApplication.mNetworkState == NetworkUtils.NETWORK_MOBILE) {
            Toast.makeText(this, R.string.error10000, Toast.LENGTH_LONG).show();

        } else if (MyApplication.mNetworkState == NetworkUtils.NETWORK_NONE) {
            Toast.makeText(this, R.string.error10001, Toast.LENGTH_LONG).show();
        } else {
            RssParse rssParse = new RssParse(this, urlName);
            rssParse.start();
        }
    }

    //重新清理Adapter,用于在切换fragment的时候使用
    public void resetUI() {
        rssItemInfos = new ArrayList<>();
        mRssAdapter = new RssAdapter(this, rssItemInfos);
        //mRssAdapter.clear();
    }


}
