package com.yuqirong.rxnews.event;

import com.yuqirong.rxnews.module.news.model.bean.NewsDetail;

/**
 * Created by Anyway on 2016/3/9.
 */
public class NewsDetailEvent extends BaseEvent {

    private String id;
    private NewsDetail newsDetail;

    public NewsDetailEvent(String id, NewsDetail newsDetail) {
        this.id = id;
        this.newsDetail = newsDetail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NewsDetail getNewsDetail() {
        return newsDetail;
    }

    public void setNewsDetail(NewsDetail newsDetail) {
        this.newsDetail = newsDetail;
    }
}
