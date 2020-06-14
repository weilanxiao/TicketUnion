package com.miyako.ticketunion.module.home;

import com.miyako.ticketunion.model.api.Api;
import com.miyako.ticketunion.model.domain.Categories;
import com.miyako.ticketunion.utils.LogUtils;
import com.miyako.ticketunion.utils.RetrofitManager;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePresenter implements HomeContract.IHomePresenter {

    private static final String TAG = "HomePresenter";

    private HomeContract.IHomeView mView;

    @Override
    public void getCategories() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<Categories> task = api.getCategories();
        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                LogUtils.i(TAG, "response code:" + response.code());
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    LogUtils.d(TAG, "请求成功");
                    Categories categories = response.body();
                    LogUtils.i(TAG, categories.toString());
                    mView.onCategoriesLoaded(categories);
                } else {
                    LogUtils.d(TAG, "请求失败");
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                LogUtils.e(TAG, "请求错误:"+t.getMessage());
            }
        });
    }

    @Override
    public void bind(HomeContract.IHomeView view) {
        if (view != null) {
            LogUtils.d(TAG, "bind");
            mView = view;
        }
    }

    @Override
    public void unBind(HomeContract.IHomeView view) {
        if (mView != null && mView == view) {
            LogUtils.d(TAG, "unBind");
            mView = null;
        }
    }
}
