package com.yuqirong.rxnews.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuqirong.rxnews.R;
import com.yuqirong.rxnews.model.News;

/**
 * Created by Administrator on 2016/2/25.
 */
public class NewsAdapter extends LoadMoreAdapter<News> {

    @Override
    public RecyclerView.ViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void bindCustomViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mViewHolder = (ViewHolder) holder;
        News news = getList().get(position);
        mViewHolder.tv_title.setText(news.title);
        mViewHolder.tv_time.setText(news.ptime);
        Glide.with(mViewHolder.mContext).load(news.imgsrc).centerCrop().placeholder(R.drawable.thumbnail_default).crossFade().into(mViewHolder.iv_img);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_img;
        public TextView tv_title;
        public TextView tv_time;
        public TextView tv_content;
        public Context mContext;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }

    }

}
