package com.zhangqiang.pageloader.ptr;

import android.support.annotation.Nullable;

import com.zhangqiang.celladapter.cell.Cell;
import com.zhangqiang.celladapter.observable.DataList;
import com.zhangqiang.pageloader.ptr.loadmore.LoadMoreWidget;
import com.zhangqiang.pageloader.ptr.refresh.RefreshWidget;

import java.util.Collections;
import java.util.List;

public class CellPtrLoadHelper<T> extends PtrLoadHelper<T> implements Callback<T> {

    private DataList<Cell> mListWidget;
    private CellConverter<T> mCellConverter;

    public CellPtrLoadHelper(PtrLoader<T> pageLoader, RefreshWidget refreshWidget, LoadMoreWidget loadMoreWidget, DataList<Cell> listWidget, CellConverter<T> cellConverter) {
        super(pageLoader, refreshWidget, loadMoreWidget);
        this.mListWidget = listWidget;
        this.mCellConverter = cellConverter;
        setCallback(this);
    }

    @Override
    public void onRefreshSuccess(@Nullable T t) {
        List<Cell> cells = mCellConverter.convertRefreshCell(t);
        if (cells == null || cells.isEmpty()) {
            Cell emptyCell = mCellConverter.convertEmptyCell();
            if (emptyCell != null) {
                cells = Collections.singletonList(emptyCell);
            }
        }
        mListWidget.setDataList(cells);
    }

    @Override
    public void onRefreshFail(Throwable e) {
        if (mListWidget.isEmpty()) {
            Cell failCell = mCellConverter.convertRefreshFailCell(e);
            if (failCell != null) {
                mListWidget.setDataList(Collections.singletonList(failCell));
                return;
            }
        }
        mCellConverter.onRefreshError(e);
    }

    @Override
    public void onLoadMoreSuccess(@Nullable T t) {
        List<Cell> cells = mCellConverter.convertLoadMoreCell(t);
        if (cells != null && !cells.isEmpty()) {
            mListWidget.addDataListAtLast(cells);
        }
    }

    @Override
    public void onLoadMoreFail(Throwable e) {
        if (mCellConverter != null) {
            mCellConverter.onLoadMoreError(e);
        }
    }

    public interface CellConverter<T> {

        List<Cell> convertRefreshCell(@Nullable T t);

        List<Cell> convertLoadMoreCell(@Nullable T t);

        Cell convertEmptyCell();

        Cell convertRefreshFailCell(Throwable e);

        void onRefreshError(Throwable e);

        void onLoadMoreError(Throwable e);
    }


}
