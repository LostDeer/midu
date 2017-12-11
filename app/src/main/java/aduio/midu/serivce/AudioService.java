package aduio.midu.serivce;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import aduio.lib.constant.Constant;
import aduio.midu.IAudio;
import aduio.midu.IAudioProgressListenery;
import aduio.midu.LocalAudioEntity;

/**
 * Created by ${LostDeer} on 2017/11/15.
 * Github:https://github.com/LostDeer
 */

public class AudioService extends Service {
    private MediaPlayer mMediaPlayer;
    private Timer mTimer;
    private List<LocalAudioEntity> mLocalAudioEntities;

    @Override
    public void onCreate() {
        super.onCreate();
        //1.创建对象
        mMediaPlayer = new MediaPlayer();
        initData();
    }

    private void initData() {
        String path = Constant.ANDROID_SD + "/miduCache";
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();//有多少个文件
            if (files != null && files.length > 0) {
                mLocalAudioEntities = new ArrayList<>();
                for (File fileEntity : files) {
                    String name = fileEntity.getName();
                    if (name.endsWith(".mp3")) {
                        LocalAudioEntity localAudioEntity = new LocalAudioEntity();
                        localAudioEntity.setName(name);
                        localAudioEntity.setPath(fileEntity.getAbsolutePath());
                        mLocalAudioEntities.add(localAudioEntity);
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        return new AudioBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timerCancel();
    }
    private String mPath;
    private int mPlayState;//0 未有任何操作 1正在播放  2暂停  3播放完成
    private IAudioProgressListenery mIAudioProgressListenery;
    class AudioBinder extends IAudio.Stub {

        @Override
        public void startAudio(String path, IAudioProgressListenery seekBar) throws RemoteException {
            callStartAudio(path, seekBar);

        }

        @Override
        public void progressAudio(IAudioProgressListenery seekBar) throws RemoteException {
            mIAudioProgressListenery=seekBar;
            if(mPlayState==1){
                updataSeekBar(seekBar);
            }else if(mPlayState==2){
                seekBar.onDuration(mMediaPlayer.getDuration());
                seekBar.onProgress(mMediaPlayer.getCurrentPosition());
            }
        }

        @Override
        public void pauseAudio() throws RemoteException {
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
                mPlayState=1;
                updataSeekBar(mIAudioProgressListenery);
                Log.e("MusicBinder", "重新播放");
            } else {
                mMediaPlayer.pause();
                timerCancel();
                mPlayState=2;
            }
        }

        @Override
        public void stopAudio() throws RemoteException {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                timerCancel();
                mPlayState=3;
            }
        }

        @Override
        public void seekTo(int progress) throws RemoteException {
            mMediaPlayer.seekTo(progress);
            mIAudioProgressListenery.onProgress(progress);
        }

        @Override
        public int playState() throws RemoteException {
            return mPlayState;
        }

        @Override
        public LocalAudioEntity currentPlay() throws RemoteException {
            if(mPath!=null){
                if(mLocalAudioEntities!=null&&!mLocalAudioEntities.isEmpty()){
                    for(LocalAudioEntity localAudioEntity:mLocalAudioEntities){
                        String path = localAudioEntity.getPath();
                        if(path.equals(mPath)){
                            return localAudioEntity;
                        }
                    }
                }
            }
            return null;
        }
    }

    private void timerCancel() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    private void callStartAudio(final String path, final IAudioProgressListenery seekBar) {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            try {
                //2.设置播放路径,本地和网络路径都可以
                mMediaPlayer.setDataSource(path);
                //3.准备播放
                if(!path.startsWith("http")){
                    mMediaPlayer.prepare();
                }else {
                    mMediaPlayer.prepareAsync();
                }
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        Log.e("AudioService", "播放成功");
                        //4.开始播放
                        mp.start();
                        mPlayState=1;
                        mPath=path;
                        try {
                            if (seekBar != null) {
                                int duration = mp.getDuration();//中长度
                                seekBar.onDuration(duration);
                                //5.更新进度条
                                updataSeekBar(seekBar);
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mPlayState=3;
                        timerCancel();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updataSeekBar(final IAudioProgressListenery seekBar) {
        mTimer = new Timer();
        mTimer.purge();
        try {
            seekBar.onDuration(mMediaPlayer.getDuration());
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        int currentPosition = mMediaPlayer.getCurrentPosition();
                        Log.e("AudioService", "currentPosition:" + currentPosition);
                        seekBar.onProgress(currentPosition);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }, 100, 1000);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
