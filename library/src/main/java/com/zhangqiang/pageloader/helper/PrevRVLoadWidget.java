package com.zhangqiang.pageloader.helper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public class PrevRVLoadWidget extends RVLoadWidget {

    private int restItemToLoad;
    private int mLastPosition = -1;

    public PrevRVLoadWidget(RecyclerView recyclerView, int restItemToLoad) {
        super(recyclerView);
        this.restItemToLoad = restItemToLoad;
    }

    @Override
    protected boolean isReadyFormLoad(RecyclerView recyclerView, int dx, int dy) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null) {
            return false;
        }
        int firstVisibleItemPosition = findFirstVisibleItemPosition(layoutManager);
        final int lastPosition = mLastPosition;
        mLastPosition = firstVisibleItemPosition;
        return lastPosition != firstVisibleItemPosition && firstVisibleItemPosition <= restItemToLoad;
    }

    private int findFirstVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }else if(layoutManager instanceof StaggeredGridLayoutManager){
            int[] positions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(positions);
            int min = Integer.MAX_VALUE;
            for (int position : positions) {
                if (position < min) {
                    min = position;
                }
            }
            return min;
        }else {
            throw  new IllegalArgumentException("cannot support layout manager for" + layoutManager);
        }
    }
}
