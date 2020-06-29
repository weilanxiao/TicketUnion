package com.miyako.ticketunion.module.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.miyako.ticketunion.model.domain.RecommendCategories;
import com.miyako.ticketunion.utils.LogUtils;
import com.miyako.ticketunion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendCategoryAdapter extends RecyclerView.Adapter<RecommendCategoryAdapter.ViewHolder> {

    private static final String TAG = "RecommendCategoryAdapter";
    private List<RecommendCategories.DataBean> mList;
    private int mCurrentCategory;
    private int cnt = 0;
    private OnListItemClickListener mListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LogUtils.d(TAG, "onCreateViewHolder" + "..." + (cnt++));
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_category, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LogUtils.d(TAG, "onBindViewHolder");
        RecommendCategories.DataBean bean = mList.get(position);
        holder.setData(bean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null && mCurrentCategory != position) {
                    int temp = mCurrentCategory;
                    mCurrentCategory = position;
                    notifyItemChanged(position);
                    notifyItemChanged(temp);
                    mListener.onItemClick(mList.get(position));
//                    notifyDataSetChanged();

                }
            }
        });
        holder.itemView.findViewById(R.id.tv_recommend_category)
                .setBackgroundColor(mCurrentCategory == position ?
                        holder.itemView.getResources().getColor(R.color.colorRecommendCategoryChecked) :
                        holder.itemView.getResources().getColor(R.color.colorRecommendCategoryNormal));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setData(List<RecommendCategories.DataBean> contentList) {
        LogUtils.d(TAG, "setData");
        if (mList == null) {
            mList = new ArrayList<>(contentList.size());
        }
        mList.clear();
        mList.addAll(contentList);
        notifyDataSetChanged();
    }

    public void addData(List<RecommendCategories.DataBean> contentList) {
        if (mList == null) {
            mList = new ArrayList<>(contentList.size());
        }
        int start = mList.size();
        mList.addAll(contentList);
        notifyItemRangeChanged(start, contentList.size());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recommend_category)
        TextView mTvCategory;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void setData(RecommendCategories.DataBean bean) {
            mTvCategory.setText(itemView.getContext().getString(R.string.format_recommend_category, bean.getFavorites_title()));
        }
    }

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnListItemClickListener {
        void onItemClick(RecommendCategories.DataBean item);
    }
}
