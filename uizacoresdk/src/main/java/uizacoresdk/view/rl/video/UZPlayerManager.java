package uizacoresdk.view.rl.video;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.Surface;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.request.target.Target;
import com.github.rubensousa.previewseekbar.PreviewLoader;
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

import uizacoresdk.glide.GlideApp;
import uizacoresdk.glide.GlideThumbnailTransformationPB;
import uizacoresdk.listerner.ProgressCallback;
import uizacoresdk.listerner.VideoAdPlayerListerner;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.rl.timebar.UZTimebar;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LConnectivityUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v2.listallentity.Subtitle;
import vn.uiza.utils.util.AppUtils;

/**
 * Manages the {@link ExoPlayer}, the IMA plugin and all video playback.
 */
public final class UZPlayerManager implements AdsMediaSource.MediaSourceFactory, PreviewLoader {
    private final String TAG = getClass().getSimpleName();
    private Context context;

    private UZVideo uzVideo;
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

    private UZTimebar uzTimebar;
    private String thumbnailsUrl;
    private ImageView imageView;
    private Player.EventListener eventListener = new Player.DefaultEventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_READY && playWhenReady) {
                //LLog.d(TAG, "onPlayerStateChanged STATE_READY");
                if (uzVideo != null) {
                    uzVideo.hideLayoutMsg();
                    uzVideo.resetCountTryLinkPlayError();
                }
                if (uzTimebar != null) {
                    uzTimebar.hidePreview();
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

    public UZPlayerManager(final UZVideo uzVideo, String linkPlay, String urlIMAAd, String thumbnailsUrl, List<Subtitle> subtitleList) {
        this.mls = 0;
        this.context = uzVideo.getContext();
        this.uzVideo = uzVideo;
        this.linkPlay = linkPlay;
        //LLog.d(TAG, "UZPlayerManagerV1 linkPlay " + linkPlay);
        this.subtitleList = subtitleList;
        if (urlIMAAd == null || urlIMAAd.isEmpty()) {
            // LLog.d(TAG, "UZPlayerManagerV1 urlIMAAd == null || urlIMAAd.isEmpty()");
        } else {
            if (UZUtil.getClickedPip(context)) {
                LLog.d(TAG, "UZPlayerManager dont init urlIMAAd because called from PIP again");
            } else {
                adsLoader = new ImaAdsLoader(context, Uri.parse(urlIMAAd));
            }
        }

        userAgent = Util.getUserAgent(context, AppUtils.getAppPackageName());

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
        this.imageView = uzVideo.getIvThumbnail();
        this.uzTimebar = uzVideo.getUZTimeBar();
        this.thumbnailsUrl = thumbnailsUrl;
        //LLog.d(TAG, "UZPlayerManagerV1 thumbnailsUrl " + thumbnailsUrl);
        setRunnable();
    }

    private boolean isOnAdEnded;

    private void onAdEnded() {
        if (!isOnAdEnded) {
            isOnAdEnded = true;
            if (uzVideo != null) {
                uzVideo.setDefaultUseController(uzVideo.isDefaultUseController());
            }
            //LLog.d(TAG, "onAdEnded " + isOnAdEnded);
            if (progressCallback != null) {
                progressCallback.onAdEnded();
            }
        }
    }

    protected boolean isPlayingAd() {
        if (videoAdPlayerListerner == null) {
            return false;
        }
        return videoAdPlayerListerner.isPlayingAd();
    }

    public void setRunnable() {
        //LLog.d(TAG, "runnable setRunnable");
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                //LLog.d(TAG, "runnable run");
                if (uzVideo.getPlayerView() != null) {
                    boolean isPlayingAd = videoAdPlayerListerner.isPlayingAd();
                    if (videoAdPlayerListerner.isEnded()) {
                        onAdEnded();
                    }
                    //LLog.d(TAG, "isPlayingAd " + isPlayingAd + ", isEnded " + isEnded);
                    if (isPlayingAd) {
                        hideProgress();
                        if (uzVideo != null) {
                            uzVideo.setUseController(false);
                        }
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
    }

    private float mls;
    private float duration;
    private int percent;
    private int s;

    private DefaultTrackSelector trackSelector;

    public DefaultTrackSelector getTrackSelector() {
        return trackSelector;
    }

    private void initSource() {
        isOnAdEnded = false;

        //Exo Player Initialization
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        uzVideo.getPlayerView().setPlayer(player);

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
        player.setPlayWhenReady(uzVideo.isAutoStart());
        seekTo(contentPosition);

        //LLog.d(TAG, "last progress volume " + uzVideo.getCurrentProgressSeekbarVolume());
        if (uzVideo.getCurrentProgressSeekbarVolume() == Constants.NOT_FOUND) {
            setVolume(0.99f);
        } else {
            setVolume(uzVideo.getCurrentProgressSeekbarVolume());
        }

        if (debugCallback != null) {
            debugCallback.onUpdateButtonVisibilities();
        }

        if (uzVideo.getDebugTextView() != null) {
            debugTextViewHelper = new DebugTextViewHelper(player, uzVideo.getDebugTextView());
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
        //MediaSource mediaSourceVideo = buildMediaSource(Uri.parse(linkPlay), null, null);
        MediaSource mediaSourceVideo = buildMediaSource(Uri.parse(linkPlay));
        return mediaSourceVideo;
    }

    private MediaSource createMediaSourceWithSubtitle(MediaSource mediaSource) {
        if (subtitleList == null || subtitleList.isEmpty()) {
            return mediaSource;
        }
        //LLog.d(TAG, "createMediaSourceWithSubtitle " + new Gson().toJson(subtitleList));

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
                uzVideo.getPlayerView().getOverlayFrameLayout(),
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

    /*@Override
    public MediaSource createMediaSource(Uri uri, @Nullable Handler handler, @Nullable MediaSourceEventListener listener) {
        return buildMediaSource(uri, handler, listener);
    }*/

    @Override
    public MediaSource createMediaSource(Uri uri) {
        return buildMediaSource(uri);
    }

    @Override
    public int[] getSupportedTypes() {
        // IMA does not support Smooth Streaming ads.
        return new int[]{C.TYPE_DASH, C.TYPE_HLS, C.TYPE_OTHER};
    }

    private MediaSource buildMediaSource(Uri uri) {
        @ContentType int type = Util.inferContentType(uri);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        manifestDataSourceFactory)
                        .createMediaSource(uri);
            //.createMediaSource(uri, handler, listener);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory), manifestDataSourceFactory)
                        .createMediaSource(uri);
            //.createMediaSource(uri, handler, listener);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(uri);
            //.createMediaSource(uri, handler, listener);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(uri);
            //.createMediaSource(uri, handler, listener);
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
        if (uzVideo.isCastingChromecast()) {
            return;
        }
        LUIUtil.hideProgressBar(uzVideo.getProgressBar());
    }

    public void showProgress() {
        LUIUtil.showProgressBar(uzVideo.getProgressBar());
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
                    //LLog.d(TAG, "onPlayerStateChanged STATE_ENDED");
                    if (uzVideo != null) {
                        uzVideo.onPlayerEnded();
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
            if (uzVideo == null) {
                return;
            }
            if (LConnectivityUtil.isConnected(context)) {
                uzVideo.tryNextLinkPlay();
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
            if (uzVideo != null) {
                uzVideo.removeVideoCover(false);
            }
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
            uzVideo.setProgressVolumeSeekbar((int) (currentVolume * 100));
        } else {
            //LLog.d(TAG, "toggleVolumeMute on -> off");
            currentVolume = player.getVolume();
            uzVideo.setProgressVolumeSeekbar(0);
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
        return Constants.NOT_FOUND;
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
            player.seekTo(player.getDuration() - 100);
        } else {
            player.seekTo(player.getCurrentPosition() + forward);
        }
    }

    //next 10000mls
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
