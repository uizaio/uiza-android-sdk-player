package com.uiza.sample.screen.customskin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.uiza.sample.R;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.player.UzPlayer;
import io.uiza.player.UzPlayerConfig;
import io.uiza.player.interfaces.UzItemClickListener;
import io.uiza.player.interfaces.UzPlayerEventListener;
import io.uiza.player.interfaces.UzPlayerUiEventListener;
import io.uiza.player.util.UzPlayerData;

public class CustomSkinCodeUzTimebarActivityListener extends AppCompatActivity implements
        UzPlayerEventListener, UzPlayerUiEventListener, UzItemClickListener {

    private Activity activity;
    private UzPlayer uzPlayer;
    private View shadow;
    private LinearLayout ll;
    private ProgressBar pb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        UzPlayerConfig.setCasty(this);
        UzPlayerConfig.setCurrentSkinRes(R.layout.framgia_controller_skin_custom_main_1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiza_custom_skin_code_uz_timebar);
        uzPlayer = findViewById(R.id.uiza_video);
        ll = findViewById(R.id.ll);
        UzDisplayUtil.setMarginDimen(ll, 0, -uzPlayer.getPixelAdded() / 2, 0, 0);
        pb = findViewById(R.id.p);
        pb.setVisibility(View.VISIBLE);
        uzPlayer.setUzPlayerUiEventListener(this);
        uzPlayer.setUzPlayerEventListener(this);
        uzPlayer.setUzItemClickListener(this);
        shadow = uzPlayer.findViewById(R.id.bkg_shadow);
        uzPlayer.setMarginDependOnUzTimeBar(shadow);
        uzPlayer.setMarginDependOnUzTimeBar(uzPlayer.getBkg());
        checkId(getIntent());
    }

    private void checkId(Intent intent) {
        if (intent == null) {
            return;
        }
        String thumb = intent.getStringExtra(Constants.KEY_UIZA_THUMBNAIL);
        uzPlayer.setUrlImgThumbnail(thumb);
        String metadataId = intent.getStringExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID);
        if (metadataId == null) {
            String entityId = intent.getStringExtra(Constants.KEY_UIZA_ENTITY_ID);
            if (entityId == null) {
                boolean isInitWithPlaylistFolder = UzPlayerData.isInitPlaylistFolder(activity);
                if (isInitWithPlaylistFolder) {
                    UzPlayerConfig.initPlaylistFolder(uzPlayer, null);
                } else {
                    UzPlayerConfig.initVodEntity(uzPlayer, null);
                }
            } else {
                UzPlayerConfig.initVodEntity(uzPlayer, entityId);
            }
        } else {
            UzPlayerConfig.initPlaylistFolder(uzPlayer, metadataId);
        }
    }

    @Override
    public void onDataInitialized(boolean initSuccess, boolean getDataSuccess, LinkPlay linkPlay,
            VideoData data) {
        if (initSuccess) {
            pb.setVisibility(View.GONE);
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
        uzPlayer.setMarginDependOnUzTimeBar(uzPlayer.getBkg());
        uzPlayer.setMarginDependOnUzTimeBar(shadow);
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
    public void onBackPressed() {
        if (uzPlayer.isLandscape()) {
            uzPlayer.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }
}
