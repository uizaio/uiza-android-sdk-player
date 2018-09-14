package testlibuiza.sample.v3.demoui;

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

import testlibuiza.R;
import vn.uiza.core.base.BaseFragment;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v2.listallentity.Item;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.uzv1.listerner.ProgressCallback;
import vn.uiza.uzv1.view.rl.video.UZPlayerView;
import vn.uiza.uzv1.view.rl.video.UZVideoV1;
import vn.uiza.uzv3.util.UZUtil;
import vn.uiza.uzv3.view.rl.video.UZCallback;
import vn.uiza.uzv3.view.rl.video.UZVideo;
import vn.uiza.views.LToast;

public class FrmVideoTop extends BaseFragment implements UZCallback {
    private UZVideo UZVideo;

    public UZVideo getUZVideo() {
        return UZVideo;
    }

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UZVideo = (UZVideo) view.findViewById(R.id.uiza_video);
        UZVideo.setUZCallback(this);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v4_frm_top;
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
                LToast.show(getActivity(), "Draw over other app permission not available");
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
            }

            @Override
            public void onVideoProgress(float currentMls, int s, float duration, int percent) {
                //LLog.d(TAG, TAG + " video progress: " + currentMls + "/" + duration + " -> " + percent + "%");
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
        UZVideo.setControllerStateCallback(new UZPlayerView.ControllerStateCallback() {
            @Override
            public void onVisibilityChange(boolean isShow) {
                if (((HomeV4CanSlideActivity) getActivity()).getDraggablePanel() != null
                        && !((HomeV4CanSlideActivity) getActivity()).isLandscapeScreen()) {
                    if (((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().isMaximized()) {
                        if (isShow) {
                            //LLog.d(TAG, TAG + " onVisibilityChange visibility == View.VISIBLE");
                            ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().setEnableSlide(false);
                        } else {
                            //LLog.d(TAG, TAG + " onVisibilityChange visibility != View.VISIBLE");
                            ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().setEnableSlide(true);
                        }
                    } else {
                        ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().setEnableSlide(true);
                    }
                }
            }
        });
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
        LLog.d(TAG, "isInitResult " + isInitSuccess);
        ((HomeV4CanSlideActivity) getActivity()).isInitResult(isGetDataSuccess, resultGetLinkPlay, data);
        if (isInitSuccess) {
            setListener();
            UZVideo.setEventBusMsgFromActivityIsInitSuccess();
        }
    }

    @Override
    public void onClickListEntityRelation(Item item, int position) {
    }

    @Override
    public void onClickBack() {
        if (LScreenUtil.isFullScreen(getActivity())) {
            UZVideo.toggleFullscreen();
        } else {
            ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().minimize();
        }
    }

    @Override
    public void onClickPip(Intent intent) {
        LLog.d(TAG, "onClickPip");
    }

    @Override
    public void onClickPipVideoInitSuccess(boolean isInitSuccess) {
        LLog.d(TAG, "onClickPipVideoInitSuccess " + isInitSuccess);
        if (isInitSuccess) {
            UZVideo.pauseVideo();
            ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().minimize();
            LUIUtil.setDelay(500, new LUIUtil.DelayCallback() {
                @Override
                public void doAfter(int mls) {
                    ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().closeToRight();
                }
            });
        }
    }

    @Override
    public void onSkinChange() {

    }

    @Override
    public void onError(Exception e) {
        LLog.d(TAG, "onError " + e.getMessage());
    }

    /*@Override
    public void onBackPressed() {
        if (LScreenUtil.isFullScreen(getActivity())) {
            uizaIMAVideoV3.toggleScreenOritation();
        } else {
            super.onBackPressed();
        }
    }*/

    public void initEntity(String entityId) {
        UZUtil.initEntity(getActivity(), UZVideo, entityId);
    }

    public void initPlaylistFolder(String metadataId) {
        UZUtil.initPlaylistFolder(getActivity(), UZVideo, metadataId);
    }
}
