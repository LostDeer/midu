package aduio.lib.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPrefaceUtil {
    private static SharedPreferences sp;

    /**
     * 保存信息
     *
     * @param key   保存信息的标示
     * @param value 保存信息的值
     */
    public static void saveBoolean(Context context, String key, boolean value) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
        /*Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();*/
    }

    /**
     * 获取保存的信息
     *
     * @param context
     * @param key      保存信息的标示
     * @param defValue 缺省的值
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }


    /**
     * 缓存网络信息
     *
     * @param key   保存信息的标示
     * @param value 保存信息的值
     */
    public static void saveString(Context context, String key, String value) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
		/*Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();*/
    }

    /**
     * 获取缓存的信息
     *
     * @param context
     * @param key      保存信息的标示
     * @param defValue 缺省的值
     * @return
     */
    public static String getString(Context context, String key, String defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }


}
