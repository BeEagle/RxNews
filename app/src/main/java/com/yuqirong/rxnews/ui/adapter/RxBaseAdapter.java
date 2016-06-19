package com.yuqirong.rxnews.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/24.
 */
public abstract class RxBaseAdapter<T> extends BaseAdapter {

    private List<T> mList = new ArrayList<>();

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(getItemViewId(), null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        getView(i, viewHolder, viewGroup);
        return view;
    }

    public abstract void getView(int i, ViewHolder viewHolder, ViewGroup viewGroup);


    public abstract int getItemViewId();

    static class ViewHolder {
        public ViewHolder(View itemView) {

        }

    }


}
