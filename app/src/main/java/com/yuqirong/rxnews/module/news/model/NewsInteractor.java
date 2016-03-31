package com.yuqirong.rxnews.module.news.model;

/**
 * 新闻列表
 * Created by Anyway on 2016/3/9.
 */
public interface NewsInteractor {

    public void getNews(int taskId, String type, String id, int startPage);

    public void initNews(int taskId, String id);

}
