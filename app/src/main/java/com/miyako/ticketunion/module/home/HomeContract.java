package com.miyako.ticketunion.module.home;

import com.miyako.ticketunion.base.IBasePresenter;
import com.miyako.ticketunion.base.IBaseView;
import com.miyako.ticketunion.model.domain.Categories;

public interface HomeContract {

    public interface IHomePresenter extends IBasePresenter<IHomeView> {
        /**
         * 获取商品数据
         */
        void getCategories();
    }

    public interface IHomeView extends IBaseView {

        /**
         * 设置数据
         */
        void onCategoriesLoaded(Categories categories);
    }
}
