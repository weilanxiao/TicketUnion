package com.miyako.ticketunion.module.home.category;

import com.miyako.ticketunion.base.IBasePresenter;
import com.miyako.ticketunion.base.IBaseView;
import com.miyako.ticketunion.model.domain.HomePagerContent;

import java.util.List;

public class CategoryPagerContract {

    public interface IHomePagerPresenter extends IBasePresenter<IHomePagerView> {

        /**
         * 根据分类获取内容
         * @param categoryId 分类Id
         */
        void getContentByCategoryId(int categoryId);

        /**
         * 加载更多
         * @param categoryId 分类Id
         */
        void getContentMore(int categoryId);

        /**
         * 重新加载
         */
        void reload();

    }

    public interface IHomePagerView extends IBaseView {

        /**
         * 设置数据
         */
        void onContentLoaded(List<HomePagerContent.DataBean> contentList);

        /**
         * 返回当前Id
         */
        int getMaterialId();

        /**
         * 加载更多成功
         */
        void onLoadMoreSuccess(List<HomePagerContent.DataBean> contentList);

        /**
         * 加载更多错误
         */
        void onLoadMoreError();

        /**
         * 加载更多数据为空
         */
        void onLoadMoreEmpty();

        /**
         * 加载更多轮播图
         */
        void onLooperLoaded(List<HomePagerContent.DataBean> contentList);
    }
}
