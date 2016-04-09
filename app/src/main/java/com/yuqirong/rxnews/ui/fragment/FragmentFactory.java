package com.yuqirong.rxnews.ui.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2016/2/25.
 */
public class FragmentFactory {

    private FragmentFactory() {

    }

    public static Fragment createNewsFragment(String type, String id) {
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.setParams(type, id);
        return newsFragment;
    }

    public static Fragment createVideoFragment(String id) {
        VideoFragment videoFragment = new VideoFragment();
        videoFragment.setParams(id);
        return videoFragment;
    }

}
