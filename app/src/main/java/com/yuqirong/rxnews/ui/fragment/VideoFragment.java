package com.yuqirong.rxnews.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.yuqirong.rxnews.R;
import com.yuqirong.rxnews.app.Constant;
import com.yuqirong.rxnews.event.VideoEvent;
import com.yuqirong.rxnews.module.video.presenter.VideoPresenter;
import com.yuqirong.rxnews.module.video.view.IVideoView;
import com.yuqirong.rxnews.ui.activity.VideoPlayActivity;
import com.yuqirong.rxnews.ui.adapter.VideoAdapter;
import com.yuqirong.rxnews.ui.view.AutoLoadRecyclerView;
import com.yuqirong.rxnews.ui.view.SpacesItemDecoration;
import com.yuqirong.rxnews.util.DisplayUtils;
import com.yuqirong.rxnews.util.NetworkUtils;

import butterknife.Bind;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Anyway on 2016/4/9.
 */
public class VideoFragment extends BaseFragment implements IVideoView, VideoAdapter.OnItemClickListener, AutoLoadRecyclerView.OnLoadingMoreListener, SwipeRefreshLayout.OnRefreshListener {

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
        mRecyclerView.setOnLoadingMoreListener(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(DisplayUtils.dip2px(mContext, 4)));
        videoAdapter = new VideoAdapter(layoutManager);
        mRecyclerView.setAdapter(videoAdapter);
        videoAdapter.setOnItemClickListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(SWIPE_REFRESH_LAYOUT_COLOR);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mVideoPresenter = new VideoPresenter(this);
        if (!NetworkUtils.isConnected(mContext)) {
            mVideoPresenter.initVideo(getTaskId(), id);
        }
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
    public void showSuccess(VideoEvent videoEvent) {
        Constant.Tag tag = videoEvent.getTag();
        switch (tag) {
            case INIT:
                videoAdapter.getList().addAll(videoEvent.getVideos());
                videoAdapter.notifyDataSetChanged();
                break;
            case REFRESH:
                videoAdapter.resetAnimPosition(); // 重置animPosition
                videoAdapter.getList().clear();
                videoAdapter.getList().addAll(videoEvent.getVideos());
                videoAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
                break;
            case LOAD_MORE:
                videoAdapter.getList().addAll(videoEvent.getVideos());
                videoAdapter.notifyDataSetChanged();
                mRecyclerView.notifyLoadingFinish();
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
        Bundle bundle = new Bundle();
        bundle.putString("videoUrl", videoAdapter.getList().get(position).mp4Url);
        startActivity(VideoPlayActivity.class, bundle);
    }

    @Override
    public void onLoadingMore() {
        startPage += 10;
        mVideoPresenter.getVideo(getTaskId(), id, startPage);
    }

    @Override
    public void onRefresh() {
        startPage = 0;
        mVideoPresenter.getVideo(getTaskId(), id, startPage);
    }
}
