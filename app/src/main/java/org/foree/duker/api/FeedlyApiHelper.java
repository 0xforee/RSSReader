package org.foree.duker.api;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.foree.duker.net.NetCallback;
import org.foree.duker.rssinfo.RssCategory;
import org.foree.duker.rssinfo.RssFeedInfo;
import org.foree.duker.rssinfo.RssItemInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by foree on 16-7-15.
 * Feedly相关的接口显示
 */
public class FeedlyApiHelper extends AbsApiHelper {
    private static final String TAG = FeedlyApiHelper.class.getSimpleName();
    private static final String API_HOST_URL = "http://cloud.feedly.com";
    private static final String API_TOKEN_TEST = "A3wMXqyNgMOZwCqIoBC5OZoKdSyKemk1IYWp12rk86Kb7KIBHlUBER2Pe2PWaro4Ur_0Rq1h8MiqQBFE_uly7A6GNbjtT5wWbIF5rf6haQetytQcjZj6_FSDSTrkmF3y5CclNtH3q_6UlK1kPPY0i4_CXXIkhIrT7aTJRUTry3b-HGvq_rwWK7JFewguG4PvV7EMozQuosYKOcMrcd3cGwmYsToq8hc:feedlydev";
    private static final String API_CATEGORIES_URL = "/v3/categories";
    private static final String API_SUBSCRIPTIONS_URL = "/v3/subscriptions";
    private static final String API_STREAM_IDS_URL = " /v3/streams/ids?streamId=:streamId";
    private static final String API_STREAM_CONTENTS_URL = "GET /v3/streams/contents?streamId=:streamId";

    @Override
    public void getCategoriesList(String token, final NetCallback<RssCategory> netCallback) {
        token = API_TOKEN_TEST;

        String url = API_HOST_URL + API_CATEGORIES_URL;

        final Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","OAuth " + token);
        NetWorkApiHelper.newInstance().getRequest(url, headers, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, response);
                if (netCallback != null){
                    netCallback.onSuccess(parseCategories(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        });
    }

    @Override
    public void getSubscriptions(String token, NetCallback<RssFeedInfo> netCallback) {

    }

    @Override
    public void getStream(String token, NetCallback<RssItemInfo> netCallback) {

    }

    private List<RssCategory> parseCategories(String data){
        List<RssCategory> rssCategories = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for( int js_i = 0; js_i < jsonArray.length(); js_i++){
                RssCategory rssCategory = new RssCategory();
                JSONObject jsonObject = jsonArray.getJSONObject(js_i);
                rssCategory.setCategoryId(jsonObject.getString("id"));
                rssCategory.setLable(jsonObject.getString("label"));

                rssCategories.add(rssCategory);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rssCategories;
    }
}
