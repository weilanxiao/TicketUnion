package com.miyako.ticketunion.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.miyako.ticketunion.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    private Unbinder mBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d(TAG, "onCreate");
        setContentView(getLayoutResId());
        mBinder = ButterKnife.bind(this);
        initView();
        initListener();
        initPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy");
        if (mBinder != null) {
            mBinder.unbind();
            mBinder = null;
        }
        release();
    }

    /**
     * 获取布局资源Id
     * @return 资源Id
     */
    protected abstract int getLayoutResId();

    /**
     * 初始化视图相关
     */
    protected abstract void initView();

    /**
     * 初始化监听器相关
     */
    protected abstract void initListener();

    /**
     * 初始化Presenter相关
     */
    protected abstract void initPresenter();

    /**
     * 子类释放资源
     */
    protected void release() {
        LogUtils.d(TAG, "release");
    }
}
