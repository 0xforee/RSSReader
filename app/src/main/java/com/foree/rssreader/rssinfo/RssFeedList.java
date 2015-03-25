package com.foree.rssreader.rssinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import com.foree.rssreader.base.BaseActivity;
import com.foree.rssreader.db.RssDao;
import com.rssreader.foree.rssreader.R;

import java.util.List;
import java.util.Map;

/**
 * Created by foree on 3/25/15.
 * 可添加的Feed信息的列表显示
 */
public class RssFeedList extends BaseActivity {
    private static final String TAG = "RssFeedList";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedlist);

        //找到控件
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.el_feedlist);

        //设置ActionBar的标题
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("feedlist");

        //获取group和child
        RssDao rssDao = new RssDao(this);
        final List<Map<String, String>> group = rssDao.findGroup();
        final List<List<Map<String, String>>> child = rssDao.findChild();
        SimpleExpandableListAdapter mAdapter = new SimpleExpandableListAdapter(
                this,
                group,
                R.layout.feedinfo,
                new String[]{"type"},
                new int[]{R.id.feedinfo},
                child,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{"name"},
                new int[]{android.R.id.text1}

        );
        expandableListView.setAdapter(mAdapter);
        //设置点击child事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //从child里找到对应的Feed名称
                String FeedName = child.get(groupPosition).get(childPosition).get("name");
                Toast.makeText(RssFeedList.this, FeedName + "", Toast.LENGTH_SHORT).show();
                //从child里找到对应的url
                String FeedUrl = child.get(groupPosition).get(childPosition).get("url");
                //加入Intent
                Intent addFeedIntent = new Intent(RssFeedList.this, RssAddFeed.class);
                //放入数据,并启动activity
                Bundle bundle = new Bundle();
                bundle.putString("url", FeedUrl);
                addFeedIntent.putExtra("com.rssreader.mainactivity", bundle);
                startActivity(addFeedIntent);
                return false;
            }
        });
    }

}
