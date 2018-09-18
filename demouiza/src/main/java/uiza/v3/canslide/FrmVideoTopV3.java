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
import vn.uiza.core.base.BaseFragment;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v2.listallentity.Item;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.uzv1.listerner.ProgressCallback;
import vn.uiza.uzv1.view.rl.video.UZVideoV1;
import vn.uiza.uzv3.util.UZData;
import vn.uiza.uzv3.util.UZInput;
import vn.uiza.uzv3.util.UZUtil;
import vn.uiza.uzv3.view.rl.video.UZCallback;
import vn.uiza.uzv3.view.rl.video.UZVideo;
import vn.uiza.views.LToast;

public class FrmVideoTopV3 extends BaseFragment implements UZCallback {
    private UZVideo uzVideo;

    public UZVideo getUZVideo() {
        return uzVideo;
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
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        uzVideo = (UZVideo) view.findViewById(R.id.uiza_video);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v3_frm_top;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uzVideo.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().isClosedAtLeft() || ((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().isClosedAtRight()) {
            return;
        }
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
        if (requestCode == UZVideoV1.CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (resultCode == Activity.RESULT_OK) {
                uzVideo.initializePiP();
            } else {
                LToast.show(getActivity(), "Draw over other app permission not available");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setListener() {
        if (uzVideo == null || uzVideo.getPlayer() == null) {
            return;
        }
        uzVideo.getPlayer().addListener(new Player.EventListener() {
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
        uzVideo.getPlayer().addAudioDebugListener(new AudioRendererEventListener() {
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
        uzVideo.setProgressCallback(new ProgressCallback() {
            @Override
            public void onAdProgress(float currentMls, int s, float duration, int percent) {
                //LLog.d(TAG, TAG + " ad progress: " + currentMls + "/" + duration + " -> " + percent + "%");
            }

            @Override
            public void onVideoProgress(float currentMls, int s, float duration, int percent) {
                //LLog.d(TAG, TAG + " video progress: " + currentMls + "/" + duration + " -> " + percent + "%");
            }
        });
        uzVideo.getPlayer().addVideoDebugListener(new VideoRendererEventListener() {
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
        uzVideo.getPlayer().addMetadataOutput(new MetadataOutput() {
            @Override
            public void onMetadata(Metadata metadata) {
                //LLog.d(TAG, "onMetadata");
            }
        });
        uzVideo.getPlayer().addTextOutput(new TextOutput() {
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
            if (frmTopCallback != null) {
                frmTopCallback.initDone(isInitSuccess, resultGetLinkPlay, data);
            }
        } else {
            UZInput prevUizaInput = UZData.getInstance().getUizaInputPrev();
            if (prevUizaInput == null) {
                ((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().minimize();
                LUIUtil.setDelay(250, new LUIUtil.DelayCallback() {
                    @Override
                    public void doAfter(int mls) {
                        ((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().closeToRight();
                    }
                });
            } else {
                boolean isPlayPrev = UZData.getInstance().isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed();
                if (isPlayPrev) {
                    setupVideo(UZData.getInstance().getEntityId(), false);
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
    public void onSkinChange() {

    }

    @Override
    public void onError(Exception e) {
        if (e != null) {
            LLog.e(TAG, "onError " + e.toString());
        }
        ((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().closeToRight();
    }

    public void setupVideo(final String entityId, final boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed) {
        if (UZData.getInstance().isSettingPlayer()) {
            return;
        }
        uzVideo.post(new Runnable() {
            @Override
            public void run() {
                uzVideo.init(entityId, isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed);
                uzVideo.setUZCallback(FrmVideoTopV3.this);
            }
        });
    }

    public void setupPlaylistFolder(final String metadataId) {
        LLog.d(TAG, "setupPlaylistFolder " + metadataId);
        if (getActivity() == null) {
            LLog.e(TAG, "setupPlaylistFolder getActivity() == null -> return");
            return;
        }
        if (UZData.getInstance().isSettingPlayer()) {
            LLog.d(TAG, "isSettingPlayer return");
            return;
        }
        if (!UZUtil.getClickedPip(getActivity())) {
            UZUtil.stopServicePiPIfRunningV3(getActivity());
        }
        uzVideo.post(new Runnable() {
            @Override
            public void run() {
                uzVideo.initPlaylistFolder(metadataId);
                uzVideo.setUZCallback(FrmVideoTopV3.this);
            }
        });
    }
}
