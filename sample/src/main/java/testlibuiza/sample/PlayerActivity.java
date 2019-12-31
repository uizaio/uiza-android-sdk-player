package testlibuiza.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import testlibuiza.R;
import timber.log.Timber;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.util.UZDataCLP;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.rl.video.UZVideo;
import uizacoresdk.view.vdh.VDHView;
import vn.uiza.core.exception.UizaException;
import vn.uiza.utils.LUIUtil;
import vn.uiza.restapi.model.v5.PlaybackInfo;
import vn.uiza.utils.ScreenUtil;
import vn.uiza.views.LToast;

/**
 * Created by loitp on 9/1/2019.
 */

public class PlayerActivity extends AppCompatActivity implements UZCallback, VDHView.Callback, UZPlayerView.OnTouchEvent, UZItemClick,
        UZPlayerView.ControllerStateCallback {
    private UZVideo uzVideo;
    private VDHView vdhv;

    private EditText etLinkPlay;
    HorizontalScrollView llBottom;
    private Button btPlay;
    private PlaybackInfo playbackInfo;

    private boolean isLive = false;

    private static final String[] urls = new String[]{
            "https://bitmovin-a.akamaihd.net/content/MI201109210084_1/mpds/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.mpd",
            "https://mnmedias.api.telequebec.tv/m3u8/29880.m3u8",
            "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
            "https://s3-ap-southeast-1.amazonaws.com/cdnetwork-test/drm_sample_byterange/manifest.mpd"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UZUtil.setUseWithVDHView(true);
        UZUtil.setCasty(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        uzVideo = findViewById(R.id.uiza_video);
        vdhv = findViewById(R.id.vdhv);
        llBottom = findViewById(R.id.hsv_bottom);
        etLinkPlay = findViewById(R.id.et_link_play);
        btPlay = findViewById(R.id.bt_play);
        vdhv.setCallback(this);
        vdhv.setOnTouchEvent(this);
        vdhv.setScreenRotate(false);
        uzVideo.addUZCallback(this);
        uzVideo.addItemClick(this);
        uzVideo.addControllerStateCallback(this);
        // If linkplay is livestream, it will auto move to live edge when onResume is called
        uzVideo.setAutoMoveToLiveEdge(true);
        uzVideo.hideDebugTextView(true);
        playbackInfo = getIntent().getParcelableExtra("extra_playback_info");
        if (playbackInfo != null) {
            llBottom.setVisibility(View.GONE);
            etLinkPlay.setVisibility(View.GONE);
            btPlay.setVisibility(View.GONE);
        } else {
            llBottom.setVisibility(View.VISIBLE);
            etLinkPlay.setVisibility(View.VISIBLE);
            btPlay.setVisibility(View.VISIBLE);
        }
        btPlay.setEnabled(playbackInfo != null);
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

        findViewById(R.id.bt_0).setOnClickListener(view -> {
            etLinkPlay.setText(urls[0]);
            LUIUtil.setLastCursorEditText(etLinkPlay);
        });
        findViewById(R.id.bt_1).setOnClickListener(view -> {
            etLinkPlay.setText(urls[1]);
            isLive = true;
            LUIUtil.setLastCursorEditText(etLinkPlay);
        });
        findViewById(R.id.bt_2).setOnClickListener(view -> {
            etLinkPlay.setText(urls[2]);
            isLive = true;
            LUIUtil.setLastCursorEditText(etLinkPlay);
        });
        findViewById(R.id.bt_3).setOnClickListener(view -> {
            etLinkPlay.setText(urls[3]);
            LUIUtil.setLastCursorEditText(etLinkPlay);
        });

        btPlay.setOnClickListener(view -> {
            final PlaybackInfo playback = new PlaybackInfo();
            playback.setHls(etLinkPlay.getText().toString());
            playback.setLive(isLive);
            UZDataCLP.getInstance().setPlaybackInfo(playback);
            boolean isInitSuccess = UZUtil.initCustomLinkPlay(uzVideo);
            if (!isInitSuccess) {
                LToast.show(this, "Init failed");
            }
        });
        findViewById(R.id.bt_stats_for_nerds).setOnClickListener(v -> {
            if (uzVideo != null) {
                uzVideo.toggleStatsForNerds();
            }
        });
        if (playbackInfo != null) {
            boolean isInitSuccess = UZUtil.initEntity(uzVideo, playbackInfo);
            if (!isInitSuccess) {
                LToast.show(this, "Init failed");
            }
        }
        if (UZUtil.getClickedPip(this)) {
            btPlay.performClick();
        }
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, PlaybackInfo data) {
        vdhv.setInitResult(isInitSuccess);
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
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
        if (isInitMiniPlayerSuccess) {
            onBackPressed();
        }
    }

    @Override
    public void onSkinChange() {
    }

    @Override
    public void onScreenRotate(boolean isLandscape) {
        if (!isLandscape) {
            int w = ScreenUtil.getScreenWidth();
            int h = w * 9 / 16;
            uzVideo.setFreeSize(false);
            uzVideo.setSize(w, h);
        }
        vdhv.setScreenRotate(isLandscape);
    }

    @Override
    public void onError(UizaException e) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uzVideo.onDestroy();
        UZUtil.setUseWithVDHView(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        uzVideo.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        vdhv.onPause();
        uzVideo.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzVideo.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (uzVideo.isLandscape()) {
            uzVideo.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }

    private void updateUIRevertMaxChange(boolean isEnableRevertMaxSize) {
        if (isEnableRevertMaxSize && vdhv.isAppear()) {
            // todo
        }
    }

    @Override
    public void onViewSizeChange(boolean isMaximizeView) {

    }

    @Override
    public void onStateChange(VDHView.State state) {

    }

    @Override
    public void onPartChange(VDHView.Part part) {

    }

    @Override
    public void onViewPositionChanged(int left, int top, float dragOffset) {

    }

    @Override
    public void onOverScroll(VDHView.State state, VDHView.Part part) {
        uzVideo.pauseVideo();
        vdhv.dissappear();
    }

    @Override
    public void onEnableRevertMaxSize(boolean isEnableRevertMaxSize) {
        updateUIRevertMaxChange(!isEnableRevertMaxSize);
    }

    @Override
    public void onAppear(boolean isAppear) {
        updateUIRevertMaxChange(vdhv.isEnableRevertMaxSize());
    }

    @Override
    public void onSingleTapConfirmed(float x, float y) {
        uzVideo.toggleShowHideController();
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
    @Override
    public void onVisibilityChange(boolean isShow) {
        vdhv.setVisibilityChange(isShow);
    }
}
