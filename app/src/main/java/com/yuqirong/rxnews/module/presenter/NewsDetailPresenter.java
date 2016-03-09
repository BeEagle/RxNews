package com.yuqirong.rxnews.module.presenter;

import com.yuqirong.rxnews.event.NewsDetailEvent;
import com.yuqirong.rxnews.module.model.NewsDetailInteractor;
import com.yuqirong.rxnews.module.model.impl.NewsDetailInteractorImpl;
import com.yuqirong.rxnews.module.view.INewsDetailView;

/**
 * Created by Anyway on 2016/3/9.
 */
public class NewsDetailPresenter {

    private INewsDetailView mINewsDetailView;
    private NewsDetailInteractor mNewsDetailInteractor;

    public NewsDetailPresenter(INewsDetailView mINewsDetailView) {
        this.mINewsDetailView = mINewsDetailView;
        mNewsDetailInteractor = new NewsDetailInteractorImpl();
    }

    public void showNewsDetail(int taskId, String id, String postId) {
        showLoading();
        mNewsDetailInteractor.showNewsDetail(taskId, id, postId);
    }

    public void showLoading() {
        mINewsDetailView.showLoading();
    }

    public void closeLoading() {
        mINewsDetailView.closeLoading();
    }

    public void showSuccess(NewsDetailEvent event) {
        mINewsDetailView.showSuccess(event);
    }

    public void showError() {
        mINewsDetailView.showError();
    }
}
