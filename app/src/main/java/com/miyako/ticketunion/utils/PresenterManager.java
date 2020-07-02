package com.miyako.ticketunion.utils;

import com.miyako.ticketunion.module.home.HomePresenter;
import com.miyako.ticketunion.module.home.category.CategoryPagerPresenter;
import com.miyako.ticketunion.module.recommend.RecommendPresenter;
import com.miyako.ticketunion.module.redPacket.RedPacketPresenter;
import com.miyako.ticketunion.module.ticket.TicketPresenter;

/**
 * presenter管理单例类
 */
public class PresenterManager {

    private static final String TAG = "PresenterManager";

    private static final PresenterManager obj = new PresenterManager();

    private final CategoryPagerPresenter mCategoryPagerPresenter;
    private final HomePresenter mHomePresenter;
    private final TicketPresenter mTicketPresenter;
    private final RecommendPresenter mRecommendPresenter;
    private final RedPacketPresenter mRedPacketPresenter;

    private PresenterManager() {
        LogUtils.d(TAG, "创建Presenter管理单例类");
        mCategoryPagerPresenter = new CategoryPagerPresenter();
        mHomePresenter = new HomePresenter();
        mTicketPresenter = new TicketPresenter();
        mRecommendPresenter = new RecommendPresenter();
        mRedPacketPresenter = new RedPacketPresenter();
    }

    public static PresenterManager getInstance() {
        return obj;
    }

    public CategoryPagerPresenter getCategoryPagerPresenter() {
        return mCategoryPagerPresenter;
    }

    public HomePresenter getHomePresenter() {
        return mHomePresenter;
    }

    public TicketPresenter getTicketPresenter() {
        return mTicketPresenter;
    }

    public RecommendPresenter getRecommendPresenter() {
        return mRecommendPresenter;
    }

    public RedPacketPresenter getRedPacketPresenter() {
        return mRedPacketPresenter;
    }
}
