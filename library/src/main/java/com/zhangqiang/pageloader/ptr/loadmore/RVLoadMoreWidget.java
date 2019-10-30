package com.zhangqiang.pageloader.ptr.loadmore;

import android.support.v7.widget.RecyclerView;

public class RVLoadMoreWidget extends LoadMoreWidget {

    private LoadMoreHelper loadMoreHelper;

    public RVLoadMoreWidget(RecyclerView recyclerView) {
        loadMoreHelper = new LoadMoreHelper(recyclerView) {
            @Override
            protected void onLoadMore() {
                notifyLoadMore();
            }
        };
    }

    @Override
    protected void onLoadMoreComplete() {
        loadMoreHelper.finishLoadMore();
    }

    @Override
    protected void onLoadMoreError(Throwable e) {

    }

    @Override
    protected void onEnableChanged(boolean enable) {
        loadMoreHelper.setLoadMoreEnable(enable);
    }

    @Override
    public boolean isEnable() {
        return loadMoreHelper.isLoadMoreEnable();
    }

}
