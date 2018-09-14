package uiza.v3.canslide;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import uiza.R;
import vn.uiza.core.base.BaseFragment;
import vn.uiza.restapi.uiza.model.v2.listallentity.Item;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.uzv1.view.rl.videoinfo.ItemAdapterV1;
import vn.uiza.uzv3.view.rl.videoinfo.UZVideoInfo;

public class FrmVideoBottomV3 extends BaseFragment implements ItemAdapterV1.Callback {
    private UZVideoInfo uizaIMAVideoInfo;

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        uizaIMAVideoInfo = (UZVideoInfo) view.findViewById(R.id.uiza_video_info);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v3_frm_bottom;
    }

    public void setup(Data data) {
        uizaIMAVideoInfo.setup(data);
    }

    @Override
    public void onClickItemBottom(Item item, int position) {

    }

    @Override
    public void onLoadMore() {
    }

    public void init(ItemAdapterV1.Callback callback) {
        uizaIMAVideoInfo.init(callback);
    }

    public void clearAllViews() {
        if (uizaIMAVideoInfo != null) {
            uizaIMAVideoInfo.clearAllViews();
        }
    }
}
