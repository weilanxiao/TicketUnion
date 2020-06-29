package com.miyako.ticketunion.module.home.category;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.miyako.ticketunion.R;
import com.miyako.ticketunion.base.BaseFragment;
import com.miyako.ticketunion.base.Constants;
import com.miyako.ticketunion.custom.AutoLooperViewPager;
import com.miyako.ticketunion.custom.TbNestedScrollView;
import com.miyako.ticketunion.model.domain.Categories;
import com.miyako.ticketunion.model.domain.HomePagerContent;
import com.miyako.ticketunion.module.adapter.CategoryPagerAdapter;
import com.miyako.ticketunion.module.adapter.LooperPagerAdapter;
import com.miyako.ticketunion.module.ticket.TicketActivity;
import com.miyako.ticketunion.utils.LogUtils;
import com.miyako.ticketunion.utils.PresenterManager;
import com.miyako.ticketunion.utils.SizeUtils;
import com.miyako.ticketunion.utils.ToastUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;


public class CategoryPagerFragment extends BaseFragment implements CategoryPagerContract.IHomePagerView {

    private static final String TAG = "HomePagerFragment";
    private CategoryPagerPresenter mPresenter;
    private int mMaterialId;

    @BindView(R.id.home_pager_content_list)
    RecyclerView mContentListView;
    @BindView(R.id.home_pager_content_looper)
    AutoLooperViewPager mVpLooper;
    @BindView(R.id.tv_home_pager_title_part)
    TextView mTvTitle;
    @BindView(R.id.layout_home_pager_looper_point)
    LinearLayout mLayoutLooperPoint;
    @BindView(R.id.layout_home_pager)
    LinearLayout mLayoutHomePager;
    @BindView(R.id.home_pager_tbNested)
    TbNestedScrollView mHomePagerTb;
    @BindView(R.id.layout_home_pager_header)
    LinearLayout mLayoutHomePagerHeader;
//     刷新框架
//    @BindView(R.id.layout_home_pager_refresh)
//    TwinklingRefreshLayout mLayoutRefresh;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private CategoryPagerAdapter mAdapter;
    private LooperPagerAdapter mLooperAdapter;

    public static CategoryPagerFragment newInstance(Categories.DataBean category) {
        CategoryPagerFragment homePagerFragment = new CategoryPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID, category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mVpLooper.startLoop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mVpLooper.stopLoop();
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
        // 设置数据适配器
        mContentListView.setAdapter(mAdapter);
        setUpState(State.SUCCESS);
        mLooperAdapter = new LooperPagerAdapter();
        // 设置轮播图适配器
        mVpLooper.setAdapter(mLooperAdapter);
//        mVpLooper.setOffscreenPageLimit(3);

        // 设置刷新相关内容，下拉刷新，上拉加载更多
//        mLayoutRefresh.setEnableRefresh(false);
//        mLayoutRefresh.setEnableLoadmore(true);
//        mHomePagerTb.setNestedScrollingEnabled(false);
//        mRefreshLayout.setNestedScrollingEnabled(false);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        mHomePagerTb.setHeaderHeight(mLayoutHomePagerHeader.getMeasuredHeight());
    }

