package aduio.midu.utils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by ${LostDeer} on 2017/12/4.
 * Github:https://github.com/LostDeer
 */

public class OkHttpUtil {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpUtil sOkHttpUtil = null;
    private OkHttpClient mOkHttpClient;
    private OkHttpUtil() {
        mOkHttpClient = new OkHttpClient.Builder()
//                                    .addInterceptor(new LoggerInterceptor("okhttp"))
                //                    .sslSocketFactory()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public static synchronized OkHttpUtil getInstance() {
        if (sOkHttpUtil == null) {
            synchronized (OkHttpUtil.class) {
                if (sOkHttpUtil == null) {
                    sOkHttpUtil = new OkHttpUtil();
                }
            }
        }
        return sOkHttpUtil;
    }

    /**
     * post转码加UI线程转换
     * @param url
     * @param map
     * @return
     */
    public Call post(String url, HashMap<String, String> map) {
        String content = postForUtfUrl(map);
//        Log.e("OkHttpUtil", content);
        RequestBody body = RequestBody.create(JSON, content);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization","APPCODE 3c7ed3f77126452a8e5880cbd381143d")
                .post(body)
                .build();
        return mOkHttpClient.newCall(request);
    }

    /**
     * post请求转码
     *
     * @param map
     */
    private String postForUtfUrl(HashMap<String, String> map) {
//        map = SignMapUtils.getSignMap(context, map);
        StringBuffer sb = new StringBuffer();
        //设置表单参数
        for (String key : map.keySet()) {
            //
            sb.append(key + "=" + map.get(key) + "&");
        }
        return sb.toString();
    }

}
