package com.miyako.ticketunion.module.home.category;

import android.os.Bundle;
import android.view.View;

import com.miyako.ticketunion.R;
import com.miyako.ticketunion.base.BaseFragment;
import com.miyako.ticketunion.base.Constants;
import com.miyako.ticketunion.model.domain.Categories;
import com.miyako.ticketunion.model.domain.HomePagerContent;
import com.miyako.ticketunion.utils.LogUtils;

import java.util.List;


public class CategoryPagerFragment extends BaseFragment implements CategoryPagerContract.IHomePagerView {

    private static final String TAG = "HomePagerFragment";
    private CategoryPagerPresenter mPresenter;

    public static CategoryPagerFragment newInstance(Categories.DataBean category) {
        CategoryPagerFragment homePagerFragment = new CategoryPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID, category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setUpState(State.SUCCESS);
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mPresenter = CategoryPagerPresenter.getInstance();
        mPresenter.bind(this);
    }

    @Override
    protected void loadData() {
        super.loadData();
        Bundle bundle = getArguments();
        String title = bundle.getString(Constants.KEY_HOME_PAGER_TITLE);
        int id = bundle.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        LogUtils.i(TAG, "title:"+title);
        LogUtils.i(TAG, "id:"+id);
        mPresenter.getContentByCategoryId(id);
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
    public void onContentLoaded(List<HomePagerContent.DataBean> contentList) {

    }

    @Override
    public void onLoading(int categoryId) {

    }

    @Override
    public void onError(int categoryId) {

    }

    @Override
    public void onEmpty(int categoryId) {

    }

    @Override
    public void onLoadMoreSuccess(List<HomePagerContent.DataBean> contentList) {

    }

    @Override
    public void onLoadMoreError(int categoryId) {

    }

    @Override
    public void onLoadMoreEmpty(int categoryId) {

    }

    @Override
    public void onLooperLoaded(List<HomePagerContent.DataBean> contentList) {

    }
}
