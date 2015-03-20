package com.xmlparse.foree.rssreader;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baseapplication.foree.rssreader.MyApplication;
import com.rssinfo.foree.rssreader.RssFeedInfo;
import com.rssreader.foree.rssreader.MainActivity;
import com.rssreader.foree.rssreader.R;
import com.rssreader.foree.rssreader.ShowDescription;
import com.utils.foree.rssreader.CacheUtils;
import com.utils.foree.rssreader.NetworkUtils;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by foree on 3/4/15.
 * 异步线程，用来处理连接网络等耗时的操作
 */
public class ParseTask extends AsyncTask<MainActivity.PlaceholderFragment, Integer, RssFeedInfo> {
    XmlParseHandler xmlParseHandler;
    ListView listView;
    ProgressBar mProgressBar;
    TextView mTextView;
    RssFeedInfo mRssFeedInfo;
    MainActivity.PlaceholderFragment placeholderFragment;

    //Activity继承的就是上下文，因此mcontext可以通过初始化activity来实现
    private MainActivity mainActivity;
    private Context mcontext;

    public ParseTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.mcontext = mainActivity;
    }

    private final String TAG = "ParseTask";

    @Override
    protected RssFeedInfo doInBackground(MainActivity.PlaceholderFragment... position) {

        //  mcontext = contexts[0];
        placeholderFragment = position[0];
        mProgressBar = placeholderFragment.getmProgressBar();
        mTextView = placeholderFragment.getmTextView();
        listView = placeholderFragment.getItemlist();

        //获取网络状态
        MyApplication.mNetworkState = NetworkUtils.getNetworkState(mcontext);

        //无网络模式下或者有缓存状态
        if (CacheUtils.isListCached(placeholderFragment.getFeedLink()) || MyApplication.mNetworkState == NetworkUtils.NETWORK_NONE) {
            Log.v(TAG, "有缓存或者无网络");
            return null;
        }
        Log.v(TAG, "doInBackground");
        try {
            //urlString[0]代表要接收的字符串
            URL url = new URL(placeholderFragment.getpostionUrl());
            InputSource is = new InputSource(url.openStream());

            //构建SAX解析工厂
            SAXParserFactory factory = SAXParserFactory.newInstance();
            //使用SAX解析工厂构造SAX解析器
            SAXParser parser = factory.newSAXParser();
            //使用SAX解析器构建xml读取工具
            XMLReader xmlReader = parser.getXMLReader();

            //获取一个代理，并设置xml解析的代理为xmlparse解析类
            xmlParseHandler = new XmlParseHandler();
            xmlReader.setContentHandler(xmlParseHandler);

            xmlReader.parse(is);

            return xmlParseHandler.getFeedInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //进行更新listview操作
    @Override
    protected void onPostExecute(final RssFeedInfo rssFeedInfo) {
        super.onPostExecute(rssFeedInfo);
        List<Map<String, Object>> list;

        Log.v(TAG, "In PostExecute");
        mRssFeedInfo = rssFeedInfo;
        //数据对象为空时，置listview为空，然后返回
        if (mRssFeedInfo != null) {
            list = mRssFeedInfo.getAllItemForListView();
            CacheUtils.writeCacheList(mRssFeedInfo);
        } else {
            Log.v(TAG, "In mRssinfo null");

            if (CacheUtils.isListCached(placeholderFragment.getFeedLink())) {
                mTextView.setVisibility(View.INVISIBLE);
                mRssFeedInfo = CacheUtils.readCacheList(placeholderFragment.getFeedLink());
                list = mRssFeedInfo.getAllItemForListView();

            } else {
                Log.v(TAG, "else else");
                mProgressBar.setVisibility(View.GONE);
                //设置listview可见
                listView.setVisibility(View.GONE);
                mTextView.setVisibility(View.VISIBLE);
                mTextView.setTextSize(20);
                mTextView.setText("无网络连接，请联网后点击重试");
                return;
            }
        }
        Log.v(TAG, "onPostExecute");
        SimpleAdapter adapter;

        //将传来的数据显示到listview中，使用title_list_layout布局
        adapter = new SimpleAdapter(mcontext,
                list,
                R.layout.title_list_layout,
                new String[]{"title", "pubdate"},
                new int[]{R.id.title, R.id.time});

        //设置进度条不可见
        mProgressBar.setVisibility(View.GONE);
        //设置listview可见
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mintent = new Intent(mcontext, ShowDescription.class);

                //绑定数据
                Bundle bundle = new Bundle();
                bundle.putString("title", mRssFeedInfo.getItem(position).getTitle());
                bundle.putString("description", mRssFeedInfo.getItem(position).getDescription());
                bundle.putString("link", mRssFeedInfo.getItem(position).getLink());
                bundle.putString("pubdate", mRssFeedInfo.getItem(position).getpubDate());

                mintent.putExtra("com.rssinfo.foree.rssreader", bundle);
                mcontext.startActivity(mintent);
            }
        });
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.v(TAG, "onPreExecute");


    }

}
