package com.foree.rssreader.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.foree.rssreader.rssinfo.RssItemInfo;
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
public class BaseActivity extends ActionBarActivity {
    private Handler mHandler;
    private RssAdapter mRssAdapter;

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
        //新构建rssAdapter,用于解析处理listview
        rssItemInfos = new ArrayList<>();
        mRssAdapter = new RssAdapter(this, rssItemInfos);

    }

    public void setContentView(int layoutResID) {
        //根据配置文件设置相应的主题
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean("ck_darktheme", false))
            setTheme(R.style.NightTheme);
        else
            setTheme(R.style.DayTheme);

        super.setContentView(layoutResID);
    }

    /**
     * rss数据适配器,专为title_list_layout布局文件打造,如果有其他的布局文件想使用
     * 可以考虑将view对象单独摘出来,通过子类来重构
     */
    public static class RssAdapter extends ArrayAdapter<RssItemInfo> {
        private LayoutInflater mLayoutInflater;

        public RssAdapter(Context context, List<RssItemInfo> objects) {
            super(context, 0, objects);
            //获取系统服务layoutInfalter
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        //重写getview方法
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //获取系统的view控件
            View view;
            if (convertView == null)
                view = mLayoutInflater.inflate(R.layout.title_list_layout, null);
            else
                view = convertView;

            //获取RssItemInfo,从Arraylist中
            RssItemInfo rssItemInfo = this.getItem(position);

            //将rssItemInfo数据设置到控件上
            TextView tv_title = (TextView) view.findViewById(R.id.title);
            TextView tv_rssfeed = (TextView) view.findViewById(R.id.rssfeed);
            TextView tv_tiem = (TextView) view.findViewById(R.id.time);
            ImageView iv_title_image = (ImageView) view.findViewById(R.id.title_image);

            tv_title.setText(rssItemInfo.getTitle());
            //rssItem中所对应的中的channel信息
            tv_rssfeed.setText(rssItemInfo.getFeedTitle());
            tv_tiem.setText(rssItemInfo.getpubDate());
            iv_title_image.setImageBitmap(rssItemInfo.getImage());

            return view;
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
                urlConnection.setConnectTimeout(1000);
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
        RssParse rssParse = new RssParse(this, urlName);
        rssParse.start();
    }

    //重新归零listview,用于在切换fragment的时候使用
    public void resetUI(ListView listView, List<RssItemInfo> items) {
        mRssAdapter = new RssAdapter(this, items);
        listView.setAdapter(mRssAdapter);
    }
}
