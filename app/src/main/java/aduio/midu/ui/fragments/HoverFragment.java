package aduio.midu.ui.fragments;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aduio.lib.ui.BaseFragment;
import aduio.midu.R;
import aduio.midu.widget.RegulatorView;
import butterknife.BindView;

/**
 * Created by ${LostDeer} on 2017/11/21.
 * Github:https://github.com/LostDeer
 */

public class HoverFragment extends BaseFragment {
    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @BindView(R.id.rv_regulator)
    RegulatorView mRvRegulator;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_hover, container, false);
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initData() {
        //1
        Message message = Message.obtain();
        message.what = 1;//标识
        mHandler.sendMessage(message);
        //2
        Message message2 = Message.obtain(mHandler);
        message2.what = 2;
        message2.obj = "12312313";
        message2.sendToTarget();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

}
