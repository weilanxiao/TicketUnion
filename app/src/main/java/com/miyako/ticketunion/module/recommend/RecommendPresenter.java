package com.miyako.ticketunion.module.recommend;

import com.miyako.ticketunion.model.api.Api;
import com.miyako.ticketunion.model.domain.HomePagerContent;
import com.miyako.ticketunion.model.domain.RecommendCategories;
import com.miyako.ticketunion.model.domain.RecommendContent;
import com.miyako.ticketunion.utils.LogUtils;
import com.miyako.ticketunion.utils.RetrofitManager;
import com.miyako.ticketunion.utils.UrlUtils;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecommendPresenter implements RecommendContract.IRecommendPresenter {

    private static final String TAG = "RecommendPresenter";
    private RecommendContract.IRecommendView mView;
    private final Api mApi;
    private RecommendCategories.DataBean mCurrentCategory;

    public RecommendPresenter() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void bind(RecommendContract.IRecommendView view) {
        if (view != null) {
            LogUtils.d(TAG, "bind");
            mView = view;
        }
    }

    @Override
    public void unBind(RecommendContract.IRecommendView view) {
        if (mView != null && mView == view) {
            LogUtils.d(TAG, "unBind");
            mView = null;
        }
    }

    @Override
    public void getCategories() {
        LogUtils.d(TAG, "getCategories");
        if (mView != null) {
            mView.onLoading();
        }
        Call<RecommendCategories> task = mApi.getRecommendCategories();
        task.enqueue(new Callback<RecommendCategories>() {

            @Override
            public void onResponse(Call<RecommendCategories> call, Response<RecommendCategories> response) {
                LogUtils.i(TAG, "response code:" + response.code());
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    LogUtils.d(TAG, "请求成功");
                    RecommendCategories categories = response.body();
                    LogUtils.i(TAG, categories.toString());
                    // 更新UI
                    handleCategories(categories);
                    //                    mView.onCategoriesLoaded(categories);
                } else {
                    LogUtils.d(TAG, "请求失败");
                    handleNetError();
                }
            }

            @Override
            public void onFailure(Call<RecommendCategories> call, Throwable t) {
                LogUtils.e(TAG, "请求错误:"+t.getMessage());
                handleNetError();
            }
        });
    }

    private void handleNetError() {
        mView.onError(1, "error");
    }

    private void handleCategories(RecommendCategories categories) {
        mView.onCategoriesLoaded(categories);
    }

    @Override
    public void getContentByCategory(RecommendCategories.DataBean category) {
        this.mCurrentCategory = category;
        if (mCurrentCategory == null) {
            return;
        }
        Call<RecommendContent> task = mApi.getRecommendContent(category.getFavorites_id());
        task.enqueue(new Callback<RecommendContent>() {
            @Override
            public void onResponse(Call<RecommendContent> call, Response<RecommendContent> response) {
                LogUtils.i(TAG, "response code:" + response.code());
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    LogUtils.d(TAG, "请求成功");
                    RecommendContent content = response.body();
                    LogUtils.i(TAG, content.toString());
                    // 更新UI
                    handleContent(content);
                    //                    mView.onCategoriesLoaded(categories);
                } else {
                    LogUtils.d(TAG, "请求失败");
                    handleNetError();
                }
            }

            @Override
            public void onFailure(Call<RecommendContent> call, Throwable t) {
                handleNetError();
            }
        });
    }

    private void handleContent(RecommendContent content) {
        mView.onContentLoaded(content);
    }

    @Override
    public void retry() {

    }
}
