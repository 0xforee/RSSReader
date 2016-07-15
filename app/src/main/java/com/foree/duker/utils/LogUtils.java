package com.foree.duker.utils;

import android.util.Log;

/**
 * Created by foree on 4/14/15.
 * Log的控制类别:
 * 实现原则:
 * 1.Verbose级别的不要编译到apk中
 * 2.Debug级别的不要在运行时显示
 * 3.Info,Warn,Error级别的总是显示
 */
public class LogUtils {
    //if print log?
    public static final boolean isCompilerLog = true;

    //control what level you want to print
    public static int logLevel = Log.VERBOSE;

    //print Verbose log
    public static void v(String TAG, String msg) {
        if (Log.VERBOSE >= logLevel)
            Log.v(TAG, msg);
    }

    //print Debug log
    public static void d(String TAG, String msg) {
        if (Log.DEBUG >= logLevel)
            Log.d(TAG, msg);
    }

    //print Info log
    public static void i(String TAG, String msg) {
        if (Log.INFO >= logLevel)
            Log.i(TAG, msg);
    }

    //print Warn log
    public static void w(String TAG, String msg) {
        if (Log.WARN >= logLevel)
            Log.w(TAG, msg);
    }

    //print Error log
    public static void e(String TAG, String msg) {
        if (Log.ERROR >= logLevel)
            Log.e(TAG, msg);
    }
}
