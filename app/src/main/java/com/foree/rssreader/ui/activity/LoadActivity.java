package com.foree.rssreader.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.foree.rssreader.base.MyApplication;
import com.rssreader.foree.rssreader.R;

/**
 * Created by foree on 3/5/15.
 * 加入android的启动画面
 */
public class LoadActivity extends Activity {
    private static final String TAG = "LoadActivity";
    TextView tv_speak1, tv_speak2, tv_speak3, tv_speak_author, tv_version, tv_app_name;
    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //根据配置文件设置相应的主题
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean(SettingsActivity.KEY_DARK_THEME, false))
            setTheme(R.style.NightTheme);
        else
            setTheme(R.style.DayTheme);

        setContentView(R.layout.activity_load);

        //应用启动初始化环境变量
        myApplication = new MyApplication(this);
        myApplication.initEnv();

        //找到两种字体
        AssetManager assetManager = getAssets();
        Typeface tf_mvboli = Typeface.createFromAsset(assetManager, "fonts/mvboli.ttf");
        Typeface tf_hobostd = Typeface.createFromAsset(assetManager, "fonts/HoboStd.otf");

        //找到speak
        tv_speak1 = (TextView) findViewById(R.id.load_tv_speak1);
        tv_speak2 = (TextView) findViewById(R.id.load_tv_speak2);
        tv_speak3 = (TextView) findViewById(R.id.load_tv_speak3);
        tv_speak_author = (TextView) findViewById(R.id.load_tv_speak_author);
        tv_speak1.setTypeface(tf_mvboli);
        tv_speak2.setTypeface(tf_mvboli);
        tv_speak3.setTypeface(tf_mvboli);
        tv_speak_author.setTypeface(tf_mvboli);

        //找到version,并设置字体
        tv_version = (TextView) findViewById(R.id.load_tv_version);
        tv_version.setText(MyApplication.myApplicationVersion);
        tv_version.setTypeface(tf_hobostd);

        //find app_name, and setTypeface
        tv_app_name = (TextView) findViewById(R.id.load_tv_app_name);
        tv_app_name.setTypeface(tf_hobostd);

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
