package org.foree.duker.base;

import android.app.Application;

/**
 * Created by foree on 16-7-15.
 */
public class BaseApplication extends Application {
    private static BaseApplication mInstance;

    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
    }
    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }
}
