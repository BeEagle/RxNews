package com.yuqirong.rxnews.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.yuqirong.rxnews.R;
import com.yuqirong.rxnews.app.AppService;
import com.yuqirong.rxnews.app.Constant;
import com.yuqirong.rxnews.event.NewsEvent;
import com.yuqirong.rxnews.module.presenter.NewsPresenter;
import com.yuqirong.rxnews.module.view.INewsView;
import com.yuqirong.rxnews.ui.activity.NewsDetailActivity;
import com.yuqirong.rxnews.ui.adapter.NewsAdapter;
import com.yuqirong.rxnews.ui.view.AutoLoadRecyclerView;

import butterknife.Bind;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by yuqirong on 2016/2/24.
 */
public class MainFragment extends BaseFragment implements INewsView, NewsAdapter.OnItemClickListener {

    @Bind(R.id.mRecyclerView)
    AutoLoadRecyclerView mRecyclerView;
    @Bind(R.id.mProgressBar)
    MaterialProgressBar mProgressBar;

    private NewsAdapter mNewsAdapter;
    private String type;
    private String id;
    private int startPage = 0;
    private static final String TAG = "MainFragment";

    private NewsPresenter mNewsPresenter;

    @Override
    public int getViewId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        mNewsAdapter = new NewsAdapter();
        mRecyclerView.setAdapter(mNewsAdapter);
        mNewsAdapter.setOnItemClickListener(this);
        mNewsPresenter = new NewsPresenter(this);
        mNewsPresenter.initViews(getTaskId(), id);
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
            case REFRESH:
                mNewsAdapter.getList().clear();
                mNewsAdapter.getList().addAll(newsEvent.getNews());
                mNewsAdapter.notifyDataSetChanged();
                break;
            case LOAD_MORE:
                break;
        }
    }

    @Override
    public void showError() {
        Snackbar.make(getRootView(), "网络错误" + type, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View itemView, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id); // id
        bundle.putString("title",mNewsAdapter.getList().get(position).title); // 标题
        bundle.putString("postId", mNewsAdapter.getList().get(position).postid); // 专辑id
        bundle.putString("imgsrc",mNewsAdapter.getList().get(position).imgsrc); // 图片url

        startActivity(NewsDetailActivity.class, "params", bundle);
    }

}
