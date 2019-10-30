package com.zhangqiang.pageloader;

public class LoadWidget {

    private OnLoadListener onLoadListener;


    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }


    public void notifyLoad(){
        if (onLoadListener != null) {
            onLoadListener.onLoad();
        }
    }

    public interface OnLoadListener{

        void onLoad();
    }

}
