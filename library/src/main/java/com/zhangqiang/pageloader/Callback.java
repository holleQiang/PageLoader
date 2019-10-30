package com.zhangqiang.pageloader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface Callback<T> {

    void onLoadNextPageSuccess(@Nullable T t,@Nullable T last);

    void onLoadNextPageFail(Throwable e,@Nullable T last);

    void onLoadPreviousPageSuccess(@Nullable T t,@NonNull T last);

    void onLoadPreviousPageFail(Throwable e,@NonNull T last);

}
