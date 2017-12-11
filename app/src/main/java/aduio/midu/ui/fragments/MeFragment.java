package aduio.midu.ui.fragments;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import aduio.lib.ui.BaseFragment;
import aduio.midu.R;
import aduio.midu.ui.activitys.DownAudioListActivity;

/**
 * Created by ${LostDeer} on 2017/11/15.
 * Github:https://github.com/LostDeer
 */

public class MeFragment extends BaseFragment {
    @BindView(R.id.iv_nikePhoto)
    ImageView mIvNikePhoto;
    @BindView(R.id.tv_nikeName)
    TextView mTvNikeName;
    @BindView(R.id.iv_account)
    ImageView mIvAccount;
    @BindView(R.id.iv_accountgo)
    ImageView mIvAccountgo;
    @BindView(R.id.tv_account)
    TextView mTvAccount;
    @BindView(R.id.rl_account)
    RelativeLayout mRlAccount;
    @BindView(R.id.iv_down)
    ImageView mIvDown;
    @BindView(R.id.rl_down)
    RelativeLayout mRlDown;
    @BindView(R.id.iv_buy)
    ImageView mIvBuy;
    @BindView(R.id.rl_buy)
    RelativeLayout mRlBuy;
    @BindView(R.id.iv_exchange)
    ImageView mIvExchange;
    @BindView(R.id.rl_exchange)
    RelativeLayout mRlExchange;
    @BindView(R.id.iv_setting)
    ImageView mIvSetting;
    @BindView(R.id.rl_setting)
    RelativeLayout mRlSetting;
    @BindView(R.id.iv_help)
    ImageView mIvHelp;
    @BindView(R.id.rl_help)
    RelativeLayout mRlHelp;
    @BindView(R.id.iv_recommend)
    ImageView mIvRecommend;
    @BindView(R.id.rl_recommend)
    RelativeLayout mRlRecommend;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.ll_nike, R.id.rl_account, R.id.rl_down, R.id.rl_buy, R.id.rl_exchange, R.id.rl_setting, R.id.rl_help, R.id.rl_recommend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_nike://用户登录/用户昵称设置
                break;
            case R.id.rl_account://账户余额
                break;
            case R.id.rl_down://已下载的音频
                startActivity(new Intent(mContext,DownAudioListActivity.class));
                break;
            case R.id.rl_buy://购买记录
                break;
            case R.id.rl_exchange://兑换码
                break;
            case R.id.rl_setting://设置
                break;
            case R.id.rl_help://帮助
                break;
            case R.id.rl_recommend://推荐
                break;
        }
    }
}
