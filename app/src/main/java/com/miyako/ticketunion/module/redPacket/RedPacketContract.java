package com.miyako.ticketunion.module.redPacket;

import com.miyako.ticketunion.base.IBasePresenter;
import com.miyako.ticketunion.base.IBaseView;
import com.miyako.ticketunion.model.domain.OnSellContent;

/**
 * 特惠界面契约类
 */
public interface RedPacketContract {

    public interface IRedPacketPresenter extends IBasePresenter<IRedPacketView> {

        /**
         * 加载特惠内容
         */
        public void getOnSellContent();

        /**
         * 加载更多
         */
        public void loadMore();

        /**
         * 重新加载
         */
        public void reload();
    }

    public interface IRedPacketView extends IBaseView {

        /**
         * 加载内容成功
         * @param content 数据结果
         */
        public void onLoadSuccess(OnSellContent content);

        /**
         * 加载更多
         * @param content 数据结果
         */
        public void onLoadMoreSuccess(OnSellContent content);

        /**
         * 加载更多空
         */
        public void onLoadMoreEmpty();

        /**
         * 加载更多失败
         * @param content 数据结果
         */
        public void onLoadMoreError(OnSellContent content);

    }
}
