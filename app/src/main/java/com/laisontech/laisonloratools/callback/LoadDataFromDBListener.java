package com.laisontech.laisonloratools.callback;

/**
 * Created by SDP
 * on 2019/5/24
 * Des：
 */
public interface LoadDataFromDBListener<T> {
    void startLoading();
    void loadDataFinished(T object);
}