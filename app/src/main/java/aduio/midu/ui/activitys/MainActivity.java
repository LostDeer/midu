package aduio.midu.ui.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import aduio.lib.entitys.MessageEvent;
import aduio.lib.ui.BaseActivity;
import aduio.midu.LocalAudioEntity;
import aduio.midu.R;
import aduio.midu.ui.fragments.BuyFragment;
import aduio.midu.ui.fragments.HomeFragment;
import aduio.midu.ui.fragments.MeFragment;
import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.fl_main)
    FrameLayout mFlMain;
    @BindView(R.id.iv_mainCrowd)
    ImageView mIvMainCrowd;
    @BindView(R.id.tv_mainCrowd)
    TextView mTvMainCrowd;
    @BindView(R.id.ll_mainCrowd)
    LinearLayout mLlMainCrowd;
    @BindView(R.id.iv_mainFind)
    ImageView mIvMainFind;
    @BindView(R.id.tv_mainFind)
    TextView mTvMainFind;
    @BindView(R.id.ll_mainFind)
    LinearLayout mLlMainFind;
    @BindView(R.id.iv_mainMe)
    ImageView mIvMainMe;
    @BindView(R.id.tv_mainMe)
    TextView mTvMainMe;
    @BindView(R.id.ll_mainMe)
    LinearLayout mLlMainMe;
    private Fragment[] mFragments = new Fragment[3];

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_main);
//        HermesEventBus.getDefault().register(this);
    }

    @Override
    protected void setListener() {
        initFragment();
        ArrayList<LocalAudioEntity> objects = new ArrayList<>();
        LocalAudioEntity localAudioEntity = new LocalAudioEntity();
        localAudioEntity.setName("1");
        localAudioEntity.setPath("2");
        objects.add(localAudioEntity);

//        String url = "http://freecityid.market.alicloudapi.com/whapi/json/alicityweather/briefforecast3days";
//        HashMap<String, String> map = new HashMap<>();
//        map.put("cityId", "3");
//        //        map.put("token", "677282c2f1b3d718152c4e25ed434bc4");
//        OkHttpUtil.getInstance().post(url, map).enqueue(new UiCallBack<LocalAudioEntity>() {
//            @Override
//            public void onResponse(LocalAudioEntity response) {
////                Log.e("MainActivity", response.body().toString());
//                Log.e("MainActivity", Thread.currentThread().getName());
//            }
//
//            @Override
//            public void onFailure(IOException ioException) {
//                Log.e("MainActivity", ioException.getMessage().toString());
//            }
//        });
    }

    @Subscribe
    public void onEventMainThread(MessageEvent messageEntity) {
        super.onEventMainThread(messageEntity);
        String message = messageEntity.getMessage();
        String data = (String) messageEntity.getData();
        Log.e("MainActivity", message);
        Log.e("MainActivity", data);
    }

    private void initFragment() {
        mFragments[0] = new HomeFragment();
        mFragments[1] = new BuyFragment();
        mFragments[2] = new MeFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_main, mFragments[0])
                .add(R.id.fl_main, mFragments[1])
                .add(R.id.fl_main, mFragments[2])
                .commit();

        getSupportFragmentManager().beginTransaction()
                .show(mFragments[0])
                .hide(mFragments[1])
                .hide(mFragments[2])
                .commit();
    }

    @Override
    protected void processLogic() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("aduio.midu.serivce.LocaltionService");
        LocalBroadcastManager.getInstance(this).registerReceiver(new LocalBroadcastReceiver(), filter);
    }

    class LocalBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String tag = intent.getStringExtra("tag");
            Log.e("LocalBroadcastReceiver", tag);
        }
    }

    @OnClick({R.id.ll_mainCrowd, R.id.ll_mainFind, R.id.ll_mainMe})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_mainCrowd:
//                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
                showFragment(0);
                break;
            case R.id.ll_mainFind:
