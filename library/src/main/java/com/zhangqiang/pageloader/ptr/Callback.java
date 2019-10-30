package com.zhangqiang.pageloader.ptr;

import android.support.annotation.Nullable;

public interface Callback<T> {

    void onRefreshSuccess(@Nullable T t);

    void onRefreshFail(Throwable e);

    void onLoadMoreSuccess(@Nullable T t);

    void onLoadMoreFail(Throwable e);
}