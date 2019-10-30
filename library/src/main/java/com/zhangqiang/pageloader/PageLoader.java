package com.zhangqiang.pageloader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public abstract class PageLoader<T> {

    private T first, last;
    private final PublishSubject<Boolean> onNextLoadLoadSubject = PublishSubject.create();
    private final PublishSubject<Boolean> onPreviousLoadSubject = PublishSubject.create();
    private Callback<T> loadCallback;

    @NonNull
    protected abstract Observable<T> getPreviousPageDataSource(@NonNull T t);

    @NonNull
    protected abstract Observable<T> getNextPageDataSource(@Nullable T t);

    public void reset() {
        first = last = null;
    }

    public void previousPage() {
        if (first == null) {
            loadNextPage(null);
            return;
        }
        loadPreviousPage(first);
    }

    private void loadPreviousPage(T t) {

        onPreviousLoadSubject.onNext(true);
        Observable<T> resultOb = getPreviousPageDataSource(t);
        resultOb.takeUntil(onPreviousLoadSubject)
                .takeUntil(onNextLoadLoadSubject)
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(T t) {
                        final T prev = first;
                        first = t;
                        if (loadCallback != null) {
                            loadCallback.onLoadPreviousPageSuccess(t, prev);
                        }
                        Log.i("Test", "====loadPreviousPage=======" + t.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        final T prev = first;
                        if (loadCallback != null) {
                            loadCallback.onLoadPreviousPageFail(e, prev);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    public void nextPage() {
        loadNextPage(last);
    }

    private void loadNextPage(final T t) {

        onNextLoadLoadSubject.onNext(true);
        getNextPageDataSource(t)
                .takeUntil(onNextLoadLoadSubject)
                .takeUntil(onPreviousLoadSubject)
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(T t) {
                        final T prev = last;
                        last = t;
                        if (loadCallback != null) {
                            loadCallback.onLoadNextPageSuccess(t, prev);
                        }
                        Log.i("Test", "====loadNextPage=======" + t.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        final T prev = last;
                        if (loadCallback != null) {
                            loadCallback.onLoadNextPageFail(e, prev);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    public void setCallback(Callback<T> loadCallback) {
        this.loadCallback = loadCallback;
    }
}
