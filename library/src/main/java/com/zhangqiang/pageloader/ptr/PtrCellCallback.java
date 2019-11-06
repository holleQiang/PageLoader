package com.zhangqiang.pageloader.ptr;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zhangqiang.celladapter.cell.Cell;
import com.zhangqiang.celladapter.observable.DataList;

import java.util.Collections;
import java.util.List;

public abstract class PtrCellCallback<T> implements PtrLoadHelper.Callback<T> {

    private DataList<Cell> mListWidget;
    private Cell tempLoadingCell;
    private Cell tempErrorCell;
    private Cell tempEmptyCell;

    public PtrCellCallback(DataList<Cell> mListWidget) {
        this.mListWidget = mListWidget;
    }

    @Override
    public void onRefreshStart(@Nullable Bundle extra, boolean autoRefresh) {
        clearTempCell();
        if (autoRefresh) {
            Cell loadingCell = provideLoadingCell();
            if (loadingCell != null) {
                tempLoadingCell = loadingCell;
                mListWidget.setDataList(Collections.singletonList(loadingCell));
            }
        }
    }

    @Override
    public void onRefreshSuccess(@NonNull T t, @Nullable Bundle extra, boolean autoRefresh) {
        List<Cell> cells = provideRefreshCell(t);
        mListWidget.setDataList(cells);
    }

    @Override
    public void onRefreshFail(Throwable e, @Nullable Bundle extra, boolean autoRefresh) {
        clearTempCell();
        if (mListWidget.isEmpty()) {
            Cell errorCell = provideErrorCell(e);
            if (errorCell != null) {
                tempErrorCell = errorCell;
                mListWidget.setDataList(Collections.singletonList(errorCell));
            }
        }
    }

    @Override
    public void onRefreshComplete(@Nullable Bundle extra, boolean autoRefresh) {
        clearTempCell();
        if (mListWidget.isEmpty()) {
            Cell emptyCell = provideEmptyCell();
            if (emptyCell != null) {
                tempEmptyCell = emptyCell;
                mListWidget.setDataList(Collections.singletonList(emptyCell));
            }
        }
    }

    @Override
    public void onLoadMoreStart(@Nullable Bundle extra) {

    }

    @Override
    public void onLoadMoreSuccess(@NonNull T t, Bundle extra) {
        List<Cell> cells = provideLoadMoreCell(t);
        if (cells != null && !cells.isEmpty()) {
            mListWidget.addDataListAtLast(cells);
        }
    }

    @Override
    public void onLoadMoreFail(Throwable e, Bundle extra) {

    }

    @Override
    public void onLoadMoreComplete(@Nullable Bundle extra) {

    }


    protected abstract List<Cell> provideRefreshCell(@NonNull T t);

    protected abstract List<Cell> provideLoadMoreCell(@NonNull T t);

    protected abstract Cell provideLoadingCell();

    protected abstract Cell provideEmptyCell();

    protected abstract Cell provideErrorCell(Throwable e);

    private void clearTempCell() {
        mListWidget.removeData(tempLoadingCell);
        mListWidget.removeData(tempEmptyCell);
        mListWidget.removeData(tempErrorCell);
        tempLoadingCell = null;
        tempEmptyCell = null;
        tempErrorCell = null;
    }
}
