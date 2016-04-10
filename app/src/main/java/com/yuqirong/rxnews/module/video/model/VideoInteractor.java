package com.yuqirong.rxnews.module.video.model;

/**
 * Created by Anyway on 2016/4/9.
 */
public interface VideoInteractor {

    public void getVideo(int taskId, String id, int startPage);

    public void initVideo(int taskId,String id);

}
