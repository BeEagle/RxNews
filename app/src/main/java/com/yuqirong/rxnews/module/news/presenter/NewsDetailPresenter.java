package com.yuqirong.rxnews.module.news.presenter;

import com.yuqirong.rxnews.event.NewsDetailEvent;
import com.yuqirong.rxnews.module.news.model.NewsDetailInteractor;
import com.yuqirong.rxnews.module.news.model.impl.NewsDetailInteractorImpl;
import com.yuqirong.rxnews.module.news.view.INewsDetailView;

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
        closeLoading();
        mINewsDetailView.showSuccess(event);
    }

    public void showError() {
        //        clearLoading();
        mINewsDetailView.showError();
    }
}
