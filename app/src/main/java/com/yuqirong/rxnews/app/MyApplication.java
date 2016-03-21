package com.yuqirong.rxnews.app;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/2/24.
 */
public class MyApplication extends Application {

    private static Context mApplicationContext;
    private static RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = this;
        mRefWatcher = LeakCanary.install(this);
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }

    public static RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

    public static Context getContext() {
        return mApplicationContext;
    }

}
