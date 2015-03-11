package com.rssreader.foree.rssreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baseapplication.foree.rssreader.BaseActivity;
import com.baseapplication.foree.rssreader.MyApplication;
import com.xmlparse.foree.rssreader.ParseTask;

import java.util.jar.Manifest;

public class MainActivity extends BaseActivity {
    // private RssFeedInfo rssFeedInfo = null;
    ListView itemlist;
    ProgressBar mProgressBar;
    TextView mTextView;
    MyApplication myApplication;
    // private String pathXML= Environment.getExternalStorageDirectory().getAbsolutePath() + "/w3cschool.xml";

    private static final String TAG = "ManiActivity";

    public ListView getItemlist() {
        return itemlist;
    }

    public ProgressBar getmProgressBar() {
        return mProgressBar;
    }

    public TextView getmTextView() {
        return mTextView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DayTheme);
        setContentView(R.layout.main);

        //应用启动初始化环境变量
        myApplication = new MyApplication(this);
        myApplication.initEnv();
        myApplication.initSettings(this);

        //搭建UI
        itemlist = new ListView(this);
        mProgressBar = new ProgressBar(this);
        mTextView = new TextView(this);

        itemlist = (ListView) findViewById(R.id.listview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mTextView = (TextView) findViewById(R.id.nonetwork);

        //使用asyncTask完成url解析操作
        ParseTask parseTask = new ParseTask(this);
        parseTask.execute(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case R.id.action_about:
                String message = "增加以下特性：" + "\n"
                        + "1.可以浏览新闻";
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("版本v0.2");
                builder.setMessage(message);
                builder.setNeutralButton("返回", null);
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
