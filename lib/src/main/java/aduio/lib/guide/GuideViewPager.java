package aduio.lib.guide;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.List;

import aduio.lib.R;

/**
 * Created by LiChaoBo on 2017/6/22.
 */

public class GuideViewPager extends FrameLayout {
    private int mIndicatorUnSelectColor;
    private int mIndicatorCount;
    //    private Paint mPaint = new Paint();
    //    private Paint mIndicatorPaint = new Paint();
    private int mIndicatorSelectColor;
    private ViewPager mViewPager;
    private LinearLayout mLinearLayout;
    private View mView;
    private View mChildAt;

    public int getIndicatorUnSelectColor() {
        return mIndicatorUnSelectColor;
    }

    public void setIndicatorUnSelectColor(int indicatorUnSelectColor) {
        mIndicatorUnSelectColor = indicatorUnSelectColor;
        initView();
    }

    public int getIndicatorCount() {
        return mIndicatorCount;
    }

    public void setIndicatorCount(int indicatorCount) {
        mIndicatorCount = indicatorCount;
        initView();
    }

    public int getIndicatorSelectColor() {
        return mIndicatorSelectColor;
    }

    public void setIndicatorSelectColor(int indicatorSelectColor) {
        mIndicatorSelectColor = indicatorSelectColor;
        initView();
    }

    public float getIndicatorSizi() {
        return mIndicatorSizi;
    }

    public void setIndicatorSizi(float indicatorSizi) {
        mIndicatorSizi = indicatorSizi;
        initView();
    }

    public float getIndicatorPadding() {
        return mIndicatorPadding;
    }

    public void setIndicatorPadding(float indicatorPadding) {
        mIndicatorPadding = indicatorPadding;
        initView();
    }

    public int getIndicatorBottom() {
        return mIndicatorBottom;
    }

    public void setIndicatorBottom(int indicatorBottom) {
        mIndicatorBottom = indicatorBottom;
        initView();
    }

    private int mLinearLayoutMeasuredWidth;
    private float mIndicatorSizi;
    private float mIndicatorPadding;
    private int mIndicatorBottom;

    public GuideViewPager(Context context) {
        this(context, null);
    }

