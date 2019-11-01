package com.zhangqiang.pageloader.ptr.refresh;

public abstract class RefreshWidget {

    private OnRefreshListener mOnRefreshListener;

    public RefreshWidget() {
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.mOnRefreshListener = onRefreshListener;
    }

    public void setRefreshComplete() {
        onRefreshComplete();
    }

    protected abstract void onRefreshError(Throwable e);

    protected abstract void onRefreshComplete();

    public abstract boolean isEnable();

    protected abstract void onEnableChanged(boolean enable);

    public void setEnable(boolean enable) {
        if (isEnable() != enable) {
            onEnableChanged(enable);
        }
    }


    public void setRefreshError(Throwable e) {
        onRefreshError(e);
        onRefreshComplete();
    }


    public interface OnRefreshListener {

        void onRefresh();
    }

    protected final void notifyRefresh() {
        onRefresh();
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh();
        }
    }

    protected void onRefresh() {

    }

}
