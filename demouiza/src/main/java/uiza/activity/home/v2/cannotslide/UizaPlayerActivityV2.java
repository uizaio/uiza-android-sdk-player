package uiza.activity.home.v2.cannotslide;

import android.content.Intent;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;

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
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uiza.app.LSApplication;
import uiza.uiza.com.demo.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.restapi.uiza.UizaService;
import vn.loitp.restapi.uiza.model.v2.auth.Auth;
import vn.loitp.restapi.uiza.model.v2.getdetailentity.GetDetailEntity;
import vn.loitp.restapi.uiza.model.v2.getdetailentity.JsonBodyGetDetailEntity;
import vn.loitp.restapi.uiza.model.v2.getlinkdownload.Mpd;
import vn.loitp.restapi.uiza.model.v2.getlinkplay.GetLinkPlay;
import vn.loitp.restapi.uiza.model.v2.listallentity.Item;
import vn.loitp.restapi.uiza.model.v2.listallentity.Subtitle;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.views.LToast;
import vn.loitp.views.uizavideo.listerner.ProgressCallback;
import vn.loitp.views.uizavideo.view.rl.UizaIMAVideo;

import static vn.loitp.core.common.Constants.KEY_UIZA_ENTITY_COVER;
import static vn.loitp.core.common.Constants.KEY_UIZA_ENTITY_ID;
import static vn.loitp.core.common.Constants.KEY_UIZA_ENTITY_TITLE;

public class UizaPlayerActivityV2 extends BaseActivity {
    private String entityId;
    private String entityCover;
    private String entityTitle;
    private UizaIMAVideo uizaIMAVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uizaIMAVideo = (UizaIMAVideo) findViewById(R.id.uiza_video);
        entityId = getIntent().getStringExtra(KEY_UIZA_ENTITY_ID);
        entityCover = getIntent().getStringExtra(KEY_UIZA_ENTITY_COVER);
        entityTitle = getIntent().getStringExtra(KEY_UIZA_ENTITY_TITLE);
        uizaIMAVideo.setEntityId(entityId, new UizaIMAVideo.Callback() {
            @Override
            public void isInitResult(boolean isInitSuccess) {
                setListener();
            }
        });
        LLog.d(TAG, "entityId " + entityId);
        LLog.d(TAG, "entityCover " + entityCover);
        LLog.d(TAG, "entityTitle " + entityTitle);
        if (entityId == null || entityId.isEmpty()) {
            showDialogError("entityId == null || entityId.isEmpty()");
            return;
        }
        getDetailEntity();

        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uizaIMAVideo.setEntityId(entityId, new UizaIMAVideo.Callback() {
                    @Override
                    public void isInitResult(boolean isInitSuccess) {
                        //do nothing
                    }
                });
            }
        });
    }

    private void init() {
        LLog.d(TAG, "init");
    }

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
        return R.layout.uiza_player_activity;
    }

    private void getDetailEntity() {
        //API v2
        UizaService service = RestClientV2.createService(UizaService.class);
        JsonBodyGetDetailEntity jsonBodyGetDetailEntity = new JsonBodyGetDetailEntity();
        jsonBodyGetDetailEntity.setId(entityId);

        subscribe(service.getDetailEntityV2(jsonBodyGetDetailEntity), new ApiSubscriber<GetDetailEntity>() {
            @Override
            public void onSuccess(GetDetailEntity getDetailEntityV2) {
                LLog.d(TAG, "getDetailEntityV2 onSuccess " + LSApplication.getInstance().getGson().toJson(getDetailEntityV2));
                init();
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getDetailEntityV2 onFail " + e.toString());
                handleException(e);
            }
        });
        //EndAPI v2
    }

    public void playOnClickItem(Item item, int position) {
        LLog.d(TAG, "onClick " + position);
        Intent intent = new Intent(activity, UizaPlayerActivityV2.class);
        intent.putExtra(KEY_UIZA_ENTITY_ID, item.getId());
        intent.putExtra(KEY_UIZA_ENTITY_COVER, item.getThumbnail());
        intent.putExtra(KEY_UIZA_ENTITY_TITLE, item.getName());
        startActivity(intent);
        LActivityUtil.tranIn(activity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uizaIMAVideo.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uizaIMAVideo.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        uizaIMAVideo.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UizaIMAVideo.CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                LLog.d(TAG, "onActivityResult RESULT_OK");
                uizaIMAVideo.initializePiP();
            } else {
                LToast.show(activity, "Draw over other app permission not available");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setListener() {
        LLog.d(TAG, TAG + " addListener");
        uizaIMAVideo.getPlayer().addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
                LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onSeekProcessed() {
                LLog.d(TAG, "onTimelineChanged");
            }
        });
        uizaIMAVideo.getPlayer().addAudioDebugListener(new AudioRendererEventListener() {
            @Override
            public void onAudioEnabled(DecoderCounters counters) {
                LLog.d(TAG, "onAudioEnabled");
            }

            @Override
            public void onAudioSessionId(int audioSessionId) {
                LLog.d(TAG, "onAudioSessionId");
            }

            @Override
            public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
                LLog.d(TAG, "onAudioDecoderInitialized");
            }

            @Override
            public void onAudioInputFormatChanged(Format format) {
                LLog.d(TAG, "onAudioInputFormatChanged");
            }

            @Override
            public void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
                LLog.d(TAG, "onAudioSinkUnderrun");
            }

            @Override
            public void onAudioDisabled(DecoderCounters counters) {
                LLog.d(TAG, "onAudioDisabled");
            }
        });
        uizaIMAVideo.setProgressCallback(new ProgressCallback() {
            @Override
            public void onAdProgress(float currentMls, float duration, int percent) {
                LLog.d(TAG, TAG + " ad progress: " + currentMls + "/" + duration + " -> " + percent + "%");
            }

            @Override
            public void onVideoProgress(float currentMls, float duration, int percent) {
                LLog.d(TAG, TAG + " video progress: " + currentMls + "/" + duration + " -> " + percent + "%");
            }
        });
        uizaIMAVideo.getPlayer().addVideoDebugListener(new VideoRendererEventListener() {
            @Override
            public void onVideoEnabled(DecoderCounters counters) {
                LLog.d(TAG, "onVideoEnabled");
            }

            @Override
            public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
                LLog.d(TAG, "onVideoDecoderInitialized");
            }

            @Override
            public void onVideoInputFormatChanged(Format format) {
                LLog.d(TAG, "onVideoInputFormatChanged");
            }

            @Override
            public void onDroppedFrames(int count, long elapsedMs) {
                LLog.d(TAG, "onDroppedFrames");
            }

            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                LLog.d(TAG, "onAudioDisabled");
            }

            @Override
            public void onRenderedFirstFrame(Surface surface) {
                LLog.d(TAG, "onRenderedFirstFrame");
            }

            @Override
            public void onVideoDisabled(DecoderCounters counters) {
                LLog.d(TAG, "onVideoDisabled");
            }
        });
        uizaIMAVideo.getPlayer().addMetadataOutput(new MetadataOutput() {
            @Override
            public void onMetadata(Metadata metadata) {
                LLog.d(TAG, "onMetadata");
            }
        });
        uizaIMAVideo.getPlayer().addTextOutput(new TextOutput() {
            @Override
            public void onCues(List<Cue> cues) {
                LLog.d(TAG, "onCues");
            }
        });
    }
}
