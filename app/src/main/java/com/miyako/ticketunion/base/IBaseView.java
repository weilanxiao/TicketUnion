package com.miyako.ticketunion.base;

public interface IBaseView {

    /**
     * 加载中
     */
    void onLoading();

    /**
     * 错误
     */
    void onError(int errorCode, String msg);

    /**
     * 空数据
     */
    void onEmpty();

}
