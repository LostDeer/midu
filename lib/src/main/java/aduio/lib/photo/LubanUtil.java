package aduio.lib.photo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by LiChaoBo on 2017/9/25.
 * 图片压缩
 */

public class LubanUtil {
    private ProgressDialog mProgressDialog;

    private LubanUtil() {
    }

    private static LubanUtil sLubanUtil = null;
    private String mPath;

    public static synchronized LubanUtil getInstance() {
        if (sLubanUtil == null) {
            synchronized (LubanUtil.class) {
                if (sLubanUtil == null) {
                    sLubanUtil = new LubanUtil();
                }
            }
        }
        return sLubanUtil;
    }


    public LubanUtil setImage(final Context context, String path) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("图片正在处理中..");
        mProgressDialog.setCancelable(false);
        Luban.with(context)
                .load(path)                                   //
                // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                .setTargetDir(getPath())                        // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        mProgressDialog.show();
                    }

                    @Override
                    public void onSuccess(File file) {
                        LubanUtil.this.mPath = file.getAbsolutePath();
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        Log.e("LubanUtil", "压缩后 size:" + file.length() / 1024);
                        mProgressDialog.dismiss();
                        if (mOnCompressListener != null) {
                            mOnCompressListener.onSuccess(file);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        mProgressDialog.dismiss();
                        if (mOnCompressListener != null) {
                            mOnCompressListener.onError(e);
                        }
                    }
                }).launch();    //启动压缩
        return this;
    }

    /**
     * 图片裁剪
     * @param context
     * @param path
     * @param code
     * @return
     */
    public LubanUtil tailoring(Context context,String path,int code) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.parse(path), "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        ((Activity)context).startActivityForResult(intent, code);
        return this;
    }

    private String getPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public String getCachePath() {
        return getPath() + "/" + mPath;
    }

    public void setOnCompressListener(onCompressListener onCompressListener) {
        mOnCompressListener = onCompressListener;
    }

    private onCompressListener mOnCompressListener;

    public interface onCompressListener {
        //输入完毕
        void onSuccess(File file);

        //取消
        void onError(Throwable e);
    }
}
