package aduio.midu.ui.activitys;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import aduio.lib.entitys.MessageEvent;
import aduio.lib.ui.BaseActivity;
import aduio.midu.IAudio;
import aduio.midu.IAudioProgressListenery;
import aduio.midu.LocalAudioEntity;
import aduio.midu.R;
import aduio.midu.constant.Constant;
import aduio.midu.serivce.AudioService;
import aduio.midu.utils.StringDataUtil;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${LostDeer} on 2017/11/15.
 * Github:https://github.com/LostDeer
 */

public class AudioActivity extends BaseActivity {
    @BindView(R.id.iv_toolBarBack)
    ImageView mIvToolBarBack;
    @BindView(R.id.tv_toolBarName)
    TextView mTvToolBarName;
    @BindView(R.id.tv_toolbarright)
    TextView mTvToolbarright;
    @BindView(R.id.ll_base)
    LinearLayout mLlBase;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_start)
    TextView mTvStart;
    @BindView(R.id.tv_end)
    TextView mTvEnd;
    @BindView(R.id.sb_progress)
    SeekBar mSbProgress;
    @BindView(R.id.iv_list)
    ImageView mIvList;
    @BindView(R.id.iv_Top)
    ImageView mIvTop;
    @BindView(R.id.iv_Play)
    ImageView mIvPlay;
    @BindView(R.id.iv_download)
    ImageView mIvDownload;
    @BindView(R.id.iv_next)
    ImageView mIvNext;
    private Conn mConn;
    private IAudio mIAudio;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_audio);
    }

    @Override
    protected void setListener() {
        mConn = new Conn();
        bindService(new Intent(mContext, AudioService.class), mConn, BIND_AUTO_CREATE);
    }

    @Override
    protected void processLogic() {

    }

    private final int mTime = 15 * 1000;//15秒

    @OnClick({R.id.iv_toolBarBack, R.id.iv_list, R.id.iv_Top, R.id.iv_Play, R.id.iv_download, R.id.iv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolBarBack:
                finish();
                break;
            case R.id.iv_list://快退
                try {
                    int playState = mIAudio.playState();
                    if (playState == 1 || playState == 2) {//正在播放或者暂停状态
                        int progress = mSbProgress.getProgress();
                        if (progress >= mTime) {
                            mIAudio.seekTo((progress - mTime));
                        } else {
                            mIAudio.seekTo(0);
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_Top://上一首

                break;
            case R.id.iv_Play:
                try {
                    int state = mIAudio.playState();
                    switch (state) {
                        case 0://没有播放
                            break;
                        case 1://正在播放,所以暂停
                        case 2://在暂停,所以继续播放
                            mIAudio.pauseAudio();
                            break;
                        case 3://重新播放
                            break;
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_download://快进
                try {
                    int playState = mIAudio.playState();
                    if (playState == 1 || playState == 2) {//正在播放或者暂停状态
                        int max = mSbProgress.getMax();
                        int progress = mSbProgress.getProgress();
                        int pro = max - progress;
                        if (pro >= mTime) {
                            mIAudio.seekTo((progress + mTime));
                        } else {
                            mIAudio.seekTo(max);
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_next://下一首

                break;
        }
    }

    private void startMuice(boolean isPaly) {
        if (mIAudio != null && !isPaly) {//点击播放按钮

        } else if (mIAudio != null && isPaly) {
            try {
                mIAudio.progressAudio(new IAudioProgressListenery.Stub() {
                    @Override
                    public void onProgress(final int progress) throws RemoteException {
                        mSbProgress.setProgress(progress);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTvStart.setText(StringDataUtil.getData(progress));
                            }
                        });
                    }

                    @Override
                    public void onDuration(int duration) throws RemoteException {
                        mSbProgress.setMax(duration);
                        mTvEnd.setText(StringDataUtil.getData(duration));
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    class Conn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIAudio = IAudio.Stub.asInterface(service);
            try {
                int state = mIAudio.playState();
                if (state == 1||state==2) {
                    startMuice(true);
                }
                LocalAudioEntity localAudioEntity = mIAudio.currentPlay();
                if (localAudioEntity != null) {
                    mTvName.setText(localAudioEntity.getName());
                }
                mSbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        try {
                            if (fromUser) {
                                mIAudio.seekTo(progress);
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Subscribe
    public void onEventMainThread(MessageEvent messageEntity) {
        super.onEventMainThread(messageEntity);
        String message = messageEntity.getMessage();
        if (message.equals(Constant.AUDIO_DOWNLAODCOMPLETED)) {//下载完成,刷新界面
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConn);
    }
}
