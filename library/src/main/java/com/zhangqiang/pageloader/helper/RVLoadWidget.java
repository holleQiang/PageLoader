package com.zhangqiang.pageloader.helper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

public abstract class RVLoadWidget extends LoadWidget {

    private RecyclerView recyclerView;
    private boolean isLoading;
    private boolean isEnable = true;

    public RVLoadWidget(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading || !isEnable) {
                    return;
                }
                boolean readyFormLoad = isReadyFormLoad(recyclerView,dx,dy);
                if (readyFormLoad) {
                    recyclerView.removeCallbacks(loadTask);
                    recyclerView.post(loadTask);
                    isLoading = true;
                }
            }
        });
    }

    protected abstract boolean isReadyFormLoad(RecyclerView recyclerView, int dx, int dy);

    @Override
    protected void onLoadComplete() {
        isLoading = false;
        recyclerView.removeCallbacks(loadTask);
    }

    @Override
    protected boolean isEnable() {
        return isEnable;
    }

    @Override
    protected void onLoadFail(Throwable e) {

    }

    @Override
    protected void onLoadSuccess() {

    }

    @Override
    protected void onEnableChanged(boolean enable) {
        isEnable = enable;
    }

    private Runnable loadTask = new Runnable() {
        @Override
        public void run() {
            notifyLoadPage();
        }
    };
}
