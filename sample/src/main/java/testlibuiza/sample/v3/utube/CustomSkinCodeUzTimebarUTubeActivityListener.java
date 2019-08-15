package testlibuiza.sample.v3.utube;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.github.rubensousa.previewseekbar.PreviewView;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.player.UzPlayer;
import io.uiza.player.UzPlayerConfig;
import io.uiza.player.interfaces.UzItemClickListener;
import io.uiza.player.interfaces.UzPlayerEventListener;
import io.uiza.player.interfaces.UzPlayerUiEventListener;
import io.uiza.player.interfaces.UzTimeBarPreviewListener;
import io.uiza.player.view.UzPlayerView;
import testlibuiza.R;
import testlibuiza.app.LSApplication;

public class CustomSkinCodeUzTimebarUTubeActivityListener extends AppCompatActivity implements
        UzPlayerEventListener, UzPlayerUiEventListener, UzItemClickListener {

    private UzPlayer uzPlayer;
    private Activity activity;
    private View shadow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        UzPlayerConfig.setCasty(this);
        UzPlayerConfig.setCurrentSkinRes(R.layout.framgia_controller_skin_custom_main_1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiza_custom_skin_u_tube);
        uzPlayer = findViewById(R.id.uiza_video);
        uzPlayer.setUzPlayerEventListener(this);
        uzPlayer.setUzPlayerUiEventListener(this);
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

        final String entityId = LSApplication.entityIdDefaultVOD;
        //final String entityId = LSApplication.entityIdDefaultLIVE;
        UzPlayerConfig.initVodEntity(uzPlayer, entityId);

        findViewById(R.id.bt_0).setOnClickListener(view -> toggleUIUZTimebar());
        findViewById(R.id.bt_1).setOnClickListener(view -> toggleUIRewFfw());
        findViewById(R.id.bt_2).setOnClickListener(view -> toggleControllerExceptUZTimebar());
    }

    private boolean isShowingController = true;

    private void onVisibilityChange(boolean isShow) {
        this.isShowingController = isShow;
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

    private void toggleUIUZTimebar() {
        if (uzPlayer.getUzTimeBar() == null) {
            return;
        }
        if (uzPlayer.getUzTimeBar().getVisibility() == View.VISIBLE) {
            uzPlayer.getUzTimeBar().setVisibility(View.INVISIBLE);
        } else {
            uzPlayer.getUzTimeBar().setVisibility(View.VISIBLE);
        }
    }

    private void toggleUIRewFfw() {
        if (uzPlayer.getIbRewIcon() != null) {
            if (uzPlayer.getIbRewIcon().getVisibility() == View.VISIBLE) {
                uzPlayer.getIbRewIcon().setVisibility(View.INVISIBLE);
            } else {
                uzPlayer.getIbRewIcon().setVisibility(View.VISIBLE);
            }
        }
        if (uzPlayer.getIbFfwdIcon() != null) {
            if (uzPlayer.getIbFfwdIcon().getVisibility() == View.VISIBLE) {
                uzPlayer.getIbFfwdIcon().setVisibility(View.INVISIBLE);
            } else {
                uzPlayer.getIbFfwdIcon().setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDataInitialized(boolean initSuccess, boolean getDataSuccess, LinkPlay linkPlay,
            VideoData data) {
        if (initSuccess) {
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
    }

    @Override
    public void onItemClick(View view) {
        setAutoHideController();
        switch (view.getId()) {
            case R.id.exo_back_screen:
                if (!uzPlayer.isLandscape()) {
                    onBackPressed();
                }
                break;
        }
    }

    @Override
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        uzPlayer.onDestroy();
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
    public void onBackPressed() {
        if (UzDisplayUtil.isFullScreen(activity)) {
            uzPlayer.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }

    private CountDownTimer countDownTimer;
    private final int INTERVAL = 1000;
    private final long millisInFuture = 8000;//controller will be hide after 8000mls

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
