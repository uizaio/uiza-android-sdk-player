package testlibuiza.sample.v3.customskin;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.SeekBar;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v2.listallentity.Item;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.uzv1.listerner.ProgressCallback;
import vn.uiza.uzv3.util.UZUtil;
import vn.uiza.uzv3.view.UZPlayerView;
import vn.uiza.uzv3.view.rl.video.UZCallback;
import vn.uiza.uzv3.view.rl.video.UZVideo;

/**
 * Created by loitp on 7/16/2018.
 */

public class CustomSkinCodeActivity extends BaseActivity implements UZCallback {
    private UZVideo uzVideo;
    private SeekBar seekBar;

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
        return R.layout.activity_uiza_custom_skin_code;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UZUtil.setCasty(this);
        UZUtil.setCurrentPlayerId(R.layout.framgia_controller_skin_custom_main);
        super.onCreate(savedInstanceState);
        uzVideo = (UZVideo) findViewById(R.id.uiza_video);
        seekBar = (SeekBar) findViewById(R.id.sb);
        uzVideo.hideUzTimebar();
        uzVideo.setUZCallback(this);
        final String entityId = LSApplication.entityIdDefaultVOD;
        UZUtil.initEntity(activity, uzVideo, entityId);
        seekBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                uzVideo.onStopPreview(seekBar.getProgress());
            }
        });
        uzVideo.setProgressCallback(new ProgressCallback() {
            @Override
            public void onAdProgress(float currentMls, int s, float duration, int percent) {
            }

            @Override
            public void onVideoProgress(float currentMls, int s, float duration, int percent) {
                seekBar.setProgress((int) currentMls);
            }
        });
        uzVideo.setControllerStateCallback(new UZPlayerView.ControllerStateCallback() {
            @Override
            public void onVisibilityChange(boolean isShow) {
                seekBar.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
        if (isInitSuccess) {
            uzVideo.setEventBusMsgFromActivityIsInitSuccess();
            seekBar.setMax((int) uzVideo.getDuration());
            updateUISeekbarPosition(false);
        }
    }

    private void updateUISeekbarPosition(final boolean isLandscape) {
        uzVideo.post(new Runnable() {
            @Override
            public void run() {
                int huzVideo = LUIUtil.getHeightOfView(uzVideo);
                int hSeekbar = LUIUtil.getHeightOfView(seekBar);
                if (isLandscape) {
                    LUIUtil.setMarginPx(seekBar, 0, huzVideo - hSeekbar, 0, 0);
                } else {
                    LUIUtil.setMarginPx(seekBar, 0, huzVideo - hSeekbar / 2, 0, 0);
                }
            }
        });
    }

    @Override
    public void onClickListEntityRelation(Item item, int position) {
    }

    @Override
    public void onClickBack() {
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
    public void onError(Exception e) {
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
        super.onStop();
        uzVideo.onStop();
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            updateUISeekbarPosition(true);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            updateUISeekbarPosition(false);
        }
    }
}
