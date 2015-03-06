package com.rssreader.foree.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by foree on 3/5/15.
 * 加入android的启动画面
 */
public class LoadActivity extends Activity {
    //private static final int LOAD_DISPLAY_TIME = 1500;
    protected boolean _active = true;
    protected int _splashTime = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.load);
        //ImageView image = (ImageView) findViewById(R.id.bg_branco);

        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                        finish();
                        startActivity(new Intent(LoadActivity.this, MainActivity.class));
                        stop();
                    }
                } catch (InterruptedException e) {
                } finally {

                }
            }
        };
        splashTread.start();
    }

}
