package com.yuqirong.rxnews.module.news.view;

import com.yuqirong.rxnews.event.NewsEvent;

/**
 * Created by Anyway on 2016/3/9.
 */
public interface INewsView {

    /**
     * 显示progressbar
     */
    public void showLoading();

    /**
     * 清除progressbar
     */
    public void clearLoading();

    public void showSuccess(NewsEvent newsEvent);

    public void showError();
}