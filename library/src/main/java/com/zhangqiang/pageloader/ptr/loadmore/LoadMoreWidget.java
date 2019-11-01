package com.zhangqiang.pageloader.ptr.loadmore;

public abstract class LoadMoreWidget {

    private OnLoadMoreListener onLoadMoreListener;

    public LoadMoreWidget() {
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoadMoreComplete() {
        onLoadMoreComplete();
    }

    protected abstract void onLoadMoreComplete();

    protected abstract void onLoadMoreError(Throwable e);

    protected abstract void onEnableChanged(boolean enable);

    public abstract boolean isEnable();

    public void setEnable(boolean enable) {
        if (isEnable() != enable) {
            onEnableChanged(enable);
        }
    }


    public void setLoadMoreError(Throwable e) {
        onLoadMoreError(e);
        onLoadMoreComplete();
    }

    public interface OnLoadMoreListener{

        void onLoadMore();
    }

    protected final void notifyLoadMore(){
        onLoadMore();
        if (onLoadMoreListener != null) {
            onLoadMoreListener.onLoadMore();
        }
    }

    protected void onLoadMore(){

    }
}
