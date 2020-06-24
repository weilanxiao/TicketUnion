package com.miyako.ticketunion.module.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.miyako.ticketunion.model.domain.HomePagerContent;
import com.miyako.ticketunion.utils.LogUtils;
import com.miyako.ticketunion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {

    private static final String TAG = "LooperPagerAdapter";

    private List<HomePagerContent.DataBean> mList;

//    @Override
//    public int getCount() {
//        return mList == null ? 0 : mList.size();
//    }

//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        ImageView iv = new ImageView(container.getContext());
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        iv.setLayoutParams(layoutParams);
//        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        Glide.with(container.getContext())
//                .load(UrlUtils.coverPath(mList.get(position).getPict_url()))
//                .into(iv);
//        //        LogUtils.d(TAG, "url:"+UrlUtils.coverPath(mList.get(position).getPict_url()));
//        container.addView(iv);
//        return iv;
//    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int index = position % mList.size();
        ImageView iv = new ImageView(container.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(layoutParams);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        int height = container.getMeasuredHeight();
        int width = container.getMeasuredWidth();
        int coverSize = Math.max(height, width) / 2;
        String url = UrlUtils.coverPath(mList.get(index).getPict_url()+String.format("_%dx%d.jpg",coverSize, coverSize));
        Glide.with(container.getContext())
                .load(url)
                .into(iv);
        container.addView(iv);
        return iv;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    public void setData(List<HomePagerContent.DataBean> contentList) {
        if (mList == null) {
            mList = new ArrayList<>(contentList.size());
        }
        mList.clear();
        mList.addAll(contentList);
        notifyDataSetChanged();
    }

    public int getDataSize() {
        return mList == null ? 0 : mList.size();
    }
}
