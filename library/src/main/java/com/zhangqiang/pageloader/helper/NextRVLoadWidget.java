package com.zhangqiang.pageloader.helper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public class NextRVLoadWidget extends RVLoadWidget {

    private int restItemToLoad;
    private int mLastPosition = -1;

    public NextRVLoadWidget(RecyclerView recyclerView, int restItemToLoad) {
        super(recyclerView);
        this.restItemToLoad = restItemToLoad;
    }

    @Override
    protected boolean isReadyFormLoad(RecyclerView recyclerView, int dx, int dy) {

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null) {
            return false;
        }
        int itemCount = layoutManager.getItemCount();
        int lastVisibleItemPosition = findLastVisibleItemPosition(layoutManager);
        final int lastPosition = mLastPosition;
        mLastPosition = lastVisibleItemPosition;
        return lastPosition != lastVisibleItemPosition && itemCount - 1 - lastVisibleItemPosition <= restItemToLoad;
    }

    private int findLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {

            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {

            int[] info = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(info);
            return findMax(info);
        } else {

            throw new IllegalArgumentException("unSupport layoutManager : " + layoutManager);
        }
    }

    private int findMax(int[] positions) {

        int max = Integer.MIN_VALUE;
        for (int a :
                positions) {

            if (a > max) {
                max = a;
            }
        }
        return max;
    }
}
