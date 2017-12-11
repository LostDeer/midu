package aduio.midu.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import aduio.lib.ui.BaseActivity;
import aduio.midu.R;
import butterknife.BindView;

/**
 * Created by ${LostDeer} on 2017/11/15.
 * Github:https://github.com/LostDeer
 */

public class BookDesActivity extends BaseActivity {

    @BindView(R.id.wb_bookdes)
    WebView mWbBookdes;
    @BindView(R.id.ll_base)
    LinearLayout mLlBase;
    private String mUrl;


    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_bookdes);
        mUrl = getIntent().getStringExtra("url");
    }

    @Override
    protected void setListener() {
        initWebView();
    }

    private void initWebView() {
        WebSettings settings = mWbBookdes.getSettings();
        settings.setJavaScriptEnabled(true);//支持js
        //        settings.setCacheMode(LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);
        settings.setBlockNetworkImage(false);//解决图片不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //http和https的问题  参见上面的参数
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWbBookdes.setWebChromeClient(new WebChromeClient());
        //        mWbHome.addJavascriptInterface(new JsInterface(getActivity()), "AndroidWebView");
        mWbBookdes.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                startActivity(new Intent(mContext, BookDesActivity.class).putExtra("url", url));
                //拦截其URL跳转其他页面
                return true;
            }

            //页面加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });
        mWbBookdes.loadUrl(mUrl);
    }

    @Override
    protected void processLogic() {

    }
}
