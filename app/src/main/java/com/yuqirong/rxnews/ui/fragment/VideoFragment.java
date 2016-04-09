package com.yuqirong.rxnews.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.yuqirong.rxnews.R;
import com.yuqirong.rxnews.app.AppService;
import com.yuqirong.rxnews.app.Constant;
import com.yuqirong.rxnews.event.VideoEvent;
import com.yuqirong.rxnews.module.video.presenter.VideoPresenter;
import com.yuqirong.rxnews.module.video.view.IVideoView;
import com.yuqirong.rxnews.ui.adapter.LoadMoreAdapter;
import com.yuqirong.rxnews.ui.adapter.VideoAdapter;
import com.yuqirong.rxnews.ui.view.AutoLoadRecyclerView;

import butterknife.Bind;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Anyway on 2016/4/9.
 */
public class VideoFragment extends BaseFragment implements IVideoView, VideoAdapter.OnItemClickListener, LoadMoreAdapter.OnLoadingMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.mRecyclerView)
    AutoLoadRecyclerView mRecyclerView;
    @Bind(R.id.mProgressBar)
    MaterialProgressBar mProgressBar;
    @Bind(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private VideoPresenter mVideoPresenter;
    private String id;
    private int startPage = 0;
    private static final String TAG = "VideoFragment";
    public static final int[] SWIPE_REFRESH_LAYOUT_COLOR = new int[]{android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light, android.R.color.holo_blue_bright, android.R.color.holo_purple};
    private VideoAdapter videoAdapter;

    public void setParams(String id) {
        this.id = id;
    }

    @Override
    public int getViewId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {
        videoAdapter = new VideoAdapter();
        mRecyclerView.setOnLoadingMoreListener(this);
        mRecyclerView.setAdapter(videoAdapter);
        videoAdapter.setOnItemClickListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(SWIPE_REFRESH_LAYOUT_COLOR);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mVideoPresenter = new VideoPresenter(this);
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        mVideoPresenter.getVideo(getTaskId(), id, startPage);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        isPrepared = true;
        lazyLoad();
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void clearLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void getVideo() {
        AppService.getInstance().getRxCollection().getVideo(getTaskId(), id, startPage);
    }

    @Override
    public void showSuccess(VideoEvent videoEvent) {
        Constant.Tag tag = videoEvent.getTag();
        switch (tag) {
            case REFRESH:
                videoAdapter.resetAnimPosition(); // 重置animPosition
                videoAdapter.getList().clear();
                videoAdapter.getList().addAll(videoEvent.getVideos());
                videoAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
                break;
            case LOAD_MORE:
//                mNewsAdapter.getList().addAll(newsEvent.getNews());
//                mNewsAdapter.notifyDataSetChanged();
//                mNewsAdapter.completeLoadMore(true);
                break;
        }
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError() {
        Snackbar.make(getRootView(), R.string.str_error, Snackbar.LENGTH_SHORT).show();
    }

    public void onEventMainThread(VideoEvent videoEvent) {
        if (!id.equals(videoEvent.getId())) {
            return;
        }
        Constant.Result result = videoEvent.getResult();
        if (result == Constant.Result.SUCCESS) {
            mVideoPresenter.showSuccess(videoEvent);
        } else {
            mVideoPresenter.showError();
        }
    }

    @Override
    public void onItemClick(View itemView, int position) {

    }

    @Override
    public void onLoadingMore() {

    }

    @Override
    public void onRefresh() {

    }
}
