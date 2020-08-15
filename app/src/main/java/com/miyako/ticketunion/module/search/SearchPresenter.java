package com.miyako.ticketunion.module.search;

import com.miyako.ticketunion.model.Histories;
import com.miyako.ticketunion.model.api.Api;
import com.miyako.ticketunion.model.domain.SearchRecommend;
import com.miyako.ticketunion.model.domain.SearchResult;
import com.miyako.ticketunion.utils.JsonCacheUtils;
import com.miyako.ticketunion.utils.LogUtils;
import com.miyako.ticketunion.utils.RetrofitManager;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenter implements SearchContract.ISearchPresenter {

    private static final String TAG = "SearchPresenter";
    private final Api mApi;
    private static final int DEFAULT_PAGE = 1;
    private int mCurrentPage;
    private String mCurrentKey = "";
    private SearchContract.ISearchView mView;
    private boolean isLoading;
    private JsonCacheUtils mCacheUtils;

    public SearchPresenter() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
        mCurrentPage = DEFAULT_PAGE;
        mCacheUtils = JsonCacheUtils.getInstance();
    }

    @Override
    public void getHistories() {
        Histories cache = mCacheUtils.getCache(KEY_HISTORIES, Histories.class);
        if (mView !=null && cache != null
                && cache.getData()!=null  && cache.getData().size()!=0) {
            mView.onHistoriesLoad(cache.getData());
        }
    }

    @Override
    public void deleteHistories() {
        mCacheUtils.delCache(KEY_HISTORIES);
    }

    @Override
    public void getSearch(String keyword) {
        if (mCurrentKey != null && !mCurrentKey.equals(keyword)) {
            saveHistiory(keyword);
            mCurrentKey = keyword;
        }
        if (mView != null) {
            mView.onLoading();
        }
        mCurrentKey = keyword;
        Call<SearchResult> task = mApi.getSearch(mCurrentKey, mCurrentPage);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                LogUtils.i(TAG, "response code:" + response.code());
                isLoading = false;
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    LogUtils.d(TAG, "请求成功");
                    SearchResult content = response.body();
                    LogUtils.i(TAG, Objects.requireNonNull(content).toString());
                    // 更新UI
                    handleSearchLoadSuccess(content);
                } else {
                    LogUtils.d(TAG, "请求失败");
                    handleNetError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {

            }
        });
    }

    @Override
    public void getRecommendWords() {
        Call<SearchRecommend> task = mApi.getSearchRecommend();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                LogUtils.i(TAG, "response code:" + response.code());
                isLoading = false;
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    LogUtils.d(TAG, "请求成功");
                    SearchRecommend content = response.body();
                    LogUtils.i(TAG, content.toString());
                    // 更新UI
                    handleRecommendWordsSuccess(content);
                } else {
                    LogUtils.d(TAG, "请求失败");
                    handleNetError();
                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {

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
        Call<SearchResult> task = mApi.getSearch(mCurrentKey, mCurrentPage);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                LogUtils.i(TAG, "response code:" + response.code());
                isLoading = false;
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    LogUtils.d(TAG, "请求成功");
                    SearchResult content = response.body();
                    LogUtils.i(TAG, content.toString());
                    // 更新UI
                    handleSearchLoadMore(content);
                } else {
                    LogUtils.d(TAG, "请求失败");
                    handleNetError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                LogUtils.e(TAG, "请求错误:"+t.getMessage());
                isLoading = false;
                handleNetError();
            }
        });
    }

    @Override
    public void reload() {
        if ("".equals(mCurrentKey)) {
            if (mView != null) {
                mView.onEmpty();
            }
        } else {
            getSearch(mCurrentKey);
        }
    }

    @Override
    public void bind(SearchContract.ISearchView view) {
        if (view != null) {
            LogUtils.d(TAG, "bind");
            mView = view;
        }
    }

    @Override
    public void unBind(SearchContract.ISearchView view) {
        if (mView != null && mView == view) {
            LogUtils.d(TAG, "unBind");
            mView = null;
        }
    }

    private void handleSearchLoadSuccess(SearchResult content) {
        LogUtils.d(TAG, "handleSearchLoadSuccess");
        if (mView != null) {
            if (isResultEmpty(content)) handleEmpty();
            else mView.onLoadMoreSuccess(content.getData());
        }
    }


    private void handleRecommendWordsSuccess(SearchRecommend content) {
        LogUtils.d(TAG, "handleRecommendWordsSuccess");
        if (mView != null) {
            mView.onRecommendLoaded(content.getData());
        }
    }

    private void handleSearchLoadMore(SearchResult content) {
        LogUtils.d(TAG, "handleSearchLoadMore");
        if (mView != null) {
            if (isResultEmpty(content)) handleEmpty();
            else mView.onLoadMoreSuccess(content.getData());
        }
    }

    private boolean isResultEmpty(SearchResult content) {
        try {
            return (content!=null) &&
                    (content.getData().getTbk_dg_material_optional_response()
                            .getResult_list().getMap_data().size()!=0);
        }catch (Exception e) {
            return false;
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

    public static final String KEY_HISTORIES = "key_histories";
    private static final int CacheMax = 10;

    private void saveHistiory(String history) {
        Histories cache = mCacheUtils.getCache(KEY_HISTORIES, Histories.class);
        LogUtils.d(TAG, "cache:"+cache);
        List<String> list = null;
        if (cache != null && cache.getData() != null) {
            list = cache.getData();
            if (list.contains(history)) {
                list.remove(history);
            }
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        if (cache == null) {
            cache = new Histories();
        }
        cache.setData(list);
        if (list.size() > CacheMax) {
            list = list.subList(0, CacheMax);
        }
        list.add(history);
        mCacheUtils.saveCache(KEY_HISTORIES, cache);
    }
}
