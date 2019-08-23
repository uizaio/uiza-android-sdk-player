package com.uiza.sample.screen.event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.audio.AudioListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.video.VideoListener;
import com.uiza.sample.LSApplication;
import com.uiza.sample.R;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.player.UzPlayer;
import io.uiza.player.UzPlayerConfig;
import io.uiza.player.ads.UzAdPlayerCallback;
import io.uiza.player.interfaces.UzAdEventListener;
import io.uiza.player.interfaces.UzLiveInfoListener;
import io.uiza.player.interfaces.UzPlayerEventListener;
import io.uiza.player.interfaces.UzPlayerUiEventListener;
import io.uiza.player.view.UzPlayerView;

public class EventActivity extends AppCompatActivity {

    private Activity activity;
    private UzPlayer uzPlayer;
    private TextView tvUzCallback;
    private TextView tvAudioListener;
    private TextView tvVideoListener;
    private TextView tvPlayerListener;
    private TextView tvMetadataOutput;
    private TextView tvTextOutput;
    private TextView tvAd;
    private TextView tvController;
    private TextView tvProgress;
    private TextView tvTouch;
    private TextView tvItemClick;
    private TextView tvLiveInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UzPlayerConfig.setCasty(this);
        activity = this;
        UzPlayerConfig.setCurrentSkinRes(R.layout.uz_player_skin_1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        uzPlayer = findViewById(R.id.uiza_video);
        tvUzCallback = findViewById(R.id.tv_uz_callback);
        tvAudioListener = findViewById(R.id.tv_audio_listener);
        tvVideoListener = findViewById(R.id.tv_video_listener);
        tvPlayerListener = findViewById(R.id.tv_player_listener);
        tvMetadataOutput = findViewById(R.id.tv_metadata_output);
        tvTextOutput = findViewById(R.id.tv_text_output);
        tvController = findViewById(R.id.tv_controller);
        tvAd = findViewById(R.id.tv_ad);
        tvProgress = findViewById(R.id.tv_progress);
        tvTouch = findViewById(R.id.tv_touch);
        tvItemClick = findViewById(R.id.tv_item_click);
        tvLiveInfo = findViewById(R.id.tv_live_info);
        uzPlayer.setControllerShowTimeoutMs(5000);

        uzPlayer.setUzPlayerEventListener(new UzPlayerEventListener() {
            @Override
            public void onDataInitialized(boolean initSuccess, boolean getDataSuccess,
                    LinkPlay linkPlay, VideoData data) {
                tvUzCallback.setText("onDataInitialized " + initSuccess);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                tvProgress.setText("onPlayerStateChanged " + playWhenReady + " - " + playbackState);
            }

            @Override
            public void onVideoProgress(long currentMls, int s, long duration, int percent) {
                tvProgress.setText("onVideoProgress " + currentMls + "/" + duration);
            }

            @Override
            public void onBufferProgress(long bufferedPosition, int bufferedPercentage,
                    long duration) {
                tvProgress.setText("onBufferProgress " + bufferedPosition + "/" + duration);
            }

            @Override
            public void onVideoEnded() {

            }

            @Override
            public void onPlayerError(UzException exception) {
                tvUzCallback.setText("onPlayerError " + exception.getMessage());
            }

        });

        uzPlayer.setUzPlayerUiEventListener(new UzPlayerUiEventListener() {
            @Override
            public void onSkinChanged() {
                tvUzCallback.setText("onSkinChanged");
            }

            @Override
            public void onStateMiniPlayer(boolean success) {
                if (success) {
                    onBackPressed();
                }
            }

            @Override
            public void onPlayerRotated(boolean isLandscape) {
                tvUzCallback.setText("onPlayerRotated " + isLandscape);
            }
        });

        uzPlayer.setUzItemClickListener(view -> {
            switch (view.getId()) {
                case R.id.exo_back_screen:
                    if (!uzPlayer.isLandscape()) {
                        onBackPressed();
                    }
                    break;
            }
            tvItemClick.setText("onItemClick " + view.getId());
        });

        uzPlayer.addAudioListener(new AudioListener() {
            @Override
            public void onAudioSessionId(int audioSessionId) {
                tvAudioListener.setText("onAudioSessionId " + audioSessionId);
            }

            @Override
            public void onAudioAttributesChanged(AudioAttributes audioAttributes) {
                tvAudioListener.setText("onAudioAttributesChanged");
            }

            @Override
            public void onVolumeChanged(float volume) {
                tvAudioListener.setText("onVolumeChanged " + volume);
            }
        });
        uzPlayer.addPlayerEventListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest,
                    int reason) {
                tvPlayerListener.setText("onTimelineChanged");
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups,
                    TrackSelectionArray trackSelections) {
                tvPlayerListener.setText("");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                tvPlayerListener.setText("onLoadingChanged " + isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                tvPlayerListener
                        .setText("onPlayerStateChanged " + playWhenReady + " - " + playbackState);
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                tvPlayerListener.setText("onRepeatModeChanged " + repeatMode);
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                tvPlayerListener.setText("onShuffleModeEnabledChanged " + shuffleModeEnabled);
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                tvPlayerListener.setText("onPlayerError");
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                tvPlayerListener.setText("onPositionDiscontinuity " + reason);
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                tvPlayerListener.setText("onPlaybackParametersChanged " + playbackParameters.speed);
            }

            @Override
            public void onSeekProcessed() {
                tvPlayerListener.setText("onSeekProcessed");
            }
        });
        uzPlayer.addMetadataOutput(metadata -> tvMetadataOutput.setText("onMetadata"));
        uzPlayer.addTextOutput(cues -> tvTextOutput.setText("onCues"));
        uzPlayer.addVideoAdPlayerCallback(new UzAdPlayerCallback() {
            @Override
            public void onPlay() {
                tvAd.setText("onPlay");
            }

            @Override
            public void onVolumeChanged(int level) {
                tvAd.setText("onVolumeChanged " + level);
            }

            @Override
            public void onPause() {
                tvAd.setText("onPause");
            }

            @Override
            public void onLoaded() {
                tvAd.setText("onLoaded");
            }

            @Override
            public void onResume() {
                tvAd.setText("onResume");
            }

            @Override
            public void onEnded() {
                tvAd.setText("onEnded");
            }

            @Override
            public void onError() {
                tvAd.setText("onError");
            }

            @Override
            public void onBuffering() {
                tvAd.setText("onBuffering");
            }
        });
        uzPlayer.setControllerStateCallback(
                isShow -> tvController.setText("onVisibilityChanged " + isShow));

        uzPlayer.setUzAdEventListener(new UzAdEventListener() {
            @Override
            public void onAdProgress(int s, int duration, int percent) {
                tvProgress.setText("onAdProgress " + s + "/" + duration);
            }

            @Override
            public void onAdEnded() {
                tvProgress.setText("onAdEnded");
            }
        });

        uzPlayer.setOnTouchEvent(new UzPlayerView.OnTouchEvent() {
            @Override
            public void onSingleTapConfirmed(float x, float y) {
                tvTouch.setText("onSingleTapConfirmed");
            }

            @Override
            public void onLongPress(float x, float y) {
                tvTouch.setText("onLongPress");
            }

            @Override
            public void onDoubleTap(float x, float y) {
                tvTouch.setText("onDoubleTap");
            }

            @Override
            public void onSwipeRight() {
                tvTouch.setText("onSwipeRight");
            }

            @Override
            public void onSwipeLeft() {
                tvTouch.setText("onSwipeLeft");
            }

            @Override
            public void onSwipeBottom() {
                tvTouch.setText("onSwipeBottom");
            }

            @Override
            public void onSwipeTop() {
                tvTouch.setText("onSwipeTop");
            }
        });
        uzPlayer.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                    float pixelWidthHeightRatio) {
                tvVideoListener.setText("Current profile " + width + "x" + height);
            }

            @Override
            public void onSurfaceSizeChanged(int width, int height) {
            }
        });
        uzPlayer.setUzLiveInfoListener(new UzLiveInfoListener() {
            @Override
            public void onStartTimeUpdate(long duration, String elapsedTime) {
            }

            @Override
            public void onCurrentViewUpdate(long watchNow) {
                tvLiveInfo.setText("onCurrentViewUpdate watchnow: " + watchNow);
            }

            @Override
            public void onLivestreamUnAvailable() {
                tvLiveInfo.setText(R.string.err_live_is_stopped);
            }
        });
        findViewById(R.id.bt_vod).setOnClickListener(view -> {
            final String entityId = LSApplication.entityIdDefaultVOD;
            UzPlayerConfig.initVodEntity(uzPlayer, entityId);
        });
        findViewById(R.id.bt_live).setOnClickListener(view -> {
            final String entityId = LSApplication.entityIdDefaultLIVE;
            UzPlayerConfig.initVodEntity(uzPlayer, entityId);
        });
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
        if (UzDisplayUtil.isFullScreen(activity)) {
            uzPlayer.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }
}
