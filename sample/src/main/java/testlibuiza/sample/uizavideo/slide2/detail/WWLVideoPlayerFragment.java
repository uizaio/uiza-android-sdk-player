package testlibuiza.sample.uizavideo.slide2.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import testlibuiza.R;
import testlibuiza.sample.uizavideo.slide2.interfaces.FragmentHost;
import testlibuiza.sample.uizavideo.slide2.utils.WWLVideoDataset;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.utilities.LLog;

/**
 * Created by thangn on 2/26/17.
 */

public class WWLVideoPlayerFragment extends BaseFragment {
    private final String TAG = getClass().getSimpleName();
    private String mUrl;
    private FragmentHost mFragmentHost;

    @Nullable

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.wwl_video_player_fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mFragmentHost = (FragmentHost) context;
    }

    public void startPlay(WWLVideoDataset.DatasetItem item) {
        this.mUrl = item.url;
        LLog.d(TAG, "startPlay " + mUrl);
    }

    public void stopPlay() {
        if (this.mUrl != null) {
            //TODO
        }
    }
}
