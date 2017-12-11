package aduio.midu.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import aduio.midu.R;
import aduio.midu.entitys.LocalAudioEntity;
import aduio.midu.ui.activitys.DownAudioListActivity;

/**
 * Created by ${LostDeer} on 2017/11/15.
 * Github:https://github.com/LostDeer
 */

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.ViewHolder> {
    private Context mContext;
    private List<LocalAudioEntity> mLocalAudioEntities;

    public AudioListAdapter(Context context, List<LocalAudioEntity> localAudioEntities) {
        this.mContext = context;
        this.mLocalAudioEntities = localAudioEntities;
    }

    @Override
    public AudioListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_audiolist, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final LocalAudioEntity localAudioEntity = mLocalAudioEntities.get(position);
        holder.mTvAudioName.setText(localAudioEntity.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DownAudioListActivity)mContext).setOnClickListener(localAudioEntity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLocalAudioEntities.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_audioName)
        TextView mTvAudioName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            AutoUtils.autoSize(view);
        }
    }
}
