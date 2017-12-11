package aduio.lib.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.autolayout.AutoLayoutActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import aduio.lib.R;
import aduio.lib.constant.Constant;
import aduio.lib.entitys.MessageEvent;
import aduio.lib.utils.ActivityNameUtils;
import aduio.lib.utils.NetWorkUtils;
import aduio.lib.utils.SPrefaceUtil;
import aduio.lib.utils.SettingValues;
import aduio.lib.utils.StringUtils;
import aduio.lib.widget.dialog.SpotsDialog;
import aduio.lib.widget.statebar.SystemBarTintManager;


/**
 * Created by Administrator on 2016/11/17.
 */

public abstract class BaseActivity extends AutoLayoutActivity {
    protected Context mContext;
    protected SpotsDialog mSpotsDialog;
//    private App mApp;
    protected SettingValues mInstance;
    private String mClassName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
//        PushAgent.getInstance(mContext).onAppStart();//推送友盟统计应用启动数据
//        MobclickAgent.enableEncrypt(true);//6.0.0版本及以后  日志加密
        mContext = getActivityContext();
        //        mApp.mContext = this;
//        mApp = (App) getApplication();
//        mApp.addActivity(this);
        mClassName = this.getClass().getSimpleName();
        EventBus.getDefault().register(this);
        setView();

        if (!ActivityNameUtils.getInstance().activityName.containsKey(mClassName)) {
            initSystemBar();
        }
    }

    private void setView() {
        boolean connected = NetWorkUtils.isNetworkConnected(this);
        if (connected) {//有网状态
            initView();
            ButterKnife.bind(this);
            initLoadDialog();
            initSettingValue();
            initdata();
        } else {
            NoNetView();
        }
    }

    private void NoNetView() {
        setContentView(R.layout.item_nonet);
        Toast.makeText(mContext, "请检查当前网络是否可用!", Toast.LENGTH_SHORT).show();

        //没有网络切换布局   主题会被改变
        //            String top = SPrefaceUtil.getString(mContext, Constant.STATUSBARHEIGHT, null);
        //            if(top!=null&& !StringUtils.isStringEmpty(top)){
        int top = getStatusBarHeight();
//        Integer integer = Integer.valueOf(top);
        View kong = findViewById(R.id.v_kong);
        if (kong != null) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) kong.getLayoutParams();
            layoutParams.height=top;
            kong.setLayoutParams(layoutParams);
//            kong.setPadding(0, integer, 0, 0);
        }
        View fanhui = findViewById(R.id.iv_fanhui);
        if (mClassName.equals("MainActivity") || mClassName.equals("WelcomeActivity")) {
            fanhui.setVisibility(View.GONE);
        }
        //        }
        findViewById(R.id.iv_noNet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setView();
            }
        });
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void itemError() {
        setContentView(R.layout.progress_error_view);
        Toast.makeText(mContext, "请检查当前网络是否可用!", Toast.LENGTH_SHORT).show();

        //没有网络切换布局   主题会被改变
        //            String top = SPrefaceUtil.getString(mContext, Constant.STATUSBARHEIGHT, null);
        //            if(top!=null&& !StringUtils.isStringEmpty(top)){
        int top = getStatusBarHeight();
//        Integer integer = Integer.valueOf(top);
        View kong = findViewById(R.id.v_kong);
        if (kong != null) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) kong.getLayoutParams();
            layoutParams.height=top;
            kong.setLayoutParams(layoutParams);
//            kong.setPadding(0, integer, 0, 0);
        }
        View fanhui = findViewById(R.id.iv_fanhui);
        if (mClassName.equals("MainActivity") || mClassName.equals("WelcomeActivity")) {
            fanhui.setVisibility(View.GONE);
        }
        //        }
        findViewById(R.id.errorStateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setView();
            }
        });
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void initSettingValue() {
        String json = SPrefaceUtil.getString(mContext, "", null);
        if (json != null) {
            SettingValues settingValues = new Gson().fromJson(json, SettingValues.class);
            mInstance = SettingValues.getInstance();
            mInstance.isLogin = settingValues.isLogin;
            mInstance.token = settingValues.token;
            mInstance.user_id = settingValues.user_id;
            mInstance.nikeName = settingValues.nikeName;
            mInstance.nikeIcon = settingValues.nikeIcon;
            mInstance.phoneNum = settingValues.phoneNum;
            mInstance.isAuth = settingValues.isAuth;
            mInstance.weiXin_Nike = settingValues.weiXin_Nike;
            mInstance.qq_Nike = settingValues.qq_Nike;
            mInstance.saveInstance();
        }
    }

    private void initLoadDialog() {
        mSpotsDialog = new SpotsDialog(mContext);
        mSpotsDialog.setCancelable(false);//禁止返回取消
    }


    protected void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.biaotihei20);//通知栏所需颜色
    }

    protected void transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(android.R.color.transparent);//通知栏所需颜色
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    public void initView() {
        loadViewLayout();
    }

    private void initdata() {
        //没有网络切换布局   主题会被改变
        String top = SPrefaceUtil.getString(mContext, Constant.STATUSBARHEIGHT, null);
        if (top != null && !StringUtils.isStringEmpty(top)) {
            Integer integer = Integer.valueOf(top);
            LinearLayout llBase = (LinearLayout) findViewById(R.id.ll_base);
            if (llBase != null) {
                llBase.setPadding(0, integer, 0, 0);
            }
        }
        setListener();
        processLogic();
    }

    @Subscribe
    public void onEventMainThread(MessageEvent messageEntity) {
        String message = messageEntity.getMessage();
        if (message.equals(mClassName)) {//刷新界面
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    itemError();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        if (mSpotsDialog != null) {
            boolean showing = mSpotsDialog.isShowing();
            if (showing) {
                mSpotsDialog.dismiss();
            }
//            mApp.removeActivity(this);
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    /**
     * 获取context
     *
     * @return
     */
    protected abstract Context getActivityContext();

    /**
     * 初始化布局
     */
    protected abstract void loadViewLayout();

    /**
     * 设置监听
     */
    protected abstract void setListener();

    /**
     * 加载数据
     */
    protected abstract void processLogic();

    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(mClassName); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
//        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(mClassName); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
//        MobclickAgent.onPause(this);
    }
}

