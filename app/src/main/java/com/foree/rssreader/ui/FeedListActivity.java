package com.foree.rssreader.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
public class FeedListActivity extends BaseActivity {
    private static final String TAG = "RssFeedList";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedlist);

        //找到控件
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.el_feedlist);

        //设置ActionBar的标题
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.feedListTitle);

        //获取group和child
        RssDao rssDao = new RssDao(this);
        final List<Map<String, String>> group = rssDao.findGroup();
        final List<List<Map<String, String>>> child = rssDao.findChild();
        SimpleExpandableListAdapter mAdapter = new SimpleExpandableListAdapter(
                this,
                group,
                R.layout.feedinfo_title,
                new String[]{"type"},
                new int[]{R.id.feedinfo_title},
                child,
                R.layout.feedinfo_itme,
                new String[]{"name"},
                new int[]{R.id.feedinfo_itme}

        );
        expandableListView.setAdapter(mAdapter);
        //设置点击child事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //从child里找到对应的Feed名称
                String FeedName = child.get(groupPosition).get(childPosition).get("name");
                Toast.makeText(FeedListActivity.this, FeedName + "", Toast.LENGTH_SHORT).show();
                //从child里找到对应的url
                String FeedUrl = child.get(groupPosition).get(childPosition).get("url");
                //加入Intent
                Intent addFeedIntent = new Intent(FeedListActivity.this, AddFeedActivity.class);
                //放入数据,并启动activity
                Bundle bundle = new Bundle();
                bundle.putString("url", FeedUrl);
                addFeedIntent.putExtra("com.rssreader.mainactivity", bundle);
                startActivity(addFeedIntent);
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedlist, menu);
        //return true表示事件处理到此处结束,不需要向下传递
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            //点击new,对侧边的数据进行增加,并调用adapter提示变化
            case R.id.action_new:
                //将dialog布局文件转换为一个view对象
                LayoutInflater factory = LayoutInflater.from(this);
                final View view = factory.inflate(R.layout.dialog_newfeed, null);

                //创建dialog
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("请输入一个合法的Rss链接");
                builder1.setView(view);
                final Intent intent1 = new Intent(this, AddFeedActivity.class);
                builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) view.findViewById(R.id.et_newfeed_url);
                        String url = editText.getText().toString();
                        //使用bundle来传递数据
                        Bundle args = new Bundle();
                        args.putString("url", url);

                        intent1.putExtra("com.rssreader.mainactivity", args);
                        startActivity(intent1);
                    }
                });
                builder1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nothings to do
                    }
                });
                builder1.show();
        }
        return super.onOptionsItemSelected(item);
    }

}
