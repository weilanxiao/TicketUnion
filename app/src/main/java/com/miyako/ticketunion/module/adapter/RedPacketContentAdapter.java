package com.miyako.ticketunion.module.adapter;

import android.graphics.Paint;
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
import com.miyako.ticketunion.model.domain.OnSellContent;
import com.miyako.ticketunion.model.domain.RecommendContent;
import com.miyako.ticketunion.utils.LogUtils;
import com.miyako.ticketunion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RedPacketContentAdapter extends RecyclerView.Adapter<RedPacketContentAdapter.ViewHolder> {

    private static final String TAG = "RedPacketContentAdapter";
    private OnListItemClickListener mListener;
    private List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mList;
    private int cnt;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LogUtils.d(TAG, "onCreateViewHolder"+"..."+(cnt++));
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_red_packet_content, parent, false);
        return new RedPacketContentAdapter.ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LogUtils.d(TAG, "onBindViewHolder");
        OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean bean = mList.get(position);
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
        return mList == null?0:mList.size();
    }

    public void setData(OnSellContent content) {
        LogUtils.d(TAG, "setData");
        if (content.getCode()==10000) {
            if (mList == null) {
                mList = new ArrayList<>(content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size());
            }
            mList.clear();
            mList.addAll(content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data());
            notifyDataSetChanged();
        }
    }

    public void addData(OnSellContent content) {
        int size = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        if (mList == null) {
            mList = new ArrayList<>(size);
        }
        int start = mList.size();
        mList.addAll(content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data());
        notifyItemRangeChanged(start, size);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_red_packet_content)
        ImageView mIvCover;
        @BindView(R.id.tv_red_packet_content)
        TextView mTvTitle;
        @BindView(R.id.tv_red_packet_content_pre_price)
        TextView mTvPrice;
        @BindView(R.id.tv_red_packet_content_off_price)
        TextView mTvOffPrice;
        @BindView(R.id.tv_red_packet_off_info)
        TextView mTvOffInfo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean bean) {
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
            mTvTitle.setText(itemView.getContext().getString(R.string.format_red_packet_content, bean.getTitle()));
            long offPrise = bean.getCoupon_amount();
            String curPrise = bean.getZk_final_price();
            float afterPrise = Float.parseFloat(curPrise) - offPrise;
            LogUtils.d(TAG, "prise:" + curPrise);
            if (!TextUtils.isEmpty(bean.getCoupon_click_url())) {
                mTvOffPrice.setText(itemView.getContext().getString(R.string.format_red_packet_off_price, afterPrise));
                mTvPrice.setText(itemView.getContext().getString(R.string.format_red_packet_pre_price, curPrise));
                mTvPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                if (!TextUtils.isEmpty(bean.getCoupon_info())) {
                    mTvOffInfo.setText(itemView.getContext().getString(R.string.format_red_packet_off_info, bean.getCoupon_info()));
                } else {
                    mTvOffInfo.setText(String.format("省%s元", bean.getCoupon_amount()));
                }

            } else {
                mTvPrice.setText("晚了，没有优惠券了");
                mTvOffPrice.setVisibility(View.GONE);
            }
        }
    }

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnListItemClickListener {
        void onItemClick(OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean item);
    }

}
