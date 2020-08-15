package com.miyako.ticketunion.module.search;

import com.miyako.ticketunion.base.IBasePresenter;
import com.miyako.ticketunion.base.IBaseView;
import com.miyako.ticketunion.model.domain.OnSellContent;
import com.miyako.ticketunion.model.domain.SearchRecommend;
import com.miyako.ticketunion.model.domain.SearchResult;

import java.util.List;

/**
 * 搜索界面契约类
 */
public interface SearchContract {

    public interface ISearchPresenter extends IBasePresenter<ISearchView> {

        /**
         * 获取搜索历史
         */
        public void getHistories();

        /**
         * 删除搜索历史
         */
        public void deleteHistories();

        /**
         * 发起搜索
         */
        public void getSearch(String keyword);

        /**
         * 获取推荐词
         */
        public void getRecommendWords();

        /**
         * 加载更多
         */
        public void loadMore();

        /**
         * 重新加载
         */
        public void reload();
    }

    public interface ISearchView extends IBaseView {

        /**
         * 获取搜索历史成功
         */
        public void onHistoriesLoad(List<String> histories);

        /**
         * 删除历史记录成功
         */
        public void onHistoriesDeleted();

        /**
         * 加载内容成功
         * @param result 数据结果
         */
        public void onSearchLoaded(SearchResult.DataBean result);

        /**
         * 获取推荐词成功
         * @param recommendWords 推荐词列表
         */
        public void onRecommendLoaded(List<SearchRecommend.DataBean> recommendWords);

        /**
         * 加载更多
         * @param result 数据结果
         */
        public void onLoadMoreSuccess(SearchResult.DataBean result);

        /**
         * 加载更多为空
         */
        public void onLoadMoreEmpty();

        /**
         * 加载更多失败
         * @param content 数据结果
         */
        public void onLoadMoreError(OnSellContent content);

    }
}
