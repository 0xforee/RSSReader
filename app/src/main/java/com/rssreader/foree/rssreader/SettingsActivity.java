package com.rssreader.foree.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

/**
 * Created by foree on 3/11/15.
 * settings的activity
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 使用fragment来替换主界面的activity
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}