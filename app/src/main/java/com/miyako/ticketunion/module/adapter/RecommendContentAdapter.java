package com.miyako.ticketunion.module.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.miyako.ticketunion.R;
import com.miyako.ticketunion.model.domain.RecommendContent;
import com.miyako.ticketunion.utils.LogUtils;
import com.miyako.ticketunion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendContentAdapter extends RecyclerView.Adapter<RecommendContentAdapter.ViewHolder> {

    private static final String TAG = "RecommendContentAdapter";
    private List<RecommendContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean> mList;
    private int cnt = 0;
    private OnListItemClickListener mListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LogUtils.d(TAG, "onCreateViewHolder"+"..."+(cnt++));
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_category_content, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LogUtils.d(TAG, "onBindViewHolder");
        RecommendContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean bean = mList.get(position);
        holder.setData(bean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(mList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setData(RecommendContent contentList) {
        LogUtils.d(TAG, "setData");
        if (contentList.getCode()==10000) {
            if (mList == null) {
                mList = new ArrayList<>(contentList.getData().getTbk_uatm_favorites_item_get_response().getResults().getUatm_tbk_item().size());
            }
            mList.clear();
            mList.addAll(contentList.getData().getTbk_uatm_favorites_item_get_response().getResults().getUatm_tbk_item());
            notifyDataSetChanged();
        }
    }

    public void addData(List<RecommendContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean> contentList) {
        if (mList == null) {
            mList = new ArrayList<>(contentList.size());
        }
        int start = mList.size();
        mList.addAll(contentList);
        notifyItemRangeChanged(start, contentList.size());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_recommend_category_content)
        ImageView mIvCover;
        @BindView(R.id.tv_recommend_category_content)
        TextView mTvTitle;
        @BindView(R.id.tv_recommend_category_content_price)
        TextView mTvPrice;
        @BindView(R.id.iv_recommend_category_content_off)
        TextView mTvOffPrice;
        @BindView(R.id.tv_recommend_category_content_buy)
        TextView mIvBuy;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(RecommendContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean bean) {
            ViewGroup.LayoutParams params = mIvCover.getLayoutParams();
            int width = params.width;
            int height = params.height;
            int size = Math.max(width, height) / 2;
            size = size-(size%100);
            String url = UrlUtils.coverPath(bean.getPict_url()+String.format("_%dx%d.jpg", size, size));
            LogUtils.d(TAG, "cover url:"+url);
            Glide.with(itemView.getContext())
                    .load(url)
                    .into(mIvCover);
            mTvTitle.setText(itemView.getContext().getString(R.string.format_recommend_category_content, bean.getTitle()));
            String price = bean.getReserve_price();
            LogUtils.d(TAG, "prise:" + price);
            if (!TextUtils.isEmpty(bean.getCoupon_click_url())) {
                mTvOffPrice.setText(itemView.getContext().getString(R.string.format_recommend_category_off_info, bean.getCoupon_info()));
                mTvPrice.setText(itemView.getContext().getString(R.string.format_recommend_content_pre_price, price));
            } else {
                mTvPrice.setText("晚了，没有优惠券了");
                mTvOffPrice.setVisibility(View.GONE);
                mIvBuy.setVisibility(View.GONE);
            }
        }
    }

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnListItemClickListener {
        void onItemClick(RecommendContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean item);
    }
}
