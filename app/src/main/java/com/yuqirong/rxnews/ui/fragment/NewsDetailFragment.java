package com.yuqirong.rxnews.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuqirong.rxnews.R;
import com.yuqirong.rxnews.app.Constant;
import com.yuqirong.rxnews.event.NewsDetailEvent;
import com.yuqirong.rxnews.module.model.bean.NewsDetail;
import com.yuqirong.rxnews.module.presenter.NewsDetailPresenter;
import com.yuqirong.rxnews.module.view.INewsDetailView;

import butterknife.Bind;

/**
 * Created by Anyway on 2016/3/9.
 */
public class NewsDetailFragment extends BaseFragment implements INewsDetailView {

    @Bind(R.id.mCoordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.mCollapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.iv_album)
    ImageView iv_album;
    @Bind(R.id.mToolbar)
    Toolbar mToolbar;
    @Bind(R.id.webLayout)
    FrameLayout webLayout;


    NewsDetailPresenter mNewsDetailPresenter;
    private String id;

    @Override
    public int getViewId() {
        return R.layout.fragment_news_detail;
    }

    @Override
    protected void initView() {
        mNewsDetailPresenter = new NewsDetailPresenter(this);

    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        String postId = getArguments().getString("postId");
        id = getArguments().getString("id");
        mNewsDetailPresenter.showNewsDetail(getTaskId(), id, postId);
    }

    public void onEventMainThread(NewsDetailEvent event) {
        if (!id.equals(event.getId())) {
            return;
        }
        Constant.Result result = event.getResult();
        if (result == Constant.Result.SUCCESS) {
            mNewsDetailPresenter.showSuccess(event);
        } else {
            mNewsDetailPresenter.showError();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void closeLoading() {

    }

    @Override
    public void showSuccess(NewsDetailEvent event) {
        NewsDetail detail = event.getNewsDetail();
        if (detail.picnews && detail.img.size() > 0) {
            Glide.with(mContext).load(detail.img.get(0).src).centerCrop()
                    .placeholder(R.drawable.thumbnail_default).crossFade().into(iv_album);
        }


    }

    @Override
    public void showError() {
        Snackbar.make(getRootView(), R.string.str_error, Snackbar.LENGTH_SHORT).show();
    }

}

