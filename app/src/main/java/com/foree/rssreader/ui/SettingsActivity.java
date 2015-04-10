package com.foree.rssreader.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    public static final String TAG = "SettingsActivity";

    //开发者的邮箱信息
    public static String MY_EMAIL = "beijing.2008.lm@163.com";

    //preference的key
    public static final String KEY_EDIT_ACCOUNT = "lp_editaccount";
    public static final String KEY_LOGOUT = "md_logout";
    public static final String KEY_DARK_THEME = "lp_darktheme";
    public static final String KEY_DOWNLOAD_ON_WIFI = "lp_downloadonwifi";
    public static final String KEY_CLEAN_CACHE = "md_cleancache";
    public static final String KEY_CONTACT_ME = "md_contactme";
    public static final String KEY_VERSION_NAME = "lp_versionnumber";

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

        if (key.equals(SettingsActivity.KEY_DARK_THEME)) {
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
            //设置VersionName
            findPreference(SettingsActivity.KEY_VERSION_NAME).setTitle(MyApplication.myVersionName);
        }
    }
}
