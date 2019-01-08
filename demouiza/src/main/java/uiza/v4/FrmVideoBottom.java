package uiza.v4;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uiza.R;
import uizacoresdk.view.rl.videoinfo.UZVideoInfo;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public class FrmVideoBottom extends Fragment {
    private UZVideoInfo uizaIMAVideoInfo;
    private String TAG = getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.v4_frm_bottom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uizaIMAVideoInfo = (UZVideoInfo) view.findViewById(R.id.uiza_video_info);
    }

    public void updateUI(ResultGetLinkPlay resultGetLinkPlay, Data data) {
        uizaIMAVideoInfo.setup(data);
    }

    public void clearUI() {
        uizaIMAVideoInfo.clearAllViews();
    }
}
