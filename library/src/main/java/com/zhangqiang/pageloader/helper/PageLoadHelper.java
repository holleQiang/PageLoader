package com.zhangqiang.pageloader.helper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zhangqiang.pageloader.PageLoader;

import io.reactivex.Observable;

public class PageLoadHelper<T> {

    private LoadWidget firstLoadWidget;
    private LoadWidget previousLoadWidget;
    private LoadWidget nextLoadWidget;
    private DataSource<T> dataSource;
    private static final String EXTRA_KEY_AUTO_LOAD_FIRST_PAGE = "auto_load_first_page";
    private Callback<T> callback;

    public PageLoadHelper(@Nullable final LoadWidget firstLoadWidget,
                          @Nullable final LoadWidget previousLoadWidget,
                          @Nullable final LoadWidget nextLoadWidget,
                          DataSource<T> dataSource) {
        this.firstLoadWidget = firstLoadWidget;
        this.previousLoadWidget = previousLoadWidget;
        this.nextLoadWidget = nextLoadWidget;
        this.dataSource = dataSource;
        if (firstLoadWidget != null) {
            firstLoadWidget.setCallback(new LoadWidget.Callback() {
                @Override
                public void onLoadPage() {
                    mPageLoader.loadFirstPage(null);
                }
            });
        }
        if (previousLoadWidget != null) {
            previousLoadWidget.setCallback(new LoadWidget.Callback() {
                @Override
                public void onLoadPage() {
                    mPageLoader.loadPreviousPage(null);
                }
            });
        }
        if (nextLoadWidget != null) {
            nextLoadWidget.setCallback(new LoadWidget.Callback() {
                @Override
                public void onLoadPage() {
                    mPageLoader.loadNextPage(null);
                }
            });
        }
        mPageLoader.setCallback(new PageLoader.Callback<T>() {
            @Override
            public void onLoadFirstPageStart(@Nullable Bundle extra) {
                if (callback != null) {
                    callback.onLoadFirstPageStart(extra, isAutoLoadFromExtra(extra));
                }
            }

            @Override
            public void onLoadFirstPageSuccess(@NonNull T t, @Nullable Bundle extra) {
                if (callback != null) {
                    callback.onLoadFirstPageSuccess(t, extra, isAutoLoadFromExtra(extra));
                }
                if (firstLoadWidget != null) {
                    firstLoadWidget.setLoadSuccess();
                }
            }

            @Override
            public void onLoadFirstPageFail(Throwable e, @Nullable Bundle extra) {
                if (callback != null) {
                    callback.onLoadFirstPageFail(e, extra, isAutoLoadFromExtra(extra));
                }
                if (firstLoadWidget != null) {
                    firstLoadWidget.setLoadFail(e);
                }
            }

            @Override
            public void onLoadFirstPageComplete(@Nullable Bundle extra) {
                if (callback != null) {
                    callback.onLoadFirstPageComplete(extra, isAutoLoadFromExtra(extra));
                }
                if (firstLoadWidget != null) {
                    firstLoadWidget.setLoadComplete();
                }
            }

            @Override
            public void onLoadNextPageStart(@Nullable Bundle extra) {
                if (callback != null) {
                    callback.onLoadNextPageStart(extra);
                }
            }

            @Override
            public void onLoadNextPageSuccess(@NonNull T t, @Nullable Bundle extra) {
                if (callback != null) {
                    callback.onLoadNextPageSuccess(t, extra);
                }
                if (nextLoadWidget != null) {
                    nextLoadWidget.setLoadSuccess();
                }
            }

            @Override
            public void onLoadNextPageFail(Throwable e, @Nullable Bundle extra) {
                if (callback != null) {
                    callback.onLoadNextPageFail(e, extra);
                }
                if (nextLoadWidget != null) {
                    nextLoadWidget.setLoadFail(e);
                }
            }

            @Override
            public void onLoadNextPageComplete(@Nullable Bundle extra) {
                if (callback != null) {
                    callback.onLoadNextPageComplete(extra);
                }
                if (nextLoadWidget != null) {
                    nextLoadWidget.setLoadComplete();
                }
            }

            @Override
            public void onLoadPreviousPageStart(@Nullable Bundle extra) {
                if (callback != null) {
                    callback.onLoadPreviousPageStart(extra);
                }
            }

            @Override
            public void onLoadPreviousPageSuccess(@NonNull T t, @Nullable Bundle extra) {
                if (callback != null) {
                    callback.onLoadPreviousPageSuccess(t, extra);
                }
                if (previousLoadWidget != null) {
                    previousLoadWidget.setLoadSuccess();
                }
            }

            @Override
            public void onLoadPreviousPageFail(Throwable e, @Nullable Bundle extra) {
                if (callback != null) {
                    callback.onLoadPreviousPageFail(e, extra);
                }
                if (previousLoadWidget != null) {
                    previousLoadWidget.setLoadFail(e);
                }
            }

            @Override
            public void onLoadPreviousPageComplete(@Nullable Bundle extra) {
                if (callback != null) {
                    callback.onLoadPreviousPageComplete(extra);
                }
                if (previousLoadWidget != null) {
                    previousLoadWidget.setLoadComplete();
                }
            }
        });
    }

