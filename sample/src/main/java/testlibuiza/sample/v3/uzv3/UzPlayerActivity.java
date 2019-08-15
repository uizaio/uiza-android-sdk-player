package testlibuiza.sample.v3.uzv3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.google.android.exoplayer2.Player;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.constant.Constants;
import io.uiza.player.UzPlayer;
import io.uiza.player.UzPlayerConfig;
import io.uiza.player.interfaces.UzAdEventListener;
import io.uiza.player.interfaces.UzItemClickListener;
import io.uiza.player.interfaces.UzPlayerEventListener;
import io.uiza.player.interfaces.UzPlayerUiEventListener;
import io.uiza.player.util.UzPlayerData;
import io.uiza.player.view.UzPlayerView;
import testlibuiza.R;

public class UzPlayerActivity extends AppCompatActivity implements UzPlayerEventListener,
        UzPlayerUiEventListener, UzItemClickListener {

    private Activity activity;
    private UzPlayer uzPlayer;
    private TextView tvProgressAd;
    private TextView tvProgressVideo;
    private TextView tvStateVideo;
    private TextView tvBuffer;
    private TextView tvClickEvent;
    private TextView tvScreenRotate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        UzPlayerConfig.setCasty(this);
        UzPlayerConfig.setCurrentSkinRes(R.layout.uz_player_skin_1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v3_cannot_slide_player);
        uzPlayer = findViewById(R.id.uiza_video);
        uzPlayer.setAutoSwitchItemPlaylistFolder(true);
        uzPlayer.setAutoStart(true);
        tvProgressAd = findViewById(R.id.tv_progress_ad);
        tvProgressVideo = findViewById(R.id.tv_progress_video);
        tvStateVideo = findViewById(R.id.tv_state_video);
        tvBuffer = findViewById(R.id.tv_buffer);
        tvClickEvent = findViewById(R.id.tv_click_event);
        tvScreenRotate = findViewById(R.id.tv_screen_rotate);
        uzPlayer.setUzPlayerEventListener(this);
        uzPlayer.setUzPlayerUiEventListener(this);
        uzPlayer.setUzItemClickListener(this);
        uzPlayer.setAutoMoveToLiveEdge(true);
        String metadataId = getIntent().getStringExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID);
        if (metadataId == null) {
            String entityId = getIntent().getStringExtra(Constants.KEY_UIZA_ENTITY_ID);
            boolean isLive = getIntent().getBooleanExtra(Constants.KEY_UIZA_IS_LIVE, false);
            if (entityId == null) {
                boolean isInitWithPlaylistFolder = UzPlayerData.isInitPlaylistFolder(activity);
                if (isInitWithPlaylistFolder) {
                    UzPlayerConfig.initPlaylistFolder(uzPlayer, metadataId);
                } else {
                    UzPlayerConfig.initVodEntity(uzPlayer, entityId);
                }
            } else {
                if (isLive) {
                    UzPlayerConfig.initLiveEntity(uzPlayer, entityId);
                } else {
                    UzPlayerConfig.initVodEntity(uzPlayer, entityId);
                }
            }
        } else {
            UzPlayerConfig.initPlaylistFolder(uzPlayer, metadataId);
        }
        uzPlayer.setOnTouchEvent(new UzPlayerView.OnTouchEvent() {
            @Override
            public void onSingleTapConfirmed(float x, float y) {
                tvClickEvent.setText("onSingleTapConfirmed");
            }

            @Override
            public void onLongPress(float x, float y) {
                tvClickEvent.setText("onLongPress");
            }

            @Override
            public void onDoubleTap(float x, float y) {
                tvClickEvent.setText("onDoubleTap");
            }

            @Override
            public void onSwipeRight() {
                tvClickEvent.setText("onSwipeRight");
            }

            @Override
            public void onSwipeLeft() {
                tvClickEvent.setText("onSwipeLeft");
            }

            @Override
            public void onSwipeBottom() {
                tvClickEvent.setText("onSwipeBottom");
            }

            @Override
            public void onSwipeTop() {
                tvClickEvent.setText("onSwipeTop");
            }
        });
        findViewById(R.id.bt_play).setOnClickListener(view -> uzPlayer.resume());
        findViewById(R.id.bt_pause).setOnClickListener(view -> uzPlayer.pause());
        findViewById(R.id.bt_next_10).setOnClickListener(view -> uzPlayer.seekToForward(10000));
        findViewById(R.id.bt_prev_10).setOnClickListener(view -> uzPlayer.seekToBackward(10000));
        findViewById(R.id.bt_volume_on_off).setOnClickListener(view -> uzPlayer.toggleVolume());
        findViewById(R.id.bt_toggle_fullscreen).setOnClickListener(
                view -> uzPlayer.toggleFullscreen());
        findViewById(R.id.bt_cc).setOnClickListener(view -> uzPlayer.showSubTitlePopup());
        findViewById(R.id.bt_hq).setOnClickListener(view -> uzPlayer.showQualityPopup());
        findViewById(R.id.bt_speed).setOnClickListener(view -> uzPlayer.showSpeed());
        findViewById(R.id.bt_next_video).setOnClickListener(view -> uzPlayer.nextVideo());
        findViewById(R.id.bt_prev_video).setOnClickListener(view -> uzPlayer.previousVideo());

        findViewById(R.id.bt_stats_for_nerds).setOnClickListener(
                v -> uzPlayer.toggleStatsForNerds());
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

    private void setListener() {
        if (uzPlayer == null || uzPlayer.getExoPlayer() == null) {
            return;
        }
        uzPlayer.setUzAdEventListener(new UzAdEventListener() {
            @Override
            public void onAdProgress(int s, int duration, int percent) {
                tvProgressAd.setText("Ad: " + s + "/" + duration + " (s) => " + percent + "%");
            }

            @Override
            public void onAdEnded() {
                tvProgressAd.setText("onAdEnded");
            }
        });
    }

    @Override
    public void onDataInitialized(boolean initSuccess, boolean getDataSuccess, LinkPlay linkPlay,
            VideoData data) {
        if (initSuccess) {
            setListener();
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_BUFFERING) {
            tvStateVideo.setText("onPlayerStateChanged STATE_BUFFERING, playWhenReady: "
                    + playWhenReady);
        } else if (playbackState == Player.STATE_IDLE) {
            tvStateVideo.setText(
                    "onPlayerStateChanged STATE_IDLE, playWhenReady: " + playWhenReady);
        } else if (playbackState == Player.STATE_READY) {
            tvStateVideo.setText(
                    "onPlayerStateChanged STATE_READY, playWhenReady: " + playWhenReady);
        } else if (playbackState == Player.STATE_ENDED) {
            tvStateVideo.setText(
                    "onPlayerStateChanged STATE_ENDED, playWhenReady: " + playWhenReady);
        }
    }

    @Override
    public void onVideoProgress(long currentMls, int s, long duration, int percent) {
        tvProgressVideo.setText(
                "Video: " + currentMls + "/" + duration + " (mls) => " + percent + "%");
    }

    @Override
    public void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration) {
        tvBuffer.setText(
                "onBufferProgress bufferedPosition: " + bufferedPosition + "/" + duration
                        + "(mls), bufferedPercentage: " + bufferedPercentage + "%");
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
        tvScreenRotate.setText("onPlayerRotated isLandScape " + isLandscape);
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
