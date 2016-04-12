package com.yuqirong.rxnews.module.video.view;

import com.yuqirong.rxnews.event.VideoEvent;

/**
 * Created by Anyway on 2016/4/9.
 */
public interface IVideoView {

    public void showLoading();

    public void clearLoading();

    public void showSuccess(VideoEvent videoEvent);

    public void showError();

}
