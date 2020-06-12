package com.miyako.ticketunion.base;

import android.app.Application;

import com.miyako.ticketunion.utils.LogUtil;

public class MyApplication extends Application {

    public static final String BASE_URL = "https://api.sunofbeach.net/shop/";
    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.setCurrent(LogUtil.DEBUG);
        LogUtil.i(TAG, "onCreate");
    }
}
