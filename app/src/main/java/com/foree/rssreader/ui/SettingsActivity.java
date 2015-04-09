package com.foree.rssreader.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.util.Log;

import com.foree.rssreader.base.BaseActivity;
import com.foree.rssreader.base.MyApplication;
import com.rssreader.foree.rssreader.R;

/**
 * Created by foree on 3/11/15.
 * settings的activity
 */
public class SettingsActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //添加ActionBar的标题
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.action_settings);
        // 使用fragment来替换主界面的fragment
        getFragmentManager().beginTransaction()
                .replace(R.id.fr_settings, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //解除注册
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册sharePreference监听器
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals("lp_darktheme")) {
            Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);

            finish();
            overridePendingTransition(0, 0);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            // overridePendingTransition(R.anim.stay, R.anim.alphaout);
            Log.v(TAG, "onSharedPreferenceChanged");
        }
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
