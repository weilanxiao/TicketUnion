package com.miyako.ticketunion.module.home.category;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miyako.ticketunion.R;
import com.miyako.ticketunion.base.BaseFragment;
import com.miyako.ticketunion.base.Constants;
import com.miyako.ticketunion.model.domain.Categories;
import com.miyako.ticketunion.model.domain.HomePagerContent;
import com.miyako.ticketunion.module.adapter.CategoryPagerAdapter;
import com.miyako.ticketunion.utils.LogUtils;

import java.util.List;

import butterknife.BindView;


public class CategoryPagerFragment extends BaseFragment implements CategoryPagerContract.IHomePagerView {

    private static final String TAG = "HomePagerFragment";
    private CategoryPagerPresenter mPresenter;
    private int mMaterialId;

    @BindView(R.id.home_pager_content_list)
    RecyclerView mContentListView;
    private CategoryPagerAdapter mAdapter;

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
        return R.layout.fragment_home_pager_container;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mContentListView.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentListView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 5;
                outRect.bottom = 5;
            }
        });
        mAdapter = new CategoryPagerAdapter();
        mContentListView.setAdapter(mAdapter);
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
        mMaterialId = bundle.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        LogUtils.i(TAG, "title:"+title);
        LogUtils.i(TAG, "id:"+ mMaterialId);
        mPresenter.getContentByCategoryId(mMaterialId);
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
        // TODO: 2020-06-14-0014 更新UI
        setUpState(State.SUCCESS);
        mAdapter.setData(contentList);
    }

    @Override
    public int getMaterialId() {
        return mMaterialId;
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onError(int errorCode, String msg) {
        setUpState(State.ERROR);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onLoadMoreSuccess(List<HomePagerContent.DataBean> contentList) {

    }

    @Override
    public void onLoadMoreError() {

    }

    @Override
    public void onLoadMoreEmpty() {

    }

    @Override
    public void onLooperLoaded(List<HomePagerContent.DataBean> contentList) {

    }
}
