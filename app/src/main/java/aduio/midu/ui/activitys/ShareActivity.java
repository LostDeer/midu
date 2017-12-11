package aduio.midu.ui.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import aduio.lib.constant.Constant;
import aduio.midu.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ${LostDeer} on 2017/11/17.
 * Github:https://github.com/LostDeer
 */

public class ShareActivity extends Activity {
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.iv_url)
    ImageView mIvUrl;
    @BindView(R.id.iv_post)
    ImageView mIvPost;
    @BindView(R.id.ll)
    LinearLayout mLl;
    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    @BindView(R.id.post)
    Button mPost;
    private String mContent;
    private  Handler mHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            final String obj = (String) msg.obj;
            Log.e("ShareActivity", obj);
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Message obtain = Message.obtain(mHandler);
                    obtain.obj=obj;
                    obtain.sendToTarget();
                }
            }, 1000);
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        mContent = getIntent().getStringExtra("content");
        mTvContent.setText(mContent);
        Bitmap bitmap = addLogo(generateBitmap(mContent, 180, 180), BitmapFactory.decodeResource(getResources(), R
                .mipmap.ic_launcher_round));
        mIvUrl.setImageBitmap(bitmap);
        mLlContent.setDrawingCacheEnabled(true);

        initMessage();


    }

    private void initMessage() {
        Message obtain = Message.obtain(mHandler);
        obtain.obj=mContent;
        obtain.sendToTarget();
    }

    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap addLogo(Bitmap qrBitmap, Bitmap logoBitmap) {
        int qrBitmapWidth = qrBitmap.getWidth();
        int qrBitmapHeight = qrBitmap.getHeight();
        int logoBitmapWidth = logoBitmap.getWidth();
        int logoBitmapHeight = logoBitmap.getHeight();
        Bitmap blankBitmap = Bitmap.createBitmap(qrBitmapWidth, qrBitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(blankBitmap);
        canvas.drawBitmap(qrBitmap, 0, 0, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        float scaleSize = 1.0f;
        while ((logoBitmapWidth / scaleSize) > (qrBitmapWidth / 5) || (logoBitmapHeight / scaleSize) > (qrBitmapHeight / 5)) {
            scaleSize *= 2;
        }
        float sx = 1.0f / scaleSize;
        canvas.scale(sx, sx, qrBitmapWidth / 2, qrBitmapHeight / 2);
        canvas.drawBitmap(logoBitmap, (qrBitmapWidth - logoBitmapWidth) / 2, (qrBitmapHeight - logoBitmapHeight) / 2, null);
        canvas.restore();
        return blankBitmap;
    }

    @OnClick({R.id.post, R.id.ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.post:
                Bitmap bitmap = mLlContent.getDrawingCache();
                String path = Constant.ANDROID_SD + "/miduCache/" + "Img";
                File file = new File(path);

                if(!file.exists()){
                    file.mkdir();
                }
                File files = new File(file.getAbsolutePath(), "save.png");
                try {
                    //1.BitMap转文件
                    FileOutputStream out = new FileOutputStream(files);
                    bitmap.compress(Bitmap.CompressFormat.PNG,90,out);
                    out.flush();
                    out.close();
                    //2.文件转Bitmap
//                    Bitmap decodeFile = BitmapFactory.decodeFile(files.getAbsolutePath());

                    FileInputStream fis = new FileInputStream(files.getAbsolutePath());
                    Bitmap stream = BitmapFactory.decodeStream(fis);
                    mIvPost.setImageBitmap(stream);

                    shareMsg("share","分享",mContent,files.getAbsolutePath());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll:
                finish();
                break;
        }
    }

    /**
     * 分享功能
     *
     *            上下文
     * @param activityTitle
     *            Activity的名字
     * @param msgTitle
     *            消息标题
     * @param msgText
     *            消息内容
     * @param imgPath
     *            图片路径，不分享图片则传null
     */
    public void shareMsg(String activityTitle, String msgTitle, String msgText,
                         String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/jpg");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
