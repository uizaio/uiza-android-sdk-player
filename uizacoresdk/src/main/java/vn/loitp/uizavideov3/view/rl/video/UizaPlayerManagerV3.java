package vn.loitp.uizavideov3.view.rl.video;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.request.target.Target;
import com.github.rubensousa.previewseekbar.base.PreviewLoader;
import com.github.rubensousa.previewseekbar.exoplayer.PreviewTimeBarLayout;
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.C.ContentType;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.util.ArrayList;
import java.util.List;

import loitp.core.R;
import vn.loitp.core.utilities.LConnectivityUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.uiza.model.v2.listallentity.Subtitle;
import vn.loitp.uizavideo.TrackSelectionHelper;
import vn.loitp.uizavideo.glide.GlideApp;
import vn.loitp.uizavideo.glide.GlideThumbnailTransformationPB;
import vn.loitp.uizavideo.listerner.ProgressCallback;
import vn.loitp.uizavideo.listerner.VideoAdPlayerListerner;

/**
 * Manages the {@link ExoPlayer}, the IMA plugin and all video playback.
 */
public final class UizaPlayerManagerV3 implements AdsMediaSource.MediaSourceFactory, PreviewLoader {
    private final String TAG = getClass().getSimpleName();
    private Context context;

    private UizaIMAVideoV3 uizaIMAVideoV3;
    private DebugTextViewHelper debugTextViewHelper;
    private ImaAdsLoader adsLoader = null;
    private final DataSource.Factory manifestDataSourceFactory;
    private final DataSource.Factory mediaDataSourceFactory;
    private long contentPosition;

    private SimpleExoPlayer player;

    private String userAgent;
    private String linkPlay;
    private List<Subtitle> subtitleList;

    public List<Subtitle> getSubtitleList() {
        return subtitleList;
    }

    public String getLinkPlay() {
        return linkPlay;
    }

    private VideoAdPlayerListerner videoAdPlayerListerner = new VideoAdPlayerListerner();

