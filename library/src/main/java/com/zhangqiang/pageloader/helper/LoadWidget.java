package com.zhangqiang.pageloader.helper;

public abstract class LoadWidget {

    private Callback callback;

    void notifyLoadPage(){
        if (callback != null) {
            callback.onLoadPage();
        }
    }

    public void setLoadComplete() {
        onLoadComplete();
    }

    protected abstract void onLoadComplete();
    protected abstract boolean isEnable();

    public void setLoadFail(Throwable e) {
        onLoadFail(e);
    }

    protected abstract void onLoadFail(Throwable e);

    public void setLoadSuccess() {

        onLoadSuccess();
    }

    protected abstract void onLoadSuccess();

    public interface Callback{

        void onLoadPage();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setEnable(boolean enable){
        if (isEnable() != enable) {
            onEnableChanged(enable);
        }
    }

    protected abstract void onEnableChanged(boolean enable);
}
