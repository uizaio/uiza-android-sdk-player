package uiza.activity.home.v2.canslide;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

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
import vn.loitp.core.utilities.LDialogUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.uiza.model.v2.getdetailentity.GetDetailEntity;
import vn.loitp.restapi.uiza.model.v2.getlinkplay.GetLinkPlay;
import vn.loitp.restapi.uiza.model.v2.listallentity.Item;
import vn.loitp.uizavideo.listerner.ProgressCallback;
import vn.loitp.uizavideo.view.rl.video.UizaIMAVideo;
import vn.loitp.uizavideo.view.util.UizaData;
import vn.loitp.uizavideo.view.util.UizaInput;
import vn.loitp.views.LToast;

public class FrmVideoTop extends BaseFragment implements UizaIMAVideo.Callback {
    private final String TAG = getClass().getSimpleName();
    private UizaIMAVideo uizaIMAVideo;

    public UizaIMAVideo getUizaIMAVideo() {
        return uizaIMAVideo;
    }

    public interface FrmTopCallback {
        public void initDone(boolean isInitSuccess, GetLinkPlay getLinkPlay, GetDetailEntity getDetailEntity);

        public void onClickListEntityRelation(Item item, int position);
    }

    private FrmTopCallback frmTopCallback;

    public void setFrmTopCallback(FrmTopCallback frmTopCallback) {
        this.frmTopCallback = frmTopCallback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frm_top, container, false);
        uizaIMAVideo = (UizaIMAVideo) view.findViewById(R.id.uiza_video);
        uizaIMAVideo.registerReceiverPiPInitSuccess();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        uizaIMAVideo.unregisterReceiverPiPInitSuccess();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uizaIMAVideo.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        uizaIMAVideo.onResume();
    }

    @Override
    public void onPause() {
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
            if (resultCode == Activity.RESULT_OK) {
                //LLog.d(TAG, "onActivityResult RESULT_OK");
                uizaIMAVideo.initializePiP();
            } else {
                LToast.show(getActivity(), "Draw over other app permission not available");
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
                //LLog.d(TAG, TAG + " ad progress: " + currentMls + "/" + duration + " -> " + percent + "%");
            }

            @Override
            public void onVideoProgress(float currentMls, int s, float duration, int percent) {
                //LLog.d(TAG, TAG + " video progress: " + currentMls + "/" + duration + " -> " + percent + "%");
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
        LLog.d(TAG, "isInitSuccess " + isInitSuccess);
        if (isInitSuccess) {
            setListener();
            if (frmTopCallback != null) {
                frmTopCallback.initDone(isInitSuccess, getLinkPlay, getDetailEntity);
            }
        } else {
            UizaInput prevUizaInput = UizaData.getInstance().getUizaInputPrev();
            if (prevUizaInput == null) {
                LLog.d(TAG, "isInitSuccess prevUizaInput == null -> exit");
                ((HomeV2CanSlideActivity) getActivity()).getDraggablePanel().minimize();
                LUIUtil.setDelay(250, new LUIUtil.DelayCallback() {
                    @Override
                    public void doAfter(int mls) {
                        ((HomeV2CanSlideActivity) getActivity()).getDraggablePanel().closeToRight();
                    }
                });
            } else {
                LLog.d(TAG, "isInitSuccess prevUizaInput " + prevUizaInput.getEntityName());
                boolean isPlayPrev = UizaData.getInstance().isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed();
                LLog.d(TAG, "isInitSuccess isPlayPrev: " + isPlayPrev);
                if (isPlayPrev) {
                    setupVideo(prevUizaInput.getEntityId(), prevUizaInput.getEntityName(), prevUizaInput.getUrlThumnailsPreviewSeekbar(), prevUizaInput.getUrlIMAAd(), prevUizaInput.getUrlThumnailsPreviewSeekbar(), false);
                } else {
                    ((HomeV2CanSlideActivity) getActivity()).getDraggablePanel().minimize();
                    LUIUtil.setDelay(250, new LUIUtil.DelayCallback() {
                        @Override
                        public void doAfter(int mls) {
                            ((HomeV2CanSlideActivity) getActivity()).getDraggablePanel().closeToRight();
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onClickListEntityRelation(Item item, int position) {
        //LLog.d(TAG, "onClickListEntityRelation " + item.getName());
        if (frmTopCallback != null) {
            frmTopCallback.onClickListEntityRelation(item, position);
        }
    }

    @Override
    public void onClickBack() {
        ((HomeV2CanSlideActivity) getActivity()).getDraggablePanel().minimize();
    }

    @Override
    public void onClickPip(Intent intent) {
        //LLog.d(TAG, "onClickPip");
        ((HomeV2CanSlideActivity) getActivity()).getDraggablePanel().setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClickPipVideoInitSuccess(boolean isInitSuccess) {
        //LLog.d(TAG, "onClickPipVideoInitSuccess isInitSuccess: " + isInitSuccess);
        ((HomeV2CanSlideActivity) getActivity()).getDraggablePanel().closeToRight();
    }

    @Override
    public void onError(Exception e) {
        if (e != null) {
            LLog.e(TAG, "onError " + e.toString());
        }
        ((HomeV2CanSlideActivity) getActivity()).getDraggablePanel().closeToRight();
    }

    public void setupVideo(String entityId, String entityTitle, String entityCover, String urlIMAAd, String urlThumnailsPreviewSeekbar, boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed) {
        //LLog.d(TAG, "setupVideo");
        if (UizaData.getInstance().isSettingPlayer()) {
            //LLog.d(TAG, "setupVideo isSettingPlayer -> return");
            return;
        }
        if (entityId == null || entityId.isEmpty()) {
            LDialogUtil.showDialog1(getActivity(), getActivity().getString(R.string.entity_cannot_be_null_or_empty), new LDialogUtil.Callback1() {
                @Override
                public void onClick1() {
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                }

                @Override
                public void onCancel() {
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
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
        UizaData.getInstance().setUizaInput(uizaInput, isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed);

        //LLog.d(TAG, "setupVideo entityId " + entityId + ", entityTitle: " + entityTitle + ", entityCover: " + entityCover);
        //LLog.d(TAG, "setupVideo init with entityId " + entityId);
        uizaIMAVideo.init(this);
    }
}
