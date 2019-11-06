package com.zhangqiang.pageloader.ptr;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zhangqiang.pageloader.ptr.loadmore.LoadMoreWidget;
import com.zhangqiang.pageloader.ptr.refresh.RefreshWidget;

import io.reactivex.Observable;

public final class PtrLoadHelper<T> implements RefreshWidget.OnRefreshListener, LoadMoreWidget.OnLoadMoreListener {

    private PtrLoader<T> pageLoader;
    private RefreshWidget refreshWidget;
    private LoadMoreWidget loadMoreWidget;
    private Callback<T> mCallback;
    private static final String EXTRA_KEY_AUTO_REFRESH = "auto_refresh";

    public PtrLoadHelper(int pageSize, final RefreshWidget refreshWidget, final LoadMoreWidget loadMoreWidget, final DataSource<T> dataSource) {
        this.pageLoader = new PtrLoader<T>(pageSize) {
            @NonNull
            @Override
            protected Observable<T> getRefreshDataSource(int pageIndex, int pageSize, int offset, int length, @Nullable Bundle extra) {
                return dataSource.getRefreshDataSource(pageIndex, pageSize, offset, length, extra, isAutoRefresh(extra));
            }

            @NonNull
            @Override
            protected Observable<T> getLoadMoreDataSource(@NonNull T t, int pageIndex, int pageSize, int offset, int length, @Nullable Bundle extra) {
                return dataSource.getLoadMoreDataSource(t, pageIndex, pageSize, offset, length, extra);
            }
        };
        this.refreshWidget = refreshWidget;
        this.loadMoreWidget = loadMoreWidget;
        refreshWidget.setOnRefreshListener(this);
        loadMoreWidget.setOnLoadMoreListener(this);
        PtrLoader.Callback<T> callback = new PtrLoader.Callback<T>() {
            @Override
            public void onRefreshStart(@Nullable Bundle extra) {
                if (mCallback != null) {
                    mCallback.onRefreshStart(extra, isAutoRefresh(extra));
                }
            }

            @Override
            public void onRefreshSuccess(@NonNull T t, @Nullable Bundle extra) {
                if (mCallback != null) {
                    mCallback.onRefreshSuccess(t, extra, isAutoRefresh(extra));
                }
            }

            @Override
            public void onRefreshFail(Throwable e, @Nullable Bundle extra) {
                if (mCallback != null) {
                    mCallback.onRefreshFail(e, extra, isAutoRefresh(extra));
                }
                refreshWidget.setRefreshError(e);
            }

            @Override
            public void onRefreshComplete(@Nullable Bundle extra) {
                if (mCallback != null) {
                    mCallback.onRefreshComplete(extra, isAutoRefresh(extra));
                }
                refreshWidget.setRefreshComplete();
            }

            @Override
            public void onLoadMoreStart(@Nullable Bundle extra) {
                if (mCallback != null) {
                    mCallback.onLoadMoreStart(extra);
                }
            }

            @Override
            public void onLoadMoreSuccess(@NonNull T t, @Nullable Bundle extra) {
                if (mCallback != null) {
                    mCallback.onLoadMoreSuccess(t, extra);
                }
            }

            @Override
            public void onLoadMoreFail(Throwable e, @Nullable Bundle extra) {
                if (mCallback != null) {
                    mCallback.onLoadMoreFail(e, extra);
                }
                loadMoreWidget.setLoadMoreError(e);
            }

            @Override
            public void onLoadMoreComplete(@Nullable Bundle extra) {
                if (mCallback != null) {
                    mCallback.onLoadMoreComplete(extra);
                }
                loadMoreWidget.setLoadMoreComplete();
            }

        };
        pageLoader.setCallback(callback);
    }


    @Override
    public void onLoadMore() {
        pageLoader.loadMore(null);
    }

    @Override
    public void onRefresh() {
        refreshInternal(null, false);
    }

    public RefreshWidget getRefreshWidget() {
        return refreshWidget;
    }

    public LoadMoreWidget getLoadMoreWidget() {
        return loadMoreWidget;
    }

    public void setCallback(Callback<T> callback) {
        this.mCallback = callback;
    }

    public void autoRefresh(Bundle extra) {
        refreshInternal(extra, true);
    }

    public void autoRefresh() {
        autoRefresh(null);
    }

    private void refreshInternal(Bundle extra, boolean autoRefresh) {
        Bundle extraInner;
        if (extra != null) {
            extraInner = new Bundle(extra);
        } else {
            extraInner = new Bundle();
        }
        extraInner.putBoolean(EXTRA_KEY_AUTO_REFRESH, autoRefresh);
        pageLoader.refresh(extraInner);
    }


    public interface DataSource<T> {

        @NonNull
        Observable<T> getRefreshDataSource(int pageIndex, int pageSize, int offset, int length, @Nullable Bundle extra, boolean autoRefresh);

        @NonNull
        Observable<T> getLoadMoreDataSource(@NonNull T t, int pageIndex, int pageSize, int offset, int length, Bundle extra);
    }


    public interface Callback<T> {

        void onRefreshStart(@Nullable Bundle extra, boolean autoRefresh);

        void onRefreshSuccess(@NonNull T t, @Nullable Bundle extra, boolean autoRefresh);

        void onRefreshFail(Throwable e, @Nullable Bundle extra, boolean autoRefresh);

        void onRefreshComplete(@Nullable Bundle extra, boolean autoRefresh);

        void onLoadMoreStart(@Nullable Bundle extra);

        void onLoadMoreSuccess(@NonNull T t, @Nullable Bundle extra);

        void onLoadMoreFail(Throwable e, @Nullable Bundle extra);

        void onLoadMoreComplete(@Nullable Bundle extra);
    }

    private boolean isAutoRefresh(Bundle extra) {
        return extra != null && extra.getBoolean(EXTRA_KEY_AUTO_REFRESH);
    }
}
