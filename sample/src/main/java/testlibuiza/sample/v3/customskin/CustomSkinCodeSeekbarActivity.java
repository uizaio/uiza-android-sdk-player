package testlibuiza.sample.v3.customskin;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import testlibuiza.R;
import testlibuiza.app.LSApplication;
import uizacoresdk.interfaces.UZItemClickListener;
import uizacoresdk.interfaces.UZPlayerStateChangedListener;
import uizacoresdk.interfaces.UZVideoStateChangedListener;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public class CustomSkinCodeSeekbarActivity extends AppCompatActivity
        implements UZPlayerStateChangedListener, UZItemClickListener {
    private UZVideo uzVideo;
    private SeekBar seekBar;
    private Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        UZUtil.setCasty(this);
        UZUtil.setCurrentPlayerId(R.layout.framgia_controller_skin_custom_main);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiza_custom_skin_code_seekbar);
        uzVideo = findViewById(R.id.uiza_video);
        seekBar = findViewById(R.id.sb);
        uzVideo.setAutoStart(true);
        uzVideo.hideUzTimebar();
        uzVideo.setUzPlayerStateChangedListener(this);
        uzVideo.setUzItemClickListener(this);
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
        uzVideo.setUzVideoStateChangedListener(new UZVideoStateChangedListener() {
            @Override
            public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess,
                    ResultGetLinkPlay resultGetLinkPlay, Data data) {
                if (isInitSuccess) {
                    seekBar.setMax((int) uzVideo.getDuration());
                    updateUISeekbarPosition(false);
                }
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onVideoProgress(long currentMls, int s, long duration, int percent) {
                seekBar.setProgress((int) currentMls);
            }

            @Override
            public void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration) {

            }

            @Override
            public void onVideoEnded() {

            }

            @Override
            public void onError(UZException exception) {

            }
        });
        uzVideo.addControllerStateCallback(
                isShow -> seekBar.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE));
        uzVideo.setControllerShowTimeoutMs(15000);
    }

    private void updateUISeekbarPosition(final boolean isLandscape) {
        uzVideo.post(() -> {
            int huzVideo = LUIUtil.getHeightOfView(uzVideo);
            int hSeekbar = LUIUtil.getHeightOfView(seekBar);
            if (isLandscape) {
                LUIUtil.setMarginPx(seekBar, 0, huzVideo - hSeekbar, 0, 0);
            } else {
                LUIUtil.setMarginPx(seekBar, 0, huzVideo - hSeekbar / 2, 0, 0);
            }
        });
    }

    @Override
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.exo_back_screen:
                if (!uzVideo.isLandscape()) {
                    onBackPressed();
                }
                break;
        }
    }

    @Override
    public void onSkinChanged() {

    }

    @Override
    public void onStateMiniPlayer(boolean isMiniPlayerInitSuccess) {

    }

    @Override
    public void onScreenRotated(boolean isLandscape) {

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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            updateUISeekbarPosition(true);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            updateUISeekbarPosition(false);
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
}
