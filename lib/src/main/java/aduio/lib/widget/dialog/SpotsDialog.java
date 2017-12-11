package aduio.lib.widget.dialog;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import aduio.lib.R;


/**
 * Created by LiChaoBo on 2017/7/21.
 */

public class SpotsDialog extends AlertDialog {

    private ImageView mYun;
    private ImageView mNiao;
    private AnimatorSet mAnimatorSet;

    public SpotsDialog(Context context) {
        super(context);
    }

    protected SpotsDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SpotsDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);

        mYun = (ImageView) findViewById(R.id.yun);
        mNiao = (ImageView) findViewById(R.id.niao);

        ObjectAnimator objectAnimatorA= ObjectAnimator.ofFloat(mYun,"Alpha",0.3f,1,0.3f);
        objectAnimatorA.setRepeatMode(ValueAnimator.RESTART);
        objectAnimatorA.setDuration(2000);
        objectAnimatorA.setRepeatCount(-1);//重复执行

        ObjectAnimator objectAnimatorB= ObjectAnimator.ofFloat(mYun,"TranslationX",80,-200);
        objectAnimatorB.setRepeatMode(ValueAnimator.RESTART);
        objectAnimatorB.setDuration(2000);
        objectAnimatorB.setRepeatCount(-1);

        ObjectAnimator animatorC = ObjectAnimator.ofFloat(mNiao, "TranslationY", -15,0, 15);
        animatorC.setRepeatCount(-1);
        animatorC.setDuration(500);
        //        animatorC.setInterpolator(new LinearInterpolator());
        //        animatorC.setInterpolator(new AccelerateInterpolator(2f));
        //        animatorC.setInterpolator(new LinearInterpolator());
        animatorC.setRepeatMode(ValueAnimator.REVERSE);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(objectAnimatorA,objectAnimatorB,animatorC);
        mAnimatorSet.start();

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(objectAnimatorA,objectAnimatorB,animatorC);
        mAnimatorSet.start();

    }

}
