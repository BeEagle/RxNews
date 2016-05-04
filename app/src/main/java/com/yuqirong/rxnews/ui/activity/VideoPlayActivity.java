package com.yuqirong.rxnews.ui.activity;

import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yuqirong.rxnews.R;
import com.yuqirong.rxnews.app.MyApplication;
import com.yuqirong.rxnews.event.VideoEvent;
import com.yuqirong.rxnews.module.video.presenter.VideoPlayPresenter;
import com.yuqirong.rxnews.module.video.view.IVideoPlayView;
import com.yuqirong.rxnews.ui.view.VideoPlayControlView;

import butterknife.Bind;
import butterknife.OnClick;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by yuqirong on 2016/4/11.
 */
public class VideoPlayActivity extends BaseActivity implements IVideoPlayView {

    @Bind(R.id.mVideoView)
    VideoView mVideoView;
    @Bind(R.id.mProgressBar)
    MaterialProgressBar mProgressBar;
    @Bind(R.id.mRelativeLayout)
    RelativeLayout mRelativeLayout;
    private VideoPlayControlView mVideoPlayControlView;
    private VideoPlayPresenter mVideoPlayPresenter;
    // 视频url
    private String videoUrl;
    // 视频播放时长
    private long videoDuration;

    @Override
    public int getContentViewId() {
        return R.layout.activity_video_play;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        videoUrl = getIntent().getExtras().getString("videoUrl");
        mVideoView.setZOrderOnTop(true);
        mVideoView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mRelativeLayout.performClick();
                return true;
            }
        });
        mVideoPlayControlView = new VideoPlayControlView(this,mVideoView);
        mVideoPlayPresenter = new VideoPlayPresenter(this);
        mVideoPlayPresenter.playVideo();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            videoUrl = savedInstanceState.getString("videoUrl");
            videoDuration = savedInstanceState.getLong("videoDuration");
        }
        if (mVideoView != null) {
            mVideoView.start();
        }
    }

    // 播放视频
    @Override
    public void playVideo() {
        if (Vitamio.isInitialized(MyApplication.getContext())) {
            mVideoView.setVideoPath(videoUrl);
            mVideoView.requestFocus();
            mVideoView.setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
                @Override
                public void onTimedText(String text) {

                }

                @Override
                public void onTimedTextUpdate(byte[] pixels, int width, int height) {

                }
            });

            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setPlaybackSpeed(1.0f);
                }
            });
            mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mVideoPlayControlView != null) {
                        mVideoPlayControlView.setComplete();
                    }
                    mVideoPlayPresenter.showPlayController();
                }
            });
            mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(VideoPlayActivity.this, "视频播放出错了", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        } else {
            Toast.makeText(this, "播放器还没初始化完成", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.mRelativeLayout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mRelativeLayout:
                mVideoPlayPresenter.showPlayController();
                break;
        }
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void clearLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showPlayController() {
        mVideoPlayControlView.show();
    }

    public void onEventMainThread(VideoEvent videoEvent) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("videoUrl", videoUrl);
        outState.putLong("videoDuration", mVideoView.getDuration());
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放资源
        if(mVideoPlayControlView!=null){
            mVideoPlayControlView.destroy();
        }
        mVideoPlayControlView = null;
        if (mVideoView != null) {
            mVideoView.destroyDrawingCache();
            mVideoView.stopPlayback();
        }
        mVideoView = null;
    }
}
