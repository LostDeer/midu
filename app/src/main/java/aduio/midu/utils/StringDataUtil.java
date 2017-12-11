package aduio.midu.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ${LostDeer} on 2017/11/15.
 * Github:https://github.com/LostDeer
 */

public class StringDataUtil {
    public static String getData(int progress){
        SimpleDateFormat format=new SimpleDateFormat("mm:ss");
        Date date=new Date(progress);
        return  format.format(date);
    }
}
