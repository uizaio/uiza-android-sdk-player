package testlibuiza.sample.v3.utube;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

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
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import testlibuiza.R;
import uizacoresdk.interfaces.CallbackUZTimebar;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.rl.video.UZVideo;

public class FrmUTVideoTop extends Fragment implements UZCallback, UZItemClick {
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
        uzVideo.addVideoListener(new VideoListener() {
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ut_frm_top, container, false);
    }

    @Override
    public void onDestroy() {
        uzVideo.onDestroy();
        super.onDestroy();
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
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, LinkPlay linkPlay, VideoData data) {
        ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).isInitResult(isGetDataSuccess, linkPlay, data);
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
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
        if (isInitMiniPlayerSuccess) {
            uzVideo.pauseVideo();
            ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).getDraggablePanel().minimize();
            UzDisplayUtil.setDelay(500, new UzDisplayUtil.DelayCallback() {
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
    public void onError(UzException e) {
    }

    public void initEntity(String entityId) {
        showController();
        int w = UzDisplayUtil.getScreenWidth();
        int h = (int) (w * Constants.RATIO_9_16);
        resizeView(w, h);
        UZUtil.initEntity(getActivity(), uzVideo, entityId);
    }

    public void initPlaylistFolder(String metadataId) {
        showController();
        int w = UzDisplayUtil.getScreenWidth();
        int h = (int) (w * Constants.RATIO_9_16);
        resizeView(w, h);
        UZUtil.initPlaylistFolder(getActivity(), uzVideo, metadataId);
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
    private boolean isShowingController = true;

    private void onVisibilityChange(boolean isShow) {
        this.isShowingController = isShow;
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

    /*private void hideUZTimebar() {
        if (uzVideo.getUZTimeBar() == null) {
            return;
        }
        uzVideo.getUZTimeBar().setVisibility(View.INVISIBLE);
    }*/

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
