package com.miyako.ticketunion.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.opengl.Visibility;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miyako.ticketunion.R;
import com.miyako.ticketunion.utils.LogUtils;

public class LoadingView extends androidx.appcompat.widget.AppCompatImageView {

    private static final String TAG = "LoadingView";
    private float mDegrees = 30;
    private boolean mIsRotate;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(mDegrees, getWidth() >> 1, getHeight() >> 1);
        super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LogUtils.d(TAG, "onAttachedToWindow");
        startRotate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtils.d(TAG, "onDetachedFromWindow");
        stopRotate();
    }

    /**
     * 当前View或其祖先的可见性改变
     * @param changedView 改变view
     * @param visibility 可见性
     */
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        LogUtils.d(TAG, "onVisibilityChanged:"+visibility);
        if (visibility==GONE) {
            stopRotate();
        }
    }

    /**
     * 包含当前View的window可见性改变
     * @param visibility 可见性
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        LogUtils.d(TAG, "onWindowVisibilityChanged:"+visibility);
        if (visibility==GONE) {
            stopRotate();
        }
    }

    private void startRotate() {
        if (mIsRotate) {
            return;
        }
        mIsRotate = true;
        LogUtils.d(TAG, "开始旋转");
        post(new Runnable() {
            @Override
            public void run() {
                mDegrees += 10;
                if (mDegrees >=360) mDegrees = 0;
                invalidate();
                if (!mIsRotate) {
                    LogUtils.d(TAG, "停止旋转");
                    removeCallbacks(this);
                } else {
                    postDelayed(this, 10);
                }
            }
        });
    }

    private void stopRotate() {
        mIsRotate = false;
    }
}