    public GuideViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRes(attrs);
        initView();
    }

    private void initView() {
        removeAllViews();
        mViewPager = new ViewPager(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mViewPager.setLayoutParams(params);

        addView(mViewPager);

        mLinearLayout = new LinearLayout(getContext());
        LayoutParams linearparams = new LayoutParams(LayoutParams
                .MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        linearparams.gravity = Gravity.BOTTOM | Gravity.CENTER;
        linearparams.bottomMargin = (int) mIndicatorBottom;

        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setGravity(Gravity.CENTER);
        mLinearLayout.setLayoutParams(linearparams);

        addView(mLinearLayout);

        GradientDrawable shape = new GradientDrawable();//动态设置shape
        shape.setShape(GradientDrawable.OVAL);//圆形
        //设置颜色
        shape.setColor(mIndicatorUnSelectColor);

        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams((int) mIndicatorSizi,
                (int) mIndicatorSizi);
        for (int i = 0; i < mIndicatorCount; i++) {
            View view = new View(getContext());
            viewParams.gravity = Gravity.CENTER;
            if (i != 0) {
                viewParams.leftMargin = (int) mIndicatorPadding;
            }
            view.setBackground(shape);
            view.setAlpha(0.4f);
            view.setLayoutParams(viewParams);
            mLinearLayout.addView(view);
        }

        GradientDrawable shapeView = new GradientDrawable();
        shapeView.setShape(GradientDrawable.OVAL);//圆形
        shapeView.setColor(mIndicatorSelectColor);

        LayoutParams vparams = new LayoutParams((int) mIndicatorSizi, (int) mIndicatorSizi);
        vparams.gravity = Gravity.BOTTOM;
        mView = new View(getContext());
        mView.setBackground(shapeView);
        mView.setAlpha(0.8f);
        mView.setLayoutParams(vparams);
        addView(mView);

        mChildAt = mLinearLayout.getChildAt(0);
        mChildAt.post(new Runnable() {
            @Override
            public void run() {
                int left = mChildAt.getLeft();
                //        int measuredWidth = childAt.getMeasuredWidth();
                LayoutParams layoutParams = (LayoutParams) mView.getLayoutParams();
                layoutParams.setMargins(left, 0, 0, (int) mIndicatorBottom);
                mView.requestLayout();
            }
        });


    }


    //    public void setAdapter(ArrayList<Integer> adapter) {
    //        ArrayList<ImageView> imageViews = new ArrayList<>();
    //        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
    //                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    //        for (int i = 0; i < adapter.size(); i++) {
    //            ImageView imageView = new ImageView(getContext());
    //            imageView.setLayoutParams(params);
    //            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    //            imageView.setImageResource(adapter.get(i));
    //            imageViews.add(imageView);
    //        }
    //        mViewPager.setAdapter(new GuidePager(imageViews));
    //        setViewPagerListener();
    //    }

    private void setViewPagerListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mViewPagerChangeListener != null) {
                    mViewPagerChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
                //线起始的位置
                int startOffsetX = position * mLinearLayoutMeasuredWidth / mIndicatorCount;
                //手指挪动的距离根据有几个tab换算成线移动的距离
                float indicatoroffsetX = positionOffset / mIndicatorCount * mLinearLayoutMeasuredWidth;
                //在起始位置的基础上进行移动
                mView.setTranslationX(indicatoroffsetX + startOffsetX);
            }

            @Override
            public void onPageSelected(int position) {
                if (mViewPagerChangeListener != null) {
                    mViewPagerChangeListener.onPageSelected(position);
                }
                if (position == 3) {
                    mLinearLayout.setVisibility(GONE);
                    mView.setVisibility(GONE);
                } else {
                    mLinearLayout.setVisibility(VISIBLE);
                    mView.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mViewPagerChangeListener != null) {
                    mViewPagerChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
    }

    public void setViewPagerChangeListener(AddViewPagerChangeListener guidePagerSetAdapter) {
        mViewPagerChangeListener = guidePagerSetAdapter;
    }

    private AddViewPagerChangeListener mViewPagerChangeListener;

    public void setAdapter(List<View> views) {
        mViewPager.setAdapter(new GuidePager(views));
        setViewPagerListener();
    }

    /**
     * viewpager滑动监听
     */
    interface AddViewPagerChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }

    private boolean isOne;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View childAt = mLinearLayout.getChildAt(0);
        int left = childAt.getLeft();
        //        int measuredWidth = childAt.getMeasuredWidth();
        //        LayoutParams layoutParams = (LayoutParams) mView.getLayoutParams();
        //        layoutParams.setMargins(left, 0, 0, (int) mIndicatorBottom);
        //        mView.requestLayout();
        if (left != 0 && !isOne) {
            //点的宽度*个数+点之间的间距*点的个数=LinearLayout的宽度
            mLinearLayoutMeasuredWidth = (int) (mIndicatorSizi * mIndicatorCount + mIndicatorPadding * mIndicatorCount);
            //            int childCount = mLinearLayout.getChildCount();
            //            for (int i = 0; i < childCount; i++) {
            //                View childAt1 = mLinearLayout.getChildAt(i);
            //                mLinearLayoutMeasuredWidth += childAt1.getLeft();
            //            }
            //            //总长度减去,左边的距离,减去控件本身的大小
            //            mLinearLayoutMeasuredWidth = mLinearLayoutMeasuredWidth - childCount * left - childCount * measuredWidth;
            isOne = true;
        }
    }

    private void initRes(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.GuideViewPager);
        mIndicatorUnSelectColor = typedArray.getColor(R.styleable.GuideViewPager_indicatorUnSelect,
                getResources().getColor(R.color.colorAccent));

        mIndicatorSelectColor = typedArray.getColor(R.styleable.GuideViewPager_indicatorSelect,
                getResources().getColor(R.color.colorAccent));

        mIndicatorCount = typedArray.getInteger(R.styleable.GuideViewPager_indicatorCount, 5);

        mIndicatorSizi = typedArray.getDimension(R.styleable.GuideViewPager_indicatorSizi, 20);

        mIndicatorPadding = typedArray.getDimension(R.styleable.GuideViewPager_indicatorPadding, 20);

        mIndicatorBottom = (int) typedArray.getDimension(R.styleable.GuideViewPager_indicatorBottom,
                100);

        typedArray.recycle();

        //        mPaint.setAntiAlias(true);//抗锯齿
        //        mPaint.setDither(true);//防抖动
        //        mPaint.setColor(mIndicatorColor);//画笔颜色
        //
        //        mIndicatorPaint.setAntiAlias(true);//抗锯齿
        //        mIndicatorPaint.setDither(true);//防抖动
        //        mIndicatorPaint.setColor(mIndicatorPaintColor);//画笔颜色
    }

    class GuidePager extends PagerAdapter {
        //        private ArrayList<ImageView> mImageViews;
        private List<View> mViews;
        //        public GuidePager(ArrayList<ImageView> imageViews) {
        //            this.mImageViews=imageViews;
        //        }

        public GuidePager(List<View> views) {
            this.mViews = views;
        }

        //        @Override
        //        public int getCount() {
        //            return mImageViews==null?mViews.size():mImageViews.size();
        //        }
        @Override
        public int getCount() {
            return mViews.size();
        }

        // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(mViews.get(position));
        }
        //        @Override
        //        public void destroyItem(ViewGroup view, int position, Object object) {
        //            view.removeView(mImageViews==null?mViews.get(position):mImageViews.get(position));
        //        }

        // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            view.addView(mViews.get(position));
            return mViews.get(position);
        }
        //        @Override
        //        public Object instantiateItem(ViewGroup view, int position) {
        //            view.addView(mImageViews==null?mViews.get(position):mImageViews.get(position));
        //            return mImageViews==null?mViews.get(position):mImageViews.get(position);
        //        }
    }

}
