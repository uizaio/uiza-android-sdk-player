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
import io.uiza.player.UzPlayer;
import io.uiza.player.UzPlayerConfig;
import io.uiza.player.interfaces.UzAdEventListener;
import io.uiza.player.interfaces.UzItemClickListener;
import io.uiza.player.interfaces.UzPlayerEventListener;
import io.uiza.player.interfaces.UzPlayerUiEventListener;
import io.uiza.player.mini.draggable.UzDraggableLayout;
import io.uiza.player.util.UzPlayerData;
import io.uiza.player.view.UzPlayerView;
import testlibuiza.R;

public class Slide0Activity extends AppCompatActivity implements UzDraggableLayout.Callback,
        UzPlayerUiEventListener, UzPlayerEventListener, UzItemClickListener, UzAdEventListener,
        UzPlayerView.OnTouchEvent, UzPlayerView.ControllerStateCallback, View.OnClickListener {

    private final String TAG = "TAG" + getClass().getSimpleName();
    private Activity activity;
    private UzDraggableLayout draggableLayout;
    private TextView tv0, tv1, tv2, tv3;
    private UzPlayer uzPlayer;

    private void findViews() {
        uzPlayer = findViewById(R.id.uiza_video);
        draggableLayout = findViewById(R.id.vdhv);
        tv0 = findViewById(R.id.tv_0);
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);
        draggableLayout.setCallback(this);
        draggableLayout.setOnTouchEvent(this);
        draggableLayout.setScreenRotate(false);
        uzPlayer.setUzPlayerUiEventListener(this);
        uzPlayer.setUzPlayerEventListener(this);
        uzPlayer.setUzAdEventListener(this);
        uzPlayer.setUzItemClickListener(this);
        uzPlayer.setControllerStateCallback(this);
        findViewById(R.id.bt_minimize_bottom_left).setOnClickListener(this);
        findViewById(R.id.bt_minimize_bottom_right).setOnClickListener(this);
        findViewById(R.id.bt_minimize_top_right).setOnClickListener(this);
        findViewById(R.id.bt_minimize_top_left).setOnClickListener(this);
        findViewById(R.id.bt_appear).setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        UzPlayerConfig.setCurrentSkinRes(R.layout.uz_player_skin_1);
        UzPlayerConfig.setUseDraggableLayout(true);
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
                boolean isInitWithPlaylistFolder = UzPlayerData.isInitPlaylistFolder(activity);
                if (isInitWithPlaylistFolder) {
                    UzPlayerConfig.initPlaylistFolder(uzPlayer, metadataId);
                } else {
                    UzPlayerConfig.initVodEntity(uzPlayer, entityId);
                }
            } else {
                UzPlayerConfig.initVodEntity(uzPlayer, entityId);
            }
        } else {
            UzPlayerConfig.initPlaylistFolder(uzPlayer, metadataId);
        }
    }

    private void updateUIRevertMaxChange(boolean isEnableRevertMaxSize) {
        if (isEnableRevertMaxSize && draggableLayout.isAppear()) {
            findViewById(R.id.bt_minimize_bottom_left).setVisibility(View.VISIBLE);
            findViewById(R.id.bt_minimize_bottom_right).setVisibility(View.VISIBLE);
            if (draggableLayout.isMinimizedAtLeastOneTime()) {
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
    public void onStateChange(UzDraggableLayout.State state) {
        tv0.setText("onStateChange: " + state.name());
    }

    @Override
    public void onPartChange(UzDraggableLayout.Part part) {
        tv2.setText("onPartChange: " + part.name());
    }

    @Override
    public void onViewPositionChanged(int left, int top, float dragOffset) {
        tv1.setText("onViewPositionChanged left: " + left + ", top: " + top + ", dragOffset: "
                + dragOffset);
    }

    @Override
    public void onOverScroll(UzDraggableLayout.State state, UzDraggableLayout.Part part) {
        uzPlayer.pause();
        draggableLayout.disAppear();
    }

    @Override
    public void onEnableRevertMaxSize(boolean isEnableRevertMaxSize) {
        updateUIRevertMaxChange(!isEnableRevertMaxSize);
    }

    @Override
    public void onAppear(boolean isAppear) {
        findViewById(R.id.bt_appear).setVisibility(isAppear ? View.INVISIBLE : View.VISIBLE);
        updateUIRevertMaxChange(draggableLayout.isEnableRevertMaxSize());
    }

    @Override
    protected void onPause() {
        super.onPause();
        draggableLayout.onPause();
        uzPlayer.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uzPlayer.onDestroy();
        UzPlayerConfig.setUseDraggableLayout(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        uzPlayer.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzPlayer.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDataInitialized(boolean initSuccess, boolean getDataSuccess, LinkPlay linkPlay,
            VideoData data) {
        draggableLayout.setInitResult(initSuccess);
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
        if (!isLandscape) {
            int w = UzDisplayUtil.getScreenWidth();
            int h = w * 9 / 16;
            uzPlayer.setFreeSize(false);
            uzPlayer.setSize(w, h);
        }
        draggableLayout.setScreenRotate(isLandscape);
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
            uzPlayer.getIbBackScreenIcon().performClick();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSingleTapConfirmed(float x, float y) {
        uzPlayer.toggleShowHideController();
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
    public void onVisibilityChanged(boolean isShow) {
        draggableLayout.setVisibilityChange(isShow);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_minimize_bottom_left:
                draggableLayout.minimizeBottomLeft();
                break;
            case R.id.bt_minimize_bottom_right:
                draggableLayout.minimizeBottomRight();
                break;
            case R.id.bt_minimize_top_right:
                draggableLayout.minimizeTopRight();
                break;
            case R.id.bt_minimize_top_left:
                draggableLayout.minimizeTopLeft();
                break;
            case R.id.bt_appear:
                draggableLayout.appear();
                uzPlayer.resume();
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
