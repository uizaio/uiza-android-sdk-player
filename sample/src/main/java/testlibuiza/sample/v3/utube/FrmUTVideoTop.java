package testlibuiza.sample.v3.utube;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.rubensousa.previewseekbar.PreviewView;
import com.google.android.exoplayer2.video.VideoListener;

import testlibuiza.R;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.base.BaseFragment;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.views.LToast;

public class FrmUTVideoTop extends BaseFragment implements UZCallback, UZItemClick {
    private UZVideo uzVideo;
    private View shadow;

    public UZVideo getUZVideo() {
        return uzVideo;
    }

    @Override
    protected String setTag() {
        return "TAG" + getClass().getSimpleName();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uzVideo = (UZVideo) view.findViewById(R.id.uiza_video);
        uzVideo.setAutoSwitchItemPlaylistFolder(false);
        uzVideo.addUZCallback(this);
        uzVideo.setVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                calSize(width, height);
            }
        });
        uzVideo.addOnTouchEvent(new UZPlayerView.OnTouchEvent() {
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
        uzVideo.addCallbackUZTimebar(new UZVideo.CallbackUZTimebar() {
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
        uzVideo.addItemClick(this);
        uzVideo.setPlayerControllerAlwayVisible();

        shadow = (View) uzVideo.findViewById(R.id.bkg_shadow);
        uzVideo.setMarginDependOnUZTimeBar(shadow);
        uzVideo.setMarginDependOnUZTimeBar(uzVideo.getBkg());

        uzVideo.setBackgroundColorUZVideoRootView(Color.TRANSPARENT);
        uzVideo.setUzTimebarBottom();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.ut_frm_top;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uzVideo.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        uzVideo.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uzVideo.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        uzVideo.onStart();
    }

    @Override
    public void onStop() {
        cancelAutoHideController();
        super.onStop();
        uzVideo.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (resultCode == Activity.RESULT_OK) {
                uzVideo.initializePiP();
            } else {
                LToast.show(getActivity(), "Draw over other app permission not available");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
        ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).isInitResult(isGetDataSuccess, resultGetLinkPlay, data);
        if (isInitSuccess) {
            ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).getDraggablePanel().setBottomUZTimebar(uzVideo.getHeightUZTimeBar() / 2);
            setAutoHideController();
        }
    }

    @Override
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.exo_back_screen:
                if (!uzVideo.isLandscape()) {
                    ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).getDraggablePanel().minimize();
                }
                break;
        }
    }

    @Override
    public void onClickPipVideoInitSuccess(boolean isInitSuccess) {
        if (isInitSuccess) {
            uzVideo.pauseVideo();
            ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).getDraggablePanel().minimize();
            LUIUtil.setDelay(500, new LUIUtil.DelayCallback() {
                @Override
                public void doAfter(int mls) {
                    ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).getDraggablePanel().closeToRight();
                }
            });
        }
    }

    @Override
    public void onSkinChange() {
    }

    @Override
    public void onScreenRotate(boolean isLandscape) {
        uzVideo.setMarginDependOnUZTimeBar(shadow);
        uzVideo.setMarginDependOnUZTimeBar(uzVideo.getBkg());
        if (!isLandscape) {
            int width = uzVideo.getVideoW();
            int height = uzVideo.getVideoH();
            calSize(width, height);
        }
    }

    @Override
    public void onError(UZException e) {
    }

    public void initEntity(String entityId) {
        int w = LScreenUtil.getScreenWidth();
        int h = (int) (w * Constants.RATIO_9_16);
        resizeView(w, h);
        UZUtil.initEntity(getActivity(), uzVideo, entityId);
    }

    public void initPlaylistFolder(String metadataId) {
        int w = LScreenUtil.getScreenWidth();
        int h = (int) (w * Constants.RATIO_9_16);
        resizeView(w, h);
        UZUtil.initPlaylistFolder(getActivity(), uzVideo, metadataId);
    }

    private void calSize(int width, int height) {
        if (width >= height) {
            int screenW = LScreenUtil.getScreenWidth();
            int screenH = height * screenW / width;
            //LLog.d(TAG, "calSize >=: " + screenW + "x" + screenH);
            resizeView(screenW, screenH);
        } else {
            int screenW = LScreenUtil.getScreenWidth();
            int screenH = screenW;
            //LLog.d(TAG, "calSize <: " + screenW + "x" + screenH);
            resizeView(screenW, screenH);
        }
    }

    private void resizeView(int w, int h) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).setTopViewHeightApllyNow(h + uzVideo.getHeightUZTimeBar() / 2);
            }
        });
        uzVideo.post(new Runnable() {
            @Override
            public void run() {
                uzVideo.setSize(w, h);
            }
        });
    }

    //listerner controller visibility state
    private void onVisibilityChange(boolean isShow) {
        if (((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).getDraggablePanel() != null
                && !((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).isLandscapeScreen()) {
            if (((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).getDraggablePanel().isMaximized()) {
                if (isShow) {
                    ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).getDraggablePanel().setEnableSlide(false);
                } else {
                    ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).getDraggablePanel().setEnableSlide(true);
                }
            } else {
                ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).getDraggablePanel().setEnableSlide(true);
            }
        }
    }

    private void toggleControllerExceptUZTimebar() {
        if (uzVideo.getLlTop() != null) {
            if (uzVideo.getLlTop().getVisibility() == View.VISIBLE) {
                uzVideo.getLlTop().setVisibility(View.INVISIBLE);
                cancelAutoHideController();
                onVisibilityChange(false);
            } else {
                uzVideo.getLlTop().setVisibility(View.VISIBLE);
                setAutoHideController();
                onVisibilityChange(true);
            }
        }
        if (uzVideo.getRlLiveInfo() != null) {
            if (uzVideo.getRlLiveInfo().getVisibility() == View.VISIBLE) {
                uzVideo.getRlLiveInfo().setVisibility(View.INVISIBLE);
            } else {
                if (uzVideo.isLivestream()) {
                    uzVideo.getRlLiveInfo().setVisibility(View.VISIBLE);
                }
            }
        }
        LinearLayout llControllerButton = uzVideo.findViewById(R.id.ll_controller_button);
        if (llControllerButton != null) {
            if (llControllerButton.getVisibility() == View.VISIBLE) {
                llControllerButton.setVisibility(View.INVISIBLE);
            } else {
                llControllerButton.setVisibility(View.VISIBLE);
            }
        }
        RelativeLayout rlInfo = uzVideo.findViewById(R.id.rl_info);
        if (rlInfo != null) {
            if (rlInfo.getVisibility() == View.VISIBLE) {
                rlInfo.setVisibility(View.INVISIBLE);
            } else {
                rlInfo.setVisibility(View.VISIBLE);
            }
        }
        if (shadow.getVisibility() == View.VISIBLE) {
            shadow.setVisibility(View.GONE);
        } else {
            shadow.setVisibility(View.VISIBLE);
        }
    }

    private void toggleUIUZTimebar() {
        if (uzVideo.getUZTimeBar() == null) {
            return;
        }
        if (uzVideo.getUZTimeBar().getVisibility() == View.VISIBLE) {
            uzVideo.getUZTimeBar().setVisibility(View.INVISIBLE);
        } else {
            uzVideo.getUZTimeBar().setVisibility(View.VISIBLE);
        }
    }

    private void toggleUIRewFfw() {
        if (uzVideo.getIbRewIcon() != null) {
            if (uzVideo.getIbRewIcon().getVisibility() == View.VISIBLE) {
                uzVideo.getIbRewIcon().setVisibility(View.INVISIBLE);
            } else {
                uzVideo.getIbRewIcon().setVisibility(View.VISIBLE);
            }
        }
        if (uzVideo.getIbFfwdIcon() != null) {
            if (uzVideo.getIbFfwdIcon().getVisibility() == View.VISIBLE) {
                uzVideo.getIbFfwdIcon().setVisibility(View.INVISIBLE);
            } else {
                uzVideo.getIbFfwdIcon().setVisibility(View.VISIBLE);
            }
        }
    }

    private CountDownTimer countDownTimer;
    private final int INTERVAL = 1000;
    private final long millisInFuture = 8000;//controller will be hide after 8000mls

    private void cancelAutoHideController() {
        if (countDownTimer != null) {
            uzVideo.getUZTimeBar().setScrubberColor(Color.TRANSPARENT);
            countDownTimer.cancel();
        }
    }

    private void setAutoHideController() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        uzVideo.getUZTimeBar().setScrubberColor(Color.RED);
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
