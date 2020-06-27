package com.miyako.ticketunion.module.ticket;

import com.miyako.ticketunion.model.api.Api;
import com.miyako.ticketunion.model.api.TicketParam;
import com.miyako.ticketunion.model.domain.HomePagerContent;
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

    @Override
    public void getTicket(String title, String url, String cover) {
        LogUtils.d(TAG, "title:"+title);
        LogUtils.d(TAG, "url:"+url);
        LogUtils.d(TAG, "cover:"+cover);
        Api api = RetrofitManager.getInstance().getRetrofit().create(Api.class);
        TicketParam param = new TicketParam(UrlUtils.getTicketUrl(url), title);
        Call<TicketResult> task = api.getTicket(param);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                LogUtils.i(TAG, "response code:" + response.code());
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    LogUtils.d(TAG, "请求成功");
                    TicketResult result = response.body();
                    LogUtils.i(TAG, result.toString());
                    // 更新UI
//                    handleLoadMore(categoryId, index, pagerContent);
                } else {
                    LogUtils.d(TAG, "请求失败");
//                    handleLoadMoreError(categoryId, "请求失败");
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                LogUtils.e(TAG, "请求错误:"+t.getMessage());
//                handleLoadMoreError(categoryId, "请求错误:");
            }
        });
    }

    @Override
    public void bind(TicketContract.ITicketView view) {

    }

    @Override
    public void unBind(TicketContract.ITicketView view) {

    }
}
