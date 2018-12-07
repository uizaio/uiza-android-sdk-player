package testlibuiza.sample.v3.utube;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import testlibuiza.R;
import vn.uiza.core.base.BaseFragment;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public class FrmUTVideoBottom extends BaseFragment {
    private TextView tvJsonData;
    private TextView tvJsonLinkplay;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v4_frm_bottom;
    }

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvJsonData = (TextView) frmRootView.findViewById(R.id.tv_json_data);
        tvJsonLinkplay = (TextView) frmRootView.findViewById(R.id.tv_json_linkplay);
    }

    public void updateUI(ResultGetLinkPlay resultGetLinkPlay, Data data) {
        LUIUtil.printBeautyJson(data, tvJsonData);
        LUIUtil.printBeautyJson(resultGetLinkPlay, tvJsonLinkplay);
    }
}
