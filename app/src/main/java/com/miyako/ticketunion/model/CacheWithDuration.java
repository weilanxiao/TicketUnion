package com.miyako.ticketunion.model;

public class CacheWithDuration {

    private long duration;
    private String cache;

    public CacheWithDuration(String cache, long time) {
        this.cache = cache;
        duration = time;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    @Override
    public String toString() {
        return "CacheWithDuration{" +
                "duration=" + duration +
                ", cache='" + cache + '\'' +
                '}';
    }
}
