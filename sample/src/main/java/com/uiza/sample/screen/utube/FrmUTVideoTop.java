package com.uiza.sample.screen.utube;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.github.rubensousa.previewseekbar.PreviewView;
import com.google.android.exoplayer2.video.VideoListener;
import com.uiza.sample.R;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.UzCommonUtil;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.player.UzPlayer;
import io.uiza.player.UzPlayerConfig;
import io.uiza.player.interfaces.UzItemClickListener;
import io.uiza.player.interfaces.UzPlayerEventListener;
import io.uiza.player.interfaces.UzPlayerUiEventListener;
import io.uiza.player.interfaces.UzTimeBarPreviewListener;
import io.uiza.player.view.UzPlayerView;

public class FrmUTVideoTop extends Fragment implements UzPlayerUiEventListener,
        UzPlayerEventListener, UzItemClickListener {

    private final int INTERVAL = 1000;
    private final long millisInFuture = 8000;//controller will be hide after 8000mls
    private UzPlayer uzPlayer;
    private View shadow;
    //listerner controller visibility state
    private boolean isShowingController = true;
    private CountDownTimer countDownTimer;

    public UzPlayer getUZVideo() {
        return uzPlayer;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uzPlayer = view.findViewById(R.id.uiza_video);
        uzPlayer.setAutoSwitchItemPlaylistFolder(false);
        uzPlayer.setUzPlayerUiEventListener(this);
        uzPlayer.setUzPlayerEventListener(this);
        uzPlayer.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                    float pixelWidthHeightRatio) {
                calSize(width, height);
            }
        });
        uzPlayer.setOnTouchEvent(new UzPlayerView.OnTouchEvent() {
            @Override
            public void onSingleTapConfirmed(float x, float y) {
                toggleControllerExceptUZTimebar();
            }

            @Override
            public void onLongPress(float x, float y) {
            }

            @Override
            public void onDoubleTap(float x, float y) {
            }

            @Override
            public void onSwipeRight() {
            }

            @Override
            public void onSwipeLeft() {
            }

            @Override
            public void onSwipeBottom() {
            }

            @Override
            public void onSwipeTop() {
            }
        });
        uzPlayer.setUzTimeBarPreviewListener(new UzTimeBarPreviewListener() {
            @Override
            public void onStartPreview(PreviewView previewView, int progress) {
            }

            @Override
            public void onStopPreview(PreviewView previewView, int progress) {
            }

            @Override
            public void onPreview(PreviewView previewView, int progress, boolean fromUser) {
                setAutoHideController();
            }
        });
        uzPlayer.setUzItemClickListener(this);
        uzPlayer.setPlayerControllerAlwaysVisible();

        shadow = uzPlayer.findViewById(R.id.bkg_shadow);
        uzPlayer.setMarginDependOnUzTimeBar(shadow);
        uzPlayer.setMarginDependOnUzTimeBar(uzPlayer.getBkg());

        uzPlayer.setBackgroundColorUzPlayerRootView(Color.TRANSPARENT);
        uzPlayer.setUzTimeBarBottom();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ut_frm_top, container, false);
    }

    @Override
    public void onDestroy() {
        uzPlayer.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        uzPlayer.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uzPlayer.onPause();
    }

    @Override
    public void onStop() {
        cancelAutoHideController();
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzPlayer.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDataInitialized(boolean initSuccess, boolean getDataSuccess, LinkPlay linkPlay,
            VideoData data) {
        ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity())
                .isInitResult(getDataSuccess, linkPlay, data);
        if (initSuccess) {
            ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).getDraggablePanel()
                    .setBottomUZTimebar(uzPlayer.getUzTimeBarHeight() / 2);
            setAutoHideController();
        }
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
        uzPlayer.setMarginDependOnUzTimeBar(shadow);
        uzPlayer.setMarginDependOnUzTimeBar(uzPlayer.getBkg());
        if (!isLandscape) {
            int width = uzPlayer.getVideoWidth();
            int height = uzPlayer.getVideoHeight();
            calSize(width, height);
        }
    }

    @Override
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.exo_back_screen:
                if (!uzPlayer.isLandscape()) {
                    ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity())
                            .getDraggablePanel().minimize();
                }
                break;
        }
    }

    @Override
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
        if (isInitMiniPlayerSuccess) {
            uzPlayer.pause();
            ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).getDraggablePanel()
                    .minimize();
            UzCommonUtil.actionWithDelayed(500,
                    mls -> ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity())
                            .getDraggablePanel().closeToRight());
        }
    }

    public void initEntity(String entityId) {
        showController();
        int w = UzDisplayUtil.getScreenWidth();
        int h = (int) (w * Constants.RATIO_9_16);
        resizeView(w, h);
        UzPlayerConfig.initVodEntity(uzPlayer, entityId);
    }

    public void initPlaylistFolder(String metadataId) {
        showController();
        int w = UzDisplayUtil.getScreenWidth();
        int h = (int) (w * Constants.RATIO_9_16);
        resizeView(w, h);
        UzPlayerConfig.initPlaylistFolder(uzPlayer, metadataId);
    }

    private void calSize(int width, int height) {
        if (width >= height) {
            int screenW = UzDisplayUtil.getScreenWidth();
            int screenH = height * screenW / width;
            resizeView(screenW, screenH);
        } else {
            int screenW = UzDisplayUtil.getScreenWidth();
            int screenH = screenW;
            resizeView(screenW, screenH);
        }
    }

    private void resizeView(int w, int h) {
        getActivity()
                .runOnUiThread(() -> ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity())
                        .setTopViewHeightApllyNow(h + uzPlayer.getUzTimeBarHeight() / 2));
        uzPlayer.post(() -> uzPlayer.setSize(w, h));
    }

    private void onVisibilityChange(boolean isShow) {
        this.isShowingController = isShow;
        if (((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).getDraggablePanel()
                != null
                && !((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity())
                .isLandscapeScreen()) {
            if (((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).getDraggablePanel()
                    .isMaximized()) {
                if (isShow) {
                    ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity())
                            .getDraggablePanel().setEnableSlide(false);
                } else {
                    ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity())
                            .getDraggablePanel().setEnableSlide(true);
                }
            } else {
                ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).getDraggablePanel()
                        .setEnableSlide(true);
            }
        }
    }

    private void showController() {
        if (uzPlayer.getLlTop() != null) {
            uzPlayer.getLlTop().setVisibility(View.VISIBLE);
            setAutoHideController();
        }
        if (uzPlayer.getRlLiveInfo() != null) {
            if (uzPlayer.isLivestream()) {
                uzPlayer.getRlLiveInfo().setVisibility(View.VISIBLE);
            }
        }
        LinearLayout llControllerButton = uzPlayer.findViewById(R.id.ll_controller_button);
        if (llControllerButton != null) {
            llControllerButton.setVisibility(View.VISIBLE);
        }
        RelativeLayout rlInfo = uzPlayer.findViewById(R.id.rl_info);
        if (rlInfo != null) {
            rlInfo.setVisibility(View.VISIBLE);
        }
        shadow.setVisibility(View.VISIBLE);
        onVisibilityChange(true);
    }

    private void hideController() {
        if (uzPlayer.getLlTop() != null) {
            if (uzPlayer.getLlTop().getVisibility() == View.VISIBLE) {
                uzPlayer.getLlTop().setVisibility(View.INVISIBLE);
                cancelAutoHideController();
            }
        }
        if (uzPlayer.getRlLiveInfo() != null) {
            if (uzPlayer.getRlLiveInfo().getVisibility() == View.VISIBLE) {
                uzPlayer.getRlLiveInfo().setVisibility(View.INVISIBLE);
            }
        }
        LinearLayout llControllerButton = uzPlayer.findViewById(R.id.ll_controller_button);
        if (llControllerButton != null) {
            if (llControllerButton.getVisibility() == View.VISIBLE) {
                llControllerButton.setVisibility(View.INVISIBLE);
            }
        }
        RelativeLayout rlInfo = uzPlayer.findViewById(R.id.rl_info);
        if (rlInfo != null) {
            if (rlInfo.getVisibility() == View.VISIBLE) {
                rlInfo.setVisibility(View.INVISIBLE);
            }
        }
        shadow.setVisibility(View.GONE);
        onVisibilityChange(false);
    }

    private void toggleControllerExceptUZTimebar() {
        if (isShowingController) {
            hideController();
        } else {
            showController();
        }
    }

    private void cancelAutoHideController() {
        if (countDownTimer != null) {
            uzPlayer.getUzTimeBar().setScrubberColor(Color.TRANSPARENT);
            countDownTimer.cancel();
        }
    }

    private void setAutoHideController() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        uzPlayer.getUzTimeBar().setScrubberColor(Color.RED);
        countDownTimer = new CountDownTimer(millisInFuture, INTERVAL) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                toggleControllerExceptUZTimebar();
            }
        };
        countDownTimer.start();
    }
}
