package uizacoresdk.view.floatview;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;

import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.C.ContentType;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;

import java.util.ArrayList;
import java.util.List;

import uizacoresdk.listerner.ProgressCallback;
import uizacoresdk.util.TmpParamData;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZExceptionUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.restapi.uiza.model.v2.listallentity.Subtitle;

public final class FUZPlayerManager implements AdsMediaSource.MediaSourceFactory {
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private FUZVideo fuzVideo;
    private DebugTextViewHelper debugTextViewHelper;
    private ImaAdsLoader adsLoader = null;
    private final DataSource.Factory manifestDataSourceFactory;
    private final DataSource.Factory mediaDataSourceFactory;
    private SimpleExoPlayer player;
    private String linkPlay;
    private List<Subtitle> subtitleList;
    private FUZVideoAdPlayerListener fUZVideoAdPlayerListener = new FUZVideoAdPlayerListener();
    private Handler handler;
    private Runnable runnable;
    private boolean isCanAddViewWatchTime;
    private long timestampPlayed;
    private ProgressCallback progressCallback;
    private int videoW;
    private int videoH;
    private DefaultTrackSelector trackSelector;

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    public FUZPlayerManager(final FUZVideo fuzVideo, String linkPlay, String urlIMAAd, String thumbnailsUrl, List<Subtitle> subtitleList) {
        this.timestampPlayed = System.currentTimeMillis();
        isCanAddViewWatchTime = true;
        this.context = fuzVideo.getContext();
        this.fuzVideo = fuzVideo;
        this.linkPlay = linkPlay;
        this.subtitleList = subtitleList;
        this.videoW = 0;
        this.videoH = 0;
        if (urlIMAAd == null || urlIMAAd.isEmpty()) {
            // skip this
        } else {
            adsLoader = new ImaAdsLoader(context, Uri.parse(urlIMAAd));
        }
        manifestDataSourceFactory = new DefaultDataSourceFactory(context, Constants.USER_AGENT);
        mediaDataSourceFactory = new DefaultDataSourceFactory(context, Constants.USER_AGENT, new DefaultBandwidthMeter());
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (fuzVideo.getPlayerView() != null) {
                    boolean isPlayingAd = fUZVideoAdPlayerListener.isPlayingAd();
                    if (isPlayingAd) {
                        fuzVideo.hideProgress();
                        if (progressCallback != null) {
                            VideoProgressUpdate videoProgressUpdate = adsLoader.getAdProgress();
                            int duration = (int) videoProgressUpdate.getDuration();
                            int s = (int) videoProgressUpdate.getCurrentTime();
                            int percent = 0;
                            if (duration != 0) {
                                percent = s * 100 / duration;
                            }
                            progressCallback.onAdProgress(s, duration, percent);
                        }
                    } else {
                        if (progressCallback != null) {
                            if (player != null) {
                                long mls = player.getCurrentPosition();
                                long duration = player.getDuration();
                                int percent = 0;
                                if (duration != 0) {
                                    percent = (int) (mls * 100 / duration);
                                }
                                int s = Math.round(mls / 1000);
                                progressCallback.onVideoProgress(mls, s, duration, percent);
                            }
                        }
                    }
                    if (handler != null && runnable != null) {
                        handler.postDelayed(runnable, 1000);
                    }
                }
            }
        };
        handler.postDelayed(runnable, 0);
        fuzVideo.getPlayerView().setControllerShowTimeoutMs(0);
    }

    public DefaultTrackSelector getTrackSelector() {
        return trackSelector;
    }

    public void init(boolean isLivestream, long contentPosition) {
        LLog.d(TAG, "miniplayer STEP 1 FUZPLayerManager init isLivestream " + isLivestream + ", contentPosition " + contentPosition);
        reset();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        fuzVideo.getPlayerView().setPlayer(player);
        MediaSource mediaSourceVideo = createMediaSourceVideo();
        //merge title to media source video
        //SUBTITLE
        MediaSource mediaSourceWithSubtitle = createMediaSourceWithSubtitle(mediaSourceVideo);
        //merge ads to media source subtitle
        //IMA ADS
        // Compose the content media source into a new AdsMediaSource with both ads and content.
        MediaSource mediaSourceWithAds = createMediaSourceWithAds(mediaSourceWithSubtitle);
        //Prepare the player with the source.
        player.addListener(new FUZPlayerEventListener());
        player.addVideoListener(new FUZVideoListener());
        if (adsLoader != null) {
            adsLoader.addCallback(fUZVideoAdPlayerListener);
        }
        player.prepare(mediaSourceWithAds);
        //setVolumeOff();
        if (isLivestream) {
            player.seekToDefaultPosition();
        } else {
            seekTo(contentPosition);
        }
        player.setPlayWhenReady(true);
    }

    public void seekTo(long position) {
        if (player == null) {
            return;
        }
        player.seekTo(position);
    }

    private MediaSource createMediaSourceVideo() {
        //Video Source
        return buildMediaSource(Uri.parse(linkPlay));
    }

    private MediaSource createMediaSourceWithSubtitle(MediaSource mediaSource) {
        if (subtitleList == null || subtitleList.isEmpty()) {
            return mediaSource;
        }
        List<SingleSampleMediaSource> singleSampleMediaSourceList = new ArrayList<>();
        for (int i = 0; i < subtitleList.size(); i++) {
            Subtitle subtitle = subtitleList.get(i);
            if (subtitle == null || subtitle.getLanguage() == null || subtitle.getUrl() == null || subtitle.getUrl().isEmpty()) {
                continue;
            }
            DefaultBandwidthMeter bandwidthMeter2 = new DefaultBandwidthMeter();
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context, Constants.USER_AGENT, bandwidthMeter2);
            //Text Format Initialization
            Format textFormat = Format.createTextSampleFormat(null, MimeTypes.TEXT_VTT, null, Format.NO_VALUE, Format.NO_VALUE, subtitle.getLanguage(), null, Format.OFFSET_SAMPLE_RELATIVE);
            SingleSampleMediaSource textMediaSource = new SingleSampleMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(subtitle.getUrl()), textFormat, C.TIME_UNSET);
            singleSampleMediaSourceList.add(textMediaSource);
        }
        MediaSource mediaSourceWithSubtitle = null;
        for (int i = 0; i < singleSampleMediaSourceList.size(); i++) {
            SingleSampleMediaSource singleSampleMediaSource = singleSampleMediaSourceList.get(i);
            if (i == 0) {
                mediaSourceWithSubtitle = new MergingMediaSource(mediaSource, singleSampleMediaSource);
            } else {
                mediaSourceWithSubtitle = new MergingMediaSource(mediaSourceWithSubtitle, singleSampleMediaSource);
            }
        }
        return mediaSourceWithSubtitle;
    }

    private MediaSource createMediaSourceWithAds(MediaSource mediaSource) {
        if (adsLoader == null) {
            return mediaSource;
        }
        return new AdsMediaSource(
                mediaSource,
                this,
                adsLoader,
                fuzVideo.getPlayerView().getOverlayFrameLayout(),
                null,
                null);
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
        if (adsLoader != null) {
            adsLoader.release();
        }
    }

    @Override
    public MediaSource createMediaSource(Uri uri) {
        return buildMediaSource(uri);
    }

    @Override
    public int[] getSupportedTypes() {
        // IMA does not support Smooth Streaming ads.
        return new int[]{C.TYPE_DASH, C.TYPE_HLS, C.TYPE_OTHER};
    }

    // Internal methods.
    private MediaSource buildMediaSource(Uri uri) {
        @ContentType int type = Util.inferContentType(uri);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        manifestDataSourceFactory)
                        .createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory), manifestDataSourceFactory)
                        .createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    private class FUZPlayerEventListener implements Player.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (fuzVideo != null) {
                fuzVideo.onPlayerStateChanged(playWhenReady, playbackState);
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
            if (fuzVideo != null) {
                fuzVideo.onPlayerError(UZExceptionUtil.getExceptionPlayback());
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


    protected int getVideoW() {
        return videoW;
    }

    protected int getVideoH() {
        return videoH;
    }

    private class FUZVideoListener implements VideoListener {
        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            videoW = width;
            videoH = height;
            if (fuzVideo != null) {
                fuzVideo.onVideoSizeChanged(width, height);
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

    private static class FUZVideoAdPlayerListener implements VideoAdPlayer.VideoAdPlayerCallback {
        private boolean isPlayingAd;
        private boolean isEnded;

        @Override
        public void onPlay() {
            isPlayingAd = true;
        }

        @Override
        public void onVolumeChanged(int i) {
        }

        @Override
        public void onPause() {
            isPlayingAd = false;
        }

        @Override
        public void onLoaded() {
        }

        @Override
        public void onResume() {
            isPlayingAd = true;
        }

        @Override
        public void onEnded() {
            isPlayingAd = false;
            isEnded = true;
        }

        @Override
        public void onError() {
            isPlayingAd = false;
        }

        @Override
        public void onBuffering() {
        }

        public boolean isPlayingAd() {
            return isPlayingAd;
        }

        public boolean isEnded() {
            return isEnded;
        }
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
}
