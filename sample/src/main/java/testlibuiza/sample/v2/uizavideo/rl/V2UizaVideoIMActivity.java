package testlibuiza.sample.v2.uizavideo.rl;

import android.content.Intent;
import android.os.Bundle;
import android.view.Surface;

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
import vn.loitp.core.utilities.LDialogUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.restapi.uiza.model.v2.getdetailentity.GetDetailEntity;
import vn.loitp.restapi.uiza.model.v2.getlinkplay.GetLinkPlay;
import vn.loitp.restapi.uiza.model.v2.listallentity.Item;
import vn.loitp.uizavideo.listerner.ProgressCallback;
import vn.loitp.uizavideo.view.ComunicateMng;
import vn.loitp.uizavideo.view.rl.video.UizaIMAVideo;
import vn.loitp.uizavideo.view.util.UizaData;
import vn.loitp.uizavideo.view.util.UizaInput;
import vn.loitp.uizavideov3.util.UizaUtil;
import vn.loitp.views.LToast;

public class V2UizaVideoIMActivity extends BaseActivity implements UizaIMAVideo.Callback {
    private UizaIMAVideo uizaIMAVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uizaIMAVideo = (UizaIMAVideo) findViewById(R.id.uiza_video);

        UizaData.getInstance().setCurrentPlayerId(Constants.PLAYER_ID_SKIN_0);
        String entityId = null;
        String entityTitle = null;
        String videoCoverUrl = null;

        if (getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_ID) == null) {
            //entityId = "9213e9b8-a926-4282-b081-12b69555cb10"; //for dev environment
            entityId = "76c21bd3-8626-4a74-9f26-b29b58c7f6b8"; //for uqc environment
        } else {
            entityId = getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_ID);
        }

        if (getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_TITLE) == null) {
            entityTitle = "Dummy title";
        } else {
            entityTitle = getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_TITLE);
        }

        if (getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_COVER) == null) {
            videoCoverUrl = "//motosaigon.vn/wp-content/uploads/2016/07/yamaha-r3-do-banh-to-190-motosaigon-5.jpg";
        } else {
            videoCoverUrl = getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_COVER);
        }
        //String urlIMAAd = activity.getString(loitp.core.R.string.ad_tag_url);
        String urlIMAAd = null;

        String urlThumnailsPreviewSeekbar = activity.getString(loitp.core.R.string.url_thumbnails);
        //String urlThumnailsPreviewSeekbar = null;
        setupVideo(entityId, entityTitle, videoCoverUrl, urlIMAAd, urlThumnailsPreviewSeekbar);
    }

    @Override
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v2_test_uiza_ima_video_activity_rl;
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
                uizaIMAVideo.initializePiP();
            } else {
                LToast.show(activity, "Draw over other app permission not available");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setListener() {
        if (uizaIMAVideo == null || uizaIMAVideo.getPlayer() == null) {
            return;
        }
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
            public void onAdProgress(float currentMls, int s, float duration, int percent) {
                LLog.d(TAG, TAG + " ad progress: " + currentMls + "/" + duration + " -> " + percent + "%");
            }

            @Override
            public void onVideoProgress(float currentMls, int s, float duration, int percent) {
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

    @Override
    public void isInitResult(boolean isInitSuccess, GetLinkPlay getLinkPlay, GetDetailEntity getDetailEntity) {
        if (isInitSuccess) {
            if (UizaUtil.getClickedPip(activity)) {
                ComunicateMng.MsgFromActivityIsInitSuccess msgFromActivityIsInitSuccess = new ComunicateMng.MsgFromActivityIsInitSuccess(null);
                msgFromActivityIsInitSuccess.setInitSuccess(true);
                ComunicateMng.postFromActivity(msgFromActivityIsInitSuccess);
            }
            setListener();
        }
    }

    @Override
    public void onClickListEntityRelation(Item item, int position) {
        String entityId = item.getId();
        String entityTitle = item.getName();
        String videoCoverUrl = null;
        //String urlIMAAd = activity.getString(loitp.core.R.string.ad_tag_url);
        String urlIMAAd = null;
        String urlThumnailsPreviewSeekbar = activity.getString(loitp.core.R.string.url_thumbnails);
        //String urlThumnailsPreviewSeekbar = null;
        UizaData.getInstance().setCurrentPlayerId(Constants.PLAYER_ID_SKIN_0);
        setupVideo(entityId, entityTitle, videoCoverUrl, urlIMAAd, urlThumnailsPreviewSeekbar);
    }

    @Override
    public void onClickBack() {
        onBackPressed();
    }

    @Override
    public void onClickPip(Intent intent) {
        //do nothing
    }

    @Override
    public void onClickPipVideoInitSuccess(boolean isInitSuccess) {
        onBackPressed();
    }

    @Override
    public void onError(Exception e) {
        if (activity != null) {
            onBackPressed();
        }
    }

    private void setupVideo(String entityId, String entityTitle, String entityCover, String urlIMAAd, String urlThumnailsPreviewSeekbar) {
        if (entityId == null || entityId.isEmpty()) {
            LDialogUtil.showDialog1(activity, "Entity ID cannot be null or empty", new LDialogUtil.Callback1() {
                @Override
                public void onClick1() {
                    if (activity != null) {
                        activity.onBackPressed();
                    }
                }

                @Override
                public void onCancel() {
                    if (activity != null) {
                        activity.onBackPressed();
                    }
                }
            });
            return;
        }
        UizaInput uizaInput = new UizaInput();
        uizaInput.setEntityId(entityId);
        uizaInput.setEntityName(entityTitle);
        uizaInput.setEntityCover(entityCover);
        uizaInput.setUrlIMAAd(urlIMAAd);
        uizaInput.setUrlThumnailsPreviewSeekbar(urlThumnailsPreviewSeekbar);
        UizaData.getInstance().setUizaInput(uizaInput, false);

        uizaIMAVideo.init(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        uizaIMAVideo.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        uizaIMAVideo.onStop();
    }
}
