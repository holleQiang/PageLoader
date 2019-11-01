package com.zhangqiang.pageloader.ptr.loadmore;

import android.support.v7.widget.RecyclerView;

import com.zhangqiang.celladapter.cell.Cell;
import com.zhangqiang.celladapter.observable.DataList;

public abstract class RVCellLoadMoreWidget extends RVLoadMoreWidget {

    private DataList<Cell> listWidget;
    private Cell loadMoreCell;


    public RVCellLoadMoreWidget(RecyclerView recyclerView, int triggerItemCount) {
        super(recyclerView, triggerItemCount);
    }

    @Override
    protected void onLoadMore() {
        super.onLoadMore();
        loadMoreCell = provideLoadMoreCell();
        if (loadMoreCell != null) {
            listWidget.addDataAtLast(loadMoreCell);
        }
    }

    @Override
    protected void onLoadMoreComplete() {
        super.onLoadMoreComplete();
        if (loadMoreCell != null) {
            listWidget.removeData(loadMoreCell);
            loadMoreCell = null;
        }
    }

    @Override
    protected void onLoadMoreError(Throwable e) {
        super.onLoadMoreError(e);
    }

    protected abstract Cell provideLoadMoreCell();


}
