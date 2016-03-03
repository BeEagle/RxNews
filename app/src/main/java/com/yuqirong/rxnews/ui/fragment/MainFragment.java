package com.yuqirong.rxnews.ui.fragment;

import android.os.Bundle;
import android.util.Log;

import com.yuqirong.rxnews.R;
import com.yuqirong.rxnews.app.AppService;
import com.yuqirong.rxnews.app.Constant;
import com.yuqirong.rxnews.event.NewsEvent;
import com.yuqirong.rxnews.ui.adapter.NewsAdapter;
import com.yuqirong.rxnews.ui.view.AutoLoadRecyclerView;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/2/24.
 */
public class MainFragment extends BaseFragment {

    @Bind(R.id.mRecyclerView)
    AutoLoadRecyclerView mRecyclerView;

    private NewsAdapter mNewsAdapter;

    private String type;
    private String id;
    private static final String TAG = "MainFragment";
    private int startPage = 0;

    @Override
    public int getViewId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        mNewsAdapter = new NewsAdapter();
        mRecyclerView.setAdapter(mNewsAdapter);
        AppService.getInstance().getRxCollection().initNews(getTaskId(), id);
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
        AppService.getInstance().getRxCollection().getNews(getTaskId(), type, id, startPage);
    }

    public void onEventMainThread(NewsEvent newsEvent) {
        if (!id.equals(newsEvent.getId())) {
            return;
        }
        Constant.Tag tag = newsEvent.getTag();
        switch (tag) {
            case INIT:
            case REFRESH:
                Constant.Result result = newsEvent.getResult();
                if (result == Constant.Result.SUCCESS) {
                    mNewsAdapter.getList().clear();
                    mNewsAdapter.getList().addAll(newsEvent.getNews());
                    mNewsAdapter.notifyDataSetChanged();
                } else {
                    Log.i(TAG, "load news error");
                }
                break;
            case LOAD_MORE:
                break;
        }
    }

    /**
     * 设置专题的关键字
     */
    public void setParams(String type, String id) {
        this.type = type;
        this.id = id;
    }

}
