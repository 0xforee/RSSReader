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

import com.rssinfo.foree.rssreader.RssFeedInfo;
import com.rssreader.foree.rssreader.MainActivity;
import com.rssreader.foree.rssreader.R;
import com.rssreader.foree.rssreader.ShowDescription;

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
public class ParseTask extends AsyncTask<Context,Integer,RssFeedInfo> {
    XmlParse xmlParse ;
    ListView listView;
    ProgressBar mProgressBar;
    public final String RSS_URL = "http://blog.sina.com.cn/rss/1267454277.xml";
    public final String RSS_URL2 = "http://www.dogear.cn/feed/9768.xml";
    public final String RSS_URL3 = "http://www.dogear.cn/feed/10244.xml";
    public final String RSS_URL4 = "http://home.meizu.cn/forum.php?mod=rss&fid=47&auth=75adh9VXN4w2LUlUU0Geo7K8IZfQN%2BCrljYieNxcSsqOKNcBDaFStvzrGsBM";

    private MainActivity mainActivity;
    private Context mcontext;
    public ParseTask(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    private final String TAG = "ParseTask";
    @Override
    protected RssFeedInfo doInBackground(Context... contexts) {
        Log.v(TAG, "doInBackground");
        try {
            //urlString[0]代表要接收的字符串
            URL url = new URL(RSS_URL2);
            InputSource is = new InputSource(url.openStream());

            mcontext = contexts[0];

            if( is != null) {
                //构建SAX解析工厂
                SAXParserFactory factory = SAXParserFactory.newInstance();
                //使用SAX解析工厂构造SAX解析器
                SAXParser parser = factory.newSAXParser();
                //使用SAX解析器构建xml读取工具
                XMLReader xmlReader = parser.getXMLReader();

                //获取一个代理，并设置xml解析的代理为xmlparse解析类
                xmlParse = new XmlParse();
                xmlReader.setContentHandler(xmlParse);

                xmlReader.parse(is);

                return xmlParse.getFeedInfo();
            }else
                Log.v(TAG,"cannot open site");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //进行更新listview操作
    @Override
    protected void onPostExecute(final RssFeedInfo rssFeedInfo) {
        super.onPostExecute(rssFeedInfo);
        List<Map<String, Object>> list;

        //数据对象为空时，置listview为空，然后返回
        if (rssFeedInfo != null)
            list = rssFeedInfo.getAllItemForListView();
        else {
            mProgressBar.setVisibility(View.GONE);
            //设置listview可见
            listView.setVisibility(View.VISIBLE);
            return;
        }

        Log.v(TAG, "onPostExecute");
        SimpleAdapter adapter;

        //将传来的数据显示到listview中，使用activity_main布局
        adapter = new SimpleAdapter(mcontext,
                list,
                R.layout.activity_main,
                new String[] { "title", "pubdate" },
                new int[] { R.id.title, R.id.time });

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
                bundle.putString("title",rssFeedInfo.getItem(position).getTitle());
                bundle.putString("description",rssFeedInfo.getItem(position).getDescription());
                bundle.putString("link",rssFeedInfo.getItem(position).getLink());
                bundle.putString("pubdate",rssFeedInfo.getItem(position).getpubData());

                mintent.putExtra("com.rssinfo.foree.rssreader", bundle);
                mcontext.startActivity(mintent);
            }
        });
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listView = mainActivity.getItemlist();
        mProgressBar = mainActivity.getmProgressBar();
        Log.v(TAG, "onPreExecute");

    }

}
