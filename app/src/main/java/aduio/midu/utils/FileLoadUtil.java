package aduio.midu.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import org.greenrobot.eventbus.EventBus;

import aduio.lib.entitys.MessageEvent;
import aduio.midu.LocalAudioEntity;
import aduio.midu.constant.Constant;

/**
 * Created by ${LostDeer} on 2017/11/16.
 * Github:https://github.com/LostDeer
 */

public class FileLoadUtil {
    Handler mHandler= new Handler(Looper.getMainLooper()){};
    private static FileLoadUtil sFileLoadUtil = null;
    private String mPath;
    private FileLoadUtil() {
    }

    public static synchronized FileLoadUtil getInstance() {
        if (sFileLoadUtil == null) {
            synchronized (FileLoadUtil.class) {
                if (sFileLoadUtil == null) {
                    sFileLoadUtil = new FileLoadUtil();
                }
            }
        }
        return sFileLoadUtil;
    }

    public void fileDownLoad(final Context context, final LocalAudioEntity audioEntity){
        mPath=Constant.AUDIO_DOWNLOADPATH+"/"+audioEntity.getName();
        FileDownloader.getImpl().create(audioEntity.getPath()).setPath(mPath)
                .setListener(new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                Log.e("DownAudioListActivity", "soFarBytes:" + soFarBytes);
                Log.e("DownAudioListActivity", "totalBytes:" + totalBytes);
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                LocalAudioEntity entity=new LocalAudioEntity();
                entity.setName(audioEntity.getName());
                entity.setPath(mPath);
                GsonUtil.getInstance().saveAudioEntity(context.getApplicationContext(),entity);
                EventBus.getDefault().post(new MessageEvent(Constant.AUDIO_DOWNLAODCOMPLETED,null));
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                Toast.makeText(context.getApplicationContext(), "下载失败,请检查网络!", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            protected void warn(BaseDownloadTask task) {

            }
        }).start();
    }
}
