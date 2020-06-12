package com.miyako.ticketunion.module.redPacket;


import android.view.View;

import com.miyako.ticketunion.R;
import com.miyako.ticketunion.base.BaseFragment;

public class RedPacketFragment extends BaseFragment {

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_red_packet;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setUpState(State.SUCCESS);
    }
}
