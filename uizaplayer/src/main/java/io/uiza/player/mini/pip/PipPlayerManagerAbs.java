package io.uiza.player.mini.pip;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import io.uiza.core.api.response.subtitle.Subtitle;
import io.uiza.core.exception.UzExceptionUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.player.analytic.TmpParamData;
import io.uiza.player.interfaces.UzAdEventListener;
import io.uiza.player.interfaces.UzPlayerEventListener;
import java.util.ArrayList;
import java.util.List;

abstract class PipPlayerManagerAbs {

    protected static final String TAG = PipPlayerManagerAbs.class.getSimpleName();
    protected Context context;
    protected UzPipPlayer uzPipPlayer;
    protected DebugTextViewHelper debugTextViewHelper;
    protected DataSource.Factory manifestDataSourceFactory;
    protected DataSource.Factory mediaDataSourceFactory;
    protected SimpleExoPlayer player;
    protected String linkPlay;
    protected List<Subtitle> subtitleList;
    protected Handler handler;
    protected Runnable runnable;
    protected boolean isCanAddViewWatchTime;
    protected long timestampPlayed;
    protected UzAdEventListener adEventListener;
    protected UzPlayerEventListener playerEventListener;
    protected int videoWidth;
    protected int videoHeight;
    protected DefaultTrackSelector trackSelector;

    public void setUzPlayerEventListener(UzPlayerEventListener playerEventListener) {
        this.playerEventListener = playerEventListener;
    }

    public void setUzAdEventListener(UzAdEventListener adEventListener) {
        this.adEventListener = adEventListener;
    }

    public DefaultTrackSelector getTrackSelector() {
        return trackSelector;
    }

    public void seekTo(long position) {
        if (player == null) {
            return;
        }
        player.seekTo(position);
    }

    MediaSource createMediaSourceVideo() {
        //Video Source
        return buildMediaSource(Uri.parse(linkPlay));
    }

    // Currently, subtitle is not supported in mini player
    MediaSource createMediaSourceWithSubtitle(MediaSource videoMediaSource) {
        if (subtitleList == null || subtitleList.isEmpty()) {
            return videoMediaSource;
        }
        List<MediaSource> mergedMediaSource = new ArrayList<>();
        mergedMediaSource.add(videoMediaSource);

        // Try to add text (subtitle) media source
        for (int i = 0; i < subtitleList.size(); i++) {

            Subtitle subtitle = subtitleList.get(i);

            if (subtitle == null || TextUtils.isEmpty(subtitle.getLanguage()) || TextUtils.isEmpty(
                    subtitle.getUrl()) || subtitle.getStatus() == Subtitle.Status.DISABLE) {
                continue;
            }

            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DefaultDataSourceFactory dataSourceFactory =
                    new DefaultDataSourceFactory(context, Constants.USER_AGENT, bandwidthMeter);

            String subLink = subtitle.getUrl();
            // String type = subtitle.getMine().toUpperCase(); //for future need to get type from Mime.
            String type = subLink.substring(subLink.lastIndexOf(".")).toUpperCase();
            String sampleMimeType = null;
            switch (type) {
                case Constants.TYPE_VTT:
                    sampleMimeType = MimeTypes.TEXT_VTT;
                    break;
                case Constants.TYPE_SRT:
                    sampleMimeType = MimeTypes.APPLICATION_SUBRIP;
                    break;
            }
            Format textFormat;
            if (TextUtils.isEmpty(subtitle.getName())) {
                textFormat = Format
                        .createTextSampleFormat(null, sampleMimeType, null, Format.NO_VALUE,
                                Format.NO_VALUE, subtitle.getLanguage(), null,
                                Format.OFFSET_SAMPLE_RELATIVE);
            } else {
                // TextFormat with custom label
                textFormat = Format
                        .createTextContainerFormat(null, subtitle.getName(), null, sampleMimeType,
                                null,
                                Format.NO_VALUE, Format.NO_VALUE, subtitle.getLanguage());
            }
            MediaSource textMediaSource =
                    new SingleSampleMediaSource.Factory(dataSourceFactory).createMediaSource(
                            Uri.parse(subLink), textFormat, C.TIME_UNSET);

            // Re-order default subtitle right after video source
            if (subtitle.getIsDefault() == 1) {
                mergedMediaSource.add(1, textMediaSource);
            } else {
                mergedMediaSource.add(textMediaSource);
            }
        }

        return new MergingMediaSource(mergedMediaSource.toArray(new MediaSource[0]));
    }

