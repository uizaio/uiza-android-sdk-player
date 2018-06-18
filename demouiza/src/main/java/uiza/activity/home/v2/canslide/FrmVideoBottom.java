package uiza.activity.home.v2.canslide;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import uiza.R;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.restapi.uiza.model.v2.getdetailentity.GetDetailEntity;
import vn.loitp.restapi.uiza.model.v2.listallentity.Item;
import vn.loitp.uizavideo.view.rl.videoinfo.ItemAdapterV2;
import vn.loitp.uizavideo.view.rl.videoinfo.UizaIMAVideoInfo;

public class FrmVideoBottom extends BaseFragment implements ItemAdapterV2.Callback {
    private final String TAG = getClass().getSimpleName();
    private UizaIMAVideoInfo uizaIMAVideoInfo;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        uizaIMAVideoInfo = (UizaIMAVideoInfo) view.findViewById(R.id.uiza_video_info);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.frm_bottom;
    }

    public void setup(GetDetailEntity getDetailEntity) {
        uizaIMAVideoInfo.setup(getDetailEntity);
    }

    @Override
    public void onClickItemBottom(Item item, int position) {

    }

    @Override
    public void onLoadMore() {
        //do nothing
    }

    public void init(ItemAdapterV2.Callback callback) {
        uizaIMAVideoInfo.init(callback);
    }

    public void clearAllViews() {
        if (uizaIMAVideoInfo != null) {
            uizaIMAVideoInfo.clearAllViews();
        }
    }
}
