package com.utils.foree.rssreader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by foree on 3/6/15.
 * 网络工具类
 * <p/>
 * 判断网络状况，并返回当前的状态:
 * 1)wifi
 * 2)移动数据
 * 3)无网络连接
 */
public class NetworkUtils {
    //无网络
    public static final int NETWORK_NONE = -1;
    //当前网络为wifi
    public static final int NETWORK_WIFI = 0;
    //当前网络为移动数据
    public static final int NETWORK_MOBILE = 1;

    public static int getNetworkState(Context context) {
        //获取网络服务
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //当前为wifi
        NetworkInfo.State state = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING)
            return NETWORK_WIFI;

        //当前为移动数据
        state = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING)
            return NETWORK_MOBILE;

        //无网络连接
        return NETWORK_NONE;
    }
}
