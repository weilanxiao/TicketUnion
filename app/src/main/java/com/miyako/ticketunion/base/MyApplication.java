package com.miyako.ticketunion.base;

import android.app.Application;

import com.miyako.ticketunion.utils.LogUtils;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.setCurrent(LogUtils.DEBUG);
        LogUtils.i(TAG, "onCreate");
    }
}
