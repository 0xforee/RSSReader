package com.baseapplication.foree.rssreader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.rssreader.foree.rssreader.R;

/**
 * Created by foree on 15-3-10.
 * 所有activity的基类，用于实现一些所有activity共有的方法，比如设置主题
 */
public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setContentView(int layoutResID) {
        //根据配置文件设置相应的主题
        SharedPreferences sp = this.getSharedPreferences("settings", MODE_PRIVATE);
        if (sp.getBoolean("darktheme", false))
            setTheme(R.style.NightTheme);
        else
            setTheme(R.style.DayTheme);

        super.setContentView(layoutResID);
    }
}
