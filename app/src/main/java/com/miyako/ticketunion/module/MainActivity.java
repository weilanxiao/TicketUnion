package com.miyako.ticketunion.module;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.miyako.ticketunion.R;
import com.miyako.ticketunion.base.BaseFragment;
import com.miyako.ticketunion.module.home.HomeFragment;
import com.miyako.ticketunion.module.recommend.RecommendFragment;
import com.miyako.ticketunion.module.redPacket.RedPacketFragment;
import com.miyako.ticketunion.module.search.SearchFragment;
import com.miyako.ticketunion.utils.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.nav_bar_main)
    BottomNavigationView mNavigationView;
    private HomeFragment mHomeFragment;
    private RecommendFragment mRecommendFragment;
    private RedPacketFragment mRedPacketFragment;
    private SearchFragment mSearchFragment;
    private FragmentManager mFm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initFragment();
        initListener();
    }

    private void initView() {

    }

    private void initFragment() {
        mHomeFragment = new HomeFragment();
        mRecommendFragment = new RecommendFragment();
        mRedPacketFragment = new RedPacketFragment();
        mSearchFragment = new SearchFragment();
        mFm = getSupportFragmentManager();
        switchFragment(mHomeFragment);
    }

    private void initListener() {
        mNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_nav_home:
                    LogUtil.d(TAG, "切换到首页");
                    switchFragment(mHomeFragment);
                    break;
                case R.id.menu_nav_recommend:
                    LogUtil.d(TAG, "切换到推荐");
                    switchFragment(mRecommendFragment);
                    break;
                case R.id.menu_nav_red_packet:
                    LogUtil.d(TAG, "切换到红包");
                    switchFragment(mRedPacketFragment);
                    break;
                case R.id.menu_nav_search:
                    LogUtil.d(TAG, "切换到搜索");
                    switchFragment(mSearchFragment);
                    break;
            }
            return true;
        });
    }

    private void switchFragment(BaseFragment fragment) {
//        LogUtil.d(TAG, "切换到搜索");
        FragmentTransaction fragmentTransaction = mFm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_main_page_container, fragment);
        fragmentTransaction.commit();
    }
}
