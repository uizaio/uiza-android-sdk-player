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
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LDialogUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.restapi.uiza.model.v2.getdetailentity.GetDetailEntity;
import vn.uiza.restapi.uiza.model.v2.getlinkplay.GetLinkPlay;
import vn.uiza.restapi.uiza.model.v2.listallentity.Item;
import vn.uiza.uzv1.listerner.ProgressCallback;
import vn.uiza.uzv1.view.ComunicateMng;
import vn.uiza.uzv1.view.rl.video.UZVideoV1;
import vn.uiza.uzv1.view.util.UizaDataV1;
import vn.uiza.uzv1.view.util.UizaInputV1;
import vn.uiza.uzv3.util.UZUtil;
import vn.uiza.views.LToast;

public class V2UizaVideoIMActivity extends BaseActivity implements UZVideoV1.Callback {
    private UZVideoV1 UZVideoV1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UZVideoV1 = (UZVideoV1) findViewById(R.id.uiza_video);

        UizaDataV1.getInstance().setCurrentPlayerId(R.layout.uz_player_skin_1);
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
        UZVideoV1.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UZVideoV1.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        UZVideoV1.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                UZVideoV1.initializePiP();
            } else {
                LToast.show(activity, "Draw over other app permission not available");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setListener() {
        if (UZVideoV1 == null || UZVideoV1.getPlayer() == null) {
            return;
        }
        UZVideoV1.getPlayer().addListener(new Player.EventListener() {
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
        UZVideoV1.getPlayer().addAudioDebugListener(new AudioRendererEventListener() {
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
        UZVideoV1.setProgressCallback(new ProgressCallback() {
            @Override
            public void onAdProgress(float currentMls, int s, float duration, int percent) {
                LLog.d(TAG, TAG + " ad progress: " + currentMls + "/" + duration + " -> " + percent + "%");
            }

            @Override
            public void onVideoProgress(float currentMls, int s, float duration, int percent) {
                LLog.d(TAG, TAG + " video progress: " + currentMls + "/" + duration + " -> " + percent + "%");
            }
        });
        UZVideoV1.getPlayer().addVideoDebugListener(new VideoRendererEventListener() {
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
        UZVideoV1.getPlayer().addMetadataOutput(new MetadataOutput() {
            @Override
            public void onMetadata(Metadata metadata) {
                LLog.d(TAG, "onMetadata");
            }
        });
        UZVideoV1.getPlayer().addTextOutput(new TextOutput() {
            @Override
            public void onCues(List<Cue> cues) {
                LLog.d(TAG, "onCues");
            }
        });
    }

    @Override
    public void isInitResult(boolean isInitSuccess, GetLinkPlay getLinkPlay, GetDetailEntity getDetailEntity) {
        if (isInitSuccess) {
            if (UZUtil.getClickedPip(activity)) {
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
        UizaDataV1.getInstance().setCurrentPlayerId(R.layout.uz_player_skin_1);
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
        UizaInputV1 uizaInputV1 = new UizaInputV1();
        uizaInputV1.setEntityId(entityId);
        uizaInputV1.setEntityName(entityTitle);
        uizaInputV1.setEntityCover(entityCover);
        uizaInputV1.setUrlIMAAd(urlIMAAd);
        uizaInputV1.setUrlThumnailsPreviewSeekbar(urlThumnailsPreviewSeekbar);
        UizaDataV1.getInstance().setUizaInput(uizaInputV1, false);

        UZVideoV1.init(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        UZVideoV1.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        UZVideoV1.onStop();
    }
}
