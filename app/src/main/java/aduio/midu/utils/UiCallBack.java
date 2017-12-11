package aduio.midu.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by ${LostDeer} on 2017/12/4.
 * Github:https://github.com/LostDeer
 */

public abstract class UiCallBack<T> implements Callback {
    private Handler mHandler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what){
                case 1://成功
                    Response response = (Response) msg.obj;
                    //转json和code判断
                    onResponse((T) response);
                    break;
                case 2://失败
                    IOException ioException = (IOException) msg.obj;
                    onFailure(ioException);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public abstract void onResponse(T response);

    public abstract void onFailure(IOException ioException);

    @Override
    public void onFailure(Call call, IOException e) {
        Message message = Message.obtain(mHandler);
        message.what=2;
        message.obj=e;
        message.sendToTarget();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Message message = Message.obtain(mHandler);
        message.what=1;
        message.obj=response;
        message.sendToTarget();
    }


}
