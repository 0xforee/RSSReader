package com.foree.rssreader.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.rssreader.foree.rssreader.R;

/**
 * Created by foree on 3/5/15.
 * 加入android的启动画面
 */
public class LoadActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_load);
        Thread loadThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(2000);
                        startActivity(new Intent(LoadActivity.this, MainActivity.class));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        };
        loadThread.start();
    }
}
