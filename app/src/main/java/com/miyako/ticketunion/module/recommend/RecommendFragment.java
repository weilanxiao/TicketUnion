package com.miyako.ticketunion.module.recommend;


import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miyako.ticketunion.R;
import com.miyako.ticketunion.base.BaseFragment;
import com.miyako.ticketunion.model.domain.HomePagerContent;
import com.miyako.ticketunion.model.domain.RecommendCategories;
import com.miyako.ticketunion.model.domain.RecommendContent;
import com.miyako.ticketunion.module.adapter.RecommendCategoryAdapter;
import com.miyako.ticketunion.module.adapter.RecommendContentAdapter;
import com.miyako.ticketunion.module.ticket.TicketActivity;
import com.miyako.ticketunion.utils.LogUtils;
import com.miyako.ticketunion.utils.PresenterManager;
import com.miyako.ticketunion.utils.SizeUtils;

import butterknife.BindView;

public class RecommendFragment extends BaseFragment implements RecommendContract.IRecommendView {

    private RecommendPresenter mPresenter;

    @BindView(R.id.recommend_category_list)
    RecyclerView mRvCategory;
    @BindView(R.id.recommend_category_content_list)
    RecyclerView mRvContent;
    private RecommendCategoryAdapter mCategoryAdapter;
    private RecommendContentAdapter mContentAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setUpState(State.LOADING);
        mRvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        mCategoryAdapter = new RecommendCategoryAdapter();
        mRvCategory.setAdapter(mCategoryAdapter);

        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentAdapter = new RecommendContentAdapter();
        mRvContent.setAdapter(mContentAdapter);
        mRvContent.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 2);
                outRect.right = SizeUtils.dip2px(getContext(), 4);
                outRect.left = SizeUtils.dip2px(getContext(), 4);
                outRect.bottom = SizeUtils.dip2px(getContext(), 2);

            }
        });
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mPresenter = PresenterManager.getInstance().getRecommendPresenter();
        mPresenter.bind(this);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mCategoryAdapter.setOnListItemClickListener(this::onCategoryClick);
        mContentAdapter.setOnListItemClickListener(this::onContentClick);
    }

    private void onCategoryClick(RecommendCategories.DataBean dataBean) {
        LogUtils.d(TAG, "onCategoryClick:"+dataBean.getFavorites_title());
        mPresenter.getContentByCategory(dataBean);
    }

    private void onContentClick(RecommendContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean dataBean) {
        LogUtils.d(TAG, "onContentClick:"+dataBean.getTitle());
        String title = dataBean.getTitle();
        // 详情地址
        String url;
        if (!TextUtils.isEmpty(dataBean.getCoupon_click_url())) {
            url = dataBean.getCoupon_click_url();
        } else {
            url = dataBean.getClick_url();
        }
        String cover = dataBean.getPict_url();
        // 为什么请求数据不在自己的activity中呢?
        PresenterManager.getInstance().getTicketPresenter().getTicket(title, url, cover);
        startActivity(new Intent(getContext(), TicketActivity.class));
    }

    @Override
    protected void loadData() {
        super.loadData();
        mPresenter.getCategories();
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
    public void onCategoriesLoaded(RecommendCategories result) {
        LogUtils.d(TAG, "onCategoriesLoaded");
        mPresenter.getContentByCategory(result.getData().get(0));
        mCategoryAdapter.setData(result.getData());
    }

    @Override
    public void onContentLoaded(RecommendContent content) {
        LogUtils.d(TAG, "onContentLoaded");
        setUpState(State.SUCCESS);
        LogUtils.d(TAG, "content size:"+content.getData().getTbk_uatm_favorites_item_get_response().getTotal_results());
        LogUtils.d(TAG, "content size:"+content.getData().getTbk_uatm_favorites_item_get_response().getResults().getUatm_tbk_item().size());
        mContentAdapter.setData(content);
        mRvContent.scrollToPosition(0);
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
