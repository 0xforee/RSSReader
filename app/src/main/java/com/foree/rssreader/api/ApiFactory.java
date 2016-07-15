package com.foree.rssreader.api;

/**
 * Created by foree on 16-7-15.
 */
public class ApiFactory extends AbsApiFactory{
    @Override
    public <T extends AbsApiHelper> T createApiHelper(Class<T> apiHelper) {
        return null;
    }
}
