package aduio.midu.ui.adapters;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import aduio.midu.R;
import aduio.midu.entitys.LocalAudioEntity;

/**
 * Created by ${LostDeer} on 2017/12/5.
 * Github:https://github.com/LostDeer
 */

public class AudioAdapter extends BaseQuickAdapter<LocalAudioEntity, BaseViewHolder> {
    public AudioAdapter(int layoutResId, @Nullable List<LocalAudioEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LocalAudioEntity item) {
        helper.addOnClickListener(R.id.ll_itemview);
        ((TextView)helper.getView(R.id.tv_audioName)).setText(item.getName());
    }
}
