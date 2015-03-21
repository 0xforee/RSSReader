package com.baseapplication.foree.rssreader;

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
import android.widget.TextView;

import com.rssinfo.foree.rssreader.RssItemInfo;
import com.rssreader.foree.rssreader.R;
import com.xmlparse.foree.rssreader.XmlParseHandler;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by foree on 15-3-10.
 * 所有activity的基类，用于实现一些所有activity共有的方法，比如设置主题
 */
public class BaseActivity extends ActionBarActivity {
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    public Handler getmHandler() {
        return mHandler;
    }

    public static class RssAddr implements Runnable {
        private RssAdaper mRssAdaper;
        private RssItemInfo mRssItemInfo;

        public RssAddr(RssItemInfo rssItemInfo) {
            this.mRssItemInfo = rssItemInfo;
        }

        @Override
        public void run() {
            mRssAdaper.add(mRssItemInfo);
        }
    }

    public static class RssAdaper extends ArrayAdapter<RssItemInfo> {
        private LayoutInflater mLayoutInflater;

        public RssAdaper(Context context, List<RssItemInfo> objects) {
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
            //rssFeedInfo中的channel信息
            tv_rssfeed.setText(rssItemInfo.getTitle());
            tv_tiem.setText(rssItemInfo.getpubDate());
            iv_title_image.setImageBitmap(rssItemInfo.getImage());

            return view;
        }
    }

    public static class RssParse extends Thread {
        //线程类,用来打开url链接,并启用解析
        private String mUrl;
        private BaseActivity mActivity;

        public RssParse(BaseActivity activity, String urlName) {
            this.mActivity = activity;
            mUrl = urlName;
        }

        @Override
        public void run() {
            try {
                //打开url连接类
                URL url = new URL(mUrl);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setConnectTimeout(1000);
                urlConnection.connect();

                InputStream in = urlConnection.getInputStream();
                //解析Rss文档
                parseRss(in);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void parseRss(InputStream in) {
            try {
                SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();

                XmlParseHandler xmlParseHandler = new XmlParseHandler(mActivity);

                saxParser.parse(in, xmlParseHandler);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