    void handleVideoProgress() {
        if (playerEventListener != null) {
            if (player != null) {
                long mls = player.getCurrentPosition();
                long duration = player.getDuration();
                int percent = 0;
                if (duration != 0) {
                    percent = (int) (mls * 100 / duration);
                }
                int s = Math.round(mls / 1000f);
                playerEventListener.onVideoProgress(mls, s, duration, percent);
            }
        }
    }

    //return true if toggleResume
    //return false if togglePause
    public boolean togglePauseResume() {
        if (player == null) {
            return false;
        }
        if (player.getPlayWhenReady()) {
            pauseVideo();
            return false;
        } else {
            resumeVideo();
            return true;
        }
    }

    public void resumeVideo() {
        if (player != null) {
            player.setPlayWhenReady(true);
        }
        timestampPlayed = System.currentTimeMillis();
        isCanAddViewWatchTime = true;
    }

    public void pauseVideo() {
        if (player != null) {
            player.setPlayWhenReady(false);
            if (isCanAddViewWatchTime) {
                long durationWatched = System.currentTimeMillis() - timestampPlayed;
                TmpParamData.getInstance().addViewWatchTime(durationWatched);
                isCanAddViewWatchTime = false;
            }
        }
    }

    public void reset() {
        if (player != null) {
            player.release();
            player = null;
            handler = null;
            runnable = null;
            if (debugTextViewHelper != null) {
                debugTextViewHelper.stop();
                debugTextViewHelper = null;
            }
        }
    }

    public void release() {
        if (player != null) {
            player.release();
            player = null;
            handler = null;
            runnable = null;
            if (debugTextViewHelper != null) {
                debugTextViewHelper.stop();
                debugTextViewHelper = null;
            }
        }
    }

    // Internal methods.
    MediaSource buildMediaSource(Uri uri) {
        @C.ContentType int type = Util.inferContentType(uri);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        manifestDataSourceFactory).createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory),
                        manifestDataSourceFactory).createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(uri);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    class PipPlayerEventListener implements Player.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups,
                TrackSelectionArray trackSelections) {
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (uzPipPlayer != null) {
                uzPipPlayer.onPlayerStateChanged(playWhenReady, playbackState);
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            if (uzPipPlayer != null) {
                uzPipPlayer.onPlayerError(UzExceptionUtil.getExceptionPlayback());
            }
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        }

        @Override
        public void onSeekProcessed() {
        }
    }

    protected int getVideoWidth() {
        return videoWidth;
    }

    protected int getVideoHeight() {
        return videoHeight;
    }

    class PipVideoListener implements VideoListener {

        @Override
        public void onVideoSizeChanged(int width, int height, int unAppliedRotationDegrees,
                float pixelWidthHeightRatio) {
            videoWidth = width;
            videoHeight = height;
            if (uzPipPlayer != null) {
                uzPipPlayer.onVideoSizeChanged(width, height);
            }
        }

        @Override
        public void onSurfaceSizeChanged(int width, int height) {
        }

        @Override
        public void onRenderedFirstFrame() {
        }
    }

    public SimpleExoPlayer getPlayer() {
        return player;
    }

    protected void setVolume(float volume) {
        if (player != null) {
            player.setVolume(volume);
        }
    }

    protected void setVolumeOn() {
        if (player != null) {
            player.setVolume(1f);
        }
    }

    protected void setVolumeOff() {
        if (player != null) {
            player.setVolume(0f);
        }
    }

    abstract void init(boolean isLivestream, long contentPosition);
}
