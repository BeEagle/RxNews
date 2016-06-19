package com.yuqirong.rxnews.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuqirong.greendao.ChannelEntity;
import com.yuqirong.rxnews.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Anyway on 2016/5/14.
 */
public class ChannelAdapter extends DragGridAdapter<ChannelEntity> {

    private Context context;

    public ChannelAdapter(Context context, List list) {
        super(list);
        this.context = context;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ChannelEntity channelEntity = getList().get(position);
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