    @Override
    protected void initListener() {
        super.initListener();

        // 设置根布局监听器
        mLayoutHomePager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mLayoutHomePagerHeader == null) {
                    return;
                }
                int headerHeight = mLayoutHomePagerHeader.getMeasuredHeight();
                if (headerHeight != 0) {
                    mHomePagerTb.setHeaderHeight(headerHeight);
                }
                int height = mLayoutHomePager.getMeasuredHeight();
//                int width = mLayoutHomePager.getMeasuredWidth();
//                LogUtils.d(TAG, "height:"+height+", width:"+width);
                // 设置滑动内容只有列表滑动，轮播图不滑动
                ViewGroup.LayoutParams layoutParams = mContentListView.getLayoutParams();
                layoutParams.height = height;
                mContentListView.setLayoutParams(layoutParams);
                if (height != 0) {
                    // 移除当前视图
                    mLayoutHomePager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        // 轮播图滑动监听器
        mVpLooper.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mLooperAdapter.getDataSize()==0) return;
                int target = position % mLooperAdapter.getDataSize();
                // 切换指示器
                updateLooperPoint(target);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        mLayoutRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
//                super.onLoadMore(refreshLayout);
//                LogUtils.d(TAG, "加载更多...");
//                if (mPresenter != null) {
//                    mPresenter.getContentMore(mMaterialId);
//                }
//            }
//        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.d(TAG, "加载更多...");
                if (mPresenter != null) {
                    mPresenter.getContentMore(mMaterialId);
                }
            }
        });

        mAdapter.setOnListItemClickListener(this::onItemClick);
        mLooperAdapter.setOnLooperItemClickListener(this::onLoopItemClick);
    }

    /**
     * 轮播图适配器item点击回调
     * @param item 具体点击的轮播图
     */
    private void  onLoopItemClick(HomePagerContent.DataBean item) {
        LogUtils.d(TAG, "onLoopItemClick:"+item.getTitle());
        handleItemClick(item);
    }

    /**
     * 分类适配器item点击回调
     * @param item 具体点击的数据
     */
    private void onItemClick(HomePagerContent.DataBean item) {
        LogUtils.d(TAG, "onItemClick:"+item.getTitle());
        handleItemClick(item);
    }

    /**
     * 处理点击事件
     * @param item 数据
     */
    private void handleItemClick(HomePagerContent.DataBean item) {
        String title = item.getTitle();
        // 详情地址
        String url;
        if (!TextUtils.isEmpty(item.getCoupon_click_url())) {
            url = item.getCoupon_click_url();
        } else {
            url = item.getClick_url();
        }
        String cover = item.getPict_url();
        // 为什么请求数据不在自己的activity中呢?
        PresenterManager.getInstance().getTicketPresenter().getTicket(title, url, cover);
        startActivity(new Intent(getContext(), TicketActivity.class));
    }

    private void updateLooperPoint(int position) {
        for (int i = 0; i < mLayoutLooperPoint.getChildCount(); i++) {
            View point = mLayoutLooperPoint.getChildAt(i);
            if (i==position) {
                point.setBackgroundResource(R.drawable.shape_looper_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_looper_point_normal);
            }
        }
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mPresenter = PresenterManager.getInstance().getCategoryPagerPresenter();
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
        mTvTitle.setText(getString(R.string.format_home_pager_title_part, title));
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
        mRefreshLayout.setEnableLoadMore(true);
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
        mAdapter.addData(contentList);
//        if (mLayoutRefresh != null) {
//            LogUtils.d(TAG, "onLoadMoreSuccess");
//            ToastUtils.showToast("加载了"+contentList.size()+"条数据");
//            mLayoutRefresh.finishLoadmore();
//        }

        if (mRefreshLayout != null) {
            LogUtils.d(TAG, "onLoadMoreSuccess");
            ToastUtils.showToast("加载了"+contentList.size()+"条数据");
            mRefreshLayout.finishLoadMore();
        }
    }

    @Override
    public void onLoadMoreError(int errorCode, String msg) {

    }

    @Override
    public void onLoadMoreEmpty() {

    }

    @Override
    public void onLooperLoaded(List<HomePagerContent.DataBean> contentList) {
        LogUtils.d(TAG, "onLooperLoaded");
        mLooperAdapter.setData(contentList);
        // 设置无限轮播图中间点
        int dx = Integer.MAX_VALUE / 2 % contentList.size();
        mVpLooper.setCurrentItem((Integer.MAX_VALUE / 2) - dx);

        // 动态添加轮播图指示器
        mLayoutLooperPoint.removeAllViews();

        for (int i = 0; i < contentList.size(); i++) {
            View point = new View(getContext());
            int size = SizeUtils.dip2px(Objects.requireNonNull(getContext()), 8);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.leftMargin = SizeUtils.dip2px(getContext(), 5);
            params.rightMargin = SizeUtils.dip2px(getContext(), 5);
            point.setLayoutParams(params);
            if (i == 0) {
                point.setBackgroundResource(R.drawable.shape_looper_point_selected);
                LogUtils.d(TAG, "select point:"+i);
            } else {
                point.setBackgroundResource(R.drawable.shape_looper_point_normal);
                LogUtils.d(TAG, "normal point:"+i);
            }

            mLayoutLooperPoint.addView(point);
        }

//        mVpLooper.startLoop();
    }
}
