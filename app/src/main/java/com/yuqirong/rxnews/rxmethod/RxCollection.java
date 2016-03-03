package com.yuqirong.rxnews.rxmethod;

import android.util.SparseArray;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/2/25.
 */
public class RxCollection {

    private static RxCollection mRxCollection;

    private RxCollection() {

    }

    public static synchronized RxCollection getInstance() {
        if (mRxCollection == null) {
            mRxCollection = new RxCollection();
        }
        return mRxCollection;
    }


    private SparseArray<CompositeSubscription> mCompositeSubByTaskId = new SparseArray<>();

    public void addCompositeSub(int taskId) {
        if (mCompositeSubByTaskId.get(taskId) == null) {
            CompositeSubscription compositeSubscription = new CompositeSubscription();
            mCompositeSubByTaskId.put(taskId, compositeSubscription);
        }
    }

    public void removeCompositeSub(int taskId) {
        if (mCompositeSubByTaskId.get(taskId) != null) {
            mCompositeSubByTaskId.remove(taskId);
        }
    }

    public CompositeSubscription getCompositeSub(int taskId) {
        CompositeSubscription compositeSubscription;
        if (mCompositeSubByTaskId.get(taskId) == null) {
            addCompositeSub(taskId);
        }
        compositeSubscription = mCompositeSubByTaskId.get(taskId);
        return compositeSubscription;
    }

    public void initNews(int taskId, String id) {
        getCompositeSub(taskId).add(RxNews.initNews(id));
    }

    public void getNews(int taskId, String type, String id, int startPage) {
        getCompositeSub(taskId).add(RxNews.getNews(type, id, startPage));
    }

}
