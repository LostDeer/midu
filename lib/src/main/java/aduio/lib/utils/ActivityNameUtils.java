package aduio.lib.utils;

import java.util.HashMap;

/**
 * Created by LiChaoBo on 2017/7/12.
 */

public class ActivityNameUtils {
    public static final HashMap<String,String> activityName=new HashMap<>();
    private ActivityNameUtils() {
        activityName.put("WelcomeActivity","");
        activityName.put("GuideActivity","");
        activityName.put("MainActivity","");
    }

    private static ActivityNameUtils sActivityNameUtils = null;

    public static ActivityNameUtils getInstance() {
        if (sActivityNameUtils == null) {
            sActivityNameUtils = new ActivityNameUtils();
        }
        return sActivityNameUtils;
    }
}
