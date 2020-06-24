package com.miyako.ticketunion.base;

import android.app.Application;
import android.content.Context;

import com.miyako.ticketunion.utils.LogUtils;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getBaseContext();
        LogUtils.setCurrent(LogUtils.DEBUG);
        LogUtils.i(TAG, "onCreate");
    }

    public static Context getContext() {
        return appContext;
    }
}
