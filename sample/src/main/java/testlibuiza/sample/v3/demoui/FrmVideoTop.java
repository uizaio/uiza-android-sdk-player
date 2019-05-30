package testlibuiza.sample.v3.demoui;

/**
 * Created by www.muathu@gmail.com on 6/3/2019.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import testlibuiza.R;
import uizacoresdk.interfaces.UZItemClickListener;
import uizacoresdk.interfaces.UZPlayerStateChangedListener;
import uizacoresdk.interfaces.UZVideoStateChangedListener;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public class FrmVideoTop extends Fragment
        implements UZItemClickListener, UZPlayerView.ControllerStateCallback, UZVideoStateChangedListener,
        UZPlayerStateChangedListener {
    private final String TAG = getClass().getSimpleName();
    private HomeCanSlideActivity homeCanSlideActivity;
    private UZVideo uzVideo;

    public UZVideo getUZVideo() {
        return uzVideo;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uzVideo = view.findViewById(R.id.uiza_video);
        uzVideo.setAutoSwitchItemPlaylistFolder(false);
        uzVideo.setUzVideoStateChangedListener(this);
        uzVideo.setUzPlayerStateChangedListener(this);
        uzVideo.setUzItemClickListener(this);
        uzVideo.addControllerStateCallback(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        homeCanSlideActivity = (HomeCanSlideActivity) getActivity();
        return inflater.inflate(R.layout.v4_frm_top, container, false);
    }

    @Override
    public void onDestroy() {
        uzVideo.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        uzVideo.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        uzVideo.onPause();
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzVideo.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess,
            ResultGetLinkPlay resultGetLinkPlay, Data data) {
        homeCanSlideActivity.isInitResult(isGetDataSuccess, resultGetLinkPlay, data);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onVideoProgress(long currentMls, int s, long duration, int percent) {

    }

    @Override
    public void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration) {

    }

    @Override
    public void onVideoEnded() {

    }

    @Override
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.exo_back_screen:
                if (!uzVideo.isLandscape()) {
                    homeCanSlideActivity.getDraggablePanel().minimize();
                }
                break;
        }
    }

    @Override
    public void onSkinChanged() {

    }

    @Override
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
        if (isInitMiniPlayerSuccess) {
            uzVideo.pause();
            homeCanSlideActivity.getDraggablePanel().minimize();
            LUIUtil.setDelay(500, new LUIUtil.DelayCallback() {
                @Override
                public void doAfter(int mls) {
                    homeCanSlideActivity.getDraggablePanel().closeToRight();
                }
            });
        }
    }

    public boolean isLandscape;

    @Override
    public void onScreenRotated(boolean isLandscape) {
        this.isLandscape = isLandscape;
        if (isLandscape) {
            homeCanSlideActivity.getDraggablePanel().setEnableSlide(false);
        } else {
            homeCanSlideActivity.getDraggablePanel().setEnableSlide(true);
        }
        calSize();
    }

    @Override
    public void onError(UZException e) {
    }

    public void initEntity(String entityId) {
        UZUtil.initEntity(getActivity(), uzVideo, entityId);
    }

    public void initPlaylistFolder(String metadataId) {
        UZUtil.initPlaylistFolder(getActivity(), uzVideo, metadataId);
    }

    private void calSize() {
        int screenW = LScreenUtil.getScreenWidth();
        int screenH;
        if (isLandscape) {
            screenH = LScreenUtil.getScreenHeight();
        } else {
            screenH = (int) (screenW * Constants.RATIO_9_16);
        }
        resizeView(screenW, screenH);
    }

    private void resizeView(int w, int h) {
        homeCanSlideActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                homeCanSlideActivity.setTopViewHeightApllyNow(h);
            }
        });
    }

    @Override
    public void onVisibilityChange(boolean isShow) {
        if (homeCanSlideActivity.getDraggablePanel() != null && !isLandscape) {
            if (homeCanSlideActivity.getDraggablePanel().isMaximized()) {
                if (isShow) {
                    homeCanSlideActivity.getDraggablePanel().setEnableSlide(false);
                } else {
                    homeCanSlideActivity.getDraggablePanel().setEnableSlide(true);
                }
            } else {
                homeCanSlideActivity.getDraggablePanel().setEnableSlide(true);
            }
        }
    }
}
