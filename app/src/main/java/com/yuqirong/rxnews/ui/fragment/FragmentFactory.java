package com.yuqirong.rxnews.ui.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2016/2/25.
 */
public class FragmentFactory {

    private FragmentFactory() {

    }

    public static Fragment create(String type, String id) {
        MainFragment mainFragment = new MainFragment();
        mainFragment.setParams(type, id);
        return mainFragment;
    }

}
