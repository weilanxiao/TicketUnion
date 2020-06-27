package com.miyako.ticketunion.module.ticket;

import com.miyako.ticketunion.base.IBasePresenter;
import com.miyako.ticketunion.base.IBaseView;
import com.miyako.ticketunion.model.domain.TicketResult;

public class TicketContract {

    public interface ITicketPresenter extends IBasePresenter<ITicketView> {

        /**
         * 生成淘口令
         * @param title 标题
         * @param url 链接
         * @param cover 封面
         */
        public void getTicket(String title, String url, String cover);
    }

    public interface ITicketView extends IBaseView {

        /**
         * 淘口令加载成功
         * @param cover 封面
         * @param ticketResult 返回结果
         */
        public void onTicketLoaded(String cover, TicketResult ticketResult);
    }
}
