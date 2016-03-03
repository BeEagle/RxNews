package com.yuqirong.rxnews.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/2/24.
 */
public abstract class RxBaseAdapter<T> extends BaseAdapter {

    private List<T> mList;

    public RxBaseAdapter(List<T> list){
        this.mList = list;
    }

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
        if(view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(getItemLayout(),null);

        }else{

        }


        return null;
    }


    public abstract int getItemLayout();

    static class ViewHolder{


    }


}
