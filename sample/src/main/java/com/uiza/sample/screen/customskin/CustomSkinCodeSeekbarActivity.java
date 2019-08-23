package com.uiza.sample.screen.customskin;

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
import com.uiza.sample.LSApplication;
import com.uiza.sample.R;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.player.UzPlayer;
import io.uiza.player.UzPlayerConfig;
import io.uiza.player.interfaces.UzItemClickListener;
import io.uiza.player.interfaces.UzPlayerEventListener;
import io.uiza.player.interfaces.UzPlayerUiEventListener;

public class CustomSkinCodeSeekbarActivity extends AppCompatActivity implements
        UzPlayerUiEventListener, UzPlayerEventListener, UzItemClickListener {

    private final String TAG = getClass().getSimpleName();
    private UzPlayer uzPlayer;
    private SeekBar seekBar;
    private Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        UzPlayerConfig.setCasty(this);
        UzPlayerConfig.setCurrentSkinRes(R.layout.framgia_controller_skin_custom_main);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiza_custom_skin_code_seekbar);
        uzPlayer = findViewById(R.id.uiza_video);
        seekBar = findViewById(R.id.sb);
        uzPlayer.setAutoStart(true);
        uzPlayer.hideUzTimeBar();
        uzPlayer.setUzPlayerEventListener(this);
        uzPlayer.setUzPlayerUiEventListener(this);
        uzPlayer.setUzItemClickListener(this);
        final String entityId = LSApplication.entityIdDefaultVOD;
        UzPlayerConfig.initVodEntity(uzPlayer, entityId);
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
                uzPlayer.onStopPreview(seekBar.getProgress());
            }
        });
        uzPlayer.setControllerStateCallback(
                isShow -> seekBar.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE));
        uzPlayer.setControllerShowTimeoutMs(15000);
    }

    @Override
    public void onDataInitialized(boolean initSuccess, boolean getDataSuccess, LinkPlay linkPlay,
            VideoData data) {
        if (initSuccess) {
            seekBar.setMax((int) uzPlayer.getDuration());
            updateUISeekbarPosition(false);
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

    }

    private void updateUISeekbarPosition(final boolean isLandscape) {
        uzPlayer.post(() -> {
            int huzVideo = UzDisplayUtil.getHeightOfView(uzPlayer);
            int hSeekbar = UzDisplayUtil.getHeightOfView(seekBar);
            if (isLandscape) {
                UzDisplayUtil.setMarginPx(seekBar, 0, huzVideo - hSeekbar, 0, 0);
            } else {
                UzDisplayUtil.setMarginPx(seekBar, 0, huzVideo - hSeekbar / 2, 0, 0);
            }
        });
    }

    @Override
    public void onItemClick(View view) {
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
        if (UzDisplayUtil.isFullScreen(activity)) {
            uzPlayer.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }
}
