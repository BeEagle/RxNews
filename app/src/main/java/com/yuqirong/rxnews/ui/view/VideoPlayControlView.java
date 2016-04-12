package com.yuqirong.rxnews.ui.view;

import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yuqirong.rxnews.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private OnVideoPlayControlListener listener;

    public final static int VIDEO_NORMAL = 0;
    public final static int VIDEO_FULLSCREEN = 1;
    // 播放状态
    private int videoSize = VIDEO_NORMAL;

    public final static int VIDEO_PLAY = 0;
    public final static int VIDEO_STOP = 1;
    // 播放状态
    private int playState = VIDEO_PLAY;

    public VideoPlayControlView(Context context) {
        this(context, null);
    }

    public void setComplete(){
        playState = VIDEO_STOP;
        ib_play.setImageResource(R.drawable.ic_button_play);
    }

    public VideoPlayControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mainView = LayoutInflater.from(context).inflate(R.layout.layout_video_play_control, this);
        ButterKnife.bind(this);
        sb_process.setOnSeekBarChangeListener(this);
    }

    @OnClick({R.id.ib_back, R.id.ib_fullscreen, R.id.ib_play})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                if (listener != null) {
                    listener.onBack();
                }
                break;
            case R.id.ib_fullscreen:
                if (videoSize == VIDEO_NORMAL) {
                    videoSize = VIDEO_FULLSCREEN;
                } else {
                    videoSize = VIDEO_NORMAL;
                }
                if (listener != null) {
                    listener.onVideoSizeChanged(videoSize);
                }
                break;
            case R.id.ib_play:
                if (playState == VIDEO_PLAY) {
                    playState = VIDEO_STOP;
                    ib_play.setImageResource(R.drawable.ic_button_play);
                } else {
                    playState = VIDEO_PLAY;
                    ib_play.setImageResource(R.drawable.ic_button_pause);
                }
                if (listener != null) {
                    listener.onVideoPlayStateChanged(playState);
                }
                break;
        }
    }

    public void setOnVideoPlayControlListener(OnVideoPlayControlListener listener) {
        this.listener = listener;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (listener != null) {
            onProgressChanged(seekBar, progress, fromUser);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (listener != null) {
            onStartTrackingTouch(seekBar);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (listener != null) {
            onStopTrackingTouch(seekBar);
        }
    }

    public interface OnVideoPlayControlListener {

        public void onBack();

        public void onVideoPlayStateChanged(int playState);

        public void onVideoSizeChanged(int videoSize);

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);

        public void onStartTrackingTouch(SeekBar seekBar);

        public void onStopTrackingTouch(SeekBar seekBar);

    }

}
