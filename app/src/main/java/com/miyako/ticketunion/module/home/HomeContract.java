package com.miyako.ticketunion.module.home;

import com.miyako.ticketunion.model.domain.Categories;

public class HomeContract {

    public interface IHomePresenter {

        /**
         * 绑定UI
         */
        void bind(IHomeView view);

        /**
         * 解除绑定
         */
        void unBind(IHomeView view);

        /**
         * 获取商品数据
         */
        void getCategory();
    }

    public interface IHomeView {

        /**
         * 设置数据
         */
        void onCategoriesLoaded(Categories categories);
    }
}
