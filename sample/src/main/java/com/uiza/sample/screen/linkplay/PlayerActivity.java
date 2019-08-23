package com.uiza.sample.screen.linkplay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.uiza.sample.R;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.model.CustomLinkPlay;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.player.UzPlayer;
import io.uiza.player.UzPlayerConfig;
import io.uiza.player.interfaces.UzItemClickListener;
import io.uiza.player.interfaces.UzPlayerEventListener;
import io.uiza.player.interfaces.UzPlayerUiEventListener;
import io.uiza.player.mini.pip.PipHelper;

public class PlayerActivity extends AppCompatActivity implements UzPlayerEventListener,
        UzPlayerUiEventListener, UzItemClickListener {

    private Activity activity;
    private UzPlayer uzPlayer;
    private EditText etLinkPlay;
    private Button btPlay;
    private CustomLinkPlay linkPlay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UzPlayerConfig.setCurrentSkinRes(R.layout.uz_player_skin_1);
        UzPlayerConfig.setCasty(this);
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);
        uzPlayer = findViewById(R.id.uiza_video);
        etLinkPlay = findViewById(R.id.et_link_play);
        btPlay = findViewById(R.id.bt_play);
        btPlay.setEnabled(false);
        uzPlayer.setUzPlayerEventListener(this);
        uzPlayer.setUzPlayerUiEventListener(this);
        uzPlayer.setUzItemClickListener(this);
        // If linkplay is livestream, it will auto move to live edge when onResume is called
        uzPlayer.setAutoMoveToLiveEdge(true);

        etLinkPlay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence == null || charSequence.toString().isEmpty()) {
                    btPlay.setEnabled(false);
                } else {
                    btPlay.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        final CustomLinkPlay uZCustomLinkPlay0 = new CustomLinkPlay();
        uZCustomLinkPlay0.setLinkPlay(
                "https://bitmovin-a.akamaihd.net/content/MI201109210084_1/mpds/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.mpd");
        uZCustomLinkPlay0.setLivestream(false);

        final CustomLinkPlay uZCustomLinkPlay1 = new CustomLinkPlay();
        uZCustomLinkPlay1.setLinkPlay(
                "https://stag-asia-southeast1-live.uizadev.io/998a1a17138644428ce028d2de20c5a0-live/593fd077-313c-4d11-a5ec-fbd66dc43763/playlist_dvr.m3u8");
        uZCustomLinkPlay1.setLivestream(true);

        final CustomLinkPlay uZCustomLinkPlay2 = new CustomLinkPlay();
        uZCustomLinkPlay2.setLinkPlay(
                "https://asia-southeast1-live.uizacdn.net/f785bc511967473fbe6048ee5fb7ea59-live/e3a3d39f-6bd7-4a82-9e90-70bfa9e1f92d/playlist_dvr.m3u8");
        uZCustomLinkPlay2.setLivestream(true);

        final CustomLinkPlay uZCustomLinkPlay3 = new CustomLinkPlay();
        uZCustomLinkPlay3.setLinkPlay(
                "https://s3-ap-southeast-1.amazonaws.com/cdnetwork-test/drm_sample_byterange/manifest.mpd");
        uZCustomLinkPlay3.setLivestream(false);

        findViewById(R.id.bt_0).setOnClickListener(view -> {
            linkPlay = uZCustomLinkPlay0;
            etLinkPlay.setText(linkPlay.getLinkPlay());
            UzDisplayUtil.setLastCursorEditText(etLinkPlay);
        });
        findViewById(R.id.bt_1).setOnClickListener(view -> {
            linkPlay = uZCustomLinkPlay1;
            etLinkPlay.setText(linkPlay.getLinkPlay());
            UzDisplayUtil.setLastCursorEditText(etLinkPlay);
        });
        findViewById(R.id.bt_2).setOnClickListener(view -> {
            linkPlay = uZCustomLinkPlay2;
            etLinkPlay.setText(linkPlay.getLinkPlay());
            UzDisplayUtil.setLastCursorEditText(etLinkPlay);
        });
        findViewById(R.id.bt_3).setOnClickListener(view -> {
            linkPlay = uZCustomLinkPlay3;
            etLinkPlay.setText(linkPlay.getLinkPlay());
            UzDisplayUtil.setLastCursorEditText(etLinkPlay);
        });
        btPlay.setOnClickListener(view -> UzPlayerConfig.initCustomLinkPlay(uzPlayer, linkPlay));
        findViewById(R.id.bt_stats_for_nerds).setOnClickListener(v -> {
            if (uzPlayer != null) {
                uzPlayer.toggleStatsForNerds();
            }
        });
        if (PipHelper.getClickedPip(activity)) {
            btPlay.performClick();
        }
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
    public void onBackPressed() {
        if (uzPlayer.isLandscape()) {
            uzPlayer.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }
}
