package com.miyako.ticketunion.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.miyako.ticketunion.base.MyApplication;
import com.miyako.ticketunion.model.CacheWithDuration;

public class JsonCacheUtils {

    private static final String TAG = "JsonCacheUtils";

    private static final String JSON_CACHE_SP_NAME = "json_cache_sp_name";
    private final SharedPreferences mSharedPreferences;
    private final Gson mGson;

    private static final class obj {
        private static final JsonCacheUtils obj = new JsonCacheUtils();
    }

    public static JsonCacheUtils getInstance() {
        return obj.obj;
    }

    private JsonCacheUtils() {
        mSharedPreferences = MyApplication.getContext().getSharedPreferences(JSON_CACHE_SP_NAME, Context.MODE_PRIVATE);
        mGson = new Gson();
    }

    public void saveCache(String key, Object value) {
        saveCache(key, value, -1L);
    }

    public void saveCache(String key, Object value, long duration) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        if (duration!=-1L) {
            duration += System.currentTimeMillis();
        }
        CacheWithDuration cacheWithDuration = new CacheWithDuration(mGson.toJson(value), duration);
        edit.putString(key, mGson.toJson(cacheWithDuration));
        edit.apply();
    }

    public void delCache(String key) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.remove(key).apply();
    }

    public <T> T getCache(String key, Class<T> clazz) {
        String value = mSharedPreferences.getString(key, null);
        if (null == value || "".equals(value)) {
            return null;
        }
        CacheWithDuration cacheWithDuration = mGson.fromJson(value, CacheWithDuration.class);
        long duration = cacheWithDuration.getDuration();
        if (-1L != duration && duration - System.currentTimeMillis() <= 0) {
            return null;
        } else {
            String cache = cacheWithDuration.getCache();
            return mGson.fromJson(cache, clazz);
        }
    }
}
