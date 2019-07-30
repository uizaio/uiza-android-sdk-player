package testlibuiza.sample.v3.event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.audio.AudioListener;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.video.VideoListener;
import java.util.List;
import testlibuiza.R;
import testlibuiza.app.LSApplication;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.interfaces.UZLiveContentCallback;
import uizacoresdk.listerner.ProgressCallback;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.rl.video.UZAdPlayerCallback;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

/**
 * Created by loitp on 1/9/2019.
 */

public class EventActivity extends AppCompatActivity {
    private Activity activity;
    private UZVideo uzVideo;
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
        UZUtil.setCasty(this);
        activity = this;
        UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        uzVideo = (UZVideo) findViewById(R.id.uiza_video);
        tvUzCallback = (TextView) findViewById(R.id.tv_uz_callback);
        tvAudioListener = (TextView) findViewById(R.id.tv_audio_listener);
        tvVideoListener = (TextView) findViewById(R.id.tv_video_listener);
        tvPlayerListener = (TextView) findViewById(R.id.tv_player_listener);
        tvMetadataOutput = (TextView) findViewById(R.id.tv_metadata_output);
        tvTextOutput = (TextView) findViewById(R.id.tv_text_output);
        tvController = (TextView) findViewById(R.id.tv_controller);
        tvAd = (TextView) findViewById(R.id.tv_ad);
        tvProgress = (TextView) findViewById(R.id.tv_progress);
        tvTouch = (TextView) findViewById(R.id.tv_touch);
        tvItemClick = (TextView) findViewById(R.id.tv_item_click);
        tvLiveInfo = (TextView) findViewById(R.id.tv_live_info);
        uzVideo.setControllerShowTimeoutMs(5000);

        uzVideo.addUZCallback(new UZCallback() {
            @Override
            public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
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
            public void onError(UZException e) {
                tvUzCallback.setText("onError");
            }
        });

        uzVideo.addItemClick(new UZItemClick() {
            @Override
            public void onItemClick(View view) {
                switch (view.getId()) {
                    case R.id.exo_back_screen:
                        if (!uzVideo.isLandscape()) {
                            onBackPressed();
                        }
                        break;
                }
                tvItemClick.setText("onItemClick " + view.getId());
            }
        });

        uzVideo.addAudioListener(new AudioListener() {
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
        uzVideo.addPlayerEventListener(new Player.EventListener() {
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
        uzVideo.addMetadataOutput(new MetadataOutput() {
            @Override
            public void onMetadata(Metadata metadata) {
                tvMetadataOutput.setText("onMetadata");
            }
        });
        uzVideo.addTextOutput(new TextOutput() {
            @Override
            public void onCues(List<Cue> cues) {
                tvTextOutput.setText("onCues");
            }
        });
        uzVideo.addVideoAdPlayerCallback(new UZAdPlayerCallback() {
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
        uzVideo.addControllerStateCallback(new UZPlayerView.ControllerStateCallback() {
            @Override
            public void onVisibilityChange(boolean isShow) {
                tvController.setText("onVisibilityChange " + isShow);
            }
        });
        uzVideo.addProgressCallback(new ProgressCallback() {
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
        uzVideo.addOnTouchEvent(new UZPlayerView.OnTouchEvent() {
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
        uzVideo.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                tvVideoListener.setText("Current profile " + width + "x" + height);
            }

            @Override
            public void onSurfaceSizeChanged(int width, int height) {
            }
        });
        uzVideo.addUZLiveContentCallback(new UZLiveContentCallback() {
            @Override
            public void onUpdateLiveInfoTimeStartLive(long duration, String hhmmss) {
            }

            @Override
            public void onUpdateLiveInfoCurrentView(long watchnow) {
                tvLiveInfo.setText("onUpdateLiveInfoCurrentView watchnow: " + watchnow);
            }

            @Override
            public void onLivestreamUnAvailable() {
                tvLiveInfo.setText(R.string.err_live_is_stopped);
            }
        });
        findViewById(R.id.bt_vod).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String entityId = LSApplication.entityIdDefaultVOD;
                UZUtil.initEntity(activity, uzVideo, entityId);
            }
        });
        findViewById(R.id.bt_live).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String entityId = LSApplication.entityIdDefaultLIVE;
                UZUtil.initEntity(activity, uzVideo, entityId);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uzVideo.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        uzVideo.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uzVideo.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzVideo.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (LScreenUtil.isFullScreen(activity)) {
            uzVideo.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }
}
