package com.miyako.ticketunion.module.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.miyako.ticketunion.R;
import com.miyako.ticketunion.model.domain.HomePagerContent;
import com.miyako.ticketunion.utils.LogUtils;
import com.miyako.ticketunion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryPagerAdapter extends RecyclerView.Adapter<CategoryPagerAdapter.ViewHolder> {

    private static final String TAG = "CategoryPagerAdapter";
    private List<HomePagerContent.DataBean> mList;
    private int cnt = 0;
    private OnListItemClickListener mListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LogUtils.d(TAG, "onCreateViewHolder"+"..."+(cnt++));
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pager_content, parent, false);
        return new ViewHolder(item);
//        return ViewHolder.getViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LogUtils.d(TAG, "onBindViewHolder");
        HomePagerContent.DataBean bean = mList.get(position);
        holder.setData(bean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(mList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setData(List<HomePagerContent.DataBean> contentList) {
        LogUtils.d(TAG, "setData");
        if (mList == null) {
            mList = new ArrayList<>(contentList.size());
        }
        mList.clear();
        mList.addAll(contentList);
        notifyDataSetChanged();
    }

    public void addData(List<HomePagerContent.DataBean> contentList) {
        if (mList == null) {
            mList = new ArrayList<>(contentList.size());
        }
        int start = mList.size();
        mList.addAll(contentList);
        notifyItemRangeChanged(start, contentList.size());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_item_goods_cover)
        ImageView mIvCover;
        @BindView(R.id.tv_item_goods_title)
        TextView mTvTitle;
        @BindView(R.id.tv_item_goods_off_prise)
        TextView mTvOffPrise;
        // 最终价格=原价-折扣
        @BindView(R.id.tv_item_goods_after_prise)
        TextView mTvAfterPrise;
        // 原价
        @BindView(R.id.tv_item_goods_cur_prise)
        TextView mTvCurPrise;
        @BindView(R.id.tv_item_goods_sell_count)
        TextView mTvCount;

        private View mConvertView;

        public ViewHolder(Context context, View itemView, ViewGroup parent) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mConvertView = itemView;
        }

        public static ViewHolder getViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pager_content, parent, false);
            return new ViewHolder(parent.getContext(), itemView, parent);
        }

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void setData(HomePagerContent.DataBean bean) {
            ViewGroup.LayoutParams params = mIvCover.getLayoutParams();
            int width = params.width;
            int height = params.height;
            int size = Math.max(width, height) / 2;
            String url = UrlUtils.coverPath(bean.getPict_url()+String.format("_%dx%d.jpg", size, size));
            LogUtils.d(TAG, "cover url:"+url);
            Glide.with(itemView.getContext())
                    .load(url)
                    .into(mIvCover);
            mTvTitle.setText(itemView.getContext().getString(R.string.format_goods_title, bean.getTitle()));
            long offPrise = bean.getCoupon_amount();
            String curPrise = bean.getZk_final_price();
            float afterPrise = Float.parseFloat(curPrise) - offPrise;
            LogUtils.d(TAG, "off prise:" + offPrise);
            LogUtils.d(TAG, "cur prise:" + curPrise);
            LogUtils.d(TAG, "after prise:" + afterPrise);
            mTvOffPrise.setText(itemView.getContext().getString(R.string.format_goods_off_prise, offPrise));
            mTvAfterPrise.setText(itemView.getContext().getString(R.string.format_goods_after_prise, afterPrise));
            mTvCurPrise.setText(itemView.getContext().getString(R.string.format_goods_cur_prise, curPrise));
            mTvCurPrise.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mTvCount.setText(itemView.getContext().getString(R.string.format_goods_sell_count, bean.getVolume()));
        }
    }

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnListItemClickListener {
        void onItemClick(HomePagerContent.DataBean item);
    }
}
