package com.rssreader.foree.rssreader;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.baseapplication.foree.rssreader.BaseActivity;

/**
 * Created by foree on 3/11/15.
 * settings的activity
 */
public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        //添加ActionBar的标题
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.action_settings);
        // 使用fragment来替换主界面的fragment
        getFragmentManager().beginTransaction()
                .replace(R.id.fr_settings, new SettingsFragment())
                .commit();
    }
}