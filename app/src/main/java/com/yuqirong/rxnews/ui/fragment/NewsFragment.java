package com.yuqirong.rxnews.ui.fragment;

import android.app.ActivityOptions;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.yuqirong.rxnews.R;
import com.yuqirong.rxnews.app.AppService;
import com.yuqirong.rxnews.app.Constant;
import com.yuqirong.rxnews.event.NewsEvent;
import com.yuqirong.rxnews.module.presenter.NewsPresenter;
import com.yuqirong.rxnews.module.view.INewsView;
import com.yuqirong.rxnews.ui.activity.NewsDetailActivity;
import com.yuqirong.rxnews.ui.adapter.LoadMoreAdapter;
import com.yuqirong.rxnews.ui.adapter.NewsAdapter;
import com.yuqirong.rxnews.ui.view.AutoLoadRecyclerView;
import com.yuqirong.rxnews.util.NetworkUtils;

import butterknife.Bind;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by yuqirong on 2016/2/24.
 */
public class NewsFragment extends BaseFragment implements INewsView, NewsAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, LoadMoreAdapter.OnLoadingMoreListener {

    @Bind(R.id.mRecyclerView)
    AutoLoadRecyclerView mRecyclerView;
    @Bind(R.id.mProgressBar)
    MaterialProgressBar mProgressBar;
    @Bind(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private NewsAdapter mNewsAdapter;
    private String type;
    private String id;
    private int startPage = 0;
    private static final String TAG = "NewsFragment";
    public static final int[] SWIPE_REFRESH_LAYOUT_COLOR = new int[]{android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light, android.R.color.holo_blue_bright, android.R.color.holo_purple};


    private NewsPresenter mNewsPresenter;

    @Override
    public int getViewId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        mNewsAdapter = new NewsAdapter();
        mRecyclerView.setOnLoadingMoreListener(this);
        mRecyclerView.setAdapter(mNewsAdapter);
        mNewsAdapter.setOnItemClickListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(SWIPE_REFRESH_LAYOUT_COLOR);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mNewsPresenter = new NewsPresenter(this);
        if (!NetworkUtils.isConnected(mContext)) {
            mNewsPresenter.initViews(getTaskId(), id);
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        isPrepared = true;
        lazyLoad();
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        mNewsPresenter.getNews(getTaskId(), type, id, startPage);
    }

    public void onEventMainThread(NewsEvent newsEvent) {
        if (!id.equals(newsEvent.getId())) {
            return;
        }
        Constant.Result result = newsEvent.getResult();
        if (result == Constant.Result.SUCCESS) {
            mNewsPresenter.showSuccess(newsEvent);
        } else {
            mNewsPresenter.showError();
        }
    }

    /**
     * 设置专题的关键字
     */
    public void setParams(String type, String id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void getNews() {
        AppService.getInstance().getRxCollection().getNews(getTaskId(), type, id, startPage);
    }

    @Override
    public void clearLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showSuccess(NewsEvent newsEvent) {
        Constant.Tag tag = newsEvent.getTag();
        switch (tag) {
            case INIT:
                mNewsAdapter.getList().addAll(newsEvent.getNews());
                mNewsAdapter.notifyDataSetChanged();
                break;
            case REFRESH:
                mNewsAdapter.resetAnimPosition(); // 重置animPosition
                mNewsAdapter.getList().clear();
                mNewsAdapter.getList().addAll(newsEvent.getNews());
                mNewsAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
                break;
            case LOAD_MORE:
                mNewsAdapter.getList().addAll(newsEvent.getNews());
                mNewsAdapter.notifyDataSetChanged();
                mNewsAdapter.completeLoadMore(true);
                break;
        }
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError() {
        Snackbar.make(getRootView(), "好像有哪里不对", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View itemView, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id); // id
        bundle.putString("title", mNewsAdapter.getList().get(position).title); // 标题
        bundle.putString("postId", mNewsAdapter.getList().get(position).postid); // 专辑id
        bundle.putString("imgsrc", mNewsAdapter.getList().get(position).imgsrc); // 图片url

        // Android 5.0 使用转场动画
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(getActivity(),
                            itemView.findViewById(R.id.iv_img), "photos");
            startActivity(NewsDetailActivity.class, bundle, options.toBundle());
        } else {
            //让新的Activity从一个小的范围扩大到全屏
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeScaleUpAnimation(itemView, itemView.getWidth() / 2,
                            itemView.getHeight() / 2, 0, 0);
            startActivity(NewsDetailActivity.class, bundle, options.toBundle());
        }
    }

    @Override
    public void onRefresh() { // 下拉刷新
        startPage = 0;
        mNewsPresenter.getNews(getTaskId(), type, id, startPage);
    }

    @Override
    public void onLoadingMore() {
        startPage += 20;
        mNewsPresenter.getNews(getTaskId(), type, id, startPage);
    }
}
