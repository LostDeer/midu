package aduio.midu.ui.activitys;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import aduio.lib.constant.Constant;
import aduio.lib.ui.BaseActivity;
import aduio.midu.IAudio;
import aduio.midu.R;
import aduio.midu.entitys.LocalAudioEntity;
import aduio.midu.serivce.AudioService;
import aduio.midu.ui.adapters.AudioAdapter;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${LostDeer} on 2017/11/15.
 * Github:https://github.com/LostDeer
 */

public class DownAudioListActivity extends BaseActivity {
    @BindView(R.id.iv_toolBarBack)
    ImageView mIvToolBarBack;
    @BindView(R.id.tv_toolBarName)
    TextView mTvToolBarName;
    @BindView(R.id.tv_toolbarright)
    TextView mTvToolbarright;
    @BindView(R.id.ll_base)
    LinearLayout mLlBase;
    @BindView(R.id.rv_audioList)
    RecyclerView mRvAudioList;
    private List<LocalAudioEntity> mLocalAudioEntities;
    private Conn mConn;
    private IAudio mIAudio;
    private String mUrl="http://bmob-cdn-14000.b0.upaiyun.com/2017/11/16/54b080374041252f8052d904f985c96e.mp3";
    private String mPath;
    private AudioAdapter mAdapter;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_downaudio);
    }

    @Override
    protected void setListener() {
        initData();
//        downLoad();
    }

    private void downLoad() {

    }

    private void initData() {
        mPath = Constant.ANDROID_SD + "/miduCache";
        File file = new File(mPath);
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
        } else {
            Toast.makeText(mContext, "没有缓存任何音频文件", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    protected void processLogic() {
        mRvAudioList.setHasFixedSize(true);
        mRvAudioList.setLayoutManager(new LinearLayoutManager(this));
        if (mLocalAudioEntities != null && !mLocalAudioEntities.isEmpty()) {
            mAdapter = new AudioAdapter(R.layout.item_audiolist, mLocalAudioEntities);
            mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
            mAdapter.setDuration(500);
            mAdapter.isFirstOnly(true);

            mRvAudioList.setAdapter(mAdapter);

            mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    LocalAudioEntity item = (LocalAudioEntity) adapter.getItem(position);
                    switch (view.getId()){
                      case  R.id.ll_itemview:
                          setOnClickListener(item);
                        break;
                    }
                }
            });
//            mRvAudioList.setAdapter(new AudioListAdapter(mContext, mLocalAudioEntities));
        }
    }


    @OnClick(R.id.iv_toolBarBack)
    public void onViewClicked() {
        finish();
    }

    private LocalAudioEntity mLocalAudioEntity;

    /**
     * item点击事件
     * @param localAudioEntity
     */
    public void setOnClickListener(LocalAudioEntity localAudioEntity) {
        if (null == mLocalAudioEntity) {
            mConn = new Conn();
            bindService(new Intent(mContext, AudioService.class), mConn, BIND_AUTO_CREATE);
        } else {
            String path = mLocalAudioEntity.getPath();
            String path1 = localAudioEntity.getPath();
            if (path.endsWith(path1)) {//跳转进播放页面
                startActivity(new Intent(mContext,AudioActivity.class));
            } else {//直接播放
                try {
                    mIAudio.startAudio(localAudioEntity.getPath(),null);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        this.mLocalAudioEntity = localAudioEntity;
    }

    class Conn implements ServiceConnection {
        /**
         * 链接成功
         *
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("Conn", "链接成功");
            mIAudio = IAudio.Stub.asInterface(service);
            try {
                int state = mIAudio.playState();
                if(state==0){
                    startAudio();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        /**
         * 意外断开链接
         *
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private void startAudio() {
        try {
            mIAudio.startAudio(mLocalAudioEntity.getPath(),null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mConn!=null){
            unbindService(mConn);
        }
    }
}
