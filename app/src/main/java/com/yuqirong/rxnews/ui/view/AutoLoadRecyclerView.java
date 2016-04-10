package com.yuqirong.rxnews.ui.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.widget.AbsListView;

import com.yuqirong.rxnews.ui.adapter.LoadMoreAdapter;

/**
 * 加载更多RecyclerView
 * Created by yuqirong on 2016/2/25.
 */
public class AutoLoadRecyclerView extends RecyclerView {

    // 加载更多监听器
    private OnLoadingMoreListener listener;
    /**
     * RecyclerView处于加载状态
     */
    public static final int STATE_LOADING = 1;
    /**
     * RecyclerView处于普通状态
     */
    public static final int STATE_NORMAL = 0;
    // RecyclerView的状态
    private int state = STATE_NORMAL;

    public AutoLoadRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 加载监听器
     */
    public interface OnLoadingMoreListener {
        public void onLoadingMore();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        // 如果adapter是LoadMoreAdapter的话
        if (adapter instanceof LoadMoreAdapter) {
            this.addOnScrollListener(new OnScrollListener() {
                                         // 最后一个可见的item的position
                                         private int lastVisibleItemPosition = RecyclerView.NO_POSITION;
                                         private int[] lastVisibleItemPositions;

                                         @Override
                                         public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                             super.onScrolled(recyclerView, dx, dy);
                                             LayoutManager layoutManager = getLayoutManager();
                                             if (layoutManager instanceof StaggeredGridLayoutManager) {
                                                 if (lastVisibleItemPositions == null) {
                                                     lastVisibleItemPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                                                 }
                                                 ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastVisibleItemPositions);
                                                 for (int pos : lastVisibleItemPositions) {
                                                     lastVisibleItemPosition = Math.max(lastVisibleItemPosition, pos);
                                                 }
                                             } else if (layoutManager instanceof LinearLayoutManager) {
                                                 lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                                             } else if (layoutManager instanceof GridLayoutManager) {
                                                 lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                                             }
                                         }

                                         @Override
                                         public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                                             if (getAdapter() == null) {
                                                 return;
                                             }
                                             if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastVisibleItemPosition == getAdapter().getItemCount() - 1 && !isLoadingMore()) {
                                                 state = STATE_LOADING;
                                                 if (listener != null) {
                                                     listener.onLoadingMore();
                                                 }
                                             }
                                         }
                                     }
            );
        }
    }

    /**
     * 通知AutoLoadRecyclerView加载完成
     */
    public void notifyLoadingFinish() {
        state = STATE_NORMAL;
    }

    /**
     * 判断AutoLoadRecyclerView是否正在加载
     * @return 如果返回true说明是正在加载状态；否则就是普通状态
     */
    public boolean isLoadingMore() {
        return state == STATE_LOADING;
    }

    public void setOnLoadingMoreListener(OnLoadingMoreListener listener) {
        this.listener = listener;
    }

}