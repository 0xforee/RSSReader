package com.rssreader.foree.rssreader;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by foree on 3/11/15.
 * fragment
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 从资源文件中加载一个配置项
        addPreferencesFromResource(R.xml.preferences);
    }
}