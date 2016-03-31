package com.yuqirong.rxnews.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.yuqirong.rxnews.R;
import com.yuqirong.rxnews.app.Constant;
import com.yuqirong.rxnews.event.NewsDetailEvent;
import com.yuqirong.rxnews.module.news.model.bean.NewsDetail;
import com.yuqirong.rxnews.module.news.presenter.NewsDetailPresenter;
import com.yuqirong.rxnews.module.news.view.INewsDetailView;

import butterknife.Bind;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Anyway on 2016/3/9.
 */
public class NewsDetailFragment extends BaseFragment implements INewsDetailView {

    @Bind(R.id.mProgressBar)
    MaterialProgressBar mProgressBar;
    @Bind(R.id.tv_content)
    TextView tv_content;

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
        Bundle bundle = getArguments();
        String postId = bundle.getString("postid");
        id = bundle.getString("id");
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
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void closeLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showSuccess(NewsDetailEvent event) {
        NewsDetail detail = event.getNewsDetail();
        tv_content.setText(Html.fromHtml(detail.body));
    }

    @Override
    public void showError() {
        Snackbar.make(getRootView(), R.string.str_error, Snackbar.LENGTH_SHORT).show();
    }

}

