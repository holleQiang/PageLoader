package com.zhangqiang.pageloader.ptr;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zhangqiang.pageloader.PageLoader;

import io.reactivex.Observable;

public abstract class PtrLoader<T> {

    private int offset;
    private final int length;
    private int pageIndex;
    private final int pageSize;
    private Callback<T> callback;

    private PageLoader<T> mPageLoader = new PageLoader<T>() {
        @NonNull
        @Override
        protected Observable<T> getPreviousPageDataSource(@NonNull T t) {
            return null;
        }

        @NonNull
        @Override
        protected Observable<T> getNextPageDataSource(@Nullable T t) {
            if (t == null) {
                return getRefreshDataSource(0, pageSize, 0, length);
            } else {
                return getLoadMoreDataSource(t, pageIndex + 1, pageSize, offset + length, length);
            }
        }
    };


    @NonNull
    protected abstract Observable<T> getRefreshDataSource(int pageIndex, int pageSize, int offset, int length);

    @NonNull
    protected abstract Observable<T> getLoadMoreDataSource(@NonNull T t, int pageIndex, int pageSize, int offset, int length);


    public PtrLoader(int pageSize) {
        this.length = this.pageSize = pageSize;
        mPageLoader.setCallback(new com.zhangqiang.pageloader.Callback<T>() {
            @Override
            public void onLoadNextPageSuccess(T t, T last) {
                if (last == null) {

                    pageIndex = 0;
                    offset = 0;
                    if (callback != null) {
                        callback.onRefreshSuccess(t);
                    }
                } else {

                    pageIndex++;
                    offset += length;
                    if (callback != null) {
                        callback.onLoadMoreSuccess(t);
                    }
                }
            }

            @Override
            public void onLoadNextPageFail(Throwable e, T last) {
                if (last == null) {
                    if (callback != null) {
                        callback.onRefreshFail(e);
                    }
                } else {
                    if (callback != null) {
                        callback.onLoadMoreFail(e);
                    }
                }
            }

            @Override
            public void onLoadPreviousPageSuccess(T t, T last) {

            }

            @Override
            public void onLoadPreviousPageFail(Throwable e, T last) {

            }
        });
    }

    public void refresh() {
        mPageLoader.reset();
        mPageLoader.nextPage();
    }

    public void loadMore() {
        mPageLoader.nextPage();
    }

    public void setCallback(Callback<T> callback) {
        this.callback = callback;
    }
}
