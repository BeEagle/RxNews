package com.yuqirong.rxnews.event;

import com.yuqirong.rxnews.app.Constant;
import com.yuqirong.rxnews.module.video.model.bean.Video;

import java.util.List;

/**
 * Created by Anyway on 2016/4/9.
 */
public class VideoEvent extends BaseEvent {

    private List<Video> videos;

    private String id;

    private Constant.Tag tag;

    public VideoEvent(String id, List<Video> videos, Constant.Tag tag) {
        this.id = id;
        this.videos = videos;
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Constant.Tag getTag() {
        return tag;
    }

    public void setTag(Constant.Tag tag) {
        this.tag = tag;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
