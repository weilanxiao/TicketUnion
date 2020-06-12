package com.miyako.ticketunion.module.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.miyako.ticketunion.R;
import com.miyako.ticketunion.base.BaseFragment;
import com.miyako.ticketunion.model.domain.Categories;
import com.miyako.ticketunion.utils.LogUtil;

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
        return R.layout.fragment_home;
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
        setUpState(State.LOADING);
        mPresenter.getCategory();
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
        LogUtil.d(TAG, "onCategoriesLoaded");
        if (categories == null || categories.getData().size()==0) {
            setUpState(State.EMPTY);
        }
        if (mHomePagerAdapter != null) {
            mHomePagerAdapter.setCategories(categories);
            setUpState(State.SUCCESS);
        }
    }
}
