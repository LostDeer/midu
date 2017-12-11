package aduio.lib.banner.adapter;

import android.content.Context;
import android.view.View;

import aduio.lib.banner.LMBanners;


/**
 * Created by luomin on 16/7/12.
 */
public interface LBaseAdapter<T> {
    View getView(LMBanners lBanners, Context context, int position, T data);
}
