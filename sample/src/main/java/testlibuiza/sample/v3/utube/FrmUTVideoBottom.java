package testlibuiza.sample.v3.utube;

/**
 * Created by www.muathu@gmail.com on 1/9/2019.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import testlibuiza.R;
import vn.uiza.restapi.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.model.v5.UizaPlayback;
import vn.uiza.utils.StringUtil;

public class FrmUTVideoBottom extends Fragment {
    private TextView tvJsonData;
    private TextView tvJsonLinkplay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedState) {
        return inflater.inflate(R.layout.v4_frm_bottom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedState) {
        super.onViewCreated(view, savedState);
        tvJsonData = view.findViewById(R.id.tv_json_data);
        tvJsonLinkplay = view.findViewById(R.id.tv_json_linkplay);
    }

    public void updateUI(ResultGetLinkPlay resultGetLinkPlay, UizaPlayback data) {
        tvJsonData.setText(StringUtil.toBeautyJson(data));
        tvJsonLinkplay.setText(StringUtil.toBeautyJson(resultGetLinkPlay));
    }
}
