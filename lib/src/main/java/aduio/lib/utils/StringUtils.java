package aduio.lib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by hasee on 2017/3/2.
 */
public class StringUtils {
    public static boolean isStringEmpty(String key) {
        if (key == null) {
            return true;
        }
        return key.length() == 0;
    }

    public static String getTargetAmount(double targetAmount) {
        if (targetAmount >= 10000) {
            double money = targetAmount / 10000;
//            String num = String.valueOf(money);
//            if (num.endsWith(".0")) {
                return StringUtils.parseMoneyNoYuan(money) + "万";
//            } else {
//                return "￥ " + money + "万元";
//            }
        } else {
            if(targetAmount==0){
                return "0元";
            }else {
                return StringUtils.parseMoneyNoYuan(targetAmount)+"元";
            }
        }
    }

    public static String parseMoney(double targetAmount) {
        if(targetAmount==0){
            return "￥ 0.00";
        }else {
            DecimalFormat df = new DecimalFormat("￥ ,###,###0.00");
            return df.format(targetAmount);
        }
    }

    public static String parseMoneyWan(double targetAmount) {
        DecimalFormat df = new DecimalFormat(",###,###");
        return df.format(targetAmount)+" 万";
    }

    public static String parseMoneyBalance(double targetAmount) {
        DecimalFormat df = new DecimalFormat("￥ ,###,###.00");
        return df.format(targetAmount);
    }

    public static String parseMoneyNoYuan(double targetAmount) {
        DecimalFormat df = new DecimalFormat(",###,###.00");
        return df.format(targetAmount);
    }

    public static String proportion(BigDecimal targetAmount) {
        DecimalFormat df = new DecimalFormat("0.0000");
        return df.format(targetAmount)+"%";
    }

    public static int getProgress(double progress) {
        int pro = (int) Math.ceil(progress);
        return pro;
    }

    public static String getVersionCode(Context context) {
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

    public static String getAreement(String name){
        int length = name.length();
        String url = name.substring(1, length-1);
        return url;
    }

    public static String getPhone(){
        StringBuffer phone = new StringBuffer();
        String phoneNum = SettingValues.getInstance().phoneNum;
        String start = phoneNum.substring(0, 3);
        String substring = phoneNum.substring(3, phoneNum.length() - 3);
        phone.append(start);
        for (int i = 0; i < substring.length(); i++) {
            phone.append("*");
        }
        String end = phoneNum.substring(phoneNum.length() - 3, phoneNum.length());
        phone.append(end);
        return phone.toString();
    }
}
