package org.foree.duker.utils;

import android.app.Activity;
import android.content.res.TypedArray;

import com.rssreader.foree.rssreader.R;

/**
 * Created by foree on 4/13/15.
 * 用于获取windows的一些系统参数
 */
public class WindowUtils {
    public static WindowUtils mWindowUtils;

    public static WindowUtils getInstance() {
        if (mWindowUtils == null) {
            mWindowUtils = new WindowUtils();
        }
        return mWindowUtils;
    }

    //获取ActionBar的高度
    public static int getactionBarHeight(Activity activity) {
        TypedArray actionbarSizeTypedArray = activity.obtainStyledAttributes(new int[]{
                android.R.attr.actionBarSize
        });
        return (int) actionbarSizeTypedArray.getDimension(0, 0);
    }

    //获取状态栏status的高度
    public int getStatusBarHeight(Activity activity) {
        int result = 0;
        //取得状态栏的高度的资源ID
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //从资源中获取数值
            result = activity.getResources().getDimensionPixelOffset(resourceId);
        }
        return result;
    }

    //获取总的偏移量
    public int getPadding(Activity activity) {
        return getactionBarHeight(activity)
                + getStatusBarHeight(activity)
                + activity.getResources().getDimensionPixelOffset(R.dimen.user_define_actionBar_padding);
    }
}
