package com.rssreader.foree.rssreader;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.CheckBox;

import com.baseapplication.foree.rssreader.BaseActivity;

import java.util.Map;
import java.util.Set;

/**
 * Created by foree on 3/5/15.
 * 设置中的信息显示
 */
public class Settings extends BaseActivity {
    private static final String TAG = "Settings";
    CheckBox checkBox;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        checkBox = (CheckBox) findViewById(R.id.cb_darktheme);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //退出activity时执行
        Log.v(TAG, "onPause");
        //退出时保存设置参数
        sp = this.getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (checkBox.isChecked())
            editor.putBoolean("darktheme", true);
        else
            editor.putBoolean("darktheme", false);

        editor.apply();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //重新进入activity执行
        Log.v(TAG, "onResume");
        //恢复时读取设置参数
        sp = this.getSharedPreferences("settings", MODE_PRIVATE);
        boolean darktheme = sp.getBoolean("darktheme", false);
        checkBox.setChecked(darktheme);
    }
}

