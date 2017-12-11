package aduio.lib.updata;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.util.List;

import aduio.lib.utils.NetWorkUtils;

/**
 * Created by ${LostDeer} on 2017/10/27.
 * Github:https://github.com/LostDeer
 */

public class UpDateAppManager {
    private static UpDateAppManager sUpDateAppManager = null;
    private String mUrl;//下载地址
    private String mDes;//更新描述
    private int mTag;//更新类型,强制更新,正常更新
    private Context mContext;//全局的
    private AlertDialog.Builder mDialog;
    private ProgressDialog mProgressDialog;
    private static final String ANDROID_SD= Environment.getExternalStorageDirectory()
            .getAbsolutePath()+"/appName/updata/";//安卓路径
    private String mApkName;

    private UpDateAppManager() {
    }

    public static synchronized UpDateAppManager getInstance() {
        if (sUpDateAppManager == null) {
            synchronized (UpDateAppManager.class) {
                if (sUpDateAppManager == null) {
                    sUpDateAppManager = new UpDateAppManager();
                }
            }
        }
        return sUpDateAppManager;
    }

    /**
     * @param url 下载地址
     * @param des 更新描述
     * @param tag 更新类型:2强制更新1正常更新
     */
    public UpDateAppManager upDataApp(Context context, String url, String des, final int tag) {
        this.mContext = context;
        this.mUrl = url;
        this.mDes = des;
        this.mTag = tag;

        mDialog = new AlertDialog.Builder(mContext);
        mDialog.setTitle("应用有新版本了!");
        mDialog.setMessage(des);
        mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mProgressDialog = new ProgressDialog(mContext);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setMax(100);
                switch (tag) {
                    case 1://正常更新
                        mProgressDialog.setCancelable(true);
                        break;
                    case 2://强更
                        mProgressDialog.setCancelable(false);
                        break;
                }
                mProgressDialog.show();
                getPermission();
            }
        });
        switch (tag) {
            case 1://正常更新
                mDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mDialog.setCancelable(true);
                break;
            case 2://强更
                mDialog.setCancelable(false);
                break;
        }
        mDialog.show();
        return this;
    }

    /**
     * 开始下载
     */
    private void getPermission() {
        //获取权限
        if (AndPermission.hasPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            getNetState();
        } else {
            AndPermission.with(mContext)
                    .requestCode(300)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ).callback(new PermissionListener() {
                @Override
                public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                    if (requestCode == 300) {//权限全部允许才走这里
                        getNetState();
                    }
                }

                @Override
                public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                    if (requestCode == 300) {
                        // 第一种：用AndPermission默认的提示语。
                        AndPermission.defaultSettingDialog(((Activity) mContext), 400).show();
                    }
                }
            })
            .start();
        }
    }

    /***
     * 获取网络状态
     */
    private void getNetState() {
        int apnType = NetWorkUtils.getAPNType(mContext);
        if (apnType == 0) {//没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
            Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
        } else if (apnType == 1) {
            downLoadApp();
        } else {
            //弹出选项
            new AlertDialog.Builder(mContext).setTitle("网络提醒!")
                    .setMessage("您现在处于移动网络下,更新会消耗您些许流量")
                    .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            downLoadApp();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener
                    () {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mTag == 2) {
                        dialog.dismiss();
                        System.exit(0);
                    }
                }
            }).show();
        }
    }

    /**
     * 开始下载
     */
    private void downLoadApp() {
        File file = new File(ANDROID_SD);
        if (!file.exists()) {
            file.mkdirs();
        }
        mApkName = "apkName"+getVersionCode(mContext)+".apk";
//        OkHttpUtils.getInstance().get().url(mUrl).build().execute(new FileCallBack(ANDROID_SD,
//                mApkName) {
//            @Override
//            public void inProgress(float progress, long total, int id) {
//                mProgressDialog.setProgress((int) (progress * 100));
//                Log.e("UpDateAppManager", "progress:" + progress);
//            }
//
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                mProgressDialog.dismiss();
//                Toast.makeText(mContext, "下载错误", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onResponse(File response, int id) {
//                mProgressDialog.dismiss();
//                loadApk(response);
//            }
//        });



    }

    /**
     * 安装apk
     * @param path
     */
    private void loadApk(File path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + "" +
                    ".fileprovider", path);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(path),
                    "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
        ((Activity) mContext).finish();
    }

    private   String getVersionCode(Context context) {
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        PackageInfo packageInfo;
        String versionCode = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionName + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
