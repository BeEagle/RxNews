package com.yuqirong.rxnews.module.video.presenter;

import com.yuqirong.rxnews.module.video.model.VideoPlayInteractor;
import com.yuqirong.rxnews.module.video.model.impl.VideoPlayInteractorImpl;
import com.yuqirong.rxnews.module.video.view.IVideoPlayView;

/**
 * Created by Anyway on 2016/4/11.
 */
public class VideoPlayPresenter {

    private IVideoPlayView iVideoPlayView;
    private VideoPlayInteractor mVideoPlayInteractor;

    public VideoPlayPresenter(IVideoPlayView iVideoPlayView) {
        this.iVideoPlayView = iVideoPlayView;
        mVideoPlayInteractor = new VideoPlayInteractorImpl();
    }

    public void showPlayController(){
        iVideoPlayView.showPlayController();
    }

    public void playVideo(){
        iVideoPlayView.clearLoading();
        iVideoPlayView.playVideo();
    }

}
