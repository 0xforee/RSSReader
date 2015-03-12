package com.rssreader.foree.rssreader;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baseapplication.foree.rssreader.BaseActivity;

/**
 * Created by foree on 3/5/15.
 * 显示每条Item的具体的信息
 */
public class ShowDescription extends BaseActivity {
    private static final String TAG = "ShowDescription";
    String link = null;
    String title = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        //接收数据
        String content = null;
        Intent intent = getIntent();

        if (intent != null) {
            Bundle mBundle = intent.getBundleExtra("com.rssinfo.foree.rssreader");

            content = mBundle.getString("title") + "\n"
                    + mBundle.getString("description") + "\n"
                    + mBundle.getString("pubdate") + "\n"
                    + mBundle.getString("link");


            link = mBundle.getString("link");
            title = mBundle.getString("title");
        }
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        //加载webview中的js脚本
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(link);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_description, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            //调用分享按钮分享
            case R.id.action_share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TITLE, title);
                shareIntent.putExtra(Intent.EXTRA_TEXT, link);
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
