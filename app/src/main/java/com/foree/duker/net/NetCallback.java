package com.foree.duker.net;

/**
 * Created by foree on 16-7-15.
 */
public interface NetCallback<T> {
    void onSuccess(T data);
    void onFail(String msg);
}
