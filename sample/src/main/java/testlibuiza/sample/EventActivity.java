package testlibuiza.sample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.audio.AudioListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.video.VideoListener;

import testlibuiza.R;
import uizacoresdk.UizaCoreSDK;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZLiveContentCallback;
import uizacoresdk.interfaces.UizaAdPlayerCallback;
import uizacoresdk.view.UizaPlayerView;
import uizacoresdk.view.UizaVideoView;
import uizacoresdk.view.VideoViewBase;
import vn.uiza.core.exception.UizaException;
import vn.uiza.models.PlaybackInfo;
import vn.uiza.utils.ScreenUtil;

/**
 * Created by loitp on 1/9/2019.
 */

public class EventActivity extends AppCompatActivity {
    private UizaVideoView uzVideoView;
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
        UizaCoreSDK.setCasty(this);
        UizaCoreSDK.setCurrentPlayerId(R.layout.uz_player_skin_1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        uzVideoView = findViewById(R.id.uiza_video);
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
        uzVideoView.setControllerShowTimeoutMs(5000);
        uzVideoView.addUZCallback(new UZCallback() {
            @Override
            public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, PlaybackInfo playback) {
                tvUzCallback.setText("isInitResult " + isInitSuccess);
            }

            @Override
            public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
                if (isInitMiniPlayerSuccess) {
                    onBackPressed();
                }
            }

            @Override
            public void onSkinChange() {
                tvUzCallback.setText("onSkinChange");
            }

            @Override
            public void onScreenRotate(boolean isLandscape) {
                tvUzCallback.setText("onScreenRotate " + isLandscape);
            }

            @Override
            public void onError(UizaException e) {
                tvUzCallback.setText(e.getMessage());
            }
        });

        uzVideoView.setUizaVideoViewItemClick(view -> {
            if (view.getId() == R.id.exo_back_screen) {
                if (!uzVideoView.isLandscape()) {
                    onBackPressed();
                }
            }
            tvItemClick.setText("onItemClick " + view.getId());
        });

        uzVideoView.addAudioListener(new AudioListener() {
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
        uzVideoView.addPlayerEventListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
                tvPlayerListener.setText("onTimelineChanged");
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                tvPlayerListener.setText("");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                tvPlayerListener.setText("onLoadingChanged " + isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                tvPlayerListener.setText("onPlayerStateChanged " + playWhenReady + " - " + playbackState);
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
        uzVideoView.addMetadataOutput(metadata -> tvMetadataOutput.setText("onMetadata"));
        uzVideoView.addTextOutput(cues -> tvTextOutput.setText("onCues"));

        uzVideoView.addVideoAdPlayerCallback(new UizaAdPlayerCallback() {
            @Override
            public void onPlay() {
                tvAd.setText("onPlay");
            }

            @Override
            public void onVolumeChanged(int i) {
                tvAd.setText("onVolumeChanged " + i);
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
        uzVideoView.addControllerStateCallback(isShow -> tvController.setText("onVisibilityChange " + isShow));
        uzVideoView.setProgressListener(new VideoViewBase.ProgressListener() {
            @Override
            public void onAdProgress(int s, int duration, int percent) {
                tvProgress.setText("onAdProgress " + s + "/" + duration);
            }

            @Override
            public void onAdEnded() {
                tvProgress.setText("onAdEnded");
            }

            @Override
            public void onVideoProgress(long currentMls, int s, long duration, int percent) {
                tvProgress.setText("onVideoProgress " + currentMls + "/" + duration);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                tvProgress.setText("onPlayerStateChanged " + playWhenReady + " - " + playbackState);
            }

            @Override
            public void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration) {
                tvProgress.setText("onBufferProgress " + bufferedPosition + "/" + duration);
            }
        });
        uzVideoView.addOnTouchEvent(new UizaPlayerView.OnTouchEvent() {
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
        uzVideoView.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                tvVideoListener.setText("Current profile " + width + "x" + height);
            }

            @Override
            public void onSurfaceSizeChanged(int width, int height) {
            }
        });
        uzVideoView.addUZLiveContentCallback(new UZLiveContentCallback() {
            @Override
            public void onUpdateLiveInfoTimeStartLive(long duration, String hhmmss) {
            }

            @Override
            public void onUpdateLiveInfoCurrentView(long watchnow) {
                tvLiveInfo.setText("onUpdateLiveInfoCurrentView watchnow: " + watchnow);
            }

            @Override
            public void onLiveStreamUnAvailable() {
                tvLiveInfo.setText(R.string.err_live_is_stopped);
            }
        });
        findViewById(R.id.bt_vod).setOnClickListener(view -> uzVideoView.play("7a2d43cf-a89d-4a25-b653-6aef10619e38"));
        findViewById(R.id.bt_live).setOnClickListener(view -> uzVideoView.play("1692c073-503a-4b4a-be5c-8332961cd5f4", true));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uzVideoView.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        uzVideoView.onResumeView();
    }

    @Override
    public void onPause() {
        super.onPause();
        uzVideoView.onPauseView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzVideoView.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (ScreenUtil.isFullScreen(this)) {
            uzVideoView.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }
}