package com.zhangqiang.pageloader.ptr.refresh;

import android.support.v4.widget.SwipeRefreshLayout;

public class SwipeRefreshWidget extends RefreshWidget {

    private SwipeRefreshLayout refreshLayout;

    public SwipeRefreshWidget(SwipeRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                notifyRefresh();
            }
        });
    }

    @Override
    protected void onRefreshError(Throwable e) {

    }

    @Override
    protected void onRefreshComplete() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public boolean isEnable() {
        return refreshLayout.isEnabled();
    }

    @Override
    protected void onEnableChanged(boolean enable) {
        refreshLayout.setEnabled(enable);
    }

}
