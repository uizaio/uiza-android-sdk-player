package testlibuiza.sample.v3.demoui;

/**
 * Created by www.muathu@gmail.com on 9/1/2019.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.util.UzDisplayUtil;
import testlibuiza.R;

public class FrmVideoBottom extends Fragment {
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
        tvJsonData = view.findViewById(R.id.tv_json_data);
        tvJsonLinkplay = view.findViewById(R.id.tv_json_linkplay);
    }

    public void updateUI(LinkPlay linkPlay, VideoData data) {
        UzDisplayUtil.printBeautyJson(data, tvJsonData);
        UzDisplayUtil.printBeautyJson(linkPlay, tvJsonLinkplay);
    }
}
