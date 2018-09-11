package testlibuiza.sample.v3.uizavideov3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.View;
import android.widget.Button;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.util.List;

import testlibuiza.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.restapi.uiza.model.v2.listallentity.Item;
import vn.loitp.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.loitp.uizavideo.listerner.ProgressCallback;
import vn.loitp.uizavideo.view.rl.video.UizaIMAVideo;
import vn.loitp.uizavideo.view.rl.video.UizaPlayerView;
import vn.loitp.uizavideov3.util.UizaUtil;
import vn.loitp.uizavideov3.view.rl.video.UizaCallback;
import vn.loitp.uizavideov3.view.rl.video.UizaIMAVideoV3;
import vn.loitp.views.LToast;

/**
 * Created by loitp on 7/16/2018.
 */

public class V3CannotSlidePlayer extends BaseActivity implements UizaCallback {
    private UizaIMAVideoV3 uizaIMAVideoV3;
    private Button btProgress;

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
        return R.layout.v3_cannot_slide_player;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //UizaDataV3.getInstance().setCasty(Casty.create(this));
        UizaUtil.setCasty(this);
        super.onCreate(savedInstanceState);
        uizaIMAVideoV3 = (UizaIMAVideoV3) findViewById(R.id.uiza_video);
        btProgress = (Button) findViewById(R.id.bt_progress);
        uizaIMAVideoV3.setUizaCallback(this);

        boolean isInitWithPlaylistFolder = getIntent().getBooleanExtra(Constants.KEY_UIZA_IS_PLAYLIST_FOLDER, false);
        if (isInitWithPlaylistFolder) {
            String metadataId = getIntent().getStringExtra(Constants.KEY_UIZA_METADAT_ENTITY_ID);
            UizaUtil.initPlaylistFolder(activity, uizaIMAVideoV3, metadataId);
        } else {
            String entityId = getIntent().getStringExtra(Constants.KEY_UIZA_ENTITY_ID);
            UizaUtil.initEntity(activity, uizaIMAVideoV3, entityId);
        }

        //set uizaIMAVideoV3 hide all controller
        //uizaIMAVideoV3.setUseController(true);
        //uizaIMAVideoV3.setControllerAutoShow(true);
        //uizaIMAVideoV3.setControllerShowTimeoutMs(5000);
        //uizaIMAVideoV3.hideControllerOnTouch(true);

