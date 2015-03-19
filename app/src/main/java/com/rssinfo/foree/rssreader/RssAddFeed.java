package com.rssinfo.foree.rssreader;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baseapplication.foree.rssreader.BaseActivity;
import com.db.foree.rssreader.RssDao;
import com.rssreader.foree.rssreader.MainActivity;
import com.rssreader.foree.rssreader.NavigationDrawerFragment;
import com.rssreader.foree.rssreader.R;
import com.rssreader.foree.rssreader.ShowDescription;
import com.xmlparse.foree.rssreader.ParseTask;
import com.xmlparse.foree.rssreader.XmlParseHandler;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by foree on 3/18/15.
 * 添加RssFeed时预览窗口
 */
public class RssAddFeed extends BaseActivity {
    private static final String TAG = "RssAddFeed";
    private RssFeedInfo mrssFeedInfo;
    ListView listView;
    TextView tv_title, tv_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_feed);
//获取listview
        listView = (ListView) findViewById(R.id.lv_addfeed);
        //获取title和description
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_description = (TextView) findViewById(R.id.tv_description);
        Button button = (Button) findViewById(R.id.bt_add);

        //获取传递的url数据
        Bundle bundle = getIntent().getBundleExtra("com.rssreader.mainactivity");
        final String url = bundle.getString("url");
//开始解析url
        UrlTask urlTask = new UrlTask();
        urlTask.execute(url);

        //点击添加到数据库
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //取得名称,填入feedinfos list
                String FeedName = tv_title.getText().toString();

                //将链接和名称加入到数据库中,如果有重复,则提示重复并退出
                RssDao rssDao = new RssDao(RssAddFeed.this, "rss.db", null, 1);
                if (rssDao.add(FeedName, url) != -1) {
                    NavigationDrawerFragment.FeedInfos.add(FeedName);
                    NavigationDrawerFragment.adapter.notifyDataSetChanged();
                    Toast.makeText(RssAddFeed.this, "添加成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RssAddFeed.this, "此订阅号已添加过啦", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 执行url解析操作
     */
    public class UrlTask extends AsyncTask<String, Integer, RssFeedInfo> {
        @Override
        protected RssFeedInfo doInBackground(String... params) {
            String urlName = params[0];
            try {
                //进行url链接
                URL url = new URL(urlName);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setConnectTimeout(10000);
                urlConnection.connect();

                //获取inputStream
                InputSource inputSource = new InputSource(urlConnection.getInputStream());

                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();
                XMLReader xmlReader = saxParser.getXMLReader();

                //获取一个xml解析代理
                XmlParseHandler xmlParseHandler = new XmlParseHandler();
                //将xmlreader的解析器设置为自定义的解析器
                xmlReader.setContentHandler(xmlParseHandler);
                //开始xml解析
                xmlReader.parse(inputSource);

                return xmlParseHandler.getFeedInfo();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 执行更新主要ui操作
         *
         * @param rssFeedInfo
         */
        @Override
        protected void onPostExecute(RssFeedInfo rssFeedInfo) {
            mrssFeedInfo = rssFeedInfo;
            //设置预览界面的标题和描述
            tv_title.setText(rssFeedInfo.getTitle());
            tv_description.setText(rssFeedInfo.getDescription());
            //设置列表显示
            listView.setAdapter(new SimpleAdapter(RssAddFeed.this,
                    mrssFeedInfo.getAllItemForListView(),
                    R.layout.title_list_layout,
                    new String[]{"title", "pubdate"},
                    new int[]{R.id.title, R.id.time}
            ));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent mintent = new Intent(RssAddFeed.this, ShowDescription.class);

                    //绑定数据
                    Bundle bundle = new Bundle();
                    bundle.putString("title", mrssFeedInfo.getItem(position).getTitle());
                    bundle.putString("description", mrssFeedInfo.getItem(position).getDescription());
                    bundle.putString("link", mrssFeedInfo.getItem(position).getLink());
                    bundle.putString("pubdate", mrssFeedInfo.getItem(position).getpubDate());

                    mintent.putExtra("com.rssinfo.foree.rssreader", bundle);
                    RssAddFeed.this.startActivity(mintent);
                }
            });
            super.onPostExecute(rssFeedInfo);
        }
    }

}
