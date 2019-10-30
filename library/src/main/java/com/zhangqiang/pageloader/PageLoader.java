package com.zhangqiang.pageloader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
                .subscribe(new InnerObserver<T>() {

                    @Override
                    protected void onSuccess(@Nullable T t) {
                        final T prev = first;
                        if (t != null) {
                            first = t;
                        }
                        if (loadCallback != null) {
                            loadCallback.onLoadPreviousPageSuccess(t, prev);
                        }
                    }

                    @Override
                    protected void onFail(Throwable e) {
                        final T prev = first;
                        if (loadCallback != null) {
                            loadCallback.onLoadPreviousPageFail(e, prev);
                        }
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
                .subscribe(new InnerObserver<T>() {

                    @Override
                    protected void onSuccess(@Nullable T t) {
                        final T prev = last;
                        if (t != null) {
                            last = t;
                        }
                        if (loadCallback != null) {
                            loadCallback.onLoadNextPageSuccess(t, prev);
                        }
                    }

                    @Override
                    protected void onFail(Throwable e) {
                        final T prev = last;
                        if (loadCallback != null) {
                            loadCallback.onLoadNextPageFail(e, prev);
                        }
                    }

                });
    }


    public void setCallback(Callback<T> loadCallback) {
        this.loadCallback = loadCallback;
    }

    private abstract static class InnerObserver<T> implements Observer<T> {

        private boolean isNextCalled;

        @Override
        public final void onSubscribe(Disposable d) {
            isNextCalled = false;
        }

        @Override
        public final void onNext(T t) {
            isNextCalled = true;
            onSuccess(t);
        }

        protected abstract void onSuccess(@Nullable T t);

        protected abstract void onFail(Throwable e);

        @Override
        public final void onError(Throwable e) {
            onFail(e);
        }


        @Override
        public final void onComplete() {
            if (isNextCalled) {
                return;
            }
            onSuccess(null);
        }
    }
}
