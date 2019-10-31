package com.zhangqiang.pageloader.ptr;

import android.os.Bundle;
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
        protected Observable<T> getFirstPageDataSource(@Nullable Bundle extra) {
            return getRefreshDataSource(0, pageSize, 0, length,extra);
        }

        @NonNull
        @Override
        protected Observable<T> getPreviousPageDataSource(@NonNull T t, @Nullable Bundle extra) {
            return Observable.empty();
        }

        @NonNull
        @Override
        protected Observable<T> getNextPageDataSource(@NonNull T t, @Nullable Bundle extra) {
            return getLoadMoreDataSource(t, pageIndex + 1, pageSize, offset + length, length,extra);
        }
    };


    @NonNull
    protected abstract Observable<T> getRefreshDataSource(int pageIndex, int pageSize, int offset, int length,@Nullable Bundle extra);

    @NonNull
    protected abstract Observable<T> getLoadMoreDataSource(@NonNull T t, int pageIndex, int pageSize, int offset, int length,@Nullable Bundle extra);


    public PtrLoader(int pageSize) {
        this.length = this.pageSize = pageSize;
        mPageLoader.setCallback(new PageLoader.Callback<T>() {
            @Override
            public void onLoadFirstPageSuccess(@NonNull T t, @Nullable Bundle extra) {
                pageIndex = 0;
                offset = 0;
                if (callback != null) {
                    callback.onRefreshSuccess(t,extra);
                }
            }

            @Override
            public void onLoadFirstPageFail(Throwable e, @Nullable Bundle extra) {
                if (callback != null) {
                    callback.onRefreshFail(e,extra);
                }
            }

            @Override
            public void onLoadFirstPageComplete(@Nullable Bundle extra) {
                if (callback != null) {
                    callback.onRefreshComplete(extra);
                }
            }

            @Override
            public void onLoadNextPageSuccess(@NonNull T t, @Nullable Bundle extra) {
                pageIndex++;
                offset += length;
                if (callback != null) {
                    callback.onLoadMoreSuccess(t,extra);
                }
            }

            @Override
            public void onLoadNextPageFail(Throwable e, @Nullable Bundle extra) {
                if (callback != null) {
                    callback.onLoadMoreFail(e,extra);
                }
            }

            @Override
            public void onLoadNextPageComplete(@Nullable Bundle extra) {
                if (callback != null) {
                    callback.onLoadMoreComplete(extra);
                }
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

        });
    }

    public void refresh(Bundle extra) {
        if (callback != null) {
            callback.onRefreshStart(extra);
        }
        mPageLoader.reset();
        mPageLoader.loadFirstPage(extra);
    }


    public void loadMore(Bundle extra) {
        if (callback != null) {
            callback.onLoadMoreStart(extra);
        }
        mPageLoader.loadNextPage(extra);
    }

    public void setCallback(Callback<T> callback) {
        this.callback = callback;
    }

    public interface Callback<T> {

        void onRefreshStart(@Nullable Bundle extra);

        void onRefreshSuccess(@NonNull T t,@Nullable Bundle extra);

        void onRefreshFail(Throwable e,@Nullable Bundle extra);

        void onRefreshComplete(@Nullable Bundle extra);

        void onLoadMoreStart(@Nullable Bundle extra);

        void onLoadMoreSuccess(@NonNull T t,@Nullable Bundle extra);

        void onLoadMoreFail(Throwable e,@Nullable Bundle extra);

        void onLoadMoreComplete(@Nullable Bundle extra);
    }
}
