package com.miyako.ticketunion.module.recommend;

import com.miyako.ticketunion.base.IBasePresenter;
import com.miyako.ticketunion.base.IBaseView;
import com.miyako.ticketunion.model.domain.RecommendCategories;
import com.miyako.ticketunion.model.domain.RecommendContent;

public interface RecommendContract {

    public interface IRecommendPresenter extends IBasePresenter<IRecommendView> {

        /**
         * 获取分类
         */
        void getCategories();

        /**
         * 根据分类加载内容
         * @param category 具体分类
         */
        void getContentByCategory(RecommendCategories.DataBean category);

        /**
         * 重试
         */
        void retry();
    }

    public interface IRecommendView extends IBaseView {

        /**
         * 获取分类结果
         */
        void onCategoriesLoaded(RecommendCategories result);

        /**
         * 分类具体内容
         */
        void onContentLoaded(RecommendContent content);
    }
}
