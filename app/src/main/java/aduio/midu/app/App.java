package aduio.midu.app;

import android.app.Application;
import android.content.Intent;

import com.liulishuo.filedownloader.FileDownloader;

import aduio.lib.constant.Constant;
import aduio.lib.utils.SPrefaceUtil;
import aduio.midu.serivce.AudioService;
import aduio.midu.serivce.LocaltionService;
import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 * Created by ${LostDeer} on 2017/11/15.
 * Github:https://github.com/LostDeer
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HermesEventBus.getDefault().init(this);
        initService();
        initIntentService();
        initStatus();
        initDownLoad();
    }

    private void initIntentService() {
        startService(new Intent(this, LocaltionService.class));
    }

    private void initDownLoad() {
        FileDownloader.setupOnApplicationOnCreate(this);
    }

    private void initService() {
        startService(new Intent(this, AudioService.class));
    }

    private void initStatus() {
        SPrefaceUtil.saveString(this, Constant.STATUSBARHEIGHT, getStatusBarHeight() + "");
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
