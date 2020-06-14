package com.miyako.ticketunion.module.home.category;

import android.util.Log;

import com.miyako.ticketunion.model.api.Api;
import com.miyako.ticketunion.model.domain.Categories;
import com.miyako.ticketunion.model.domain.HomePagerContent;
import com.miyako.ticketunion.utils.LogUtils;
import com.miyako.ticketunion.utils.RetrofitManager;
import com.miyako.ticketunion.utils.UrlUtils;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagerPresenter implements CategoryPagerContract.IHomePagerPresenter {

    private static final String TAG = "HomePagerPresenter";
    private static final int DEFAULT_PAGE = 1;

    private CategoryPagerContract.IHomePagerView mView;
    private Map<Integer, Integer> pagesInfo = new HashMap<>();

    private CategoryPagerPresenter() {}

    private static CategoryPagerPresenter sInstance = null;

    public static CategoryPagerPresenter getInstance() {
        if (sInstance == null) {
            sInstance = new CategoryPagerPresenter();
        }
        return sInstance;
    }

    @Override
    public void getContentByCategoryId(int categoryId) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Integer targetPage = pagesInfo.get(categoryId);
        if (targetPage == null) {
            pagesInfo.put(categoryId, DEFAULT_PAGE);
            targetPage = DEFAULT_PAGE;
        }
        String url = UrlUtils.createCategoryContentUrl(categoryId, targetPage);
        LogUtils.i(TAG, "category url:"+url);
        Call<HomePagerContent> task = api.geCategoryContent(url);
        task.enqueue(new Callback<HomePagerContent>() {

            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                LogUtils.i(TAG, "response code:" + response.code());
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    LogUtils.d(TAG, "请求成功");
                    HomePagerContent pagerContent = response.body();
                    LogUtils.i(TAG, pagerContent.toString());
//                    mView.onCategoriesLoaded(categories);
                } else {
                    LogUtils.d(TAG, "请求失败");
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.e(TAG, "请求错误:"+t.getMessage());
            }
        });
    }

    @Override
    public void getContentMore(int categoryId) {

    }

    @Override
    public void reload() {

    }

    @Override
    public void bind(CategoryPagerContract.IHomePagerView view) {
        if (view != null) {
            LogUtils.d(TAG, "bind");
            mView = view;
        }
    }

    @Override
    public void unBind(CategoryPagerContract.IHomePagerView view) {
        if (mView != null && mView == view) {
            LogUtils.d(TAG, "unBind");
            mView = null;
        }
    }
}
