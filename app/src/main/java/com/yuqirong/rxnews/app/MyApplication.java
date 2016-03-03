package com.yuqirong.rxnews.app;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

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
    }

    public static RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

    public static Context getContext() {
        return mApplicationContext;
    }
}
