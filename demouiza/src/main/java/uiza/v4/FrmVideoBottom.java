package uiza.v4;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import uiza.R;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.loitp.uizavideov3.view.rl.videoinfo.UizaIMAVideoInfoV3;

public class FrmVideoBottom extends BaseFragment {
    private UizaIMAVideoInfoV3 uizaIMAVideoInfo;

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v4_frm_bottom;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uizaIMAVideoInfo = (UizaIMAVideoInfoV3) view.findViewById(R.id.uiza_video_info);
    }

    public void updateUI(ResultGetLinkPlay resultGetLinkPlay, Data data) {
        uizaIMAVideoInfo.setup(data);
    }

    public void clearUI() {
        uizaIMAVideoInfo.clearAllViews();
    }
}
