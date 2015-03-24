package com.foree.rssreader.rssinfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.foree.rssreader.base.BaseActivity;
import com.foree.rssreader.db.RssDao;
import com.foree.rssreader.ui.NavigationDrawerFragment;
import com.foree.rssreader.ui.ShowDescription;
import com.foree.rssreader.utils.ImageDownLoader;
import com.foree.rssreader.xmlparse.XmlParseHandler;
import com.rssreader.foree.rssreader.R;

/**
 * Created by foree on 3/18/15.
 * 添加RssFeed时预览窗口
 */
public class RssAddFeed extends BaseActivity implements XmlParseHandler.ParseHandlerCallbacks {
    private static final String TAG = "RssAddFeed";
    private RssFeedInfo mrssFeedInfo;
    TextView tv_title, tv_description;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    private String FeedLink = null;
    private String FeedName;
    boolean isUpdate = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_feed);
        //获取listview
        listView = (ListView) findViewById(R.id.lv_addfeed);
        //获取title和description
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_description = (TextView) findViewById(R.id.tv_description);
        //获取progressBar和linearLayout布局
        progressBar = (ProgressBar) findViewById(R.id.pb_addfeed);
        linearLayout = (LinearLayout) findViewById(R.id.ll_addfeed);
        //准备显示ListView

        Button button = (Button) findViewById(R.id.bt_add);

        //获取传递的url数据
        Bundle bundle = getIntent().getBundleExtra("com.rssreader.mainactivity");
        final String url = bundle.getString("url");

        listView.setAdapter(this.getmRssAdapter());
        listView.setOnScrollListener(this);
        //开始解析url
        doRss(url);
        //绑定listview点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mintent = new Intent(RssAddFeed.this, ShowDescription.class);

                //绑定数据
                Bundle bundle = new Bundle();
                bundle.putString("title", getRssItemInfos().get(position).getTitle());
                bundle.putString("description", getRssItemInfos().get(position).getDescription());
                bundle.putString("link", getRssItemInfos().get(position).getLink());
                bundle.putString("pubdate", getRssItemInfos().get(position).getpubDate());

                mintent.putExtra("com.rssinfo.foree.rssreader", bundle);
                RssAddFeed.this.startActivity(mintent);
            }
        });
        //点击添加到数据库
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //取得要作为缓存文件的链接存入数据库中
                FeedLink = getRssItemInfos().get(1).getFeedLink();
                //将链接和名称加入到数据库中,如果有重复,则提示重复并退出
                RssDao rssDao = new RssDao(RssAddFeed.this, "rss.db", null, 1);
                if (rssDao.add(FeedName, url, FeedLink) != -1) {
                    NavigationDrawerFragment.FeedInfos.add(FeedName);
                    NavigationDrawerFragment.adapter.notifyDataSetChanged();
                    //如果用户点击添加,那么保存缓存文件
                    // CacheUtils.writeCacheList(mrssFeedInfo);

                    Toast.makeText(RssAddFeed.this, "添加成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RssAddFeed.this, "此订阅号已添加过啦", Toast.LENGTH_SHORT).show();
                }
                }
        });
    }

    @Override
    public void notifyUiUpdate() {
        this.getmHandler().post(new updatUI());
    }

    private class updatUI implements Runnable {

        @Override
        public void run() {
            progressBar.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            //解析完毕,设置界面相应的信息
            FeedName = getRssItemInfos().get(0).getFeedTitle();
            tv_title.setText(FeedName);
            tv_description.setText(getRssItemInfos().get(0).getFeedDescription());

        }
    }


}
