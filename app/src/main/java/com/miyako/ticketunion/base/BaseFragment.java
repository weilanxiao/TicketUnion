package com.miyako.ticketunion.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.miyako.ticketunion.R;
import com.miyako.ticketunion.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    protected String TAG = this.getClass().getSimpleName();
    private Unbinder mBind;
    private FrameLayout mBaseContainer;

    private State mCurrentState;
    private View mLoadingView;
    private View mSuccessView;
    private View mErrorView;
    private View mEmptyView;

    public enum State {
        NONE, LOADING, SUCCESS, ERROR, EMPTY
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.d(TAG, "onCreateView");
        mCurrentState = State.NONE;
        View rootView = loadRootView(inflater, container);
        mBaseContainer = rootView.findViewById(R.id.base_container);
        loadStatesView(inflater, container);
        mBind = ButterKnife.bind(this, rootView);
        initView(rootView);
        initListener();
        initPresenter();
        loadData();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onCreateView");
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_fragment_layout, container, false);
    }

    /**
     * 加载各种状态View
     * @param inflater 加载器
     * @param container 容器
     */
    protected void loadStatesView(LayoutInflater inflater, ViewGroup container) {

        // 完成界面
        mSuccessView = loadSuccess(inflater, container);
        mBaseContainer.addView(mSuccessView);

        // 加载中界面
        mLoadingView = loadLoadingView(inflater, container);
        mBaseContainer.addView(mLoadingView);

        // 错误界面
        mErrorView = loadErrorView(inflater, container);
        mBaseContainer.addView(mErrorView);

        // 空白界面
        mEmptyView = loadEmptyView(inflater, container);
        mBaseContainer.addView(mEmptyView);

        setUpState(State.NONE);
    }

    /**
     * 空白界面
     * @param inflater 加载器
     * @param container 容器
     * @return 空白view
     */
    protected View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty, container, false);
    }

    /**
     * 加载界面
     * @param inflater 加载器
     * @param container 容器
     * @return 加载view
     */
    protected View loadLoadingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    /**
     * 完成界面
     * @param inflater 加载器
     * @param container 容器
     * @return 完成view
     */
    protected View loadSuccess(LayoutInflater inflater, ViewGroup container) {
        LogUtils.d(TAG, "loadRootView");
        int resId = getRootViewResId();
        return inflater.inflate(resId, container, false);
    }

    /**
     * 错误界面
     * @param inflater 加载器
     * @param container 容器
     * @return 错误view
     */
    protected View loadErrorView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    /**
     * 切换界面状态
     * @param state 状态
     */
    protected void setUpState(State state) {
        mCurrentState = state;
        mSuccessView.setVisibility(mCurrentState == State.SUCCESS ? View.VISIBLE : View.GONE);
        mLoadingView.setVisibility(mCurrentState == State.LOADING ? View.VISIBLE : View.GONE);
        mErrorView.setVisibility(mCurrentState == State.ERROR ? View.VISIBLE : View.GONE);
        mEmptyView.setVisibility(mCurrentState == State.EMPTY ? View.VISIBLE : View.GONE);
    }

    /**
     * 初始化view
     * @param rootView view
     */
    protected void initView(View rootView) {
        LogUtils.d(TAG, "initView");
    }

    /**
     * 初始化监听器
     */
    protected void initListener() {
        LogUtils.d(TAG, "initListener");
    }

    /**
     * 初始化presenter
     */
    protected void initPresenter() {
        LogUtils.d(TAG, "initPresenter");
    }

    /**
     * 界面加载数据
     */
    protected void loadData() {
        LogUtils.d(TAG, "loadData");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.d(TAG, "onDestroyView");
        release();
    }

    @OnClick({R.id.layout_net_error})
    public void netError(View view) {
        LogUtils.d(TAG, "on retry");
        onNetError();
    }

    protected void onNetError() {
        LogUtils.d(TAG, "on retry");
    }

    /**
     * 释放资源
     */
    protected void release() {
        LogUtils.d(TAG, "release");
        if (mBind != null) {
            mBind.unbind();
            mBind = null;
        }
    }

    protected abstract int getRootViewResId();

}
