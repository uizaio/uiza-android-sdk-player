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
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import uiza.R;
import uiza.v4.videoinfo.UZVideoInfo;

public class FrmVideoBottom extends Fragment {
    private final String TAG = getClass().getSimpleName();
    private UZVideoInfo uizaIMAVideoInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.v4_frm_bottom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uizaIMAVideoInfo = view.findViewById(R.id.uiza_video_info);
    }

    public void updateUI(LinkPlay linkPlay, VideoData data) {
        uizaIMAVideoInfo.setup(data);
    }

    public void clearUI() {
        uizaIMAVideoInfo.clearAllViews();
    }
}