    private PageLoader<T> mPageLoader = new PageLoader<T>() {
        @NonNull
        @Override
        protected Observable<T> getFirstPageDataSource(@Nullable Bundle extra) {
            return dataSource.getFirstPageDataSource(extra);
        }

        @NonNull
        @Override
        protected Observable<T> getPreviousPageDataSource(@NonNull T t, @Nullable Bundle extra) {
            return dataSource.getPreviousPageDataSource(t, extra);
        }

        @NonNull
        @Override
        protected Observable<T> getNextPageDataSource(@NonNull T t, @Nullable Bundle extra) {
            return dataSource.getNextPageDataSource(t, extra);
        }
    };

    public void autoLoadFirstPage(Bundle extra) {

        Bundle bundle;
        if (extra == null) {
            bundle = new Bundle();
        } else {
            bundle = extra;
        }
        bundle.putBoolean(EXTRA_KEY_AUTO_LOAD_FIRST_PAGE, true);
        mPageLoader.loadFirstPage(bundle);
    }

    public void autoLoadFirstPage() {
        autoLoadFirstPage(null);
    }

    private boolean isAutoLoadFromExtra(Bundle extra) {
        return extra != null && extra.getBoolean(EXTRA_KEY_AUTO_LOAD_FIRST_PAGE);
    }

    public interface DataSource<T> {


        Observable<T> getFirstPageDataSource(Bundle extra);

        Observable<T> getPreviousPageDataSource(T t, Bundle extra);

        Observable<T> getNextPageDataSource(T t, Bundle extra);
    }

    public interface Callback<T> {

        void onLoadFirstPageStart(@Nullable Bundle extra, boolean autoLoad);

        void onLoadFirstPageSuccess(@NonNull T t, @Nullable Bundle extra, boolean autoLoad);

        void onLoadFirstPageFail(Throwable e, @Nullable Bundle extra, boolean autoLoad);

        void onLoadFirstPageComplete(@Nullable Bundle extra, boolean autoLoad);

        void onLoadNextPageStart(@Nullable Bundle extra);

        void onLoadNextPageSuccess(@NonNull T t, @Nullable Bundle extra);

        void onLoadNextPageFail(Throwable e, @Nullable Bundle extra);

        void onLoadNextPageComplete(@Nullable Bundle extra);

        void onLoadPreviousPageStart(@Nullable Bundle extra);

        void onLoadPreviousPageSuccess(@NonNull T t, @Nullable Bundle extra);

        void onLoadPreviousPageFail(Throwable e, @Nullable Bundle extra);

        void onLoadPreviousPageComplete(@Nullable Bundle extra);
    }

    public void setCallback(Callback<T> callback) {
        this.callback = callback;
    }

    public LoadWidget getFirstLoadWidget() {
        return firstLoadWidget;
    }

    public LoadWidget getPreviousLoadWidget() {
        return previousLoadWidget;
    }

    public LoadWidget getNextLoadWidget() {
        return nextLoadWidget;
    }
}
