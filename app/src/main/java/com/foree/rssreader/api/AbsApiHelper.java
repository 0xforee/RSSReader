package com.foree.rssreader.api;


import com.foree.rssreader.net.NetCallback;
import com.foree.rssreader.rssinfo.RssCategory;
import com.foree.rssreader.rssinfo.RssFeedInfo;
import com.foree.rssreader.rssinfo.RssItemInfo;

/**
 * Created by foree on 16-7-15.
 */
public abstract class AbsApiHelper {
    public abstract void getCategoriesList(String token, NetCallback<RssCategory> netCallback);
    public abstract void getSubscriptions(String token, NetCallback<RssFeedInfo> netCallback);
    public abstract void getStream(String token, NetCallback<RssItemInfo> netCallback);
}
