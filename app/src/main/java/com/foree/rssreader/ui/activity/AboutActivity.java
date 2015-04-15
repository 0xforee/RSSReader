package com.foree.rssreader.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.foree.rssreader.base.BaseActivity;
import com.foree.rssreader.ui.fragment.AboutFragment;
import com.rssreader.foree.rssreader.R;

/**
 * Created by foree on 4/15/15.
 * about Activity
 */
public class AboutActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //add actionBar title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.action_settings);
        // using fragment replace settings UI
        getFragmentManager().beginTransaction()
                .replace(R.id.fr_settings, new AboutFragment())
                .commit();
    }
}
