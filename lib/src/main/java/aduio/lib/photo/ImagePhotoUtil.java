package aduio.lib.photo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.util.List;

/**
 * Created by hasee on 2017/10/26.
 * 相机和图库,图片压缩裁剪
 */

public class ImagePhotoUtil {
    public static final int REQUEST_IMAGE = 101;//开启相机或者开启图库
    public final int REQUESTTAILORINGCODE = 13;//裁剪完成的图片
    private static ImagePhotoUtil sCameraPhotoUtil = null;
    private String mSavaPath;
    private String [] mPermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    private static final String SAVED_IMAGE_DIR_PATH=Environment.getExternalStorageDirectory().getPath()
            + "/menglib/camera/";// 拍照路径;

    public static synchronized ImagePhotoUtil getInstance() {
        if (sCameraPhotoUtil == null) {
            synchronized (ImagePhotoUtil.class) {
                if (sCameraPhotoUtil == null) {
                    sCameraPhotoUtil = new ImagePhotoUtil();
                }
            }
        }
        return sCameraPhotoUtil;
    }

    public void openCamera(Context context) {
        if(AndPermission.hasPermission(context,mPermission)){
            startCamera(context);
        }else {
            getPermission(context);
        }
    }

    public String getSavaPath() {
        return mSavaPath;
    }

    /**
     * // 拍照并显示图片
     *  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
        startActivityForResult(intent, REQUEST_CAMERA_1);
       // 拍照后存储并显示图片
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
        Uri photoUri = Uri.fromFile(new File(mFilePath)); // 传递路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);// 更改系统默认存储路径
        startActivityForResult(intent, REQUEST_CAMERA_2);
     * @param context
     */
    private void startCamera(Context context) {
        mSavaPath = SAVED_IMAGE_DIR_PATH + System.currentTimeMillis() + ".png";
        String out_file_path = SAVED_IMAGE_DIR_PATH;
        File file = new File(out_file_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 把文件地址转换成Uri格式
        Uri imageUri ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(context, "com.meng.face.fileprovider",
                    new File(mSavaPath));//通过FileProvider创建一个content类型的Uri
        } else {
            imageUri = Uri.fromFile(new File(mSavaPath));
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        ((Activity)context).startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void getPermission(final Context context) {
        // 如果你不想申请权限组，仅仅想申请某几个权限：
        AndPermission.with(context)
                .requestCode(300)
                .permission(mPermission
                ).callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                            if(requestCode==300){//权限全部允许才走这里
                                startCamera(context);
                            }
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        if(requestCode==300){
                            // 第一种：用AndPermission默认的提示语。
                            AndPermission.defaultSettingDialog(((Activity) context), 400).show();
                        }
                    }
                })
                .start();
    }

    /**
     * 图片压缩和裁剪
     * @param context
     * @param path
     */
    private void comPression(final Context context, String path) {
        //压缩
        LubanUtil.getInstance().setImage(context, path).setOnCompressListener(new LubanUtil.onCompressListener() {
            @Override
            public void onSuccess(File file) {
                String msg = file.getAbsolutePath();
                if (msg.startsWith("/content")) {
                    String path = msg.replace(":", ":/");
                    path = path.substring(1, path.length());
                    //                    Log.e("MainActivity", path);
                    //系统裁剪调用图片库

                    LubanUtil.getInstance().tailoring(context, path,
                            REQUESTTAILORINGCODE);
                } else if (msg.startsWith("/storage/")) {
                    //                            String absolutePath = file.getAbsolutePath();
                    LubanUtil.getInstance().tailoring(context, "file://" + msg, 13);
                } else {
//                    String path = msg.substring(1, msg.length());
                    //                    Log.e("MainActivity", path);
                    String[] split = file.getAbsolutePath().split(":");
                    LubanUtil.getInstance().tailoring(context, "file://" + split[1],
                            REQUESTTAILORINGCODE);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });

    }
}
