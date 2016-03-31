package com.yuqirong.rxnews.module.news.model.impl;

import com.yuqirong.rxnews.app.AppService;
import com.yuqirong.rxnews.module.news.model.NewsDetailInteractor;

/**
 * Created by Anyway on 2016/3/9.
 */
public class NewsDetailInteractorImpl implements NewsDetailInteractor {

    @Override
    public void showNewsDetail(int taskId, String id, String postId) {
        AppService.getInstance().getRxCollection().getNewsDetail(taskId, id, postId);
    }

}
