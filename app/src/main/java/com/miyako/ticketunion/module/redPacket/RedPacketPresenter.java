package com.miyako.ticketunion.module.redPacket;

import com.miyako.ticketunion.model.api.Api;
import com.miyako.ticketunion.model.domain.OnSellContent;
import com.miyako.ticketunion.model.domain.RecommendCategories;
import com.miyako.ticketunion.utils.LogUtils;
import com.miyako.ticketunion.utils.RetrofitManager;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RedPacketPresenter implements RedPacketContract.IRedPacketPresenter {

    private static final String TAG = "RedPacketPresenter";
    private final Api mApi;
    private static final int DEFAULT_PAGE = 1;
    private int mCurrentPage;
    private RedPacketContract.IRedPacketView mView;
    private boolean isLoading;

    public RedPacketPresenter() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
        mCurrentPage = 1;
    }

    @Override
    public void getOnSellContent() {
        LogUtils.d(TAG, "getOnSellContent");
        if (isLoading) {
            LogUtils.d(TAG, "is loading...");
            return;
        }
        isLoading = true;
        if (mView != null) {
            mView.onLoading();
        }
        Call<OnSellContent> task = mApi.getOnSellContent(mCurrentPage);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                LogUtils.i(TAG, "response code:" + response.code());
                isLoading = false;
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    LogUtils.d(TAG, "请求成功");
                    OnSellContent content = response.body();
                    LogUtils.i(TAG, content.toString());
                    // 更新UI
                    handleOnSellContent(content);
                } else {
                    LogUtils.d(TAG, "请求失败");
                    handleNetError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                LogUtils.e(TAG, "请求错误:"+t.getMessage());
                isLoading = false;
                handleNetError();
            }
        });
    }

    @Override
    public void loadMore() {
        LogUtils.d(TAG,"loadMore");
        if (isLoading) {
            LogUtils.d(TAG, "is loading...");
            return;
        }
        isLoading = true;
        mCurrentPage++;
        Call<OnSellContent> task = mApi.getOnSellContent(mCurrentPage);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                LogUtils.i(TAG, "response code:" + response.code());
                isLoading = false;
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    LogUtils.d(TAG, "请求成功");
                    OnSellContent content = response.body();
                    LogUtils.i(TAG, content.toString());
                    // 更新UI
                    handleLoadMore(content);
                } else {
                    LogUtils.d(TAG, "请求失败");
                    handleNetError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                LogUtils.e(TAG, "请求错误:"+t.getMessage());
                isLoading = false;
                handleNetError();
            }
        });
    }

    private void handleLoadMore(OnSellContent content) {
        LogUtils.d(TAG, "handleOnSellContent");
        if (mView != null) {
            try {
                int size = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
                if (size==0) {
                    handleLoadMoreEmpty();
                } else {
                    mView.onLoadMoreSuccess(content);
                }
            } catch (Exception e) {
                e.printStackTrace();
                handleLoadMoreEmpty();
            }
        }
    }

    private void handleLoadMoreEmpty() {
        LogUtils.d(TAG, "handleLoadMoreEmpty");
        mView.onLoadMoreEmpty();
    }

    private void handleOnSellContent(OnSellContent content) {
        LogUtils.d(TAG, "handleOnSellContent");
        if (mView != null) {
            try {
                int size = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
                if (size==0) {
                    handleEmpty();
                } else {
                    mView.onLoadSuccess(content);
                }
            } catch (Exception e) {
                e.printStackTrace();
                handleEmpty();
            }
        }
    }

    private void handleEmpty() {
        LogUtils.d(TAG, "handleEmpty");
        mView.onEmpty();
    }

    private void handleNetError() {
        LogUtils.d(TAG, "handleNetError");
        mView.onError(1, "msg");
    }

    @Override
    public void reload() {

    }

    @Override
    public void bind(RedPacketContract.IRedPacketView view) {
        if (view != null) {
            LogUtils.d(TAG, "bind");
            mView = view;
        }
    }

    @Override
    public void unBind(RedPacketContract.IRedPacketView view) {
        if (mView != null && mView == view) {
            LogUtils.d(TAG, "unBind");
            mView = null;
        }
    }
}
