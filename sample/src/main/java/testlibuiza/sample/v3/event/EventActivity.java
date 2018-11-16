package testlibuiza.sample.v3.event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;
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

import java.util.List;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.rl.video.UZCallback;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.restapi.uiza.model.v2.listallentity.Item;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.views.LToast;

/**
 * Created by loitp on 7/16/2018.
 */

public class EventActivity extends BaseActivity {
    private UZVideo uzVideo;
    private TextView tvUzCallback;
    private TextView tvAudioListener;
    private TextView tvPlayerListener;
    private TextView tvMetadataOutput;
    private TextView tvTextOutput;
    private TextView tvAd;

    @Override
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected String setTag() {
        return "TAG" + getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_event;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UZUtil.setCasty(this);
        UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_1);
        super.onCreate(savedInstanceState);
        uzVideo = (UZVideo) findViewById(R.id.uiza_video);
        tvUzCallback = (TextView) findViewById(R.id.tv_uz_callback);
        tvAudioListener = (TextView) findViewById(R.id.tv_audio_listener);
        tvPlayerListener = (TextView) findViewById(R.id.tv_player_listener);
        tvMetadataOutput = (TextView) findViewById(R.id.tv_metadata_output);
        tvTextOutput = (TextView) findViewById(R.id.tv_text_output);
        tvAd = (TextView) findViewById(R.id.tv_ad);
        uzVideo.setControllerShowTimeoutMs(5000);

        uzVideo.addUZCallback(new UZCallback() {
            @Override
            public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
                tvUzCallback.setText("isInitResult " + isInitSuccess);
            }

            @Override
            public void onClickListEntityRelation(Item item, int position) {
            }

            @Override
            public void onClickBack() {
                onBackPressed();
            }

            @Override
            public void onClickPip(Intent intent) {
                tvUzCallback.setText("onClickPip");
            }

            @Override
            public void onClickPipVideoInitSuccess(boolean isInitSuccess) {
                if (isInitSuccess) {
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
        uzVideo.addVideoAdPlayerCallback(new VideoAdPlayer.VideoAdPlayerCallback() {
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
        });
        final String entityId = LSApplication.entityIdDefaultVOD;
        UZUtil.initEntity(activity, uzVideo, entityId);
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
    public void onStart() {
        super.onStart();
        uzVideo.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        uzVideo.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (resultCode == Activity.RESULT_OK) {
                uzVideo.initializePiP();
            } else {
                LToast.show(activity, "Draw over other app permission not available");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
