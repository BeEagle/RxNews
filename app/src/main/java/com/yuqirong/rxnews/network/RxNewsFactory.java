package com.yuqirong.rxnews.network;

/**
 * Created by Administrator on 2016/2/24.
 */
public class RxNewsFactory {

    private static final Object WATCH_DOG = new Object();
    private static RxNewsClient mRxNewsClient;
    private static RxNewsAPI mRxNewsAPI;

    private RxNewsFactory() {
    }

    public static RxNewsAPI getRxNewsAPIInstance() {
        synchronized (WATCH_DOG) {
            if (mRxNewsClient == null) {
                mRxNewsClient = new RxNewsClient();
                mRxNewsAPI = mRxNewsClient.getRxNewsAPI();
            }
            return mRxNewsAPI;
        }
    }


}
