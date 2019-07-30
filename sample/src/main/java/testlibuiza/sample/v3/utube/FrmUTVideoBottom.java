package testlibuiza.sample.v3.utube;

/**
 * Created by www.muathu@gmail.com on 1/9/2019.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import testlibuiza.R;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public class FrmUTVideoBottom extends Fragment {
    private TextView tvJsonData;
    private TextView tvJsonLinkplay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.v4_frm_bottom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvJsonData = (TextView) view.findViewById(R.id.tv_json_data);
        tvJsonLinkplay = (TextView) view.findViewById(R.id.tv_json_linkplay);
    }

    public void updateUI(ResultGetLinkPlay resultGetLinkPlay, Data data) {
        LUIUtil.printBeautyJson(data, tvJsonData);
        LUIUtil.printBeautyJson(resultGetLinkPlay, tvJsonLinkplay);
    }
}
