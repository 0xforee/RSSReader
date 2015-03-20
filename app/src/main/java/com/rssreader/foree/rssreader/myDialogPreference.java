package com.rssreader.foree.rssreader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.utils.foree.rssreader.CacheUtils;

/**
 * Created by foree on 3/20/15.
 * preference
 */
//清除缓存的实现
public class myDialogPreference extends DialogPreference {

    private static final String TAG = "CleanCachePreference";
    private Context mContext;

    public myDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);
        switch (which) {
            case AlertDialog.BUTTON_POSITIVE:
                if (getKey().equals("md_cleancache"))
                    if (CacheUtils.cleanAllCache())
                        Toast.makeText(mContext, "清理成功", Toast.LENGTH_SHORT).show();
                if (getKey().equals("md_logout"))
                    Log.v(TAG, "log_out");
                break;
            case AlertDialog.BUTTON_NEGATIVE:
                Log.v(TAG, "取消");
        }


    }

}
