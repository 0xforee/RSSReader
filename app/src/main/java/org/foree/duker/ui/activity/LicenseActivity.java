package org.foree.duker.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.webkit.WebView;

import org.foree.duker.base.BaseActivity;
import org.foree.duker.R;

/**
 * Created by foree on 4/15/15.
 * using webView show public license
 */
public class LicenseActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdescription);

        WebView webView = (WebView) findViewById(R.id.webview);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.public_license);

        //load license
        webView.loadUrl("file:///android_asset/licenses.html");
    }
}
