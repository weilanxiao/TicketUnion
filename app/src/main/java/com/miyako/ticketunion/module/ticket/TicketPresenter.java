package com.miyako.ticketunion.module.ticket;

import com.miyako.ticketunion.model.api.Api;
import com.miyako.ticketunion.model.api.TicketParam;
import com.miyako.ticketunion.model.domain.TicketResult;
import com.miyako.ticketunion.utils.LogUtils;
import com.miyako.ticketunion.utils.RetrofitManager;
import com.miyako.ticketunion.utils.UrlUtils;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketPresenter implements TicketContract.ITicketPresenter {

    private static final String TAG = "TicketPresenter";
    private TicketContract.ITicketView mView;
    private LoadState mCurrentState = LoadState.NONE;
    private TicketResult mResult;
    private String mCover;

    enum LoadState {
        LADDING, SUCCESS, ERROR, NONE
    }

    @Override
    public void getTicket(String title, String url, String cover) {
        handleTicketLoading();
        LogUtils.d(TAG, "title:" + title);
        LogUtils.d(TAG, "url:" + url);
        LogUtils.d(TAG, "cover:" + cover);
        this.mCover = cover;
        mCurrentState = LoadState.LADDING;
        Api api = RetrofitManager.getInstance().getRetrofit().create(Api.class);
        TicketParam param = new TicketParam(UrlUtils.getTicketUrl(url), title);
        Call<TicketResult> task = api.getTicket(param);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                LogUtils.i(TAG, "response code:" + response.code());
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    LogUtils.d(TAG, "请求成功");
                    mResult = response.body();
                    LogUtils.i(TAG, mResult.toString());
                    // 更新UI
                    if (mResult.getData().getTbk_tpwd_create_response() != null) {
                        mCurrentState = LoadState.SUCCESS;
                        handleTicketSuccess();
                    } else {
                        mCurrentState = LoadState.ERROR;
                        handleTicketError("数据为空");
                    }
                } else {
                    LogUtils.d(TAG, "请求失败");
                    handleTicketError("请求失败");
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                LogUtils.e(TAG, "请求错误:" + t.getMessage());
                handleTicketError("请求错误");
            }
        });
    }

    private void handleTicketError(String msg) {
        if (mView != null) {
            mView.onError(1, msg);
        }
    }

    private void handleTicketSuccess() {
        if (mView != null) {
            mView.onTicketLoaded(mCover, mResult);
        }
    }

    private void handleTicketLoading() {
        if (mView != null) {
            mView.onLoading();
        }
    }

    @Override
    public void bind(TicketContract.ITicketView view) {
        if (mCurrentState != LoadState.NONE) {
            if (mCurrentState == LoadState.SUCCESS) {
                handleTicketSuccess();
            } else if (mCurrentState == LoadState.ERROR) {
                handleTicketError("asd");
            } else if (mCurrentState == LoadState.LADDING) {
                handleTicketLoading();
            }
        }
        if (view != null) {
            mView = view;
        }
    }

    @Override
    public void unBind(TicketContract.ITicketView view) {
        if (mView != null && view == mView) {
            mView = null;
        }
    }
}
