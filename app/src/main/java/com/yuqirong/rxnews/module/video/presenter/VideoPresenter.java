package com.yuqirong.rxnews.module.video.presenter;

import com.yuqirong.rxnews.event.VideoEvent;
import com.yuqirong.rxnews.module.video.model.VideoInteractor;
import com.yuqirong.rxnews.module.video.model.impl.VideoInteractorImpl;
import com.yuqirong.rxnews.module.video.view.IVideoView;

/**
 * Created by Anyway on 2016/3/9.
 */
public class VideoPresenter {

    private IVideoView mIVideoView;
    private VideoInteractor mVideoInteractor;

    public VideoPresenter(IVideoView mIVideoView) {
        this.mIVideoView = mIVideoView;
        mVideoInteractor = new VideoInteractorImpl();
    }

    public void getVideo(int taskId, String id, int startPage) {
        mVideoInteractor.getVideo(taskId, id, startPage);
    }

    public void initVideo(int taskId, String id) {
        showLoading();
        mVideoInteractor.initVideo(taskId, id);
    }

    public void showSuccess(VideoEvent videoEvent) {
        mIVideoView.showSuccess(videoEvent);
        clearLoading();
    }

    public void showError() {
        mIVideoView.showError();
    }

    public void showLoading() {
        mIVideoView.showLoading();
    }

    public void clearLoading() {
        mIVideoView.clearLoading();
    }

}
