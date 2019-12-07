package com.zhangqiang.pageloader.helper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class SimpleCallback<T> implements PageLoadHelper.Callback<T> {

    @Override
    public void onLoadFirstPageStart(@Nullable Bundle extra, boolean autoLoad) {

    }

    @Override
    public void onLoadFirstPageSuccess(@NonNull T t, @Nullable Bundle extra, boolean autoLoad) {

    }

    @Override
    public void onLoadFirstPageFail(Throwable e, @Nullable Bundle extra, boolean autoLoad) {

    }

    @Override
    public void onLoadFirstPageComplete(@Nullable Bundle extra, boolean autoLoad) {

    }

    @Override
    public void onLoadNextPageStart(@Nullable Bundle extra) {

    }

    @Override
    public void onLoadNextPageSuccess(@NonNull T t, @Nullable Bundle extra) {

    }

    @Override
    public void onLoadNextPageFail(Throwable e, @Nullable Bundle extra) {

    }

    @Override
    public void onLoadNextPageComplete(@Nullable Bundle extra) {

    }

    @Override
    public void onLoadPreviousPageStart(@Nullable Bundle extra) {

    }

    @Override
    public void onLoadPreviousPageSuccess(@NonNull T t, @Nullable Bundle extra) {

    }

    @Override
    public void onLoadPreviousPageFail(Throwable e, @Nullable Bundle extra) {

    }

    @Override
    public void onLoadPreviousPageComplete(@Nullable Bundle extra) {

    }
}
