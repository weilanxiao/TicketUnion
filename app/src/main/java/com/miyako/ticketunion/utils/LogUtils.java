package com.miyako.ticketunion.utils;

import android.util.Log;

public class LogUtils {

    private static final String TAG = "LogUtil";

    private static int currentLev = 4;

    public static final int DEBUG = 4;
    public static final int INFO = 3;
    public static final int WARNING = 2;
    public static final int ERROR = 1;

    public static void setCurrent(int level) {
        if(level<1||level>4) {
            Log.d(TAG, "log level set error(1~4)");
        }
        else {
            currentLev = level;
            Log.d(TAG, "log level set success");
            Log.d(TAG, "log current level "+level);
        }
    }

    public static void d(String tag, String msg) {
        if (currentLev >= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (currentLev >= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (currentLev >= WARNING) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (currentLev >= ERROR) {
            Log.e(tag, msg);
        }
    }
}
