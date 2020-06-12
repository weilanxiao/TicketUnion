package com.miyako.ticketunion.base;

import android.app.Application;

import com.miyako.ticketunion.utils.LogUtil;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.setCurrent(LogUtil.DEBUG);
    }
}
