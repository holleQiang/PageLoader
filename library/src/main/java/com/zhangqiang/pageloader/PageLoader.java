package com.zhangqiang.pageloader;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public abstract class PageLoader<T> {

    private T first, last;
    private final PublishSubject<Boolean> stopFirstLoadSubject = PublishSubject.create();
    private final PublishSubject<Boolean> stopNextLoadLoadSubject = PublishSubject.create();
    private final PublishSubject<Boolean> stopPreviousLoadSubject = PublishSubject.create();
    private Callback<T> loadCallback;


    @NonNull
    protected abstract Observable<T> getFirstPageDataSource(@Nullable Bundle extra);

    @NonNull
    protected abstract Observable<T> getPreviousPageDataSource(@NonNull T t,@Nullable Bundle extra);

    @NonNull
    protected abstract Observable<T> getNextPageDataSource(@NonNull T t,@Nullable Bundle extra);


    public void reset() {
        first = last = null;
    }

    public void loadFirstPage(Bundle extra){
        reset();
        loadFirstPageInternal(extra);
    }

    private void loadFirstPageInternal(final Bundle extra){

        stopFirstLoadSubject.onNext(true);
        stopNextLoadLoadSubject.onNext(true);
        stopPreviousLoadSubject.onNext(true);
        Observable<T> resultOb = getFirstPageDataSource(extra);
        resultOb.takeUntil(stopFirstLoadSubject)
                .takeUntil(stopNextLoadLoadSubject)
                .takeUntil(stopPreviousLoadSubject)
                .subscribe(new Observer<T>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(T t) {
                        first = last = t;
                        if (loadCallback != null) {
                            loadCallback.onLoadFirstPageSuccess(t,extra);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (loadCallback != null) {
                            loadCallback.onLoadFirstPageFail(e,extra);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (loadCallback != null) {
                            loadCallback.onLoadFirstPageComplete(extra);
                        }
                    }
                });
    }

    public void loadPreviousPage(Bundle extra) {
        if (first == null) {
            return;
        }
        loadPreviousPageInternal(first,extra);
    }

    private void loadPreviousPageInternal(T t, final Bundle extra) {

        stopPreviousLoadSubject.onNext(true);
        Observable<T> resultOb = getPreviousPageDataSource(t,extra);
        resultOb.takeUntil(stopPreviousLoadSubject)
                .takeUntil(stopNextLoadLoadSubject)
                .takeUntil(stopFirstLoadSubject)
                .subscribe(new Observer<T>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(T t) {
                        first = t;
                        if (loadCallback != null) {
                            loadCallback.onLoadPreviousPageSuccess(t,extra);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (loadCallback != null) {
                            loadCallback.onLoadPreviousPageFail(e,extra);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (loadCallback != null) {
                            loadCallback.onLoadPreviousPageComplete(extra);
                        }
                    }
                });
    }


    public void loadNextPage(Bundle extra) {
        if (last == null) {
            return;
        }
        loadNextPageInternal(last,extra);
    }

    private void loadNextPageInternal(final T t, final Bundle extra) {

        stopNextLoadLoadSubject.onNext(true);
        getNextPageDataSource(t,extra)
                .takeUntil(stopNextLoadLoadSubject)
                .takeUntil(stopPreviousLoadSubject)
                .takeUntil(stopFirstLoadSubject)
                .subscribe(new Observer<T>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(T t) {
                        final T prev = last;
                        last = t;
                        if (loadCallback != null) {
                            loadCallback.onLoadNextPageSuccess(t, extra);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        final T prev = last;
                        if (loadCallback != null) {
                            loadCallback.onLoadNextPageFail(e, extra);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (loadCallback != null) {
                            loadCallback.onLoadNextPageComplete(extra);
                        }
                    }
                });
    }


    public void setCallback(Callback<T> loadCallback) {
        this.loadCallback = loadCallback;
    }


    public interface Callback<T> {

        void onLoadFirstPageSuccess(@NonNull T t, @Nullable Bundle extra);

        void onLoadFirstPageFail(Throwable e,@Nullable Bundle extra);

        void onLoadFirstPageComplete(@Nullable Bundle extra);

        void onLoadNextPageSuccess(@NonNull T t,@Nullable Bundle extra);

        void onLoadNextPageFail(Throwable e,@Nullable Bundle extra);

        void onLoadNextPageComplete(@Nullable Bundle extra);

        void onLoadPreviousPageSuccess(@NonNull T t,@Nullable Bundle extra);

        void onLoadPreviousPageFail(Throwable e,@Nullable Bundle extra);

        void onLoadPreviousPageComplete(@Nullable Bundle extra);
    }
}