        uizaIMAVideoV3.setOnTouchEvent(new UizaPlayerView.OnTouchEvent() {
            @Override
            public void onSingleTapConfirmed() {
            }

            @Override
            public void onLongPress() {
            }

            @Override
            public void onDoubleTap() {
                uizaIMAVideoV3.setDisplayPortrait(!uizaIMAVideoV3.isDisplayPortrait());
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
        });

        findViewById(R.id.bt_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uizaIMAVideoV3.resumeVideo();
            }
        });
        findViewById(R.id.bt_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uizaIMAVideoV3.pauseVideo();
            }
        });
        findViewById(R.id.bt_next_10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uizaIMAVideoV3.seekToForward(10000);
            }
        });
        findViewById(R.id.bt_prev_10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uizaIMAVideoV3.seekToBackward(10000);
            }
        });
        findViewById(R.id.bt_volume_on_off).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uizaIMAVideoV3.toggleVolume();
            }
        });
        findViewById(R.id.bt_toggle_fullscreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uizaIMAVideoV3.toggleFullscreen();
            }
        });
        findViewById(R.id.bt_cc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uizaIMAVideoV3.showCCPopup();
            }
        });
        findViewById(R.id.bt_hq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uizaIMAVideoV3.showHQPopup();
            }
        });
        findViewById(R.id.bt_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uizaIMAVideoV3.showSharePopup();
            }
        });
        findViewById(R.id.bt_picture_in_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uizaIMAVideoV3.showPip();
            }
        });
        findViewById(R.id.bt_next_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uizaIMAVideoV3.skipNextVideo();
            }
        });
        findViewById(R.id.bt_prev_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uizaIMAVideoV3.skipPreviousVideo();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uizaIMAVideoV3.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        uizaIMAVideoV3.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uizaIMAVideoV3.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        uizaIMAVideoV3.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        uizaIMAVideoV3.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UizaIMAVideo.CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (resultCode == Activity.RESULT_OK) {
                uizaIMAVideoV3.initializePiP();
            } else {
                LToast.show(activity, "Draw over other app permission not available");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setListener() {
        if (uizaIMAVideoV3 == null || uizaIMAVideoV3.getPlayer() == null) {
            return;
        }
        uizaIMAVideoV3.getPlayer().addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
                //LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                //LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                //LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                //LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                //LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                //LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                //LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                //LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                //LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onSeekProcessed() {
                //LLog.d(TAG, "onTimelineChanged");
            }
        });
        uizaIMAVideoV3.getPlayer().addAudioDebugListener(new AudioRendererEventListener() {
            @Override
            public void onAudioEnabled(DecoderCounters counters) {
                //LLog.d(TAG, "onAudioEnabled");
            }

            @Override
            public void onAudioSessionId(int audioSessionId) {
                //LLog.d(TAG, "onAudioSessionId");
            }

            @Override
            public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
                //LLog.d(TAG, "onAudioDecoderInitialized");
            }

            @Override
            public void onAudioInputFormatChanged(Format format) {
                //LLog.d(TAG, "onAudioInputFormatChanged");
            }

            @Override
            public void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
                //LLog.d(TAG, "onAudioSinkUnderrun");
            }

            @Override
            public void onAudioDisabled(DecoderCounters counters) {
                //LLog.d(TAG, "onAudioDisabled");
            }
        });
        uizaIMAVideoV3.setProgressCallback(new ProgressCallback() {
            @Override
            public void onAdProgress(float currentMls, int s, float duration, int percent) {
                //LLog.d(TAG, TAG + " ad progress: " + currentMls + "/" + duration + " -> " + percent + "%");
                btProgress.setText("Ad: " + currentMls + "/" + duration + " (mls) => " + percent + "%");
            }

            @Override
            public void onVideoProgress(float currentMls, int s, float duration, int percent) {
                //LLog.d(TAG, TAG + " video progress: " + currentMls + "/" + duration + " -> " + percent + "%");
                btProgress.setText("Video: " + currentMls + "/" + duration + " (mls) => " + percent + "%");
                ;
            }
        });
        uizaIMAVideoV3.getPlayer().addVideoDebugListener(new VideoRendererEventListener() {
            @Override
            public void onVideoEnabled(DecoderCounters counters) {
                //LLog.d(TAG, "onVideoEnabled");
            }

            @Override
            public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
                //LLog.d(TAG, "onVideoDecoderInitialized");
            }

            @Override
            public void onVideoInputFormatChanged(Format format) {
                //LLog.d(TAG, "onVideoInputFormatChanged");
            }

            @Override
            public void onDroppedFrames(int count, long elapsedMs) {
                //LLog.d(TAG, "onDroppedFrames");
            }

            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                //LLog.d(TAG, "onAudioDisabled");
            }

            @Override
            public void onRenderedFirstFrame(Surface surface) {
                //LLog.d(TAG, "onRenderedFirstFrame");
            }

            @Override
            public void onVideoDisabled(DecoderCounters counters) {
                //LLog.d(TAG, "onVideoDisabled");
            }
        });
        uizaIMAVideoV3.getPlayer().addMetadataOutput(new MetadataOutput() {
            @Override
            public void onMetadata(Metadata metadata) {
                //LLog.d(TAG, "onMetadata");
            }
        });
        uizaIMAVideoV3.getPlayer().addTextOutput(new TextOutput() {
            @Override
            public void onCues(List<Cue> cues) {
                //LLog.d(TAG, "onCues");
            }
        });
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
        if (isInitSuccess) {
            setListener();
            uizaIMAVideoV3.setEventBusMsgFromActivityIsInitSuccess();
        }
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
        LLog.d(TAG, "onClickPip");
    }

    @Override
    public void onClickPipVideoInitSuccess(boolean isInitSuccess) {
        LLog.d(TAG, "onClickPipVideoInitSuccess " + isInitSuccess);
        if (isInitSuccess) {
            onBackPressed();
        }
    }

    @Override
    public void onSkinChange() {

    }

    @Override
    public void onError(Exception e) {
        LLog.d(TAG, "onError " + e.getMessage());
    }

    @Override
    public void onBackPressed() {
        if (LScreenUtil.isFullScreen(activity)) {
            uizaIMAVideoV3.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }
}
