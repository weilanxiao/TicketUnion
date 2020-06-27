package com.miyako.ticketunion.module;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.miyako.ticketunion.R;
import com.miyako.ticketunion.base.BaseActivity;
import com.miyako.ticketunion.base.BaseFragment;
import com.miyako.ticketunion.module.home.HomeFragment;
import com.miyako.ticketunion.module.recommend.RecommendFragment;
import com.miyako.ticketunion.module.redPacket.RedPacketFragment;
import com.miyako.ticketunion.module.search.SearchFragment;
import com.miyako.ticketunion.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.nav_bar_main)
    BottomNavigationView mNavigationView;
    private HomeFragment mHomeFragment;
    private RecommendFragment mRecommendFragment;
    private RedPacketFragment mRedPacketFragment;
    private SearchFragment mSearchFragment;
    private FragmentManager mFm;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mHomeFragment = new HomeFragment();
        mRecommendFragment = new RecommendFragment();
        mRedPacketFragment = new RedPacketFragment();
        mSearchFragment = new SearchFragment();
        mFm = getSupportFragmentManager();
        switchFragment(mHomeFragment);
    }

    @Override
    public void initListener() {
        mNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_nav_home:
                    LogUtils.d(TAG, "切换到首页");
                    switchFragment(mHomeFragment);
                    break;
                case R.id.menu_nav_recommend:
                    LogUtils.d(TAG, "切换到推荐");
                    switchFragment(mRecommendFragment);
                    break;
                case R.id.menu_nav_red_packet:
                    LogUtils.d(TAG, "切换到红包");
                    switchFragment(mRedPacketFragment);
                    break;
                case R.id.menu_nav_search:
                    LogUtils.d(TAG, "切换到搜索");
                    switchFragment(mSearchFragment);
                    break;
            }
            return true;
        });
    }

    @Override
    protected void initPresenter() {

    }

    private BaseFragment lastFragment;

    private void switchFragment(BaseFragment fragment) {
//        LogUtil.d(TAG, "切换到搜索");
        FragmentTransaction fragmentTransaction = mFm.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.fragment_main_page_container, fragment);
        } else {
            fragmentTransaction.show(fragment);
        }
        if (lastFragment != null && fragment!=lastFragment) {
            fragmentTransaction.hide(lastFragment);
        }
        lastFragment = fragment;
//        fragmentTransaction.replace(R.id.fragment_main_page_container, fragment);
        fragmentTransaction.commit();
    }
}
