package com.miyako.ticketunion.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    private Unbinder mBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        mBinder = ButterKnife.bind(this);
        initView();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBinder != null) {
            mBinder.unbind();
            mBinder = null;
        }
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
}
