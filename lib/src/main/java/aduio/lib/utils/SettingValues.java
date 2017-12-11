package aduio.lib.utils;

import android.content.Context;

import com.google.gson.Gson;

import java.io.Serializable;

import aduio.lib.constant.Constant;

public class SettingValues implements Serializable {
    private static SettingValues instance;
    //要保存的值开始

    public String token = "";//用户唯一标识
    public String user_id = "";//用户id
    public String nikeName = "";//昵称
    public String nikeIcon = "";//头像
    public String phoneNum = "";//手机号码
    public String qq_Nike="";//qq昵称
    public String weiXin_Nike="";//weixin昵称
    public boolean isLogin = false;
    public boolean isAuth = false;//是否认证


    public static SettingValues getInstance() {
        if (instance == null) {
            Object object = FileUtil.getObjectFromFile(FinalValues.SettingPath,
                    FinalValues.SettingName);
            if (object == null) {
                //判断存储区域内是否有
                instance = new SettingValues();
            } else {
                instance = (SettingValues) object;
            }
        }
        return instance;
    }

    /**
     * 保存设置到文件中
     */
    public void saveInstance() {
        FileUtil.saveObjectToFile(FinalValues.SettingPath,
                FinalValues.SettingName, this);
    }

    /**
     * 清空
     */
    public void clear() {
        token = "";
        user_id = "";
        nikeName = "";
        nikeIcon = "";
        phoneNum = "";
        qq_Nike="";
        weiXin_Nike="";
        isLogin = false;
        isAuth = false;
        saveInstance();
    }

    /**
     * 是否登录
     *
     * @return
     */
    public boolean getIsLogin(Context context) {
        String json = SPrefaceUtil.getString(context, Constant.SPKEY, null);
        if (json == null) {
            return false;
        } else {
            SettingValues settingValues = new Gson().fromJson(json, SettingValues.class);
            if (settingValues == null) {
                return false;
            } else {
                if (settingValues.token != null && !StringUtils.isStringEmpty(settingValues.token)) {
                    return true;
                }
            }
        }
        return false;
    }

}
