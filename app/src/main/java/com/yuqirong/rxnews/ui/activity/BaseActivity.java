package com.yuqirong.rxnews.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yuqirong.rxnews.app.AppService;
import com.yuqirong.rxnews.app.MyApplication;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/2/24.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    public abstract int getContentViewId();

    private int mTaskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        mTaskId = getTaskId();
        AppService.getInstance().getRxCollection().addCompositeSub(mTaskId);
        AppService.getInstance().getEventBus().register(this);
        initView(savedInstanceState);
    }

    protected abstract void initView(Bundle savedInstanceState);

    public int getTaskId() {
        return mTaskId;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppService.getInstance().getRxCollection().removeCompositeSub(mTaskId);
        AppService.getInstance().getEventBus().unregister(this);
        MyApplication.getRefWatcher().watch(this);
    }

    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

}
