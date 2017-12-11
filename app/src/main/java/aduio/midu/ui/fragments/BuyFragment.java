package aduio.midu.ui.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import aduio.lib.ui.BaseFragment;
import aduio.midu.R;
import aduio.midu.ui.activitys.ShareActivity;
import aduio.midu.widget.ActionSelectListener;
import aduio.midu.widget.CustomActionWebView;
import butterknife.BindView;

/**
 * Created by ${LostDeer} on 2017/11/15.
 * Github:https://github.com/LostDeer
 * 已购
 */

public class BuyFragment extends BaseFragment {
    @BindView(R.id.iv_toolBarBack)
    ImageView mIvToolBarBack;
    @BindView(R.id.tv_toolBarName)
    TextView mTvToolBarName;
    @BindView(R.id.tv_toolbarright)
    TextView mTvToolbarright;
    @BindView(R.id.ll_base)
    LinearLayout mLlBase;
    @BindView(R.id.wb_home)
    CustomActionWebView mWbHome;
    private ClipboardManager mManager;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    protected void initListener() {
        mIvToolBarBack.setVisibility(View.GONE);
        mTvToolBarName.setText("已购");
        initWebView();
    }


    private void initWebView() {
        WebSettings settings = mWbHome.getSettings();
        settings.setJavaScriptEnabled(true);//支持js
        //        settings.setCacheMode(LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);
        settings.setBlockNetworkImage(false);//解决图片不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //http和https的问题  参见上面的参数
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
//        mWbHome.setWebChromeClient(new ProgressWebChromeClient());
        //        mWbHome.addJavascriptInterface(new JsInterface(getActivity()), "AndroidWebView");
        mWbHome.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //拦截其URL跳转其他页面
                return true;
            }

            //页面加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });
        mWbHome.loadUrl("http://api.zygvip.com/zyg_webview/article/40");
    }


    @Override
    protected void initData() {
        mManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        List<String> list = new ArrayList<>();
        list.add("复制");
        list.add("分享");
        list.add("全选");
        //设置item
        mWbHome.setActionList(list);
        //链接js注入接口，使能选中返回数据
        mWbHome.linkJSInterface();
        mWbHome.setActionSelectListener(new ActionSelectListener() {
            @Override
            public void onClick(String title, String content) {
                switch (title){
                    case "复制":
                        ClipData clipData = ClipData.newPlainText(title, content);
                        mManager.setPrimaryClip(clipData);
                        Toast.makeText(mContext, "复制成功", Toast.LENGTH_SHORT).show();
                        break;
                    case "分享":
                        startActivity(new Intent(mContext, ShareActivity.class).putExtra
                                ("content",content));
                        break;
                    case "全选":
                        break;
                }
            }
        });
    }

}
