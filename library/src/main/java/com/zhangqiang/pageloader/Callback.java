package com.zhangqiang.pageloader;

public interface Callback<T> {

    void onLoadNextPageSuccess(T t, T last);

    void onLoadNextPageFail(Throwable e, T last);

    void onLoadPreviousPageSuccess(T t, T last);

    void onLoadPreviousPageFail(Throwable e, T last);

}
