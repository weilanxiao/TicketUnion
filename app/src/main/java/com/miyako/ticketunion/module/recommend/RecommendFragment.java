package com.miyako.ticketunion.module.recommend;


import android.view.View;

import com.miyako.ticketunion.R;
import com.miyako.ticketunion.base.BaseFragment;

public class RecommendFragment extends BaseFragment {

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setUpState(State.SUCCESS);
    }
}
