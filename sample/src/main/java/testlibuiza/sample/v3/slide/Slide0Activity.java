package testlibuiza.sample.v3.slide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import testlibuiza.R;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.listerner.ProgressCallback;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.rl.video.UZVideo;
import uizacoresdk.view.vdh.VDHView;

public class Slide0Activity extends AppCompatActivity implements VDHView.Callback, UZCallback, UZItemClick, UZPlayerView.OnTouchEvent, UZPlayerView.ControllerStateCallback, View.OnClickListener, ProgressCallback {
    private final String TAG = "TAG" + getClass().getSimpleName();
    private Activity activity;
    private VDHView vdhv;
    private TextView tv0, tv1, tv2, tv3;
    private UZVideo uzVideo;

    private void findViews() {
        uzVideo = (UZVideo) findViewById(R.id.uiza_video);
        vdhv = (VDHView) findViewById(R.id.vdhv);
        tv0 = (TextView) findViewById(R.id.tv_0);
        tv1 = (TextView) findViewById(R.id.tv_1);
        tv2 = (TextView) findViewById(R.id.tv_2);
        tv3 = (TextView) findViewById(R.id.tv_3);
        vdhv.setCallback(this);
        vdhv.setOnTouchEvent(this);
        vdhv.setScreenRotate(false);
        uzVideo.addUZCallback(this);
        uzVideo.addItemClick(this);
        uzVideo.addControllerStateCallback(this);
        uzVideo.addProgressCallback(this);
        findViewById(R.id.bt_minimize_bottom_left).setOnClickListener(this);
        findViewById(R.id.bt_minimize_bottom_right).setOnClickListener(this);
        findViewById(R.id.bt_minimize_top_right).setOnClickListener(this);
        findViewById(R.id.bt_minimize_top_left).setOnClickListener(this);
        findViewById(R.id.bt_appear).setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_1);
        UZUtil.setUseWithVDHView(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_0);
        findViews();
        setupContent();
    }

    private void setupContent() {
        String metadataId = getIntent().getStringExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID);
        if (metadataId == null) {
            String entityId = getIntent().getStringExtra(Constants.KEY_UIZA_ENTITY_ID);
            if (entityId == null) {
                boolean isInitWithPlaylistFolder = UZUtil.isInitPlaylistFolder(activity);
                if (isInitWithPlaylistFolder) {
                    UZUtil.initPlaylistFolder(activity, uzVideo, metadataId);
                } else {
                    UZUtil.initEntity(activity, uzVideo, entityId);
                }
            } else {
                UZUtil.initEntity(activity, uzVideo, entityId);
            }
        } else {
            UZUtil.initPlaylistFolder(activity, uzVideo, metadataId);
        }
    }

    private void updateUIRevertMaxChange(boolean isEnableRevertMaxSize) {
        if (isEnableRevertMaxSize && vdhv.isAppear()) {
            findViewById(R.id.bt_minimize_bottom_left).setVisibility(View.VISIBLE);
            findViewById(R.id.bt_minimize_bottom_right).setVisibility(View.VISIBLE);
            if (vdhv.isMinimizedAtLeastOneTime()) {
                findViewById(R.id.bt_minimize_top_right).setVisibility(View.VISIBLE);
                findViewById(R.id.bt_minimize_top_left).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.bt_minimize_top_right).setVisibility(View.INVISIBLE);
                findViewById(R.id.bt_minimize_top_left).setVisibility(View.INVISIBLE);
            }
        } else {
            findViewById(R.id.bt_minimize_top_right).setVisibility(View.INVISIBLE);
            findViewById(R.id.bt_minimize_top_left).setVisibility(View.INVISIBLE);
            findViewById(R.id.bt_minimize_bottom_left).setVisibility(View.INVISIBLE);
            findViewById(R.id.bt_minimize_bottom_right).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onViewSizeChange(boolean isMaximizeView) {
        tv3.setText("onViewSizeChange isMaximizeView: " + isMaximizeView);
    }

    @Override
    public void onStateChange(VDHView.State state) {
        tv0.setText("onStateChange: " + state.name());
    }

    @Override
    public void onPartChange(VDHView.Part part) {
        tv2.setText("onPartChange: " + part.name());
    }

    @Override
    public void onViewPositionChanged(int left, int top, float dragOffset) {
        tv1.setText("onViewPositionChanged left: " + left + ", top: " + top + ", dragOffset: " + dragOffset);
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
        findViewById(R.id.bt_appear).setVisibility(isAppear ? View.INVISIBLE : View.VISIBLE);
        updateUIRevertMaxChange(vdhv.isEnableRevertMaxSize());
    }

    @Override
    protected void onPause() {
        super.onPause();
        vdhv.onPause();
        uzVideo.onPause();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzVideo.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, LinkPlay linkPlay, VideoData data) {
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
            int w = UzDisplayUtil.getScreenWidth();
            int h = w * 9 / 16;
            uzVideo.setFreeSize(false);
            uzVideo.setSize(w, h);
        }
        vdhv.setScreenRotate(isLandscape);
    }

    @Override
    public void onError(UzException e) {
    }

    @Override
    public void onBackPressed() {
        if (uzVideo.isLandscape()) {
            uzVideo.getIbBackScreenIcon().performClick();
        } else {
            super.onBackPressed();
        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_minimize_bottom_left:
                vdhv.minimizeBottomLeft();
                break;
            case R.id.bt_minimize_bottom_right:
                vdhv.minimizeBottomRight();
                break;
            case R.id.bt_minimize_top_right:
                vdhv.minimizeTopRight();
                break;
            case R.id.bt_minimize_top_left:
                vdhv.minimizeTopLeft();
                break;
            case R.id.bt_appear:
                vdhv.appear();
                uzVideo.resumeVideo();
                break;
        }
    }

    @Override
    public void onAdProgress(int s, int duration, int percent) {
    }

    @Override
    public void onAdEnded() {
    }

    @Override
    public void onVideoProgress(long currentMls, int s, long duration, int percent) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
    }

    @Override
    public void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration) {
    }
}
