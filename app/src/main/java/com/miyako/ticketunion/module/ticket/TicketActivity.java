package com.miyako.ticketunion.module.ticket;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.miyako.ticketunion.R;
import com.miyako.ticketunion.base.BaseActivity;
import com.miyako.ticketunion.model.domain.TicketResult;
import com.miyako.ticketunion.utils.LogUtils;
import com.miyako.ticketunion.utils.PresenterManager;
import com.miyako.ticketunion.utils.ToastUtils;
import com.miyako.ticketunion.utils.UrlUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class TicketActivity extends BaseActivity implements TicketContract.ITicketView {

    //    private static final String TAG = "TicketActivity";
    private TicketPresenter mPresenter;

    @BindView(R.id.iv_tao_kou_ling_back)
    ImageView mIvBack;

    @BindView(R.id.iv_tao_kou_ling_cover)
    ImageView mIvCover;
    @BindView(R.id.et_tao_kou_ling)
    EditText mEtTao;
    @BindView(R.id.btn_ling_juan)
    Button mBtnLingJuan;

    @BindView(R.id.layout_tao_kou_ling_loading)
    LinearLayout mLayoutLoading;
    @BindView(R.id.layout_tao_kou_ling)
    LinearLayout mLayoutContent;
    private boolean mHasTaobaoApp;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }

    @Override
    protected void initView() {
        mLayoutLoading.setVisibility(View.VISIBLE);
        mLayoutContent.setVisibility(View.GONE);
    }

    @Override
    protected void initListener() {
        mIvBack.setOnClickListener(this::onBack);
        mBtnLingJuan.setOnClickListener(this::onLingJuan);
    }

    @Override
    protected void initPresenter() {
        mPresenter = PresenterManager.getInstance().getTicketPresenter();
        if (mPresenter != null) {
            mPresenter.bind(this);
        }
    }

    private void onBack(View view) {
        finish();
    }

    private void onLingJuan(View view) {
        LogUtils.d(TAG, "onLingJuan");
        // 跳转到淘宝:com.taobao.taobao
        String code = mEtTao.getText().toString().trim();
        LogUtils.d(TAG, "ticket code:"+code);
        ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("sob_taobao_ticket", code);
        // 复制到剪贴板
        clip.setPrimaryClip(data);

        if (mHasTaobaoApp) {
            // 打开淘宝
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setComponent(new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity"));
            startActivity(intent);
        } else {
            ToastUtils.showToast("复制成功");
        }
    }


    @Override
    protected void release() {
        super.release();
        if (mPresenter != null) {
            mPresenter.unBind(this);
        }
    }

    @Override
    public void onTicketLoaded(String cover, TicketResult ticketResult) {
        //        LogUtils.d(TAG, "cover:"+cover);
        //        LogUtils.d(TAG, "res:"+ticketResult);
        if (mIvCover != null && !TextUtils.isEmpty(cover)) {
            Glide.with(this).load(UrlUtils.coverPath(cover)).into(mIvCover);
            mEtTao.setText(ticketResult.getData().getTbk_tpwd_create_response().getData().getModel());
            mLayoutLoading.setVisibility(View.GONE);
            mLayoutContent.setVisibility(View.VISIBLE);
            PackageManager packageManager = getPackageManager();
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
                mHasTaobaoApp = packageInfo != null;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                mHasTaobaoApp = false;
            }
            LogUtils.d(TAG, "mHasTaobaoApp:" + mHasTaobaoApp);
            mBtnLingJuan.setText(mHasTaobaoApp ? "打开淘宝领卷" : "复制淘口令");
        }
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onError(int errorCode, String msg) {

    }

    @Override
    public void onEmpty() {

    }
}
