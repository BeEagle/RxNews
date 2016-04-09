package com.yuqirong.rxnews.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuqirong.rxnews.R;
import com.yuqirong.rxnews.module.video.model.bean.Video;

/**
 * Created by Administrator on 2016/2/25.
 */
public class VideoAdapter extends LoadMoreAdapter<Video> {

    private OnItemClickListener listener;
    // 动画执行到的position
    private int animPosition = -1;

    public void resetAnimPosition() {
        animPosition = -1;
    }

    @Override
    public RecyclerView.ViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void bindCustomViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ViewHolder mViewHolder = (ViewHolder) holder;
        Video video = getList().get(position);
        mViewHolder.tv_title.setText(video.title);
        Glide.with(mViewHolder.mContext).load(video.cover).centerCrop()
                .placeholder(R.drawable.thumbnail_default).crossFade().into(mViewHolder.iv_album);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(v, position);
                }
            }
        });
        if (position > animPosition) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.bottom_in_anim);
            holder.itemView.setAnimation(animation);
            animPosition++;
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_album;
        public TextView tv_title;
        public Context mContext;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            iv_album = (ImageView) itemView.findViewById(R.id.iv_album);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }

    }

    public interface OnItemClickListener {

        void onItemClick(View itemView, int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
