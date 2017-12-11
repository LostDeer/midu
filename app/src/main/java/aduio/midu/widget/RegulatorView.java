package aduio.midu.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import aduio.midu.utils.DensityUtil;

/**
 * Created by ${LostDeer} on 2017/12/5.
 * Github:https://github.com/LostDeer
 */

public class RegulatorView extends View {

    private Paint mPaint;
    private float mCenterX;
    private float mCenterY;
    private int mCurAngle = 120;//旋转角度

    public RegulatorView(Context context) {
        this(context, null);
    }

    public RegulatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RegulatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        //        mCurAngle = (360-mThreeRingAngle)/2 + 90;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        setLayerType(LAYER_TYPE_SOFTWARE, null); //没办法了，要关闭硬件加速
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCenterX = getMeasuredWidth() / 2f;
        mCenterY = getMeasuredHeight() / 2f;

        drawAllCircle(canvas);

        drawPointer(canvas);

        drawText(canvas);
    }

    private int mSecondCircleColor = 0xfff5f5f5;
    private int mSecondCircleWidth = (int) DensityUtil.dptopx(getContext(), 24f);
    private int mGap1Width = (int) DensityUtil.dptopx(getContext(), 24f);
    private int mThreeCircleWidth = (int) DensityUtil.dptopx(getContext(), 20f);
    private float mSecondCircleRadius;
    private int mFirstCircleColor = 0xfff0f0f0;
    private float mSecondScale = 0.25f;
    private float mThreeCircleRadius;
    private int mThreeRingAngle = 300;
    private int mThreeCircleColor = 0xffe0e0e0;

    public void setCurShadowRadius(float curShadowRadius) {
        mCurShadowRadius = curShadowRadius;
        invalidate();
    }

    private float mCurShadowRadius = DensityUtil.dptopx(getContext(), 10f);
    private int mSecondCircleShadowColor = 0x66ff0000;
    private int[] mColors = new int[]{0xff2ab62d, 0xff2ab62d, 0xff56f318, 0xff8ff318, 0xffd2f318, 0xfff3b318, 0xfff36a18, 0xffe73046, 0xffff0000, 0xffff0000};

    private void drawAllCircle(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mSecondCircleColor);
        mPaint.setShadowLayer(mCurShadowRadius, 0, 0, mSecondCircleShadowColor);//阴影
        mPaint.setStrokeWidth(mSecondCircleWidth);
        //先画第二个圆
        mSecondCircleRadius = Math.min(getMeasuredWidth(), getMeasuredHeight()) * mSecondScale;
        canvas.drawCircle(mCenterX, mCenterY, mSecondCircleRadius, mPaint);

        //绘制第一个圆(实心圆)
        mPaint.setShadowLayer(0, 0, 0, Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(0);
        mPaint.setColor(mFirstCircleColor);
        canvas.drawCircle(mCenterX, mCenterY, mSecondCircleRadius - mSecondCircleWidth / 2, mPaint);

        //绘制第三个圆(外圆环)
        mThreeCircleRadius = Math.min(getMeasuredWidth(), getMeasuredHeight()) * mSecondScale +
                mGap1Width + mSecondCircleWidth / 2f;
        RectF rectF = new RectF();
        rectF.left = mCenterX - mThreeCircleRadius;
        rectF.right = mCenterX + mThreeCircleRadius;
        rectF.top = mCenterY - mThreeCircleRadius;
        rectF.bottom = mCenterY + mThreeCircleRadius;

        int startAngle = (360 - mThreeRingAngle) / 2 + 90;
        mPaint.setStrokeWidth(mThreeCircleWidth);
        mPaint.setColor(mThreeCircleColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//圆角
        //        RectF rectF1 = new RectF(50, 50, 150, 150);
        //        canvas.drawArc(rectF1,120,300,false,mPaint);
        canvas.drawArc(rectF, startAngle, mThreeRingAngle, false, mPaint);

        //绘制颜色
        if (mColors != null && mColors.length != 0) {
            canvas.save();//保存之前的绘图

            canvas.rotate(90, mCenterX, mCenterY);
            if (mColors.length == 1) {
                mPaint.setColor(mColors[0]);
            } else if (mColors.length > 1) {
                //view有三种常用渐变方法这个一看就知道(1. LinearGradient 线性渐变 2.RadialGradient 辐射渐变 3.SweepGradient 环形渐变)
                SweepGradient sweepGradient = new SweepGradient(mCenterX, mCenterY, mColors, null);
                mPaint.setShader(sweepGradient);
            }
            canvas.drawArc(rectF, (360 - mThreeRingAngle) / 2, mThreeRingAngle, false, mPaint);

            mPaint.setShader(null);
            canvas.restore();
        }
    }

    private int mPointerColor = 0xffff0000;
    private float mPointScale = 0.5f;     //长度
    private float mPointerWidth = DensityUtil.dptopx(getContext(), 4f);

    /**
     * 绘制指针
     *
     * @param canvas
     */
    private void drawPointer(Canvas canvas) {
        mPointerColor = mColors[1];
        mPaint.setColor(mPointerColor);
        mPaint.setStrokeWidth(mPointerWidth);
        canvas.save();
        canvas.rotate(mCurAngle, mCenterX, mCenterY);
        canvas.drawLine(mCenterX + mSecondCircleRadius - mSecondCircleWidth * mPointScale / 2f, mCenterY, mCenterX + mSecondCircleRadius + mSecondCircleWidth * mPointScale / 2f, mCenterY, mPaint);
        canvas.restore();
    }


    private void drawText(Canvas canvas) {
        mPaint.setColor(0xff333333);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(DensityUtil.sptopx(getContext(), 46f));
        mPaint.setTextAlign(Paint.Align.CENTER);
        float heigth = Math.abs(mPaint.descent() + mPaint.ascent());//文本的底部-高度=文本的高度
        float weigth = mPaint.measureText("10");
        canvas.drawText("10", mCenterX, mCenterY + heigth / 2, mPaint);

        mPaint.setColor(0xff333333);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(DensityUtil.sptopx(getContext(), 20f));
        mPaint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText("o", mCenterX + weigth / 2 + 15, mCenterY - heigth / 2 + Math.abs(mPaint.descent()
                + mPaint.ascent()) / 2, mPaint);
    }

    private ObjectAnimator mBacklightAnim;
    private float mSecondCircleShadowRadius = DensityUtil.dptopx(getContext(), 10f);

    private boolean isOpenBacklightAnim = true;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isOpenBacklightAnim) {
            onStartBacklightAnim();
        }
    }

    /**
     * 开启背光灯动画
     */
    private void onStartBacklightAnim() {
        if (mBacklightAnim != null && mBacklightAnim.isRunning()) {
            return;
        }

        mBacklightAnim = ObjectAnimator.ofFloat(this, "curShadowRadius", 0, mSecondCircleShadowRadius);
        mBacklightAnim.setDuration(3000);
        mBacklightAnim.setRepeatCount(ValueAnimator.INFINITE);
        mBacklightAnim.setRepeatMode(ValueAnimator.REVERSE);
        mBacklightAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCurShadowRadius = mSecondCircleShadowRadius;
            }
        });
        mBacklightAnim.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //防止内存泄漏先关闭动画
        onEndBacklightAnim();
        //        onStopAutoFlingAnim();
    }

    private void onEndBacklightAnim() {
        if (mBacklightAnim != null && mBacklightAnim.isRunning()) {
            mBacklightAnim.removeAllListeners();
            mBacklightAnim.cancel();
            mBacklightAnim = null;
        }
    }

    private void onStopAutoFlingAnim() {
        if (mAutoFlingAnim != null && mAutoFlingAnim.isRunning()) {
            mAutoFlingAnim.cancel();
            mAutoFlingAnim.removeAllUpdateListeners();
            mAutoFlingAnim = null;
        }
    }

    private boolean isForbidSlide = false;
    private ValueAnimator mAutoFlingAnim;
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (isForbidSlide)
//            return false;
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                onStopAutoFlingAnim();  //有动画立刻停止
//                boolean isTakeOver = isTakeOverTouch(event.getX(), event.getY()); //第一次单机在圆环上才接管触摸
//                if (isTakeOver) {
//                    mPreX = (int) event.getX();
//                    mPreY = (int) event.getY();
//                    refreshAngle(mPreX, mPreY, true);
//                    return true;
//                } else {
//                    return false;
//                }
//            case MotionEvent.ACTION_MOVE:
//                refreshAngle((int) event.getX(), (int) event.getY(), false);
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//        return true;
//    }

    /**
     * 判断是否接管触摸 两种情况
     */
    private boolean isTakeOverTouch(float downX,float downY) {

        //加个0.5防止有些人眼神或手不好点不到圆弧上
        float minRadius = mThreeCircleRadius - mThreeCircleWidth/2f - 0.5f;
        float maxRadius = mThreeCircleRadius + mThreeCircleWidth/2f + 0.5f;

        //到按下点到圆心的距离
        float distanceCircle = (float) Math.abs(Math.sqrt((downX-mCenterX)*(downX-mCenterX)+(downY-mCenterY)*(downY-mCenterY)));
        if (distanceCircle >= minRadius && distanceCircle <= maxRadius) {
            if (mThreeRingAngle > 180 && downY > mCenterY) {
                float angle = (float) (Math.atan(Math.abs(downX-mCenterX)/Math.abs(downY-mCenterY))*180/Math.PI);
                if ((360-mThreeRingAngle)/2f > angle) {
                    return false;
                }
            } else if (mThreeRingAngle <= 180) {
                if (downY>mCenterY) {
                    return false;
                } else {
                    float angle = (float) (Math.atan(Math.abs(downX-mCenterX)/Math.abs(downY-mCenterY))*180/Math.PI);
                    if (angle > (360-mThreeRingAngle)/2f) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
