package uiza.activity.home.v2.cannotslide;

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

import uiza.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LDialogUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
import vn.loitp.restapi.uiza.model.v2.getdetailentity.GetDetailEntity;
import vn.loitp.restapi.uiza.model.v2.getlinkplay.GetLinkPlay;
import vn.loitp.restapi.uiza.model.v2.listallentity.Item;
import vn.loitp.uizavideo.listerner.ProgressCallback;
import vn.loitp.uizavideo.view.rl.video.UizaIMAVideo;
import vn.loitp.uizavideo.view.rl.videoinfo.ItemAdapterV2;
import vn.loitp.uizavideo.view.rl.videoinfo.UizaIMAVideoInfo;
import vn.loitp.uizavideo.view.util.UizaData;
import vn.loitp.uizavideo.view.util.UizaInput;
import vn.loitp.views.LToast;

public class UizaPlayerActivityV2 extends BaseActivity implements UizaIMAVideo.Callback, ItemAdapterV2.Callback {
    private UizaIMAVideo uizaIMAVideo;
    private UizaIMAVideoInfo uizaIMAVideoInfo;
    private long positionFromPipService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uizaIMAVideo = (UizaIMAVideo) findViewById(R.id.uiza_video);
        uizaIMAVideo.registerReceiverPiPInitSuccess();
        uizaIMAVideoInfo = (UizaIMAVideoInfo) findViewById(R.id.uiza_video_info);

        positionFromPipService = getIntent().getLongExtra(Constants.FLOAT_CURRENT_POSITION, 0l);
        //LLog.d(TAG, ">>> positionFromPipService: " + positionFromPipService);

