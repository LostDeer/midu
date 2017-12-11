package aduio.midu.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import aduio.lib.utils.SPrefaceUtil;
import aduio.midu.LocalAudioEntity;

/**
 * Created by ${LostDeer} on 2017/11/16.
 * Github:https://github.com/LostDeer
 */

public class GsonUtil {
    private static GsonUtil sGsonUtil = null;
    private final Gson mGson;

    private GsonUtil() {
        mGson = new Gson();
    }

    public static synchronized GsonUtil getInstance() {
        if (sGsonUtil == null) {
            synchronized (GsonUtil.class) {
                if (sGsonUtil == null) {
                    sGsonUtil = new GsonUtil();
                }
            }
        }
        return sGsonUtil;
    }

    public void saveAudioEntity(Context context,LocalAudioEntity entitie){
        ArrayList<LocalAudioEntity> entity = getAudioEntity(context);
        if(entity!=null&&!entity.isEmpty()){
            boolean contains = entity.contains(entitie);
            if(!contains){
                entity.add(entitie);
            }else {
                int progress=-1;
                for (int i = 0; i < entity.size(); i++) {
                    LocalAudioEntity audioEntity = entity.get(i);
                    String name = audioEntity.getName();
                    if(name.equals(entitie.getName())){
                        progress=i;
                    }
                }
                if(progress!=-1){
                    entity.set(progress,entitie);//替换元素坐标
                }
            }
            String json = mGson.toJson(entity, ArrayList.class);
            SPrefaceUtil.saveString(context,aduio.midu.constant.Constant.AUDIO_LIST,json);
        }else {
            entity=new ArrayList<>();
            entity.add(entitie);
            String json = mGson.toJson(entity, ArrayList.class);
            SPrefaceUtil.saveString(context,aduio.midu.constant.Constant.AUDIO_LIST,json);
        }
    }

    public ArrayList<LocalAudioEntity> getAudioEntity(Context context){
        String json = SPrefaceUtil.getString(context, aduio.midu.constant.Constant.AUDIO_LIST, null);
        ArrayList<LocalAudioEntity> list = fromJsonList(json, LocalAudioEntity.class);
        return list;
    }

    private  <T> ArrayList<T> fromJsonList(String json, Class<T> cls) {
        ArrayList<T> mList = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem : array){
            mList.add(mGson.fromJson(elem, cls));
        }
        return mList;
    }
}
