package com.uiza.sample.screen.fullscreen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.uiza.sample.R;

import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.player.UzPlayer;
import io.uiza.player.UzPlayerConfig;

public class PortraitFullScreenActivity extends AppCompatActivity {

    private UzPlayer uzPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UzPlayerConfig.setCasty(this);
        UzPlayerConfig.setCurrentSkinRes(R.layout.fullscreen_skin);
        super.onCreate(savedInstanceState);
        Constants.setDebugMode(false);
        setContentView(R.layout.activity_fullscreen_portrait);
        uzPlayer = findViewById(R.id.uiza_video);
        UzDisplayUtil.toggleFullscreen(this);
        uzPlayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT); // optional
        uzPlayer.initVodEntity("78ded059-a268-44e8-8b39-115612c69187");
        uzPlayer.setFreeSize(true); // must be set this line
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

}
