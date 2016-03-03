package com.yuqirong.rxnews.event;

import com.yuqirong.rxnews.app.Constant;
import com.yuqirong.rxnews.model.News;

import java.util.List;

/**
 * Created by Administrator on 2016/2/24.
 */
public class NewsEvent extends BaseEvent {

    private List<News> news;

    private String id;

    private Constant.Tag tag;

    public NewsEvent(Constant.Tag tag, List<News> news, String id) {
        this.tag = tag;
        this.news = news;
        this.id = id;
    }

    public Constant.Tag getTag() {
        return tag;
    }

    public void setTag(Constant.Tag tag) {
        this.tag = tag;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }

    public List<News> getNews() {
        return news;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
