package aduio.lib.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import aduio.lib.R;

/**
 * Created by LiChaoBo on 2017/4/13.
 */

public class GlideUtils {

    public static void setImage(Context context, String url, ImageView imageView){
        Glide.with(context.getApplicationContext())
                .load(url)
                .thumbnail(0.1f)//加载缩略图
                .skipMemoryCache(true)//不放到内存中
                .placeholder(R.drawable.iv_loading)//加载中的图片
                .error(R.drawable.iv_loading)//加载错误图片
                .crossFade()//淡入淡出效果
                .into(imageView);
    }

    /**
     * 圆头像
     * @param context
     * @param url
     * @param imageView
     */
    public static void SetRoundImage(Context context, String url, ImageView imageView){
        Glide.with(context.getApplicationContext())
                .load(url)
                .skipMemoryCache(true)//不放到内存中
//                .placeholder(R.drawable.iv_loading)//加载中的图片
                .thumbnail(0.1f)//加载缩略图
                .error(R.drawable.iv_loading)//加载错误图片
                .crossFade()//淡入淡出效果
                .bitmapTransform(new CropCircleTransformation(context))
                .into(imageView);
    }

}
