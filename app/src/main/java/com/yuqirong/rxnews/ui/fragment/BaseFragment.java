package com.yuqirong.rxnews.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuqirong.rxnews.app.AppService;
import com.yuqirong.rxnews.app.MyApplication;

import butterknife.ButterKnife;

/**
 * Created by yuqirong on 2016/2/24.
 */
public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    private View rootView;
    private int mTaskId;
    protected boolean isVisible;
    protected static final String TAG = "BaseFragment";
    protected boolean isPrepared;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.mTaskId = getActivity().getTaskId();
        // 注册 EventBus
        AppService.getInstance().getEventBus().register(this);
    }

    public int getTaskId() {
        return mTaskId;
    }

    public View getRootView() {
        return rootView;
    }

    /**
     * 返回View.xml的id
     *
     * @return
     */
    public abstract int getViewId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getViewId(), container, false);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    protected abstract void initView();

    /**
     * 在这里实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onInvisible() {

    }

    protected void onVisible() {
        //填充各控件的数据
        lazyLoad();
    }

    /**
     * 填充各控件的数据
     */
    protected abstract void lazyLoad();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    protected abstract void initData(Bundle savedInstanceState);

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 注销 EventBus
        AppService.getInstance().getEventBus().unregister(this);
        MyApplication.getRefWatcher().watch(this);
    }

    /**
     * 跳转Activity
     * @param clazz
     * @param bundle
     */
    public void startActivity(Class clazz, Bundle bundle) {
        Intent intent = new Intent(mContext, clazz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 跳转Activity
     * @param clazz
     * @param bundle
     * @param options
     */
    public void startActivity(Class clazz , Bundle bundle,Bundle options) {
        Intent intent = new Intent(mContext, clazz);
        intent.putExtras(bundle);
        startActivity(intent,options);
    }


    /**
     * 跳转Activity
     * @param clazz
     */
    public void startActivity(Class clazz) {
        Intent intent = new Intent(mContext, clazz);
        startActivity(intent);
    }

}
