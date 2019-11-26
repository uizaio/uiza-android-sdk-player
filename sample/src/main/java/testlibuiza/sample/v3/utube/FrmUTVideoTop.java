package testlibuiza.sample.v3.utube;

/**
 * Created by www.muathu@gmail.com on 6/3/2019.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.rubensousa.previewseekbar.PreviewView;

import testlibuiza.R;
import uizacoresdk.interfaces.CallbackUZTimebar;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public class FrmUTVideoTop extends Fragment implements UZCallback, UZItemClick {
    private final String TAG = getClass().getSimpleName();
    private CustomSkinCodeUZTimebarUTubeWithSlideActivity activity;
    private UZVideo uzVideo;
    private View shadow;

    public UZVideo getUZVideo() {
        return uzVideo;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uzVideo = (UZVideo) view.findViewById(R.id.uiza_video);
        uzVideo.setAutoSwitchItemPlaylistFolder(false);
        uzVideo.addUZCallback(this);
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
        uzVideo.addCallbackUZTimebar(new CallbackUZTimebar() {
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
        calSize();
        activity.getDraggablePanel().setBottomUZTimebar(uzVideo.getHeightUZTimeBar() / 2);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity();
        return inflater.inflate(R.layout.ut_frm_top, container, false);
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
    public void onStop() {
        cancelAutoHideController();
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzVideo.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
        activity.isInitResult(isGetDataSuccess, resultGetLinkPlay, data);
        if (isInitSuccess) {
            setAutoHideController();
        }
    }

    @Override
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.exo_back_screen:
                if (!uzVideo.isLandscape()) {
                    activity.getDraggablePanel().minimize();
                }
                break;
        }
    }

    @Override
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
        if (isInitMiniPlayerSuccess) {
            uzVideo.pauseVideo();
            activity.getDraggablePanel().minimize();
            LUIUtil.setDelay(500, new LUIUtil.DelayCallback() {
                @Override
                public void doAfter(int mls) {
                    activity.getDraggablePanel().closeToRight();
                }
            });
        }
    }

    @Override
    public void onSkinChange() {
    }

    public boolean isLandscape;

    @Override
    public void onScreenRotate(boolean isLandscape) {
        this.isLandscape = isLandscape;
        uzVideo.setMarginDependOnUZTimeBar(shadow);
        uzVideo.setMarginDependOnUZTimeBar(uzVideo.getBkg());
        if (isLandscape) {
            activity.getDraggablePanel().setEnableSlide(false);
        } else {
            activity.getDraggablePanel().setEnableSlide(true);
        }
        calSize();
    }

    @Override
    public void onError(UZException e) {
    }

    public void initEntity(String entityId) {
        hideController();
        uzVideo.pauseVideo();
        UZUtil.initEntity(getActivity(), uzVideo, entityId);
    }

    public void initPlaylistFolder(String metadataId) {
        hideController();
        uzVideo.pauseVideo();
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
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setTopViewHeightApllyNow(screenH + uzVideo.getHeightUZTimeBar() / 2);
            }
        });
    }

    //listerner controller visibility state
    private boolean isShowingController = true;

    private void onVisibilityChange(boolean isShow) {
        this.isShowingController = isShow;
        if (activity.getDraggablePanel() != null && !(uzVideo.isLandscape())) {
            if (activity.getDraggablePanel().isMaximized()) {
                if (isShow) {
                    activity.getDraggablePanel().setEnableSlide(false);
                } else {
                    activity.getDraggablePanel().setEnableSlide(true);
                }
            } else {
                activity.getDraggablePanel().setEnableSlide(true);
            }
        }
    }

    private void showController() {
        if (uzVideo.getLlTop() != null) {
            uzVideo.getLlTop().setVisibility(View.VISIBLE);
            setAutoHideController();
        }
        if (uzVideo.getRlLiveInfo() != null) {
            if (uzVideo.isLivestream()) {
                uzVideo.getRlLiveInfo().setVisibility(View.VISIBLE);
            }
        }
        LinearLayout llControllerButton = uzVideo.findViewById(R.id.ll_controller_button);
        if (llControllerButton != null) {
            llControllerButton.setVisibility(View.VISIBLE);
        }
        RelativeLayout rlInfo = uzVideo.findViewById(R.id.rl_info);
        if (rlInfo != null) {
            rlInfo.setVisibility(View.VISIBLE);
        }
        shadow.setVisibility(View.VISIBLE);
        onVisibilityChange(true);
    }

    private void hideController() {
        if (uzVideo.getLlTop() != null) {
            if (uzVideo.getLlTop().getVisibility() == View.VISIBLE) {
                uzVideo.getLlTop().setVisibility(View.INVISIBLE);
                cancelAutoHideController();
            }
        }
        if (uzVideo.getRlLiveInfo() != null) {
            if (uzVideo.getRlLiveInfo().getVisibility() == View.VISIBLE) {
                uzVideo.getRlLiveInfo().setVisibility(View.INVISIBLE);
            }
        }
        LinearLayout llControllerButton = uzVideo.findViewById(R.id.ll_controller_button);
        if (llControllerButton != null) {
            if (llControllerButton.getVisibility() == View.VISIBLE) {
                llControllerButton.setVisibility(View.INVISIBLE);
            }
        }
        RelativeLayout rlInfo = uzVideo.findViewById(R.id.rl_info);
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
