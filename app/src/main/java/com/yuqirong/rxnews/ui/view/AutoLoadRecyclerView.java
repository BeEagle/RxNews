package com.yuqirong.rxnews.ui.view;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.AbsListView;

import com.yuqirong.rxnews.ui.adapter.LoadMoreAdapter;

/**
 * Created by Administrator on 2016/2/25.
 */
public class AutoLoadRecyclerView extends RecyclerView {

    // 加载更多监听器
    private LoadMoreAdapter.OnLoadingMoreListener listener;

    private LoadMoreAdapter adapter;

    public AutoLoadRecyclerView(Context context) {
        this(context, null);
    }

    public AutoLoadRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        this.adapter = (LoadMoreAdapter) adapter;
        ((LoadMoreAdapter) adapter).setOnLoadingMoreListener(listener);
    }

    public void setOnLoadingMoreListener(LoadMoreAdapter.OnLoadingMoreListener listener) {
        this.listener = listener;
    }

    private void initView() {
        this.setHasFixedSize(true);
        this.setItemAnimator(new DefaultItemAnimator());
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        this.setLayoutManager(mLinearLayoutManager);
        this.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        this.addOnScrollListener(new OnScrollListener() {
            private int lastVisibleItem;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                if (adapter == null) {
                    return;
                }
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastVisibleItem == adapter.getItemCount() - 1 && !adapter.isLoadingMore()) {
                    adapter.setIsLoadingMore(true);
                    if (listener != null) {
                        listener.onLoadingMore();
                    }
                }
            }
        });
    }

}