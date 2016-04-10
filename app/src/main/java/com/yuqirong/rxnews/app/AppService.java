package com.yuqirong.rxnews.app;

import com.google.gson.Gson;
import com.yuqirong.greendao.DBHelper;
import com.yuqirong.greendao.ResultEntityDao;
import com.yuqirong.rxnews.network.RxNewsAPI;
import com.yuqirong.rxnews.network.RxNewsFactory;
import com.yuqirong.rxnews.rxmethod.RxCollection;

import de.greenrobot.event.EventBus;

/**
 * Created by yuqirong on 2016/2/24.
 */
public class AppService {

    private RxNewsAPI mRxNewsAPI;
    private EventBus mEventBus;
    private static AppService mAppService;
    private RxCollection mRxCollection;
    private DBHelper mDBHelper;
    private ResultEntityDao mResultEntityDao;
    private Gson mGson;

    private static final Object WATCH_DOG = new Object();

    private AppService() {
        mEventBus = EventBus.getDefault();
        mRxCollection = RxCollection.getInstance();
        mRxNewsAPI = RxNewsFactory.getRxNewsAPIInstance();
        mDBHelper = DBHelper.getInstance(MyApplication.getContext());
        mResultEntityDao = mDBHelper.getResultEntityDao();
        mGson = new Gson();
    }

    public EventBus getEventBus() {
        return mEventBus;
    }

    public RxNewsAPI getRxNewsAPI() {
        return mRxNewsAPI;
    }

    public RxCollection getRxCollection() {
        return mRxCollection;
    }

    public DBHelper getDBHelper() {
        return mDBHelper;
    }

    public ResultEntityDao getResultEntityDao() {
        return mResultEntityDao;
    }

    public Gson getGson() {
        return mGson;
    }

    public static AppService getInstance() {
        if (mAppService == null) {
            synchronized (WATCH_DOG) {
                if (mAppService == null) {
                    mAppService = new AppService();
                }

            }
        }
        return mAppService;
    }

}
