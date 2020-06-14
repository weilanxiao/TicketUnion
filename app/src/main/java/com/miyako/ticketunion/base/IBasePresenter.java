package com.miyako.ticketunion.base;

import com.miyako.ticketunion.module.home.HomeContract;

public interface IBasePresenter<T extends IBaseView> {

    /**
     * 绑定UI
     */
    void bind(T view);

    /**
     * 解除绑定
     */
    void unBind(T view);
}
