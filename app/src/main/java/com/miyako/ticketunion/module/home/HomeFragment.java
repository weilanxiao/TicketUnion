package com.miyako.ticketunion.module.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.miyako.ticketunion.R;
import com.miyako.ticketunion.base.BaseFragment;
import com.miyako.ticketunion.model.domain.Categories;
import com.miyako.ticketunion.module.adapter.HomePagerAdapter;
import com.miyako.ticketunion.utils.LogUtils;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements HomeContract.IHomeView {

    private HomePresenter mPresenter;

    @BindView(R.id.tabL_home_indicator)
    TabLayout mTabLayout;
    @BindView(R.id.vp_home)
    ViewPager mViewPager;
    private HomePagerAdapter mHomePagerAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mPresenter = new HomePresenter();
        mPresenter.bind(this);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mTabLayout.setupWithViewPager(mViewPager);
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mHomePagerAdapter);
    }

    @Override
    protected void loadData() {
        super.loadData();
        mPresenter.getCategories();
    }

    @Override
    protected void onNetError() {
        super.onNetError();
        if (mPresenter != null) {
            mPresenter.getCategories();
        }
    }

    @Override
    protected void release() {
        super.release();
        if (mPresenter != null) {
            mPresenter.unBind(this);
            mPresenter = null;
        }
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        LogUtils.d(TAG, "onCategoriesLoaded");
        if (categories == null || categories.getData().size()==0) {
            setUpState(State.EMPTY);
        }
        if (mHomePagerAdapter != null) {
            mHomePagerAdapter.setCategories(categories);
            setUpState(State.SUCCESS);
        }
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onError(int errorCode, String msg) {

    }

    @Override
    public void onEmpty() {

    }
}
