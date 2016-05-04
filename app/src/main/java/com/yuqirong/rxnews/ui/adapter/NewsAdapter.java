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
import com.yuqirong.rxnews.module.news.model.bean.News;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/2/25.
 */
public class NewsAdapter extends LoadMoreAdapter<News> {

    private OnItemClickListener listener;
    // 动画执行到的position
    private int animPosition = -1;

    public NewsAdapter(RecyclerView.LayoutManager layoutManager) {
        super(layoutManager);
    }

    public void resetAnimPosition() {
        animPosition = -1;
    }

    @Override
    public RecyclerView.ViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void bindCustomViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ViewHolder mViewHolder = (ViewHolder) holder;
        News news = getList().get(position);
        mViewHolder.tv_title.setText(news.title);
        mViewHolder.tv_content.setText(news.digest);
        mViewHolder.tv_time.setText(news.ptime);
        Glide.with(mViewHolder.mContext).load(news.imgsrc).centerCrop()
                .placeholder(R.drawable.thumbnail_default).crossFade().into(mViewHolder.iv_img);
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

        @Bind(R.id.iv_img)
        public ImageView iv_img;
        @Bind(R.id.tv_title)
        public TextView tv_title;
        @Bind(R.id.tv_time)
        public TextView tv_time;
        @Bind(R.id.tv_content)
        public TextView tv_content;
        public Context mContext;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this,itemView);
        }

    }

    public interface OnItemClickListener {

        void onItemClick(View itemView, int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
