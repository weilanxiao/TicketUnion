package com.miyako.ticketunion.module.search;


import android.view.View;

import com.miyako.ticketunion.R;
import com.miyako.ticketunion.base.BaseFragment;

public class SearchFragment extends BaseFragment {

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setUpState(State.SUCCESS);
    }
}
