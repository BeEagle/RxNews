package com.yuqirong.rxnews.ui.activity;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.yuqirong.rxnews.R;
import com.yuqirong.rxnews.app.MyApplication;
import com.yuqirong.rxnews.event.VideoEvent;
import com.yuqirong.rxnews.module.video.presenter.VideoPlayPresenter;
import com.yuqirong.rxnews.module.video.view.IVideoPlayView;
import com.yuqirong.rxnews.ui.view.VideoPlayControlView;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by yuqirong on 2016/4/11.
 */
public class VideoPlayActivity extends BaseActivity implements IVideoPlayView, VideoPlayControlView.OnVideoPlayControlListener {

    @Bind(R.id.mVideoView)
    VideoView mVideoView;
    @Bind(R.id.mProgressBar)
    MaterialProgressBar mProgressBar;
    @Bind(R.id.mRelativeLayout)
    RelativeLayout mRelativeLayout;
    private VideoPlayControlView mVideoPlayControlView;
    private PopupWindow mPopupWindow;
    private MHandler mHandler = new MHandler(this);
    private VideoPlayPresenter mVideoPlayPresenter;

    private static final int REMOVE_WINDOW = 1;
    // 视频url
    private String url;

    private MediaController mMediaController;

    @Override
    public void onBack() {
        mHandler.removeCallbacksAndMessages(null);
        mVideoView.stopPlayback();
        mVideoView.destroyDrawingCache();
        mVideoPlayControlView = null;
        mVideoView = null;
        finish();
    }

    @Override
    public void onVideoPlayStateChanged(int playState) {
        if (playState == VideoPlayControlView.VIDEO_PLAY) {
            if (mVideoView.getCurrentPosition() == mVideoView.getDuration()) {
                // 播放完了，重播
                mVideoView.seekTo(0);
            }
            mVideoView.start();
        } else {
            mVideoView.pause();
        }
    }

    @Override
    public void onVideoSizeChanged(int videoSize) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    static class MHandler extends Handler {

        private WeakReference<VideoPlayActivity> mReference;

        public MHandler(VideoPlayActivity activity) {
            mReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REMOVE_WINDOW:
                    if (mReference.get().mPopupWindow != null && mReference.get().mPopupWindow.isShowing()) {
                        mReference.get().mPopupWindow.dismiss();
                        mReference.get().mPopupWindow = null;
                    }
                    break;
            }
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_video_play;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        url = getIntent().getExtras().getString("videoUrl");
        mMediaController = new MediaController(this);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setZOrderOnTop(true);
        mVideoView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mRelativeLayout.performClick();
                return true;
            }
        });
        mRelativeLayout.setOnClickListener(this);
        mVideoPlayPresenter = new VideoPlayPresenter(this);
        mVideoPlayPresenter.playVideo();
    }

    // 播放视频
    @Override
    public void playVideo() {
        if (Vitamio.isInitialized(MyApplication.getContext())) {
            mVideoView.setVideoPath(url);
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
                    Toast.makeText(VideoPlayActivity.this, "视频播放出错了╮(╯Д╰)╭", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        } else {
            Toast.makeText(this, "播放器还没初始化完哎，等等咯╮(╯Д╰)╭", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
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
        if (mPopupWindow == null) {
            if (mVideoPlayControlView == null) {
                mVideoPlayControlView = new VideoPlayControlView(this);
                mVideoPlayControlView.setOnVideoPlayControlListener(this);
            }
            mPopupWindow = new PopupWindow(mVideoPlayControlView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(null);
        mPopupWindow.setTouchable(true);
        mPopupWindow.showAtLocation(mVideoView, Gravity.CENTER, 0, 0);
        mHandler.sendEmptyMessageDelayed(REMOVE_WINDOW, 3000);
    }

    public void onEventMainThread(VideoEvent videoEvent) {

    }


}
