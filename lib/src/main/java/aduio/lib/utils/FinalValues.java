package aduio.lib.utils;

import android.content.Context;

/**
 * 静态不可改变常量
 * Created by Neo on 2015/12/17 0017.
 */
public final class FinalValues {
    public static final String SettingName = "setting.dat";
    public static String SettingPath = "";
    public static String cachePath = "";

    /**
     * 初始化程序路径
     *
     * @param context
     */
    public static void initPath(Context context) {//防止内存泄漏
        Context applicationContext = context.getApplicationContext();
        cachePath = applicationContext.getExternalCacheDir().getPath();
        SettingPath = applicationContext.getFilesDir().getPath();
    }
}
