package uiza.v3.canslide;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.List;

import uiza.R;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.uiza.model.v2.listallentity.Item;
import vn.loitp.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.loitp.uizavideo.listerner.ProgressCallback;
import vn.loitp.uizavideo.view.rl.video.UizaIMAVideo;
import vn.loitp.uizavideov3.view.rl.video.UizaCallback;
import vn.loitp.uizavideov3.view.rl.video.UizaIMAVideoV3;
import vn.loitp.uizavideov3.util.UizaDataV3;
import vn.loitp.uizavideov3.util.UizaInputV3;
import vn.loitp.views.LToast;

public class FrmVideoTopV3 extends BaseFragment implements UizaCallback {
    private final String TAG = getClass().getSimpleName();
    private UizaIMAVideoV3 uizaIMAVideoV3;

    public UizaIMAVideoV3 getUizaIMAVideoV3() {
        return uizaIMAVideoV3;
    }

    public interface FrmTopCallback {
        public void initDone(boolean isInitSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data);

        public void onClickListEntityRelation(Item item, int position);
    }

    private FrmTopCallback frmTopCallback;

    public void setFrmTopCallback(FrmTopCallback frmTopCallback) {
        this.frmTopCallback = frmTopCallback;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        uizaIMAVideoV3 = (UizaIMAVideoV3) view.findViewById(R.id.uiza_video);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v3_frm_top;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uizaIMAVideoV3.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().isClosedAtLeft() || ((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().isClosedAtRight()) {
            return;
        }
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
                LToast.show(getActivity(), "Draw over other app permission not available");
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
            }

            @Override
            public void onVideoProgress(float currentMls, int s, float duration, int percent) {
                //LLog.d(TAG, TAG + " video progress: " + currentMls + "/" + duration + " -> " + percent + "%");
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
    public void isInitResult(boolean isInitSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
        if (isInitSuccess) {
            setListener();
            if (frmTopCallback != null) {
                frmTopCallback.initDone(isInitSuccess, resultGetLinkPlay, data);
            }
        } else {
            UizaInputV3 prevUizaInput = UizaDataV3.getInstance().getUizaInputPrev();
            if (prevUizaInput == null) {
                ((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().minimize();
                LUIUtil.setDelay(250, new LUIUtil.DelayCallback() {
                    @Override
                    public void doAfter(int mls) {
                        ((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().closeToRight();
                    }
                });
            } else {
                boolean isPlayPrev = UizaDataV3.getInstance().isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed();
                if (isPlayPrev) {
                    setupVideo(UizaDataV3.getInstance().getEntityId(), prevUizaInput.getUrlIMAAd(), prevUizaInput.getUrlThumnailsPreviewSeekbar(), false);
                } else {
                    ((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().minimize();
                    LUIUtil.setDelay(250, new LUIUtil.DelayCallback() {
                        @Override
                        public void doAfter(int mls) {
                            ((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().closeToRight();
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onClickListEntityRelation(Item item, int position) {
        if (frmTopCallback != null) {
            frmTopCallback.onClickListEntityRelation(item, position);
        }
    }

    @Override
    public void onClickBack() {
        ((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().minimize();
    }

    @Override
    public void onClickPip(Intent intent) {
        ((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClickPipVideoInitSuccess(boolean isInitSuccess) {
        ((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().closeToRight();
    }

    @Override
    public void onError(Exception e) {
        if (e != null) {
            LLog.e(TAG, "onError " + e.toString());
        }
        ((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().closeToRight();
    }

    public void setupVideo(final String entityId, final String urlIMAAd, final String urlThumnailsPreviewSeekbar, final boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed) {
        if (UizaDataV3.getInstance().isSettingPlayer()) {
            return;
        }
        uizaIMAVideoV3.post(new Runnable() {
            @Override
            public void run() {
                uizaIMAVideoV3.init(entityId, urlIMAAd, urlThumnailsPreviewSeekbar, isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed);
                uizaIMAVideoV3.setUizaCallback(FrmVideoTopV3.this);
            }
        });
    }

    public void setupPlaylistFolder(final String metadataId) {
        LLog.d(TAG, "setupPlaylistFolder " + metadataId);
        if (UizaDataV3.getInstance().isSettingPlayer()) {
            LLog.d(TAG, "isSettingPlayer return");
            return;
        }
        uizaIMAVideoV3.post(new Runnable() {
            @Override
            public void run() {
                uizaIMAVideoV3.initPlaylistFolder(metadataId);
                uizaIMAVideoV3.setUizaCallback(FrmVideoTopV3.this);
            }
        });
    }
}
