package aduio.midu.serivce;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import aduio.lib.entitys.MessageEvent;
import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 * Created by ${LostDeer} on 2017/12/6.
 * Github:https://github.com/LostDeer
 */

public class LocaltionService extends IntentService {
    public LocaltionService() {
        super("LocaltionService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e("LocaltionService", "aduio.midu.serivce.LocaltionService");
//        SystemClock.sleep(3000);
        HermesEventBus.getDefault().post(new MessageEvent("tag","LocaltionService"));
//        stopSelf();
//        Intent intent1 = new Intent();
//        intent1.setAction("aduio.midu.serivce.LocaltionService");
//        intent1.putExtra("tag","LocaltionService");
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
    }
}
