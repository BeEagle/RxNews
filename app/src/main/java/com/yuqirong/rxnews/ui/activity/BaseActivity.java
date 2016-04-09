package com.yuqirong.rxnews.ui.activity;

import android.content.Intent;
import android.os.Build;
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

    /**
     * 跳转Activity
     * @param clazz
     * @param bundle
     */
    public void startActivity(Class clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
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
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options);
        }else{
            startActivity(intent);
        }
    }


    /**
     * 跳转Activity
     * @param clazz
     */
    public void startActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

}
