package com.rssreader.foree.rssreader;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        //接收数据
        String content = null;
        String link = null;
        Intent intent = getIntent();

        if (intent != null) {
            Bundle mBundle = intent.getBundleExtra("com.rssinfo.foree.rssreader");

            content = mBundle.getString("title") + "\n"
                    + mBundle.getString("description") + "\n"
                    + mBundle.getString("pubdate") + "\n"
                    + mBundle.getString("link");

            link = mBundle.getString("link");

        }
/*
        TextView textView = (TextView)findViewById(R.id.textview);
        textView.setText(content);

        Button button = (Button)findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }); */
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(link);
    }

}
