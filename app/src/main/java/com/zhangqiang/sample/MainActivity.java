package com.zhangqiang.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zhangqiang.celladapter.CellListAdapter;
import com.zhangqiang.celladapter.CellRVAdapter;
import com.zhangqiang.celladapter.cell.Cell;
import com.zhangqiang.celladapter.cell.MultiCell;
import com.zhangqiang.celladapter.cell.ViewHolderBinder;
import com.zhangqiang.celladapter.vh.ViewHolder;
import com.zhangqiang.pageloader.ptr.CellPtrLoadHelper;
import com.zhangqiang.pageloader.ptr.PtrLoader;
import com.zhangqiang.pageloader.ptr.loadmore.RVLoadMoreWidget;
import com.zhangqiang.pageloader.ptr.refresh.SwipeRefreshWidget;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class MainActivity extends AppCompatActivity {

    public static final int DELAY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh);
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CellRVAdapter cellRVAdapter = new CellRVAdapter();
        recyclerView.setAdapter(cellRVAdapter);
        CellPtrLoadHelper<List<String>> ptrLoadHelper = new CellPtrLoadHelper<>(new PtrLoader<List<String>>(10) {
            @NonNull
            @Override
            protected Observable<List<String>> getRefreshDataSource(int pageIndex, int pageSize, int offset, int length) {
                List<String> strings = new ArrayList<>();
                for (int i = offset; i < offset + length; i++) {
                    strings.add(i + "");
                }
                return Observable.fromIterable(strings).buffer(strings.size());
            }

            @NonNull
            @Override
            protected Observable<List<String>> getLoadMoreDataSource(@NonNull List<String> strings, int pageIndex, int pageSize, int offset, int length) {
                return getRefreshDataSource(pageIndex, pageSize, offset, length);
            }
        }, new SwipeRefreshWidget(swipeRefreshLayout), new RVLoadMoreWidget(recyclerView), cellRVAdapter,
                new CellPtrLoadHelper.CellConverter<List<String>>() {
                    @Override
                    public List<Cell> convertRefreshCell(List<String> strings) {
                        List<Cell> cells = new ArrayList<>();
                        for (String string : strings) {
                            cells.add(new MultiCell<>(R.layout.item_text, string, new ViewHolderBinder<String>() {
                                @Override
                                public void onBind(ViewHolder viewHolder, String s) {
                                    viewHolder.setText(R.id.tv_title, s);
                                }
                            }));
                        }
                        return cells;
                    }

                    @Override
                    public List<Cell> convertLoadMoreCell(List<String> strings) {
                        return convertRefreshCell(strings);
                    }

                    @Override
                    public Cell convertEmptyCell() {
                        return null;
                    }

                    @Override
                    public Cell convertRefreshFailCell(Throwable e) {
                        return null;
                    }

                    @Override
                    public void onRefreshError(Throwable e) {

                    }

                    @Override
                    public void onLoadMoreError(Throwable e) {

                    }
                });
        ptrLoadHelper.refresh();
    }
}
