package com.miyako.ticketunion.utils;

import com.miyako.ticketunion.base.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static final String TAG = "RetrofitManager";
    private static final RetrofitManager obj = new RetrofitManager();
    private final Retrofit mRetrofit;

    private RetrofitManager() {
        LogUtils.i(TAG, "创建Retrofit实例");
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitManager getInstance() {
        return obj;
    }

    /**
     * 获取retrofit单例
     * @return mRetrofit
     */
    public Retrofit getRetrofit() {
        return mRetrofit;
    }
}
