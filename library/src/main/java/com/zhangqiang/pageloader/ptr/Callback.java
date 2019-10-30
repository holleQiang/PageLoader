package com.zhangqiang.pageloader.ptr;

public interface Callback<T> {

    void onRefreshSuccess(T t);

    void onRefreshFail(Throwable e);

    void onLoadMoreSuccess(T t);

    void onLoadMoreFail(Throwable e);
}