package testlibuiza.sample.v3.uizavideov3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.restapi.uiza.model.v2.listallentity.Item;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.uzv1.listerner.ProgressCallback;
import vn.uiza.uzv1.view.rl.video.UZPlayerViewV1;
import vn.uiza.uzv1.view.rl.video.UZVideoV1;
import vn.uiza.uzv3.util.UZUtil;
import vn.uiza.uzv3.view.rl.video.UZCallback;
import vn.uiza.uzv3.view.rl.video.UZVideo;
import vn.uiza.views.LToast;

/**
 * Created by loitp on 7/16/2018.
 */

public class V3CannotSlidePlayer extends BaseActivity implements UZCallback {
    private UZVideo UZVideo;
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
        UZUtil.setCasty(this);
        super.onCreate(savedInstanceState);
        UZVideo = (UZVideo) findViewById(R.id.uiza_video);
        btProgress = (Button) findViewById(R.id.bt_progress);
        UZVideo.setUZCallback(this);

        boolean isInitWithPlaylistFolder = getIntent().getBooleanExtra(Constants.KEY_UIZA_IS_PLAYLIST_FOLDER, false);
        if (isInitWithPlaylistFolder) {
            String metadataId = getIntent().getStringExtra(Constants.KEY_UIZA_METADAT_ENTITY_ID);
            UZUtil.initPlaylistFolder(activity, UZVideo, metadataId);
        } else {
            String entityId = getIntent().getStringExtra(Constants.KEY_UIZA_ENTITY_ID);
            UZUtil.initEntity(activity, UZVideo, entityId);
        }

        //set uizaIMAVideoV3 hide all controller
        //uizaIMAVideoV3.setUseController(true);
        //uizaIMAVideoV3.setControllerAutoShow(true);
        //uizaIMAVideoV3.hideControllerOnTouch(true);
        //uizaIMAVideoV3.getIbFullscreenIcon().setVisibility(View.GONE);
        //uizaIMAVideoV3.getIbSettingIcon().setVisibility(View.GONE);
        //uizaIMAVideoV3.getIbSettingIcon().setImageResource(R.mipmap.ic_launcher);
        UZVideo.setColorAllViewsEnable(ContextCompat.getColor(activity, R.color.White));

        UZVideo.setOnTouchEvent(new UZPlayerViewV1.OnTouchEvent() {
            @Override
            public void onSingleTapConfirmed() {
            }

            @Override
            public void onLongPress() {
            }

            @Override
            public void onDoubleTap() {
                UZVideo.setDisplayPortrait(!UZVideo.isDisplayPortrait());
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
                UZVideo.resumeVideo();
            }
        });
        findViewById(R.id.bt_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UZVideo.pauseVideo();
            }
        });
        findViewById(R.id.bt_next_10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UZVideo.seekToForward(10000);
            }
        });
        findViewById(R.id.bt_prev_10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UZVideo.seekToBackward(10000);
            }
        });
        findViewById(R.id.bt_volume_on_off).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UZVideo.toggleVolume();
            }
        });
        findViewById(R.id.bt_toggle_fullscreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UZVideo.toggleFullscreen();
            }
        });
        findViewById(R.id.bt_cc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UZVideo.showCCPopup();
            }
        });
        findViewById(R.id.bt_hq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UZVideo.showHQPopup();
            }
        });
        findViewById(R.id.bt_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UZVideo.showSharePopup();
            }
        });
        findViewById(R.id.bt_picture_in_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UZVideo.showPip();
            }
        });
        findViewById(R.id.bt_next_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UZVideo.skipNextVideo();
            }
        });
        findViewById(R.id.bt_prev_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UZVideo.skipPreviousVideo();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UZVideo.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        UZVideo.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        UZVideo.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        UZVideo.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        UZVideo.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UZVideoV1.CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (resultCode == Activity.RESULT_OK) {
                UZVideo.initializePiP();
            } else {
                LToast.show(activity, "Draw over other app permission not available");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setListener() {
        if (UZVideo == null || UZVideo.getPlayer() == null) {
            return;
        }
        UZVideo.getPlayer().addListener(new Player.EventListener() {
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
        UZVideo.getPlayer().addAudioDebugListener(new AudioRendererEventListener() {
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
        UZVideo.setProgressCallback(new ProgressCallback() {
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
        UZVideo.getPlayer().addVideoDebugListener(new VideoRendererEventListener() {
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
        UZVideo.getPlayer().addMetadataOutput(new MetadataOutput() {
            @Override
            public void onMetadata(Metadata metadata) {
                //LLog.d(TAG, "onMetadata");
            }
        });
        UZVideo.getPlayer().addTextOutput(new TextOutput() {
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
            UZVideo.setEventBusMsgFromActivityIsInitSuccess();
            //uizaIMAVideoV3.setControllerShowTimeoutMs(0);
            //uizaIMAVideoV3.setColorAllViewsEnable(ContextCompat.getColor(activity, R.color.Red));
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
            UZVideo.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }
}
