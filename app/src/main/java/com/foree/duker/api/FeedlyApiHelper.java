package com.foree.duker.api;

import com.foree.duker.net.NetCallback;
import com.foree.duker.rssinfo.RssCategory;
import com.foree.duker.rssinfo.RssFeedInfo;
import com.foree.duker.rssinfo.RssItemInfo;

/**
 * Created by foree on 16-7-15.
 */
public class FeedlyApiHelper extends AbsApiHelper {
    private static final String API_TOKEN = "A3wMXqyNgMOZwCqIoBC5OZoKdSyKemk1IYWp12rk86Kb7KIBHlUBER2Pe2PWaro4Ur_0Rq1h8MiqQBFE_uly7A6GNbjtT5wWbIF5rf6haQetytQcjZj6_FSDSTrkmF3y5CclNtH3q_6UlK1kPPY0i4_CXXIkhIrT7aTJRUTry3b-HGvq_rwWK7JFewguG4PvV7EMozQuosYKOcMrcd3cGwmYsToq8hc:feedlydev";
    private static final String API_CATEGORIES_URL = "/v3/categories";
    private static final String API_SUBSCRIPTIONS_URL = "/v3/subscriptions";
    private static final String API_STREAM_IDS_URL = " /v3/streams/ids?streamId=:streamId";
    private static final String API_STREAM_CONTENTS_URL = "GET /v3/streams/contents?streamId=:streamId";


    @Override
    public void getCategoriesList(String token, NetCallback<RssCategory> netCallback) {

    }

    @Override
    public void getSubscriptions(String token, NetCallback<RssFeedInfo> netCallback) {

    }

    @Override
    public void getStream(String token, NetCallback<RssItemInfo> netCallback) {

    }
}
