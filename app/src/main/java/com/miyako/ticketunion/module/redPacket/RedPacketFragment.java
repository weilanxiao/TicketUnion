package com.miyako.ticketunion.module.redPacket;


import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miyako.ticketunion.R;
import com.miyako.ticketunion.base.BaseFragment;
import com.miyako.ticketunion.model.domain.OnSellContent;
import com.miyako.ticketunion.module.adapter.RedPacketContentAdapter;
import com.miyako.ticketunion.utils.LogUtils;
import com.miyako.ticketunion.utils.PresenterManager;
import com.miyako.ticketunion.utils.SizeUtils;
import com.miyako.ticketunion.utils.ToastUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

import butterknife.BindView;

public class RedPacketFragment extends BaseFragment implements RedPacketContract.IRedPacketView{

    private RedPacketPresenter mPresenter;
    private RedPacketContentAdapter mAdapter;

    @BindView(R.id.red_packet_list)
    RecyclerView mRvContent;
    @BindView(R.id.refresh_red_packet)
    SmartRefreshLayout mRefreshLayout;

    private static final int DEFAULT_COLUMN = 2;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_red_packet;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setUpState(State.LOADING);
        mAdapter = new RedPacketContentAdapter();
        mRvContent.setAdapter(mAdapter);
        mRvContent.setLayoutManager(new GridLayoutManager(getContext(), DEFAULT_COLUMN));
        mRvContent.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 2);
                outRect.right = SizeUtils.dip2px(getContext(), 2);
                outRect.left = SizeUtils.dip2px(getContext(), 2);
                outRect.bottom = SizeUtils.dip2px(getContext(), 2);

            }
        });

        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mPresenter = PresenterManager.getInstance().getRedPacketPresenter();
        mPresenter.bind(this);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.d(TAG, "加载更多...");
                if (mPresenter != null) {
                    mPresenter.loadMore();
                }
            }
        });
    }

    @Override
    protected void release() {
        super.release();
        if (mPresenter != null) {
            mPresenter.unBind(this);
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        mPresenter.getOnSellContent();
    }

    @Override
    public void onLoadSuccess(OnSellContent content) {
        LogUtils.d(TAG, "onLoadSuccess");
        setUpState(State.SUCCESS);
        mAdapter.setData(content);
        mRefreshLayout.setEnableLoadMore(true);
    }

    @Override
    public void onLoadMoreSuccess(OnSellContent content) {
        LogUtils.d(TAG, "onLoadMoreSuccess");
        mAdapter.addData(content);
        if (mRefreshLayout != null) {
            ToastUtils.showToast("加载了"+content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size()+"条数据");
            mRefreshLayout.finishLoadMore();
        }
    }

    @Override
    public void onLoadMoreEmpty() {
        LogUtils.d(TAG, "onLoadMoreEmpty");
    }

    @Override
    public void onLoadMoreError(OnSellContent content) {
        LogUtils.d(TAG, "onLoadMoreError");
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
