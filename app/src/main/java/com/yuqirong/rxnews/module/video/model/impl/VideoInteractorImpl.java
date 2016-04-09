package com.yuqirong.rxnews.module.video.model.impl;

import com.yuqirong.rxnews.app.AppService;
import com.yuqirong.rxnews.module.video.model.VideoInteractor;

/**
 * Created by Anyway on 2016/4/9.
 */
public class VideoInteractorImpl implements VideoInteractor {

    @Override
    public void getVideo(int taskId, String id, int startPage) {
        AppService.getInstance().getRxCollection().getVideo(taskId, id, startPage);
    }

}
