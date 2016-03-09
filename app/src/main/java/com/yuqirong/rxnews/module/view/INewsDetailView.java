package com.yuqirong.rxnews.module.view;

import com.yuqirong.rxnews.event.NewsDetailEvent;

/**
 * Created by Anyway on 2016/3/9.
 */
public interface INewsDetailView {

    public void showLoading();

    public void closeLoading();

    public void showSuccess(NewsDetailEvent event);

    public void showError();
}
