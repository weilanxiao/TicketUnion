package com.miyako.ticketunion.module.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.miyako.ticketunion.model.domain.Categories;
import com.miyako.ticketunion.module.home.category.CategoryPagerFragment;
import com.miyako.ticketunion.utils.LogUtils;

import java.util.List;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "HomePagerAdapter";
    private List<Categories.DataBean> mList;

    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getTitle();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return CategoryPagerFragment.newInstance(mList.get(position));
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setCategories(Categories categories) {
        LogUtils.d(TAG, "setCategories");
        if (mList == null) {
            mList = categories.getData();
        } else {
            mList.clear();
            mList.addAll(categories.getData());
        }
        notifyDataSetChanged();
    }
}
