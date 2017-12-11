package aduio.lib.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import aduio.lib.R;
import aduio.lib.utils.NetWorkUtils;
import aduio.lib.widget.dialog.SpotsDialog;

/**
 * Created by Administrator on 2016/11/17.
 */

public abstract class BaseFragment extends Fragment {
    private View mRootView;
    protected Context mContext;
    protected SpotsDialog mSpotsDialog;
    private String mName;
    //    private ProgressActivity mProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mContext=getContext();
        boolean connected = NetWorkUtils.isNetworkConnected(mContext);
        if(connected){
            mRootView = initView(inflater, container);
            ButterKnife.bind(this, mRootView);//绑定到butterknife
            //            mProgress = (ProgressActivity) mRootView.findViewById(R.id.progress);
            return mRootView;
        }else {
            return inflater.inflate(R.layout.item_nonet, container, false);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListener();
        initLoadDialog();
        initData();
    }

    private void initLoadDialog() {
        mSpotsDialog = new SpotsDialog(mContext);
        mSpotsDialog.setCancelable(false);//禁止返回取消
    }


    /**
     * 初始化布局
     *
     * @param inflater
     * @param container
     * @return
     */
    protected abstract View initView(LayoutInflater inflater, ViewGroup container);

    /**
     * 设置监听
     */
    protected abstract void initListener();

    /**
     * 数据加载
     */
    protected abstract void initData();

    public void onResume() {
        super.onResume();
        mName = this.getClass().getSimpleName();
//        MobclickAgent.onPageStart(mName); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(mName);
    }
}
