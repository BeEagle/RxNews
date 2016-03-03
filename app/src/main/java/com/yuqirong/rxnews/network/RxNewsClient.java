package com.yuqirong.rxnews.network;

import com.yuqirong.rxnews.app.Constant;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;


/**
 * Created by Administrator on 2016/2/24.
 */
public class RxNewsClient {

    private final RxNewsAPI mRxNewsAPI;

    public RxNewsClient() {

       Retrofit mRetrofit = new Retrofit.Builder()
                                .baseUrl(Constant.BASE_URL)
                                .addConverterFactory(JacksonConverterFactory.create())
                                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                .build();
        mRxNewsAPI = mRetrofit.create(RxNewsAPI.class);

    }

    public RxNewsAPI getRxNewsAPI() {
        return mRxNewsAPI;
    }
}

