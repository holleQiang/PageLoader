package com.zhangqiang.sample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.zhangqiang.celladapter.CellRVAdapter;
import com.zhangqiang.celladapter.cell.Cell;
import com.zhangqiang.celladapter.cell.MultiCell;
import com.zhangqiang.celladapter.cell.ViewHolderBinder;
import com.zhangqiang.celladapter.vh.ViewHolder;
import com.zhangqiang.pageloader.ptr.PtrCellCallback;
import com.zhangqiang.pageloader.ptr.PtrLoadHelper;
import com.zhangqiang.pageloader.ptr.loadmore.RVLoadMoreWidget;
import com.zhangqiang.pageloader.ptr.refresh.SwipeRefreshWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

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
        PtrLoadHelper<List<String>> ptrLoadHelper = new PtrLoadHelper<>(10,
                new SwipeRefreshWidget(swipeRefreshLayout),
                new RVLoadMoreWidget(recyclerView, 1),
                new PtrLoadHelper.DataSource<List<String>>() {
                    @NonNull
                    @Override
                    public Observable<List<String>> getRefreshDataSource(int pageIndex, int pageSize, int offset, int length, @Nullable Bundle extra, boolean autoRefresh) {
//
//                        int result = ((int) (Math.random() * 10)) % 3;
//                        if (result == 0) {
//                            return Observable.error(new RuntimeException());
//                        }else if(result == 1){
//                            return Observable.empty();
//                        }

                        List<String> strings = new ArrayList<>();
                        for (int i = offset; i < offset + length; i++) {
                            strings.add(i + "");
                        }
                        return Observable.fromIterable(strings).buffer(strings.size()).delay(1, TimeUnit.SECONDS).observeOn(new Scheduler() {
                            @Override
                            public Worker createWorker() {
                                return new Worker() {
                                    @Override
                                    public Disposable schedule(final Runnable run, long delay, TimeUnit unit) {
                                        final Handler handler = new Handler(Looper.getMainLooper());
                                        handler.post(run);
                                        return new Disposable() {
                                            @Override
                                            public void dispose() {
                                                handler.removeCallbacks(run);
                                            }

                                            @Override
                                            public boolean isDisposed() {
                                                return false;
                                            }
                                        };
                                    }

                                    @Override
                                    public void dispose() {

                                    }

                                    @Override
                                    public boolean isDisposed() {
                                        return false;
                                    }
                                };
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public Observable<List<String>> getLoadMoreDataSource(@NonNull List<String> strings, int pageIndex, int pageSize, int offset, int length, Bundle extra) {
                        return getRefreshDataSource(pageIndex, pageSize, offset, length, extra, false);
                    }
                });
        ptrLoadHelper.setCallback(new PtrCellCallback<List<String>>(cellRVAdapter) {
            @Override
            protected List<Cell> provideRefreshCell(@NonNull List<String> strings) {
                List<Cell> cells = new ArrayList<>();
                for (String string : strings) {

                    cells.add(new MultiCell<>(R.layout.item_text, string, viewHolderBinder));
                }
                return cells;
            }

            @Override
            protected List<Cell> provideLoadMoreCell(@NonNull List<String> strings) {
                return provideRefreshCell(strings);
            }

            @Override
            protected Cell provideLoadingCell() {
                return new MultiCell<>(R.layout.item_loading, "", viewHolderBinder);
            }

            @Override
            protected Cell provideEmptyCell() {
                return new MultiCell<>(R.layout.item_empty, "", viewHolderBinder);
            }

            @Override
            protected Cell provideErrorCell(Throwable e) {
                return new MultiCell<>(R.layout.item_load_error, "", viewHolderBinder);
            }
        });
        ptrLoadHelper.autoRefresh();
    }

    ViewHolderBinder<String> viewHolderBinder = new ViewHolderBinder<String>() {
        @Override
        public void onBind(ViewHolder viewHolder, String s) {
            try{
                viewHolder.setText(R.id.tv_title, s);

            }catch (Throwable e){

            }
            View view = viewHolder.getView();
            View.OnAttachStateChangeListener listener = (View.OnAttachStateChangeListener) view.getTag(R.id.key_attach_listener);
            if (listener != null) {
                view.removeOnAttachStateChangeListener(listener);
            }
            listener = new View.OnAttachStateChangeListener() {

                @Override
                public void onViewAttachedToWindow(View v) {
                    Log.i("Test", "=======onViewAttachedToWindow=========" + v);
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    Log.i("Test", "======onViewDetachedFromWindow==========" + v);
                }
            };
            view.setTag(R.id.key_attach_listener, listener);
            view.addOnAttachStateChangeListener(listener);
        }
    };
}