        String entityId = null;
        String entityCover = null;
        String entityTitle = null;
        if (LPref.getClickedPip(activity)) {
            //activity is called from PiP Service
            entityId = getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_ID);
            entityTitle = getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_TITLE);
            entityCover = getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_COVER);
        } else {
            entityId = getIntent().getStringExtra(Constants.KEY_UIZA_ENTITY_ID);
            entityTitle = getIntent().getStringExtra(Constants.KEY_UIZA_ENTITY_TITLE);
            entityCover = getIntent().getStringExtra(Constants.KEY_UIZA_ENTITY_COVER);
        }
        if (entityId == null || entityId.isEmpty()) {
            LDialogUtil.showDialog1(activity, "Entity cannot be null or empty", new LDialogUtil.Callback1() {
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

        LLog.d(TAG, ">>> entityId " + entityId);
        LLog.d(TAG, ">>> entityCover " + entityCover);
        LLog.d(TAG, ">>> entityTitle " + entityTitle);

        setupVideo(entityId, entityTitle, entityCover, false);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uizaIMAVideo.onDestroy();
        uizaIMAVideo.unregisterReceiverPiPInitSuccess();
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
    public void onStart() {
        super.onStart();
        uizaIMAVideo.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        uizaIMAVideo.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UizaIMAVideo.CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                //LLog.d(TAG, "onActivityResult RESULT_OK");
                uizaIMAVideo.initializePiP();
            } else {
                LToast.show(activity, "Draw over other app permission not available");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setListener() {
        //LLog.d(TAG, TAG + " addListener");
        if (uizaIMAVideo == null || uizaIMAVideo.getPlayer() == null) {
            return;
        }
        uizaIMAVideo.getPlayer().addListener(new Player.EventListener() {
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
        uizaIMAVideo.getPlayer().addAudioDebugListener(new AudioRendererEventListener() {
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
        uizaIMAVideo.setProgressCallback(new ProgressCallback() {
            @Override
            public void onAdProgress(float currentMls, int s, float duration, int percent) {
                //LLog.d(TAG, TAG + " ad progress currentMls: " + currentMls + ", s:" + s + ", duration: " + duration + ",percent: " + percent + "%");
            }

            @Override
            public void onVideoProgress(float currentMls, int s, float duration, int percent) {
                //LLog.d(TAG, TAG + " video progress currentMls: " + currentMls + ", s:" + s + ", duration: " + duration + ",percent: " + percent + "%");
            }
        });
        uizaIMAVideo.getPlayer().addVideoDebugListener(new VideoRendererEventListener() {
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
        uizaIMAVideo.getPlayer().addMetadataOutput(new MetadataOutput() {
            @Override
            public void onMetadata(Metadata metadata) {
                //LLog.d(TAG, "onMetadata");
            }
        });
        uizaIMAVideo.getPlayer().addTextOutput(new TextOutput() {
            @Override
            public void onCues(List<Cue> cues) {
                //LLog.d(TAG, "onCues");
            }
        });
    }

    @Override
    public void isInitResult(boolean isInitSuccess, GetLinkPlay getLinkPlay, GetDetailEntity getDetailEntity) {
        //LLog.d(TAG, "isPiPInitResult " + isInitSuccess);
        if (isInitSuccess) {
            if (LPref.getClickedPip(activity)) {
                uizaIMAVideo.seekTo(positionFromPipService);
            }
            setListener();
            if (uizaIMAVideoInfo != null) {
                uizaIMAVideoInfo.setup(getDetailEntity);
            }
        } else {
            UizaInput prevUizaInput = UizaData.getInstance().getUizaInputPrev();
            if (prevUizaInput == null) {
                LLog.d(TAG, "isInitResult prevUizaInput null -> exit");
                if (activity != null) {
                    activity.onBackPressed();
                }
            } else {
                LLog.d(TAG, "isInitResult prevUizaInput: " + prevUizaInput.getEntityName());
                boolean isPlayPrev = UizaData.getInstance().isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed();
                LLog.d(TAG, "isInitResult isPlayPrev: " + isPlayPrev);
                if (isPlayPrev) {
                    LPref.setClickedPip(activity, false);
                    setupVideo(prevUizaInput.getEntityId(), prevUizaInput.getEntityName(), prevUizaInput.getUrlThumnailsPreviewSeekbar(), false);
                } else {
                    activity.onBackPressed();
                }
            }
        }
    }

    @Override
    public void onClickListEntityRelation(Item item, int position) {
        LPref.setClickedPip(activity, false);
        setupVideo(item.getId(), item.getName(), item.getThumbnail(), true);
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
        //LLog.d(TAG, "onClickPipVideoInitSuccess isInitSuccess: " + isInitSuccess);
        if (isInitSuccess) {
            onBackPressed();
        }
    }

    @Override
    public void onClickItemBottom(Item item, int position) {
        LPref.setClickedPip(activity, false);
        setupVideo(item.getId(), item.getName(), item.getThumbnail(), true);
    }

    @Override
    public void onLoadMore() {
        //do nothing
    }

    private void setupVideo(String entityId, String entityTitle, String entityCover, boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed) {
        if (entityId == null || entityId.isEmpty()) {
            LDialogUtil.showDialog1(activity, "Entity Id cannot be null or empty", new LDialogUtil.Callback1() {
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
        //LLog.d(TAG, "setupVideo entityId: " + entityId + ", entityTitle: " + entityTitle);

        UizaInput uizaInput = new UizaInput();

        uizaInput.setEntityId(entityId);
        uizaInput.setEntityName(entityTitle);
        uizaInput.setEntityCover(entityCover);

        //String urlIMAAd = activity.getString(loitp.core.R.string.ad_tag_url);
        String urlIMAAd = null;
        uizaInput.setUrlIMAAd(urlIMAAd);

        //String urlThumnailsPreviewSeekbar = activity.getString(loitp.core.R.string.url_thumbnails);
        String urlThumnailsPreviewSeekbar = null;
        uizaInput.setUrlThumnailsPreviewSeekbar(urlThumnailsPreviewSeekbar);

        UizaData.getInstance().setUizaInput(uizaInput, isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed);

        uizaIMAVideo.init(this);
        uizaIMAVideoInfo.init(this);
    }
}
