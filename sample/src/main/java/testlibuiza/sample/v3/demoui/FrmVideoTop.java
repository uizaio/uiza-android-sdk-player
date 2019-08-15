package testlibuiza.sample.v3.demoui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.exoplayer2.video.VideoListener;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.UzCommonUtil;
import io.uiza.core.util.UzCommonUtil.DelayCallback;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.player.UzPlayer;
import io.uiza.player.UzPlayerConfig;
import io.uiza.player.interfaces.UzItemClickListener;
import io.uiza.player.interfaces.UzPlayerEventListener;
import io.uiza.player.interfaces.UzPlayerUiEventListener;
import io.uiza.player.view.UzPlayerView;
import testlibuiza.R;

public class FrmVideoTop extends Fragment implements UzPlayerEventListener, UzPlayerUiEventListener,
        UzItemClickListener, UzPlayerView.ControllerStateCallback {

    private UzPlayer uzPlayer;
    public boolean isLandscape;

    public UzPlayer getUZVideo() {
        return uzPlayer;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uzPlayer = view.findViewById(R.id.uiza_video);
        uzPlayer.setAutoSwitchItemPlaylistFolder(false);
        uzPlayer.setUzPlayerEventListener(this);
        uzPlayer.setUzPlayerUiEventListener(this);
        uzPlayer.setUzItemClickListener(this);
        uzPlayer.setControllerStateCallback(this);
        uzPlayer.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                    float pixelWidthHeightRatio) {
                calSize(width, height);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.v4_frm_top, container, false);
    }

    @Override
    public void onDestroy() {
        uzPlayer.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        uzPlayer.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        uzPlayer.onPause();
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzPlayer.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDataInitialized(boolean initSuccess, boolean getDataSuccess, LinkPlay linkPlay,
            VideoData data) {
        ((HomeCanSlideActivity) getActivity()).isInitResult(getDataSuccess, linkPlay, data);
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
    public void onPlayerError(UzException exception) {

    }

    @Override
    public void onSkinChanged() {

    }

    @Override
    public void onPlayerRotated(boolean isLandscape) {
        this.isLandscape = isLandscape;
        if (isLandscape) {
            ((HomeCanSlideActivity) getActivity()).getDraggablePanel().setEnableSlide(false);
        } else {
            ((HomeCanSlideActivity) getActivity()).getDraggablePanel().setEnableSlide(true);
        }
        calSize(uzPlayer.getVideoWidth(), uzPlayer.getVideoHeight());
    }

    @Override
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.exo_back_screen:
                if (!uzPlayer.isLandscape()) {
                    ((HomeCanSlideActivity) getActivity()).getDraggablePanel().minimize();
                }
                break;
        }
    }

    @Override
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
        if (isInitMiniPlayerSuccess) {
            uzPlayer.pause();
            ((HomeCanSlideActivity) getActivity()).getDraggablePanel().minimize();
            UzCommonUtil.actionWithDelayed(500, new DelayCallback() {
                @Override
                public void doAfter(int mls) {
                    ((HomeCanSlideActivity) getActivity()).getDraggablePanel().closeToRight();
                }
            });
        }
    }

    public void initEntity(String entityId) {
        int w = UzDisplayUtil.getScreenWidth();
        int h = (int) (w * Constants.RATIO_9_16);
        resizeView(w, h);
        if (uzPlayer != null) {
            uzPlayer.pause();
        }
        UzPlayerConfig.initVodEntity(uzPlayer, entityId);
    }

    public void initPlaylistFolder(String metadataId) {
        int w = UzDisplayUtil.getScreenWidth();
        int h = (int) (w * Constants.RATIO_9_16);
        resizeView(w, h);
        if (uzPlayer != null) {
            uzPlayer.pause();
        }
        UzPlayerConfig.initPlaylistFolder(uzPlayer, metadataId);
    }

    private void calSize(int width, int height) {
        int screenW;
        int screenH;
        if (isLandscape) {
            screenW = UzDisplayUtil.getScreenWidth();
            screenH = UzDisplayUtil.getScreenHeight();
        } else {
            if (width >= height) {
                screenW = UzDisplayUtil.getScreenWidth();
                screenH = height * screenW / width;
            } else {
                screenW = UzDisplayUtil.getScreenWidth();
                screenH = screenW;
            }
        }
        resizeView(screenW, screenH);
    }

    private void resizeView(int w, int h) {
        getActivity().runOnUiThread(
                () -> ((HomeCanSlideActivity) getActivity()).setTopViewHeightApllyNow(h));
    }

    @Override
    public void onVisibilityChanged(boolean isShow) {
        if (((HomeCanSlideActivity) getActivity()).getDraggablePanel() != null && !isLandscape) {
            if (((HomeCanSlideActivity) getActivity()).getDraggablePanel().isMaximized()) {
                if (isShow) {
                    ((HomeCanSlideActivity) getActivity()).getDraggablePanel()
                            .setEnableSlide(false);
                } else {
                    ((HomeCanSlideActivity) getActivity()).getDraggablePanel().setEnableSlide(true);
                }
            } else {
                ((HomeCanSlideActivity) getActivity()).getDraggablePanel().setEnableSlide(true);
            }
        }
    }
}
