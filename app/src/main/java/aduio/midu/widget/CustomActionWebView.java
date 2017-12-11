package aduio.midu.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${LostDeer} on 2017/11/30.
 * Github:https://github.com/LostDeer
 */

public class CustomActionWebView extends WebView {
    static String TAG = "CustomActionWebView";
    private ProgressBar progressbar;
    ActionMode mActionMode;

    List<String> mActionList = new ArrayList<>();

    ActionSelectListener mActionSelectListener;
    private ProgressBar mProgressBar;

    public CustomActionWebView(Context context) {
        this(context, null);
    }

    public CustomActionWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomActionWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 3, 0, 0));
        addView(progressbar);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int width = display.getWidth() / 2-50;
        int height = display.getHeight() / 2-89;
        mProgressBar = new ProgressBar(context);
        LayoutParams params = new LayoutParams(100, 100,width,height);
        mProgressBar.setLayoutParams(params);
        addView(mProgressBar);
        setWebChromeClient(new ProgressWebChromeClient());
        setWebViewClient(new WebViewClient(){
            // 页面开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressbar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }
            // 页面加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                progressbar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });
    }

    public  class ProgressWebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.e(TAG, "newProgress:" + newProgress);
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
                mProgressBar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    private ActionMode resolveActionMode(ActionMode actionMode) {
        if (actionMode != null) {
            Menu menu = actionMode.getMenu();
            this.mActionMode = actionMode;
            menu.clear();

            int i;
            for (i = 0; i < this.mActionList.size(); ++i) {
                menu.add((CharSequence) this.mActionList.get(i));
            }

            for (i = 0; i < menu.size(); ++i) {
                MenuItem menuItem = menu.getItem(i);
                menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        getSelectedData((String) item.getTitle());
                        releaseAction();
                        return true;
                    }
                });
            }
        }

        this.mActionMode = actionMode;
        return actionMode;
    }

    public ActionMode startActionMode(ActionMode.Callback callback) {
        ActionMode actionMode = super.startActionMode(callback);
        return this.resolveActionMode(actionMode);
    }

    public ActionMode startActionMode(ActionMode.Callback callback, int type) {
        ActionMode actionMode = super.startActionMode(callback, type);
        return this.resolveActionMode(actionMode);
    }

    private void releaseAction() {
        if (this.mActionMode != null) {
            this.mActionMode.finish();
            this.mActionMode = null;
        }

    }

    private void getSelectedData(String title) {
        String js = "(function getSelectedText() {var txt;var title = \"" + title + "\";if (window.getSelection) {txt = window.getSelection().toString();} else if (window.document.getSelection) {txt = window.document.getSelection().toString();} else if (window.document.selection) {txt = window.document.selection.createRange().text;}JSInterface.callback(txt,title);})()";
        if (Build.VERSION.SDK_INT >= 19) {
            this.evaluateJavascript("javascript:" + js, (ValueCallback) null);
        } else {
            this.loadUrl("javascript:" + js);
        }

    }

    public void linkJSInterface() {
        this.addJavascriptInterface(new ActionSelectInterface(this), "JSInterface");
    }

    public void setActionList(List<String> actionList) {
        this.mActionList = actionList;
    }

    public void setActionSelectListener(ActionSelectListener actionSelectListener) {
        this.mActionSelectListener = actionSelectListener;
    }

    public void dismissAction() {
        this.releaseAction();
    }

    private class ActionSelectInterface {
        CustomActionWebView mContext;

        ActionSelectInterface(CustomActionWebView c) {
            this.mContext = c;
        }

        @JavascriptInterface
        public void callback(String value, String title) {
            if (mActionSelectListener != null) {
                mActionSelectListener.onClick(title, value);
            }

        }
    }
}
