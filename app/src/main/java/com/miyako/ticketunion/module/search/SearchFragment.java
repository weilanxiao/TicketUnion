package com.miyako.ticketunion.module.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miyako.ticketunion.R;
import com.miyako.ticketunion.base.BaseFragment;
import com.miyako.ticketunion.model.domain.OnSellContent;
import com.miyako.ticketunion.model.domain.SearchRecommend;
import com.miyako.ticketunion.model.domain.SearchResult;
import com.miyako.ticketunion.utils.LogUtils;
import com.miyako.ticketunion.utils.PresenterManager;

import java.util.List;

public class SearchFragment extends BaseFragment implements SearchContract.ISearchView{

    private static final String TAG = "SearchFragment";
    private SearchPresenter mPresenter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setUpState(State.SUCCESS);
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mPresenter = PresenterManager.getInstance().getSearchPresenter();
        mPresenter.bind(this);
    }

    @Override
    protected void loadData() {
        super.loadData();
        mPresenter.getRecommendWords();
        mPresenter.getSearch("机箱");
        mPresenter.getHistories();
    }

    @Override
    protected void release() {
        super.release();
        if (mPresenter != null) {
            mPresenter.unBind(this);
        }
    }

    @Override
    public void onHistoriesLoad(List<String> histories) {

    }

    @Override
    public void onHistoriesDeleted() {

    }

    @Override
    public void onSearchLoaded(SearchResult.DataBean result) {
        LogUtils.d(TAG, "search result:"+result);
    }

    @Override
    public void onRecommendLoaded(List<SearchRecommend.DataBean> recommendWords) {
        LogUtils.d(TAG, "recommend words size:"+recommendWords.size());
    }

    @Override
    public void onLoadMoreSuccess(SearchResult.DataBean result) {
        LogUtils.d(TAG, "加载数据成功");
    }

    @Override
    public void onLoadMoreEmpty() {

    }

    @Override
    public void onLoadMoreError(OnSellContent content) {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onError(int errorCode, String msg) {

    }

    @Override
    public void onEmpty() {

    }
}
