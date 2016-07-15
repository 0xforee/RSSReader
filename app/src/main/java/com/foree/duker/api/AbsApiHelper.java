package com.foree.duker.api;


import com.foree.duker.net.NetCallback;
import com.foree.duker.rssinfo.RssCategory;
import com.foree.duker.rssinfo.RssFeedInfo;
import com.foree.duker.rssinfo.RssItemInfo;

/**
 * Created by foree on 16-7-15.
 */
public abstract class AbsApiHelper {
    public abstract void getCategoriesList(String token, NetCallback<RssCategory> netCallback);
    public abstract void getSubscriptions(String token, NetCallback<RssFeedInfo> netCallback);
    public abstract void getStream(String token, NetCallback<RssItemInfo> netCallback);
}
