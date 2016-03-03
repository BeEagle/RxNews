package com.yuqirong.rxnews.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/24.
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> list = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        list.add(fragment);
        titles.add(title);
    }

    public void clear() {
        if (list != null) {
            list.clear();
        }
        if (titles != null) {
            titles.clear();
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

}