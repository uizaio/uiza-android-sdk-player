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
import testlibuiza.R;
import testlibuiza.app.LSApplication;
import uizacoresdk.interfaces.CallbackUZTimebar;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

/**
 * Created by loitp on 9/1/2019.
 */

public class CustomSkinCodeUZTimebarUTubeActivity extends AppCompatActivity implements UZCallback, UZItemClick {
    private UZVideo uzVideo;
    private Activity activity;
    private View shadow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        UZUtil.setCasty(this);
        UZUtil.setCurrentPlayerId(R.layout.framgia_controller_skin_custom_main_1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiza_custom_skin_u_tube);
        uzVideo = (UZVideo) findViewById(R.id.uiza_video);
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

        final String entityId = LSApplication.entityIdDefaultVOD;
        //final String entityId = LSApplication.entityIdDefaultLIVE;
        UZUtil.initEntity(activity, uzVideo, entityId);

        findViewById(R.id.bt_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleUIUZTimebar();
            }
        });
        findViewById(R.id.bt_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleUIRewFfw();
            }
        });
        findViewById(R.id.bt_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleControllerExceptUZTimebar();
            }
        });
    }

    private boolean isShowingController = true;

    private void onVisibilityChange(boolean isShow) {
        this.isShowingController = isShow;
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

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
        if (isInitSuccess) {
            setAutoHideController();
        }
    }

    @Override
    public void onItemClick(View view) {
        setAutoHideController();
        switch (view.getId()) {
            case R.id.exo_back_screen:
                if (!uzVideo.isLandscape()) {
                    onBackPressed();
                }
                break;
        }
    }

    @Override
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
    }

    @Override
    public void onSkinChange() {
    }

    @Override
    public void onScreenRotate(boolean isLandscape) {
        uzVideo.setMarginDependOnUZTimeBar(shadow);
        uzVideo.setMarginDependOnUZTimeBar(uzVideo.getBkg());
    }

    @Override
    public void onError(UZException e) {
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
    public void onBackPressed() {
        if (LScreenUtil.isFullScreen(activity)) {
            uzVideo.toggleFullscreen();
        } else {
            super.onBackPressed();
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