    private PreviewTimeBarLayout previewTimeBarLayout;
    private String thumbnailsUrl;
    private ImageView imageView;
    private Player.EventListener eventListener = new Player.DefaultEventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_READY && playWhenReady) {
                LLog.d(TAG, "onPlayerStateChanged STATE_READY");
                if (uizaIMAVideoV3 != null) {
                    uizaIMAVideoV3.hideLLMsg();
                    uizaIMAVideoV3.resetCountTryLinkPlayError();
                }
                if (previewTimeBarLayout != null) {
                    previewTimeBarLayout.hidePreview();
                }
            }
        }
    };

    private Handler handler;
    private Runnable runnable;

    private ProgressCallback progressCallback;

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    public UizaPlayerManagerV3(final UizaIMAVideoV3 uizaIMAVideoV3, String linkPlay, String urlIMAAd, String thumbnailsUrl, List<Subtitle> subtitleList) {
        this.mls = 0;
        this.context = uizaIMAVideoV3.getContext();
        this.uizaIMAVideoV3 = uizaIMAVideoV3;
        this.linkPlay = linkPlay;
        //LLog.d(TAG, "UizaPlayerManager linkPlay " + linkPlay);
        this.subtitleList = subtitleList;
        if (urlIMAAd == null || urlIMAAd.isEmpty()) {
            // LLog.d(TAG, "UizaPlayerManager urlIMAAd == null || urlIMAAd.isEmpty()");
        } else {
            adsLoader = new ImaAdsLoader(context, Uri.parse(urlIMAAd));
        }

        userAgent = Util.getUserAgent(context, context.getString(R.string.app_name));

        //OPTION 1 OK
        /*manifestDataSourceFactory = new DefaultDataSourceFactory(context, userAgent);
        mediaDataSourceFactory = new DefaultDataSourceFactory(
                context,
                userAgent,
                new DefaultBandwidthMeter());*/

        //OPTION 2 PLAY LINK REDIRECT
        // Default parameters, except allowCrossProtocolRedirects is true
        manifestDataSourceFactory = new DefaultHttpDataSourceFactory(
                userAgent,
                null /* listener */,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true /* allowCrossProtocolRedirects */
        );
        mediaDataSourceFactory = new DefaultDataSourceFactory(
                context,
                null /* listener */,
                manifestDataSourceFactory
        );

        //SETUP ORTHER
        this.imageView = uizaIMAVideoV3.getIvThumbnail();
        this.previewTimeBarLayout = uizaIMAVideoV3.getPreviewTimeBarLayout();
        this.thumbnailsUrl = thumbnailsUrl;
        //LLog.d(TAG, "UizaPlayerManager thumbnailsUrl " + thumbnailsUrl);
        setRunnable();
    }

    public void setRunnable() {
        //LLog.d(TAG, "runnable setRunnable");
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                //LLog.d(TAG, "runnable run");
                if (uizaIMAVideoV3.getPlayerView() != null) {
                    boolean isPlayingAd = videoAdPlayerListerner.isPlayingAd();
                    //LLog.d(TAG, "isPlayingAd " + isPlayingAd);
                    if (isPlayingAd) {
                        hideProgress();
                        if (progressCallback != null) {
                            VideoProgressUpdate videoProgressUpdate = adsLoader.getAdProgress();
                            mls = videoProgressUpdate.getCurrentTime();
                            duration = videoProgressUpdate.getDuration();
                            percent = (int) (mls * 100 / duration);
                            s = Math.round(mls / 1000);
                            //LLog.d(TAG, "runnable ad mls: " + mls + ", s: " + s + ", duration: " + duration + ", percent: " + percent + "%");
                            progressCallback.onAdProgress(mls, s, duration, percent);
                        }
                    } else {
                        if (progressCallback != null) {
                            if (player != null) {
                                mls = player.getCurrentPosition();
                                duration = player.getDuration();
                                percent = (int) (mls * 100 / duration);
                                s = Math.round(mls / 1000);
                                //LLog.d(TAG, "runnable video mls: " + mls + ", s: " + s + ", duration: " + duration + ", percent: " + percent + "%");
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

        //playerView.setControllerAutoShow(false);
        uizaIMAVideoV3.getPlayerView().setControllerShowTimeoutMs(0);
    }

    private float mls;
    private float duration;
    private int percent;
    private int s;

    private DefaultTrackSelector trackSelector;

    public DefaultTrackSelector getTrackSelector() {
        return trackSelector;
    }

    private TrackSelectionHelper trackSelectionHelper;

    public TrackSelectionHelper getTrackSelectionHelper() {
        return trackSelectionHelper;
    }

    private void initSource() {
        //Exo Player Initialization
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        trackSelectionHelper = new TrackSelectionHelper(trackSelector, videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        uizaIMAVideoV3.getPlayerView().setPlayer(player);

        MediaSource mediaSourceVideo = createMediaSourceVideo();

        //merge title to media source video
        //SUBTITLE
        MediaSource mediaSourceWithSubtitle = createMediaSourceWithSubtitle(mediaSourceVideo);

        //merge ads to media source subtitle
        //IMA ADS
        // Compose the content media source into a new AdsMediaSource with both ads and content.
        MediaSource mediaSourceWithAds = createMediaSourceWithAds(mediaSourceWithSubtitle);

        // Prepare the player with the source.

        player.addListener(eventListener);
        player.addListener(new PlayerEventListener());
        player.addAudioDebugListener(new AudioEventListener());
        player.addVideoDebugListener(new VideoEventListener());
        player.addMetadataOutput(new MetadataOutputListener());
        player.addTextOutput(new TextOutputListener());

        if (adsLoader != null) {
            adsLoader.addCallback(videoAdPlayerListerner);
        }
        player.prepare(mediaSourceWithAds);
        player.setPlayWhenReady(true);
        seekTo(contentPosition);

        LLog.d(TAG, "last progress volume " + uizaIMAVideoV3.getCurrentProgressSeekbarVolume());
        //uizaIMAVideoV3.setProgressVolumeSeekbar(uizaIMAVideoV3.getCurrentProgressSeekbarVolume());
        setVolume(uizaIMAVideoV3.getCurrentProgressSeekbarVolume());

        if (debugCallback != null) {
            debugCallback.onUpdateButtonVisibilities();
        }

        if (uizaIMAVideoV3.getDebugTextView() != null) {
            debugTextViewHelper = new DebugTextViewHelper(player, uizaIMAVideoV3.getDebugTextView());
            debugTextViewHelper.start();
        }
    }

    public void init() {
        reset();
        initSource();
    }

    public void initWithoutReset() {
        initSource();
    }

    private MediaSource createMediaSourceVideo() {
        //Video Source
        //MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(linkPlay));
        MediaSource mediaSourceVideo = buildMediaSource(Uri.parse(linkPlay), null, null);
        return mediaSourceVideo;
    }

    private MediaSource createMediaSourceWithSubtitle(MediaSource mediaSource) {
        if (subtitleList == null || subtitleList.isEmpty()) {
            return mediaSource;
        }
        //LLog.d(TAG, "createMediaSourceWithSubtitle " + gson.toJson(subtitleList));

        List<SingleSampleMediaSource> singleSampleMediaSourceList = new ArrayList<>();
        for (int i = 0; i < subtitleList.size(); i++) {
            Subtitle subtitle = subtitleList.get(i);
            if (subtitle == null || subtitle.getLanguage() == null || subtitle.getUrl() == null || subtitle.getUrl().isEmpty()) {
                continue;
            }
            DefaultBandwidthMeter bandwidthMeter2 = new DefaultBandwidthMeter();
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context, userAgent, bandwidthMeter2);
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


        //ADD SUBTITLE MANUAL -> WORK PERFECTLY
        /*DefaultBandwidthMeter bandwidthMeter2 = new DefaultBandwidthMeter();
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context, userAgent, bandwidthMeter2);
        //Text Format Initialization
        Format textFormat = Format.createTextSampleFormat(null, MimeTypes.TEXT_VTT, null, Format.NO_VALUE, Format.NO_VALUE, "ar", null, Format.OFFSET_SAMPLE_RELATIVE);

        String linkSub = "https://dev-static.uiza.io/subtitle_56a4f990-17e6-473c-8434-ef6c7e40bba1_vi_1522812445904.vtt";
        //String linkSub = "https://s3-ap-southeast-1.amazonaws.com/58aa3a0eb555420a945a27b47ce9ef2f-data/static/type_caption__entityId_81__language_en.vtt";
        //Arabic Subtitles
        SingleSampleMediaSource textMediaSourceAr = new SingleSampleMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(linkSub), textFormat, C.TIME_UNSET);
        //English Subtitles
        SingleSampleMediaSource textMediaSourceEn = new SingleSampleMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(linkSub), textFormat, C.TIME_UNSET);
        //French Subtitles
        SingleSampleMediaSource textMediaSourceFr = new SingleSampleMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(linkSub), textFormat, C.TIME_UNSET);

        //Final MediaSource
        MediaSource mediaSourceWithSubtitle = new MergingMediaSource(mediaSource, textMediaSourceAr, textMediaSourceEn, textMediaSourceFr);
        //player.prepare(mediaSource);
        //player.setPlayWhenReady(true);

        return mediaSourceWithSubtitle;*/
    }

    private MediaSource createMediaSourceWithAds(MediaSource mediaSource) {
        if (adsLoader == null) {
            return mediaSource;
        }
        MediaSource mediaSourceWithAds = new AdsMediaSource(
                mediaSource,
                this,
                adsLoader,
                uizaIMAVideoV3.getPlayerView().getOverlayFrameLayout(),
                null,
                null);
        return mediaSourceWithAds;
    }

    public void resumeVideo() {
        if (player != null) {
            player.setPlayWhenReady(true);
        }
    }

    public void pauseVideo() {
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }

    public void reset() {
        if (player != null) {
            contentPosition = player.getContentPosition();
            player.release();
            player = null;

            handler = null;
            runnable = null;

            trackSelectionHelper = null;
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

            trackSelectionHelper = null;
            if (debugTextViewHelper != null) {
                debugTextViewHelper.stop();
                debugTextViewHelper = null;
            }
        }
        if (adsLoader != null) {
            adsLoader.release();
        }
    }

    // AdsMediaSource.MediaSourceFactory implementation.

    @Override
    public MediaSource createMediaSource(Uri uri, @Nullable Handler handler, @Nullable MediaSourceEventListener listener) {
        return buildMediaSource(uri, handler, listener);
    }

    @Override
    public int[] getSupportedTypes() {
        // IMA does not support Smooth Streaming ads.
        return new int[]{C.TYPE_DASH, C.TYPE_HLS, C.TYPE_OTHER};
    }

    // Internal methods.
    private MediaSource buildMediaSource(Uri uri, @Nullable Handler handler, @Nullable MediaSourceEventListener listener) {
        @ContentType int type = Util.inferContentType(uri);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        manifestDataSourceFactory)
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory), manifestDataSourceFactory)
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(uri, handler, listener);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    @Override
    public void loadPreview(long currentPosition, long max) {
        player.setPlayWhenReady(false);
        if (thumbnailsUrl != null) {
            GlideApp.with(imageView)
                    .load(thumbnailsUrl)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .transform(new GlideThumbnailTransformationPB(currentPosition))
                    .into(imageView);
        }
    }

    public void hideProgress() {
        if (uizaIMAVideoV3.isCastingChromecast()) {
            return;
        }
        LUIUtil.hideProgressBar(uizaIMAVideoV3.getProgressBar());
    }

    public void showProgress() {
        LUIUtil.showProgressBar(uizaIMAVideoV3.getProgressBar());
    }

    private ExoPlaybackException exoPlaybackException;

    public ExoPlaybackException getExoPlaybackException() {
        return exoPlaybackException;
    }

    public class PlayerEventListener implements Player.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            //LLog.d(TAG, "onTimelineChanged");
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            //LLog.d(TAG, "onTracksChanged");
            if (debugCallback != null) {
                debugCallback.onUpdateButtonVisibilities();
            }
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            //LLog.d(TAG, "onLoadingChanged isLoading " + isLoading);
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            //LLog.d(TAG, "onPlayerStateChanged playWhenReady: " + playWhenReady);
            switch (playbackState) {
                case Player.STATE_BUFFERING:
                    showProgress();
                    break;
                case Player.STATE_ENDED:
                    LLog.d(TAG, "onPlayerStateChanged STATE_ENDED");
                    if (uizaIMAVideoV3 != null) {
                        uizaIMAVideoV3.onPlayerEnded();
                    }
                    hideProgress();
                    break;
                case Player.STATE_IDLE:
                    showProgress();
                    break;
                case Player.STATE_READY:
                    hideProgress();
                    break;
            }
            if (debugCallback != null) {
                debugCallback.onUpdateButtonVisibilities();
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            //LLog.d(TAG, "onRepeatModeChanged repeatMode: " + repeatMode);
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            //LLog.d(TAG, "onShuffleModeEnabledChanged shuffleModeEnabled: " + shuffleModeEnabled);
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            if (error == null) {
                return;
            }
            LLog.e(TAG, "onPlayerError " + error.toString() + " - " + error.getMessage());
            exoPlaybackException = error;
            if (debugCallback != null) {
                debugCallback.onUpdateButtonVisibilities();
            }
            if (uizaIMAVideoV3 == null) {
                return;
            }
            if (LConnectivityUtil.isConnected(context)) {
                uizaIMAVideoV3.tryNextLinkPlay();
            }
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            //LLog.d(TAG, "onPositionDiscontinuity");
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            //LLog.d(TAG, "onPlaybackParametersChanged");
        }

        @Override
        public void onSeekProcessed() {
            //LLog.d(TAG, "onSeekProcessed");
        }
    }

    public class AudioEventListener implements AudioRendererEventListener {

        @Override
        public void onAudioEnabled(DecoderCounters counters) {
            //LLog.d(TAG, "onAudioEnabled");
        }

        @Override
        public void onAudioSessionId(int audioSessionId) {
            //LLog.d(TAG, "onAudioSessionId audioSessionId: " + audioSessionId);
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
    }

    public class VideoEventListener implements VideoRendererEventListener {

        @Override
        public void onVideoEnabled(DecoderCounters counters) {
            //LLog.d(TAG, "onVideoEnabled");
        }

        @Override
        public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
            //LLog.d(TAG, "onVideoDecoderInitialized decoderName: " + decoderName + ", initializedTimestampMs " + initializedTimestampMs + ", initializationDurationMs " + initializationDurationMs);
        }

        @Override
        public void onVideoInputFormatChanged(Format format) {
            //LLog.d(TAG, "onVideoInputFormatChanged");
        }

        @Override
        public void onDroppedFrames(int count, long elapsedMs) {
            //LLog.d(TAG, "onDroppedFrames count " + count + ",elapsedMs " + elapsedMs);
        }

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            //LLog.d(TAG, "onVideoSizeChanged " + width + "x" + height + ", pixelWidthHeightRatio " + pixelWidthHeightRatio);
        }

        @Override
        public void onRenderedFirstFrame(Surface surface) {
            //LLog.d(TAG, "onRenderedFirstFrame");
            exoPlaybackException = null;
            uizaIMAVideoV3.removeVideoCover(false);
        }

        @Override
        public void onVideoDisabled(DecoderCounters counters) {
            //LLog.d(TAG, "onVideoDisabled");
        }
    }

    public class MetadataOutputListener implements MetadataOutput {

        @Override
        public void onMetadata(Metadata metadata) {
            //LLog.d(TAG, "onMetadata " + metadata.length());
        }
    }

    public class TextOutputListener implements TextOutput {

        @Override
        public void onCues(List<Cue> cues) {
            //LLog.d(TAG, "onCues " + cues.size());
        }
    }

    private float currentVolume;

    public void toggleVolumeMute(ImageButton exoVolume) {
        //LLog.d(TAG, "toggleVolumeMute");
        if (player == null || exoVolume == null) {
            //LLog.d(TAG, "toggleVolumeMute player == null || exoVolume == null -> return");
            return;
        }
        if (player.getVolume() == 0f) {
            //LLog.d(TAG, "toggleVolumeMute off -> on");
            uizaIMAVideoV3.setProgressVolumeSeekbar((int) (currentVolume * 100));
        } else {
            //LLog.d(TAG, "toggleVolumeMute on -> off");
            currentVolume = player.getVolume();
            uizaIMAVideoV3.setProgressVolumeSeekbar(0);
        }
        //LLog.d(TAG, "toggleVolumeMute currentVolume " + currentVolume);
        //LLog.d(TAG, "toggleVolumeMute player.getVolume() " + player.getVolume());
    }

    public SimpleExoPlayer getPlayer() {
        return player;
    }

    public void setVolume(float volume) {
        if (player != null) {
            //LLog.d(TAG, "setVolume volume " + volume);
            player.setVolume(volume);
            //LLog.d(TAG, "-> setVolume done: " + player.getVolume());
        }
    }

    public float getVolume() {
        if (player != null) {
            return player.getVolume();
        }
        return 0;
    }

    public boolean seekTo(long positionMs) {
        //LLog.d(TAG, "seekTo positionMs: " + positionMs);
        if (player != null) {
            player.seekTo(positionMs);
            //LLog.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>seekTo positionMs done");
            return true;
        }
        return false;
    }

    //forward  10000mls
    public void seekToForward(long forward) {
        if (player.getCurrentPosition() + forward > player.getDuration()) {
            player.seekTo(player.getDuration());
        } else {
            player.seekTo(player.getCurrentPosition() + forward);
        }
    }//next 10000mls

    public void seekToBackward(long backward) {
        if (player.getCurrentPosition() - backward > 0) {
            player.seekTo(player.getCurrentPosition() - backward);
        } else {
            player.seekTo(0);
        }
    }

    public interface DebugCallback {
        public void onUpdateButtonVisibilities();
    }

    private DebugCallback debugCallback;

    public void setDebugCallback(DebugCallback debugCallback) {
        this.debugCallback = debugCallback;
    }

    //if player is playing then turn off connection -> player is error -> store current position
    //then if connection is connected again, resume position
    public void setResumeIfConnectionError() {
        //LLog.d(TAG, "onMessageEvent setResumeIfConnectionError player current position mls: " + mls);
        contentPosition = (long) mls;
    }

    public long getCurrentPosition() {
        if (player == null) {
            return 0;
        }
        return player.getCurrentPosition();
    }
}
