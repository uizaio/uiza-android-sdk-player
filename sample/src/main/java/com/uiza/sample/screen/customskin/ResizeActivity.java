package com.uiza.sample.screen.customskin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
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
import java.util.Random;

public class ResizeActivity extends AppCompatActivity implements UzPlayerEventListener,
        UzPlayerUiEventListener, UzItemClickListener {

    private UzPlayer uzPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UzPlayerConfig.setCasty(this);
        UzPlayerConfig.setCurrentSkinRes(R.layout.uz_player_skin_1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resize);
        uzPlayer = findViewById(R.id.uiza_video);
        uzPlayer.setUzPlayerUiEventListener(this);
        uzPlayer.setUzPlayerEventListener(this);
        uzPlayer.setUzItemClickListener(this);

        final String entityId = LSApplication.entityIdDefaultVODportrait;
        UzPlayerConfig.initVodEntity(uzPlayer, entityId);

        findViewById(R.id.bt_bkg_ran).setOnClickListener(
                view -> uzPlayer.setBackgroundColorBkg(getRandomColor()));
        findViewById(R.id.bt_bkg_red).setOnClickListener(
                view -> uzPlayer.setBackgroundColorBkg(Color.RED));
        findViewById(R.id.bt_0).setOnClickListener(
                view -> uzPlayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT));
        findViewById(R.id.bt_1).setOnClickListener(
                view -> uzPlayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH));
        findViewById(R.id.bt_2).setOnClickListener(
                view -> uzPlayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM));
        findViewById(R.id.bt_3).setOnClickListener(
                view -> uzPlayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL));
        findViewById(R.id.bt_4).setOnClickListener(
                view -> uzPlayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT));
        findViewById(R.id.bt_5).setOnClickListener(view -> {
            if (uzPlayer == null) {
                return;
            }
            uzPlayer.setFreeSize(true);
        });
        findViewById(R.id.bt_6).setOnClickListener(view -> {
            if (uzPlayer == null) {
                return;
            }
            int w = UzDisplayUtil.getScreenWidth();
            int h = w * 9 / 16;
            uzPlayer.setFreeSize(false);
            uzPlayer.setSize(w, h);
        });
    }

    private int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzPlayer.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDataInitialized(boolean initSuccess, boolean getDataSuccess, LinkPlay linkPlay,
            VideoData data) {

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
        if (isInitMiniPlayerSuccess) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (uzPlayer.isLandscape()) {
            uzPlayer.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }
}
