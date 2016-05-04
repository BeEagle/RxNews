package com.yuqirong.rxnews.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yuqirong.rxnews.R;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vov.vitamio.utils.StringUtils;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by yuqirong on 2016/4/11.
 */
public class VideoPlayControlView extends FrameLayout implements SeekBar.OnSeekBarChangeListener {

    private View mainView;
    @Bind(R.id.ib_back)
    ImageButton ib_back;
    @Bind(R.id.ib_fullscreen)
    ImageButton ib_fullscreen;
    @Bind(R.id.ib_play)
    ImageButton ib_play;
    @Bind(R.id.sb_process)
    AppCompatSeekBar sb_process;
    @Bind(R.id.tv_start)
    TextView tv_start;
    @Bind(R.id.tv_end)
    TextView tv_end;
    // 小屏状态
    public final static int VIDEO_NORMAL = 0;
    // 全屏状态
    public final static int VIDEO_FULLSCREEN = 1;
    // 默认是小屏状态
    private int videoSize = VIDEO_NORMAL;
    // 播放状态
    public final static int VIDEO_PLAY = 0;
    // 暂停状态
    public final static int VIDEO_PAUSE = 1;
    // 默认是播放状态
    private int playState = VIDEO_PLAY;
    // 进度改变
    private static final int MESSAGE_CHANGE_TEXT_PROGRESS = 4;
    // window gone
    private static final int MESSAGE_WINDOW_GONE = 5;

    private PopupWindow mPopupWindow;

    private VideoView mVideoView;

    private Activity mActivity;

    private VideoHandler mHandler;
    // 当前时长
    private long currentPosition;
    // 总时长
    private long duration;

    private boolean isFinish;

    private static final int DEFAULT_DISMISS_TIME = 3000;

    private static class VideoHandler extends Handler {

        private WeakReference<VideoPlayControlView> mReference;

        public VideoHandler(VideoPlayControlView controlView) {
            mReference = new WeakReference(controlView);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_CHANGE_TEXT_PROGRESS:
                    mReference.get().initProgress();
                    break;
                case MESSAGE_WINDOW_GONE: // 隐藏Window
                    if (mReference.get().mPopupWindow != null && mReference.get().mPopupWindow.isShowing()) {
                        mReference.get().mPopupWindow.dismiss();
                    }
                    break;
            }
        }
    }

    public VideoPlayControlView(Activity mActvity, VideoView mVideoView) {
        super(mActvity);
        this.mActivity = mActvity;
        this.mVideoView = mVideoView;
        initView(mActvity);
    }

    /**
     * 设置完成状态
     */
    public void setComplete() {
        playState = VIDEO_PAUSE;
        ib_play.setImageResource(R.drawable.ic_button_play);
        isFinish = true;
    }

    private void initView(Context context) {
        mainView = LayoutInflater.from(context).inflate(R.layout.layout_video_play_control, this);
        ButterKnife.bind(this);
        mHandler = new VideoHandler(this);
        sb_process.setOnSeekBarChangeListener(this);
    }

    private void initProgress() {
        currentPosition = mVideoView.getCurrentPosition();
        duration = mVideoView.getDuration();
        sb_process.setProgress((int) (100 * currentPosition / duration));
        tv_start.setText(StringUtils.generateTime(currentPosition));
        tv_end.setText(StringUtils.generateTime(duration));
        mHandler.sendEmptyMessageDelayed(MESSAGE_CHANGE_TEXT_PROGRESS, 1000);
    }

    /**
     * 显示
     */
    public void show() {
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(this, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if (mPopupWindow.isShowing()) {
            return;
        }
        initProgress();
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(null);
        mPopupWindow.setTouchable(true);
        mPopupWindow.showAtLocation(mVideoView, Gravity.CENTER, 0, 0);
        mHandler.sendEmptyMessageDelayed(MESSAGE_WINDOW_GONE, DEFAULT_DISMISS_TIME);
    }


    /**
     * 销毁
     */
    public void destroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mActivity = null;
        mVideoView = null;
        mPopupWindow = null;
    }

    @OnClick({R.id.ib_back, R.id.ib_fullscreen, R.id.ib_play})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back: // 返回或者退出
                if (mActivity != null) {
                    mActivity.finish();
                }
                break;
            case R.id.ib_fullscreen: // 全屏或者小屏
                if (videoSize == VIDEO_NORMAL) {
                    videoSize = VIDEO_FULLSCREEN;
                } else {
                    videoSize = VIDEO_NORMAL;
                }
                // 全屏或者小屏
                if (videoSize == VideoPlayControlView.VIDEO_NORMAL && mActivity != null) {
                    // 退出全屏
                    final WindowManager.LayoutParams attrs = mActivity.getWindow().getAttributes();
                    attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    mActivity.getWindow().setAttributes(attrs);
                    mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    // 进入全屏
                    mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                break;
            case R.id.ib_play: // 恢复播放或者暂停播放
                if (playState == VIDEO_PLAY) {
                    playState = VIDEO_PAUSE;
                    ib_play.setImageResource(R.drawable.ic_button_play);
                } else {
                    playState = VIDEO_PLAY;
                    ib_play.setImageResource(R.drawable.ic_button_pause);
                }
                // 恢复播放或者暂停播放
                if (playState == VideoPlayControlView.VIDEO_PLAY) {
                    if (mVideoView.getCurrentPosition() == mVideoView.getDuration()) {
                        // 播放完了，重播
                        mVideoView.seekTo(0);
                    }
                    mVideoView.start();
                } else {
                    mVideoView.pause();
                }
                break;
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            long currentPosition = progress * duration / seekBar.getMax();
            tv_start.setText(StringUtils.generateTime(currentPosition));
            mVideoView.seekTo(currentPosition);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // 开始触摸seekbar时
        mHandler.removeMessages(MESSAGE_CHANGE_TEXT_PROGRESS);
        mHandler.removeMessages(MESSAGE_WINDOW_GONE);
        mVideoView.pause();

        // 如果是播放完之后再拖动的话
        if (isFinish) {
            playState = VIDEO_PLAY;
            ib_play.setImageResource(R.drawable.ic_button_pause);
            isFinish = false;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // 结束触摸seekbar时
        long currentPosition = seekBar.getProgress() * duration / seekBar.getMax();
        mVideoView.seekTo(currentPosition);
        tv_start.setText(StringUtils.generateTime(currentPosition));
        mVideoView.start();
        mHandler.sendEmptyMessage(MESSAGE_CHANGE_TEXT_PROGRESS);
        mHandler.sendEmptyMessageDelayed(MESSAGE_WINDOW_GONE, DEFAULT_DISMISS_TIME);
    }


}
