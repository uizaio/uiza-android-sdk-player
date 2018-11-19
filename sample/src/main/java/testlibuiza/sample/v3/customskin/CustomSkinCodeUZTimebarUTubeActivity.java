package testlibuiza.sample.v3.customskin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.rubensousa.previewseekbar.PreviewView;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.restapi.uiza.model.v2.listallentity.Item;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

/**
 * Created by loitp on 7/16/2018.
 */

public class CustomSkinCodeUZTimebarUTubeActivity extends BaseActivity implements UZCallback {
    private UZVideo uzVideo;
    private View shadow;

    @Override
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected String setTag() {
        return "TAG" + getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_uiza_custom_skin_u_tube;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UZUtil.setCasty(this);
        UZUtil.setCurrentPlayerId(R.layout.u_tube_controller_skin_custom_main);
        super.onCreate(savedInstanceState);
        uzVideo = (UZVideo) findViewById(R.id.uiza_video);

        //listener
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
        uzVideo.addItemClick(new UZItemClick() {
            @Override
            public void onClick(View view) {
                setAutoHideController();
            }
        });

        uzVideo.setPlayerControllerAlwayVisible();

        shadow = (View) uzVideo.findViewById(R.id.bkg_shadow);
        uzVideo.setMarginDependOnUZTimeBar(shadow);

        uzVideo.setBackgroundColorUZVideoRootView(Color.TRANSPARENT);
        uzVideo.setUzTimebarBottom();

        final String entityId = LSApplication.entityIdDefaultVOD;
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

    private void toggleControllerExceptUZTimebar() {
        if (uzVideo.getLlTop() != null) {
            if (uzVideo.getLlTop().getVisibility() == View.VISIBLE) {
                uzVideo.getLlTop().setVisibility(View.INVISIBLE);
                cancelAutoHideController();
            } else {
                uzVideo.getLlTop().setVisibility(View.VISIBLE);
                setAutoHideController();
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

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
        if (isInitSuccess) {
            setAutoHideController();
        }
    }

    @Override
    public void onClickListEntityRelation(Item item, int position) {
    }

    @Override
    public void onClickBack() {
        onBackPressed();
    }

    @Override
    public void onClickPip(Intent intent) {
    }

    @Override
    public void onClickPipVideoInitSuccess(boolean isInitSuccess) {
    }

    @Override
    public void onSkinChange() {
    }

    @Override
    public void onScreenRotate(boolean isLandscape) {
        uzVideo.setMarginDependOnUZTimeBar(shadow);
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
    public void onStart() {
        super.onStart();
        uzVideo.onStart();
    }

    @Override
    public void onStop() {
        uzVideo.onStop();
        cancelAutoHideController();
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (resultCode == Activity.RESULT_OK) {
                uzVideo.initializePiP();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
