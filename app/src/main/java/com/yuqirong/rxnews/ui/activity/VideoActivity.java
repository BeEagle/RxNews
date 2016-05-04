package com.yuqirong.rxnews.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yuqirong.rxnews.R;
import com.yuqirong.rxnews.app.Constant;
import com.yuqirong.rxnews.event.NewsEvent;
import com.yuqirong.rxnews.ui.adapter.FragmentAdapter;
import com.yuqirong.rxnews.ui.fragment.FragmentFactory;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Anyway on 2016/4/9.
 */
public class VideoActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab)
    FloatingActionButton mFAButton;
    @Bind(R.id.mTabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.mViewPager)
    ViewPager mViewPager;
    private FragmentAdapter adapter;
    private FragmentManager fm;

    @Override
    public int getContentViewId() {
        return R.layout.activity_video;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm);
        adapter.addFragment(FragmentFactory.createVideoFragment(Constant.VIDEO_HOT_ID), Constant.VIDEO_TITLE_ARRAYS[0]);
        adapter.addFragment(FragmentFactory.createVideoFragment(Constant.VIDEO_ENTERTAINMENT_ID), Constant.VIDEO_TITLE_ARRAYS[1]);
        adapter.addFragment(FragmentFactory.createVideoFragment(Constant.VIDEO_FUN_ID), Constant.VIDEO_TITLE_ARRAYS[2]);
        adapter.addFragment(FragmentFactory.createVideoFragment(Constant.VIDEO_CHOICE_ID), Constant.VIDEO_TITLE_ARRAYS[3]);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @OnClick({R.id.ib_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:

                break;
        }
    }

    public void onEventMainThread(NewsEvent newsEvent) {

    }

}
