package org.foree.duker.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.foree.duker.base.BaseActivity;
import com.rssreader.foree.rssreader.R;

/**
 * Created by foree on 3/5/15.
 * 显示每条Item的具体的信息
 */
public class DescriptionActivity extends BaseActivity {
    private static final String TAG = "ShowDescription";
    String link = null;
    String title = null;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdescription);

        //获取Actionbar,设置标题
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);

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
            actionBar.setTitle(title);
        }
        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        //加载webview中的js脚本
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(link);
    }

    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            return new AlertDialog.Builder(this)
                    .setTitle(R.string.action_textsize)
                    .setItems(R.array.textsize, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            switch (whichButton) {
                                case 0:
                                    webView.getSettings().setTextZoom(70);
                                    break;
                                case 1:
                                    webView.getSettings().setTextZoom(100);
                                    break;
                                case 2:
                                    webView.getSettings().setTextZoom(130);

                            }
                        }
                    }).create();
        }
        return null;
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
            case R.id.action_textsize:
                showDialog(1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