//                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
                showFragment(1);
                break;
            case R.id.ll_mainMe:
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
                showFragment(2);
                break;
        }
    }

    private void showFragment(int position) {
        //        switch (position) {
        //            case 0:
        //                mTvMainCrowd.setTextColor(getResources().getColor(R.color.money860));
        //                mIvMainCrowd.setImageResource(R.drawable.zongcou_yes);
        //
        //                //                mTvMainBeauty.setTextColor(getResources().getColor(R.color.huisec7));
        //                //                mIvMainBeauty.setImageResource(R.drawable.meirengu_no);
        //
        //                mTvMainFind.setTextColor(getResources().getColor(R.color.huicb));
        //                mIvMainFind.setImageResource(R.drawable.faxian_no);
        //
        //                mTvMainMe.setTextColor(getResources().getColor(R.color.huicb));
        //                mIvMainMe.setImageResource(R.drawable.me_no);
        //                break;
        //            //            case 1:
        //            //                mTvMainCrowd.setTextColor(getResources().getColor(R.color.huisec7));
        //            //                mIvMainCrowd.setImageResource(R.drawable.zongcou_no);
        //            //
        //            ////                mTvMainBeauty.setTextColor(getResources().getColor(R.color.money860));
        //            ////                mIvMainBeauty.setImageResource(R.drawable.meirengu_yes);
        //            //
        //            //                mTvMainFind.setTextColor(getResources().getColor(R.color.huisec7));
        //            //                mIvMainFind.setImageResource(R.drawable.faxian_no);
        //            //
        //            //                mTvMainMe.setTextColor(getResources().getColor(R.color.huisec7));
        //            //                mIvMainMe.setImageResource(R.drawable.me_no);
        //            //                break;
        //            case 1:
        //                mTvMainCrowd.setTextColor(getResources().getColor(R.color.huicb));
        //                mIvMainCrowd.setImageResource(R.drawable.zongcou_no);
        //
        //                //                mTvMainBeauty.setTextColor(getResources().getColor(R.color.huisec7));
        //                //                mIvMainBeauty.setImageResource(R.drawable.meirengu_no);
        //
        //                mTvMainFind.setTextColor(getResources().getColor(R.color.money860));
        //                mIvMainFind.setImageResource(R.drawable.faxian_yes);
        //
        //                mTvMainMe.setTextColor(getResources().getColor(R.color.huicb));
        //                mIvMainMe.setImageResource(R.drawable.me_no);
        //                break;
        //            case 2:
        //                boolean isLogin = SettingValues.getInstance().getIsLogin();
        //                if (!isLogin) {
        //                    startActivity(new Intent(mContext, LoginAvtivity.class));
        //                    return;
        //                }
        //                //                String json = SPrefaceUtil.getString(mContext, Constant.SPKEY, null);//判断有没有登录
        //                //                if (null == json || StringUtils.isStringEmpty(json)) {//未登录
        //                //
        //                //                }
        //
        //                mTvMainCrowd.setTextColor(getResources().getColor(R.color.huicb));
        //                mIvMainCrowd.setImageResource(R.drawable.zongcou_no);
        //
        //                //                mTvMainBeauty.setTextColor(getResources().getColor(R.color.huisec7));
        //                //                mIvMainBeauty.setImageResource(R.drawable.meirengu_no);
        //
        //                mTvMainFind.setTextColor(getResources().getColor(R.color.huicb));
        //                mIvMainFind.setImageResource(R.drawable.faxian_no);
        //
        //                mTvMainMe.setTextColor(getResources().getColor(R.color.money860));
        //                mIvMainMe.setImageResource(R.drawable.me_yes);
        //                break;
        //        }

        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < mFragments.length; i++) {
            if (position == i) {
                beginTransaction.show(mFragments[i]);
                mFragments[i].onStart();
            } else {
                beginTransaction.hide(mFragments[i]);
            }
        }
        beginTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        HermesEventBus.getDefault().unregister(this);
    }
}
