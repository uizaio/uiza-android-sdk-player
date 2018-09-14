package uiza.v2.home.canslide;

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
import vn.loitp.uzv1.view.rl.videoinfo.ItemAdapterV1;
import vn.loitp.uzv1.view.rl.videoinfo.UZVideoInfoV1;

public class FrmVideoBottom extends BaseFragment implements ItemAdapterV1.Callback {
    private UZVideoInfoV1 UZVideoInfoV1;

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        UZVideoInfoV1 = (UZVideoInfoV1) view.findViewById(R.id.uiza_video_info);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.frm_bottom;
    }

    public void setup(GetDetailEntity getDetailEntity) {
        UZVideoInfoV1.setup(getDetailEntity);
    }

    @Override
    public void onClickItemBottom(Item item, int position) {

    }

    @Override
    public void onLoadMore() {
    }

    public void init(ItemAdapterV1.Callback callback) {
        UZVideoInfoV1.init(callback);
    }

    public void clearAllViews() {
        if (UZVideoInfoV1 != null) {
            UZVideoInfoV1.clearAllViews();
        }
    }
}
