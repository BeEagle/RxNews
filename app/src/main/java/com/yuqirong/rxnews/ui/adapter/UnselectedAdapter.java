package com.yuqirong.rxnews.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yuqirong.greendao.ChannelEntity;
import com.yuqirong.rxnews.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Anyway on 2016/5/17.
 */
public class UnselectedAdapter extends BaseAdapter {

    private Context context;
    private List<ChannelEntity> list;

    public UnselectedAdapter(Context context, List list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ChannelEntity channelEntity = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_channel_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_text.setText(channelEntity.getName());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_text)
        TextView tv_text;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }

}
