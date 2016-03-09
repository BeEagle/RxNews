package com.yuqirong.rxnews.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.yuqirong.rxnews.R;
import com.yuqirong.rxnews.event.NewsEvent;
import com.yuqirong.rxnews.ui.fragment.NewsDetailFragment;
import com.yuqirong.rxnews.ui.view.RevealBackgroundView;

import butterknife.Bind;

/**
 * Created by Anyway on 2016/3/9.
 */
public class NewsDetailActivity extends BaseActivity implements RevealBackgroundView.OnStateChangeListener {

    @Bind(R.id.mRevealBackground)
    RevealBackgroundView mRevealBackground;
    @Bind(R.id.mFrameLayout)
    FrameLayout mFrameLayout;
    @Bind(R.id.mLinearLayout)
    LinearLayout mLinearLayout;

    NewsDetailFragment mNewsDetailFragment;

    public static final String ARG_REVEAL_START_LOCATION = "arg_reveal_start_location";

    @Override
    public int getContentViewId() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setupRevealBackground(savedInstanceState);
        mNewsDetailFragment = new NewsDetailFragment();
        mNewsDetailFragment.setArguments(getIntent().getBundleExtra("location"));
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mFrameLayout, mNewsDetailFragment, "NewsDetailFragment").commit();
    }

    private void setupRevealBackground(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mRevealBackground.setFillPaintColor(getResources().getColor(R.color.colorPrimary, this.getTheme()));
        } else {
            mRevealBackground.setFillPaintColor(getResources().getColor(R.color.colorPrimary));
        }
        mRevealBackground.setOnStateChangeListener(this);
        if (savedInstanceState == null) {
            final int[] startingLocation = getIntent().getBundleExtra("location").getIntArray(ARG_REVEAL_START_LOCATION);
            mRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                    mRevealBackground.startFromLocation(startingLocation);
                    return false;
                }
            });
        } else {
            mRevealBackground.setToFinishedFrame();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
            if (RevealBackgroundView.STATE_FINISHED == state) {
                mRevealBackground.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mLinearLayout.setBackgroundColor(getResources().getColor(android.R.color.white, getTheme()));
                }else{
                    mLinearLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
                }
            }
        }
    }

    public void onEventMainThread(NewsEvent newsEvent) {

    }

}
