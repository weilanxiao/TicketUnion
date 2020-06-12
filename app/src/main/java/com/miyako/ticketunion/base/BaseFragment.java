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
import com.miyako.ticketunion.utils.LogUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    protected String TAG = this.getClass().getSimpleName();
    private Unbinder mBind;
    private FrameLayout mBaseContianer;

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
        LogUtil.d(TAG, "onCreateView");
        mCurrentState = State.NONE;
        View rootView = inflater.inflate(R.layout.base_fragment_layout, container, false);
        mBaseContianer = rootView.findViewById(R.id.base_container);
        loadStatesView(inflater, container);
        mBind = ButterKnife.bind(this, rootView);
        initView(rootView);
        initPresenter();
        loadData();
        return rootView;
    }

    /**
     * 加载各种状态View
     * @param inflater 加载器
     * @param container 容器
     */
    protected void loadStatesView(LayoutInflater inflater, ViewGroup container) {

        // 完成界面
        mSuccessView = loadSuccess(inflater, container);
        mBaseContianer.addView(mSuccessView);

        // 加载中界面
        mLoadingView = loadLoadingView(inflater, container);
        mBaseContianer.addView(mLoadingView);

        // 错误界面
        mErrorView = loadErrorView(inflater, container);
        mBaseContianer.addView(mErrorView);

        // 空白界面
        mEmptyView = loadEmptyView(inflater, container);
        mBaseContianer.addView(mEmptyView);

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
        LogUtil.d(TAG, "loadRootView");
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
        LogUtil.d(TAG, "initView");
    }

    /**
     * 初始化presenter
     */
    protected void initPresenter() {
        LogUtil.d(TAG, "initPresenter");
    }

    /**
     * 界面加载数据
     */
    protected void loadData() {
        LogUtil.d(TAG, "loadData");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d(TAG, "onDestroyView");
        release();
    }

    /**
     * 释放资源
     */
    protected void release() {
        LogUtil.d(TAG, "release");
        if (mBind != null) {
            mBind.unbind();
            mBind = null;
        }
    }

    protected abstract int getRootViewResId();

}
