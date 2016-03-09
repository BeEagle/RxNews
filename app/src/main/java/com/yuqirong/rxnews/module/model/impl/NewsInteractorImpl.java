package com.yuqirong.rxnews.module.model.impl;

import com.yuqirong.rxnews.app.AppService;
import com.yuqirong.rxnews.module.model.NewsInteractor;

/**
 * Created by Anyway on 2016/3/9.
 */
public class NewsInteractorImpl implements NewsInteractor {

    @Override
    public void getNews(int taskId, String type, String id, int startPage) {
        AppService.getInstance().getRxCollection().getNews(taskId, type, id, startPage);
    }

    @Override
    public void initNews(int taskId, String id) {
        AppService.getInstance().getRxCollection().initNews(taskId, id);
    }

}
