package com.foree.duker.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.util.Log;

import com.foree.duker.base.BaseActivity;
import com.foree.duker.ui.fragment.SettingsFragment;
import com.rssreader.foree.rssreader.R;

/**
 * Created by foree on 3/11/15.
 * settings activity
 */
public class SettingsActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String TAG = "SettingsActivity";

    //email address
    public static String MY_EMAIL = "beijing.2008.lm@163.com";

    /**
     * keys for Application preference
     */
    //key: edit account
    public static final String KEY_EDIT_ACCOUNT = "lp_editaccount";
    //key: log out
    public static final String KEY_LOGOUT = "md_logout";
    //key: change theme
    public static final String KEY_CHANGE_THEME = "pf_change_theme";
    //key: dark theme
    public static final String KEY_DARK_THEME = "lp_darktheme";
    //key: download only on wifi
    public static final String KEY_DOWNLOAD_ON_WIFI = "lp_downloadonwifi";
    //key: clean your cache
    public static final String KEY_CLEAN_CACHE = "md_cleancache";
    //key: contact me using email
    public static final String KEY_CONTACT_ME = "pf_contact_me";
    //key: version number
    public static final String KEY_VERSION_NAME = "pf_version_number";
    //key: application Directory
    public static final String KEY_APPLICATION_DIR = "pf_dir_application";
    //key: application Cache Directory
    public static final String KEY_APPLICATION_CACHE_DIR = "pf_dir_cache";
    //key: first run
    public static final String KEY_FIRST_RUN = "FIRST_RUN";
    //key: if user have learned open navigation drawer
    public static final String KEY_PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

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
                .replace(R.id.fr_settings, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //register sharePreference Listener
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
            Log.i(TAG, "onSharedPreferenceChanged");
        }
    }
}
