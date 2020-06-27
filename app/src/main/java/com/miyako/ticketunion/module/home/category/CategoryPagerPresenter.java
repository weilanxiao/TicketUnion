package com.miyako.ticketunion.module.home.category;

import com.miyako.ticketunion.model.api.Api;
import com.miyako.ticketunion.model.domain.HomePagerContent;
import com.miyako.ticketunion.utils.LogUtils;
import com.miyako.ticketunion.utils.RetrofitManager;
import com.miyako.ticketunion.utils.UrlUtils;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagerPresenter implements CategoryPagerContract.IHomePagerPresenter {

    private static final String TAG = "HomePagerPresenter";
    private static final int DEFAULT_PAGE = 1;

    private List<CategoryPagerContract.IHomePagerView> mViewList;
    private Map<Integer, Integer> pagesInfo = new HashMap<>();

    @Override
    public void getContentByCategoryId(int categoryId) {
        for (CategoryPagerContract.IHomePagerView view : mViewList) {
            if(categoryId == view.getMaterialId()) view.onLoading();
        }
        Integer targetPage = pagesInfo.get(categoryId);
        if (targetPage == null) {
            pagesInfo.put(categoryId, DEFAULT_PAGE);
            targetPage = DEFAULT_PAGE;
        }
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
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
                    // 更新UI
                    handleCategoryContent(categoryId, pagerContent);
//                    mView.onCategoriesLoaded(categories);
                } else {
                    LogUtils.d(TAG, "请求失败");
                    handleNetError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.e(TAG, "请求错误:"+t.getMessage());
                handleNetError(categoryId);
            }
        });
    }

    private void handleNetError(int categoryId) {
        for (CategoryPagerContract.IHomePagerView view : mViewList) {
            if (categoryId == view.getMaterialId()) view.onError(0, "网络错误");
        }
    }

    private void handleCategoryContent(int categoryId, HomePagerContent pagerContent) {
        List<HomePagerContent.DataBean> data = pagerContent.getData();
        for (CategoryPagerContract.IHomePagerView view : mViewList) {
            if(categoryId == view.getMaterialId()) {
                if(pagerContent==null||pagerContent.getData().size()==0) {
                    view.onEmpty();
                } else {
                    List<HomePagerContent.DataBean> loopData = data.subList(data.size() - 5, data.size());
                    view.onLooperLoaded(loopData);
                    view.onContentLoaded(data);
                }
            }
        }
    }

    @Override
    public void getContentMore(int categoryId) {
        LogUtils.d(TAG, "getContentMore");
        // 加载更多
        // 获取页码，页码增加，加载数据
        Integer page = pagesInfo.get(categoryId);
        int index;
        if (page == null ) {
            index = 1;
        } else {
            index = ++page;
        }
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        String url = UrlUtils.createCategoryContentUrl(categoryId, index);
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
                    // 更新UI
                    handleLoadMore(categoryId, index, pagerContent);
                } else {
                    LogUtils.d(TAG, "请求失败");
                    handleLoadMoreError(categoryId, "请求失败");
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.e(TAG, "请求错误:"+t.getMessage());
                handleLoadMoreError(categoryId, "请求错误:");
            }
        });
    }

    // 处理加载更多
    private void handleLoadMore(int categoryId, int page, HomePagerContent pagerContent) {
        List<HomePagerContent.DataBean> data = pagerContent.getData();
        for (CategoryPagerContract.IHomePagerView view : mViewList) {
            if(categoryId == view.getMaterialId()) {
                if(pagerContent==null||pagerContent.getData().size()==0) {
                    view.onLoadMoreEmpty();
                } else {
//                    List<HomePagerContent.DataBean> loopData = data.subList(data.size() - 5, data.size());
//                    view.onLooperLoaded(loopData);
                    pagesInfo.put(categoryId, page);
                    view.onLoadMoreSuccess(data);
                }
            }
        }
    }

    // 处理加载更多错误
    private void handleLoadMoreError(int categoryId, String msg) {
        for (CategoryPagerContract.IHomePagerView view : mViewList) {
            if (categoryId == view.getMaterialId()) view.onLoadMoreError(0, msg);
        }
    }



    @Override
    public void reload() {

    }

    @Override
    public void bind(CategoryPagerContract.IHomePagerView view) {
        if (view != null) {
            LogUtils.d(TAG, "bind");
            if (mViewList == null) {
                mViewList = new ArrayList<>();
            }
            if (!mViewList.contains(view)) {
                mViewList.add(view);
            }
        }
    }

    @Override
    public void unBind(CategoryPagerContract.IHomePagerView view) {
        if (mViewList != null && mViewList.contains(view)) {
            LogUtils.d(TAG, "unBind");
            mViewList.remove(view);
        }
    }
}
