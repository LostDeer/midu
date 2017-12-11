package aduio.midu.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by ${LostDeer} on 2017/11/21.
 * Github:https://github.com/LostDeer
 */

public class HoverScrollview extends ScrollView {

    private View mUpView;
    private View mItView;
    private View mChildAt;

    public HoverScrollview(Context context) {
        this(context, null);
    }

    public HoverScrollview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HoverScrollview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    int height = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mChildAt = getChildAt(0);
        if (mChildAt instanceof LinearLayout) {
            int childCount = ((LinearLayout) mChildAt).getChildCount();
            Log.e("HoverScrollview", "childCount:" + childCount);
            if (childCount > 0) {
                for (int i = 0; i < childCount; i++) {
                    View childAt = ((LinearLayout) mChildAt).getChildAt(i);
                    if(childAt instanceof ViewPager){
                        Log.e("HoverScrollview", "childAt.getMeasuredHeight():" + childAt
                                .getMeasuredHeight());
                        int count = ((ViewPager) childAt).getChildCount();
                        Log.e("HoverScrollview", "count:" + count);
                        if(count>0){
                            for (int j = 0; j < count; j++) {
                                View at = ((ViewPager) childAt).getChildAt(j);
                                Log.e("HoverScrollview", "at.getMeasuredHeight():" + at
                                        .getMeasuredHeight());
//                                if(at instanceof WebView){
//                                    Log.e("HoverScrollview", "webview");
//                                    Log.e("HoverScrollview", "at.getMeasuredHeight():" + at
//                                            .getMeasuredHeight());
//                                }
                            }
                        }
                    }
                    height += childAt.getLayoutParams().height;
                }
            }
            Log.e("HoverScrollview", "height:" + height);
            mChildAt.measure(widthMeasureSpec, height);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e("HoverScrollview", "changed:" + changed);
        int childCount = getChildCount();
        Log.e("HoverScrollview", "childCount:" + childCount);
        if (childCount > 0) {
            mChildAt = getChildAt(0);
            if (mChildAt instanceof LinearLayout) {
                int count = ((LinearLayout) mChildAt).getChildCount();
                if (count > 0) {
                    for (int i = 0; i < count; i++) {
                        View view = ((LinearLayout) mChildAt).getChildAt(i);
                        int top = view.getTop();
                        Log.e("HoverScrollview", "top:" + top);
                        if (view instanceof ViewPager) {
                            //上一个控件
                            mUpView = ((LinearLayout) mChildAt).getChildAt(i - 1);
                            //本控件
                            mItView = ((LinearLayout) mChildAt).getChildAt(i);
                            Log.e("HoverScrollview", "mItView.getMeasuredHeight():" + mItView
                                    .getMeasuredHeight());
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        final int top = mUpView.getTop();
//        Log.e("HoverScrollview", "top:" + top);
//        if (top != 0 && top > 0) {
//            return true;
//        } else {
//            return super.onInterceptTouchEvent(ev);
//        }
                return super.onInterceptTouchEvent(ev);
    }

    private float mDownY;

    //触摸
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
//                int measuredHeight = mChildAt.getMeasuredHeight();
//                Log.e("HoverScrollview", "measuredHeight:" + measuredHeight);
//                float moveY = ev.getY();
//                float move = getScrollY() + mDownY - moveY;
//                if (move < -measuredHeight) {
//                    Log.e("HoverScrollview", "move:" + move);
//                    mChildAt.scrollBy(0, -(int) move);
//                } else if (move > 0) {
//                    mChildAt.scrollBy(0, 0);
//                }
//                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        //        postInvalidate();
        invalidate();
        //        final int top = mUpView.getTop();
        //        Log.e("HoverScrollview", "top:" + top);
        //        if (top != 0 && top > 0) {
        //            scrollTo(0, 20);
        //            invalidate();
        //            return true;
        //        } else {
        //            return false;
        //        }
        //        mItView.setOnTouchListener(new View.OnTouchListener() {
        //            @Override
        //            public boolean onTouch(View v, MotionEvent event) {
        //                if(top!=0&&top>0){
        //
        //                    return true;
        //                }else {
        //                    return false;
        //                }
        //            }
        //        });

        //                boolean b = super.onTouchEvent(ev);
        //        Log.e("onTouchEvent", "b:" + b);
        return true;
    }
}
