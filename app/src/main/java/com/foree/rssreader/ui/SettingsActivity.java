package com.foree.rssreader.ui;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;

import com.foree.rssreader.base.BaseActivity;
import com.foree.rssreader.base.MyApplication;
import com.rssreader.foree.rssreader.R;

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

    //settingFragment的内部类实现
    public static class SettingsFragment extends PreferenceFragment {
        private static final String TAG = "SettingsFragment";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // 从资源文件中加载一个配置项
            addPreferencesFromResource(R.xml.preferences);
            ListPreference listPreference = (ListPreference) findPreference("lp_versionnumber");
            listPreference.setTitle(MyApplication.myVersionName);
        }
    }
}
