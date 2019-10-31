package com.zhangqiang.pageloader.ptr;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zhangqiang.celladapter.cell.Cell;
import com.zhangqiang.celladapter.observable.DataList;
import com.zhangqiang.pageloader.ptr.loadmore.LoadMoreWidget;
import com.zhangqiang.pageloader.ptr.refresh.RefreshWidget;

import java.util.Collections;
import java.util.List;

public class CellPtrLoadHelper<T> extends PtrLoadHelper<T> implements PtrLoadHelper.Callback<T> {

    private DataList<Cell> mListWidget;
    private CellProvider<T> mCellProvider;

    public CellPtrLoadHelper(int pageSize, RefreshWidget refreshWidget, LoadMoreWidget loadMoreWidget, DataList<Cell> listWidget, DataSource<T> dataSource, CellProvider<T> cellProvider) {
        super(pageSize, refreshWidget, loadMoreWidget, dataSource);
        this.mListWidget = listWidget;
        this.mCellProvider = cellProvider;
        setCallback(this);
    }


    @Override
    public void onRefreshStart(@Nullable Bundle extra, boolean autoRefresh) {
        if (autoRefresh) {
            Cell loadingCell = mCellProvider.provideLoadingCell();
            if (loadingCell != null && mListWidget.isEmpty()) {
                mListWidget.setDataList(Collections.singletonList(loadingCell));
            }
        }
    }

    @Override
    public void onRefreshSuccess(@NonNull T t, @Nullable Bundle extra, boolean autoRefresh) {
        List<Cell> cells = mCellProvider.provideRefreshCell(t);
        if (cells == null || cells.isEmpty()) {
            Cell emptyCell = mCellProvider.provideEmptyCell();
            if (emptyCell != null) {
                cells = Collections.singletonList(emptyCell);
            }
        }
        mListWidget.setDataList(cells);
    }

    @Override
    public void onRefreshFail(Throwable e, @Nullable Bundle extra, boolean autoRefresh) {
        if (mListWidget.isEmpty()) {
            Cell failCell = mCellProvider.provideErrorCell(e);
            if (failCell != null) {
                mListWidget.setDataList(Collections.singletonList(failCell));
                return;
            }
        }
        mCellProvider.onRefreshError(e);
    }

    @Override
    public void onRefreshComplete(@Nullable Bundle extra, boolean autoRefresh) {

    }

    @Override
    public void onLoadMoreStart(@Nullable Bundle extra) {

    }

    @Override
    public void onLoadMoreSuccess(@Nullable T t, Bundle extra) {
        List<Cell> cells = mCellProvider.provideLoadMoreCell(t);
        if (cells != null && !cells.isEmpty()) {
            mListWidget.addDataListAtLast(cells);
        }
    }

    @Override
    public void onLoadMoreFail(Throwable e, Bundle extra) {
        if (mCellProvider != null) {
            mCellProvider.onLoadMoreError(e);
        }
    }

    @Override
    public void onLoadMoreComplete(@Nullable Bundle extra) {

    }

    public interface CellProvider<T> {

        List<Cell> provideRefreshCell(@NonNull T t);

        List<Cell> provideLoadMoreCell(@NonNull T t);

        Cell provideLoadingCell();

        Cell provideEmptyCell();

        Cell provideErrorCell(Throwable e);

        void onRefreshError(Throwable e);

        void onLoadMoreError(Throwable e);
    }


}
