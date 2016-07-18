package org.foree.duker.api;


import org.foree.duker.net.NetCallback;
import org.foree.duker.rssinfo.RssCategory;
import org.foree.duker.rssinfo.RssFeedInfo;
import org.foree.duker.rssinfo.RssItemInfo;

/**
 * Created by foree on 16-7-15.
 */
public abstract class AbsApiHelper {
    public abstract void getCategoriesList(String token, NetCallback<RssCategory> netCallback);
    public abstract void getSubscriptions(String token, NetCallback<RssFeedInfo> netCallback);
    public abstract void getStream(String token, String streamId, NetCallback<RssItemInfo> netCallback);
}
