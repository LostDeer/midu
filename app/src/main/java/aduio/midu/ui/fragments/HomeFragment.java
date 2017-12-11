package aduio.midu.ui.fragments;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import aduio.lib.ui.BaseFragment;
import aduio.midu.R;
import aduio.midu.widget.CustomActionWebView;
import butterknife.BindView;

/**
 * Created by ${LostDeer} on 2017/11/15.
 * Github:https://github.com/LostDeer
 * 首页
 */

public class HomeFragment extends BaseFragment {
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

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    protected void initListener() {
        mIvToolBarBack.setVisibility(View.GONE);
        mTvToolBarName.setText("首页");
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
//        mWbHome.setWebChromeClient(new WebChromeClient());
        //        mWbHome.addJavascriptInterface(new JsInterface(getActivity()), "AndroidWebView");
        mWbHome.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                startActivity(new Intent(mContext,BookDesActivity.class).putExtra("url",url));
                //拦截其URL跳转其他页面
                return false;
            }

            //页面加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });
        mWbHome.loadUrl("http://www.acfun.cn/");
    }

    @Override
    protected void initData() {

    }

}
