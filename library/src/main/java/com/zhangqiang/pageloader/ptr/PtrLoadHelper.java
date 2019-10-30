package com.zhangqiang.pageloader.ptr;

import android.support.annotation.Nullable;

import com.zhangqiang.pageloader.ptr.loadmore.LoadMoreWidget;
import com.zhangqiang.pageloader.ptr.refresh.RefreshWidget;

public class PtrLoadHelper<T> implements RefreshWidget.OnRefreshListener, LoadMoreWidget.OnLoadMoreListener {

    private PtrLoader<T> pageLoader;
    private RefreshWidget refreshWidget;
    private LoadMoreWidget loadMoreWidget;
    private Callback<T> mCallback;
    private boolean refreshing;

    public PtrLoadHelper(final PtrLoader<T> pageLoader, final RefreshWidget refreshWidget, final LoadMoreWidget loadMoreWidget) {
        this.pageLoader = pageLoader;
        this.refreshWidget = refreshWidget;
        this.loadMoreWidget = loadMoreWidget;
        refreshWidget.setOnRefreshListener(this);
        loadMoreWidget.setOnLoadMoreListener(this);
        Callback<T> callback = new Callback<T>() {
            @Override
            public void onRefreshSuccess(@Nullable T t) {
                refreshing = false;
                if (mCallback != null) {
                    mCallback.onRefreshSuccess(t);
                }
                refreshWidget.setRefreshComplete();
            }

            @Override
            public void onRefreshFail(Throwable e) {
                refreshing = false;
                if (mCallback != null) {
                    mCallback.onRefreshFail(e);
                }
                refreshWidget.setRefreshError(e);
            }

            @Override
            public void onLoadMoreSuccess(@Nullable T t) {
                if (mCallback != null) {
                    mCallback.onLoadMoreSuccess(t);
                }
                loadMoreWidget.setLoadMoreComplete();
            }

            @Override
            public void onLoadMoreFail(Throwable e) {
                if (mCallback != null) {
                    mCallback.onLoadMoreFail(e);
                }
                loadMoreWidget.setLoadMoreError(e);
            }
        };
        pageLoader.setCallback(callback);
    }


    @Override
    public void onLoadMore() {
        pageLoader.loadMore();
    }

    @Override
    public void onRefresh() {
        refresh();
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

    public void refresh() {
        if (refreshing) {
            return;
        }
        refreshing = true;
        pageLoader.refresh();
    }

    public boolean isRefreshing() {
        return refreshing;
    }
}
