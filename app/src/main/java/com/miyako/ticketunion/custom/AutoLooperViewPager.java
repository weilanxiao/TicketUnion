package com.miyako.ticketunion.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.miyako.ticketunion.utils.LogUtils;

public class AutoLooperViewPager extends ViewPager {

    private static final String TAG = "AutoLooperViewPager";

    private boolean isLoop;
    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            int currentItem = getCurrentItem();
            currentItem++;
            setCurrentItem(currentItem);
            if (isLoop) {
                postDelayed(this, 3000);
            }
        }
    };

    public AutoLooperViewPager(@NonNull Context context) {
        super(context);
    }

    public AutoLooperViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void startLoop() {
        LogUtils.d(TAG, "startLoop");
        if (isLoop) {
            return;
        }
        isLoop = true;
        post(mTask);
    }

    public void stopLoop() {
        LogUtils.d(TAG, "stopLoop");
        isLoop = false;
        removeCallbacks(mTask);
    }
}
