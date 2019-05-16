package uizacoresdk.view.rl.video;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.request.target.Target;
import com.github.rubensousa.previewseekbar.PreviewLoader;
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.C.ContentType;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.audio.AudioListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
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
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import uizacoresdk.glide.GlideApp;
import uizacoresdk.glide.GlideThumbnailTransformationPB;
import uizacoresdk.listerner.ProgressCallback;
import uizacoresdk.util.TmpParamData;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.rl.timebar.UZTimebar;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZExceptionUtil;
import vn.uiza.core.utilities.LConnectivityUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v2.listallentity.Subtitle;
import vn.uiza.utils.util.SentryUtils;
import vn.uiza.views.autosize.UZImageButton;

/**
 * Manages the {@link ExoPlayer}, the IMA plugin and all video playback.
 */
//https://medium.com/@takusemba/understands-callbacks-of-exoplayer-c05ac3c322c2
public final class UZPlayerManager implements AdsMediaSource.MediaSourceFactory, PreviewLoader {
    private final String TAG = "TAG" + getClass().getSimpleName();
    private static final String PLAYER_STATE_FORMAT = "playWhenReady:%s playbackState:%s window:%s";
    private Context context;
    private UZVideo uzVideo;
    private ImaAdsLoader adsLoader = null;
    private final DataSource.Factory manifestDataSourceFactory;
    private final DataSource.Factory mediaDataSourceFactory;
    private long contentPosition;
    private SimpleExoPlayer player;
    private UZPlayerHelper playerHelper;
    private String linkPlay;
    private List<Subtitle> subtitleList;
    private FrameworkMediaDrm mediaDrm;
    private boolean isFirstStateReady;

    public List<Subtitle> getSubtitleList() {
        return subtitleList;
    }
    public String getLinkPlay() {
        return linkPlay;
    }

    private UZVideoAdPlayerListener uzVideoAdPlayerListener = new UZVideoAdPlayerListener();

    private UZTimebar uzTimebar;
    private String thumbnailsUrl;
    private ImageView imageView;
    private Handler handler;
    private Runnable runnable;
    private boolean isCanAddViewWatchTime;
    private long timestampPlayed;

    private ProgressCallback progressCallback;
    private long mls = 0;
    private long duration = 0;
    private int percent = 0;
    private int s = 0;
    private long bufferPosition = 0;
    private int bufferPercentage = 0;
    private int videoW = 0;
    private int videoH = 0;
    private DefaultTrackSelector trackSelector;
    private float volumeToggle;
    private DebugCallback debugCallback;
    private ExoPlaybackException exoPlaybackException;
    private boolean isOnAdEnded;

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    public UZPlayerManager(final UZVideo uzVideo, String linkPlay, String urlIMAAd, String thumbnailsUrl, List<Subtitle> subtitleList) {
        TmpParamData.getInstance().setPlayerInitTime(System.currentTimeMillis());
        this.timestampPlayed = System.currentTimeMillis();
        isCanAddViewWatchTime = true;
        //LLog.d(TAG, "timestampPlayed: " + timestampPlayed);
        this.context = uzVideo.getContext();
        this.videoW = 0;
        this.videoH = 0;
        this.mls = 0;
        this.bufferPosition = 0;
        this.bufferPercentage = 0;
        this.uzVideo = uzVideo;
        this.linkPlay = linkPlay;
        this.subtitleList = subtitleList;
        this.isFirstStateReady = false;
        if (urlIMAAd != null && !urlIMAAd.isEmpty()) {
            if (UZUtil.getClickedPip(context)) {
                LLog.e(TAG, "UZPlayerManager don't init urlIMAAd because called from PIP again");
            } else {
                adsLoader = new ImaAdsLoader(context, Uri.parse(urlIMAAd));
            }
        }
        //OPTION 1 OK
        /*manifestDataSourceFactory = new DefaultDataSourceFactory(context, userAgent);
        mediaDataSourceFactory = new DefaultDataSourceFactory(
                context,
                userAgent,
                new DefaultBandwidthMeter());*/

        //OPTION 2 PLAY LINK REDIRECT
        // Default parameters, except allowCrossProtocolRedirects is true
        manifestDataSourceFactory = new DefaultHttpDataSourceFactory(
                Constants.USER_AGENT,
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
        setRunnable();
    }

    private void onAdEnded() {
        if (!isOnAdEnded && uzVideo != null) {
            isOnAdEnded = true;
            if (progressCallback != null) {
                progressCallback.onAdEnded();
            }
        }
    }

    protected boolean isPlayingAd() {
        if (uzVideoAdPlayerListener == null) {
            return false;
        }
        return uzVideoAdPlayerListener.isPlayingAd();
    }

    public void setRunnable() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (uzVideo != null && uzVideo.getUzPlayerView() != null) {
                    boolean isPlayingAd = uzVideoAdPlayerListener.isPlayingAd();
                    if (uzVideoAdPlayerListener.isEnded()) {
                        onAdEnded();
                    }
                    if (isPlayingAd) {
                        hideProgress();
                        uzVideo.setUseController(false);
                        if (progressCallback != null) {
                            VideoProgressUpdate videoProgressUpdate = adsLoader.getAdProgress();
                            duration = (int) videoProgressUpdate.getDuration();
                            s = (int) (videoProgressUpdate.getCurrentTime()) + 1;//add 1 second
                            if (duration != 0) {
                                percent = (int) (s * 100 / duration);
                            }
                            progressCallback.onAdProgress(s, (int) duration, percent);
                        }
                    } else {
                        if (progressCallback != null && isPlayerValid()) {
                            mls = getCurrentPosition();
                            duration = getDuration();
                            if (mls >= duration) {
                                mls = duration;
                            }
                            if (duration != 0) {
                                percent = (int) (mls * 100 / duration);
                            }
                            s = Math.round(mls / 1000);
                            progressCallback.onVideoProgress(mls, s, duration, percent);
                            //buffer changing
                            if (bufferPosition != uzVideo.getBufferedPosition() || bufferPercentage != uzVideo.getBufferedPercentage()) {
                                bufferPosition = uzVideo.getBufferedPosition();
                                bufferPercentage = uzVideo.getBufferedPercentage();
                                progressCallback.onBufferProgress(uzVideo.getBufferedPosition(), uzVideo.getBufferedPercentage(), duration);
                            }
                        }
                    }
                    if (uzVideo.getDebugTextView() != null) {
                        uzVideo.getDebugTextView().setText(getDebugString());
                    }
                    if (handler != null && runnable != null) {
                        handler.postDelayed(runnable, 1000);
                    }
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    public DefaultTrackSelector getTrackSelector() {
        return trackSelector;
    }

    private DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

    private void initSource() {
        isOnAdEnded = false;
        //TODO DRM
        //Exo Player Initialization
        String drmScheme = Constants.DRM_SCHEME_NULL;
        //String drmScheme = Constants.DRM_SCHEME_PLAYREADY;
        //String drmScheme = Constants.DRM_SCHEME_WIDEVINE;
        DefaultDrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
        if (drmScheme != Constants.DRM_SCHEME_NULL) {
            String drmLicenseUrl = "https://wv.service.expressplay.com/hms/wv/rights/?ExpressPlayToken=BAAaXbkVKbEAAABg_0gifyfSLlqtjYGc9boiYUIudGi445e5xHzay2CzEazC0uj6GWg79k_yexpv7t2GmjWF10ehecUV2kqV5MBWM-7kURuaQcSJ368ocXFpcoT4l2EXQO8_9R67vZC3Y9lDqLE-9_FTTIqg7C-oWLoXZgWAmJQ";
            String[] keyRequestPropertiesArray = null;
            boolean multiSession = false;
            String errorStringId = "An unknown DRM error occurred";
            if (Util.SDK_INT < 18) {
                errorStringId = "Protected content not supported on API levels below 18";
            } else {
                try {
                    UUID drmSchemeUuid = Util.getDrmUuid(drmScheme);
                    drmSessionManager = buildDrmSessionManagerV18(drmSchemeUuid, drmLicenseUrl, keyRequestPropertiesArray, multiSession);
                } catch (UnsupportedDrmException e) {
                    LLog.e(TAG, "UnsupportedDrmException " + e.toString());
                    SentryUtils.captureException(e);
                }
            }
            if (drmSessionManager == null) {
                LLog.e(TAG, "Error drmSessionManager: " + errorStringId);
                return;
            }
        }
        @DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode = DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(context, extensionRendererMode);
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(context, renderersFactory, trackSelector, drmSessionManager);
        playerHelper = new UZPlayerHelper(player);
        uzVideo.getUzPlayerView().setPlayer(player);
        MediaSource mediaSourceVideo = createMediaSourceVideo();
        //merge title to media source video
        //SUBTITLE
        MediaSource mediaSourceWithSubtitle = createMediaSourceWithSubtitle(mediaSourceVideo);
        //merge ads to media source subtitle
        //IMA ADS
        // Compose the content media source into a new AdsMediaSource with both ads and content.
        MediaSource mediaSourceWithAds = createMediaSourceWithAds(mediaSourceWithSubtitle);
        // Prepare the player with the source.
        player.addListener(new UZPlayerEventListener());
        player.addAudioListener(new UZAudioEventListener());
        player.addVideoListener(new UZVideoEventListener());
        player.addMetadataOutput(new UZMetadataOutputListener());
        player.addTextOutput(new UZTextOutputListener());
        if (adsLoader != null) {
            adsLoader.addCallback(uzVideoAdPlayerListener);
        }
        player.prepare(mediaSourceWithAds);
        setPlayWhenReady(uzVideo.isAutoStart());
        if (uzVideo.isLivestream()) {
            playerHelper.seekToDefaultPosition();
        } else {
            seekTo(contentPosition);
        }
        if (debugCallback != null) {
            debugCallback.onUpdateButtonVisibilities();
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
        return buildMediaSource(Uri.parse(linkPlay));
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory() {
        return new DefaultHttpDataSourceFactory(Constants.USER_AGENT);
    }

    private DefaultDrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManagerV18(UUID uuid, String licenseUrl, String[] keyRequestPropertiesArray, boolean multiSession) throws UnsupportedDrmException {
        HttpDataSource.Factory licenseDataSourceFactory = buildHttpDataSourceFactory();
        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl, licenseDataSourceFactory);
        if (keyRequestPropertiesArray != null) {
            for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i], keyRequestPropertiesArray[i + 1]);
            }
        }
        releaseMediaDrm();
        mediaDrm = FrameworkMediaDrm.newInstance(uuid);
        return new DefaultDrmSessionManager<>(uuid, mediaDrm, drmCallback, null, multiSession);
    }

    private void releaseMediaDrm() {
        if (mediaDrm != null) {
            mediaDrm.release();
            mediaDrm = null;
        }
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
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context, Constants.USER_AGENT, bandwidthMeter);
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
                uzVideo.getUzPlayerView().getOverlayFrameLayout(),
                null,
                null);
    }

    protected void resumeVideo() {
        setPlayWhenReady(true);
        timestampPlayed = System.currentTimeMillis();
        isCanAddViewWatchTime = true;
    }

    protected void pauseVideo() {
        if (isPlayerValid()) {
            setPlayWhenReady(false);
            if (isCanAddViewWatchTime) {
                long durationWatched = System.currentTimeMillis() - timestampPlayed;
                TmpParamData.getInstance().addViewWatchTime(durationWatched);
                isCanAddViewWatchTime = false;
            }
        }
    }

    protected void reset() {
        if (isPlayerValid()) {
            contentPosition = playerHelper.getContentPosition();
            playerHelper.release();
            handler = null;
            runnable = null;
        }
    }

    public void release() {
        if (isPlayerValid()) {
            playerHelper.release();
            handler = null;
            runnable = null;
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

    private MediaSource buildMediaSource(Uri uri) {
        @ContentType int type = Util.inferContentType(uri);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(new DefaultDashChunkSource.Factory(mediaDataSourceFactory), manifestDataSourceFactory).createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(new DefaultSsChunkSource.Factory(mediaDataSourceFactory), manifestDataSourceFactory).createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    @Override
    public void loadPreview(long currentPosition, long max) {
        if (!isPlayerValid()) {
            return;
        }
        setPlayWhenReady(false);
        if (thumbnailsUrl != null) {
            GlideApp.with(imageView)
                    .load(thumbnailsUrl)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .transform(new GlideThumbnailTransformationPB(currentPosition))
                    .into(imageView);
        }
    }

    protected void hideProgress() {
        if (uzVideo.isCastingChromecast()) {
            return;
        }
        LUIUtil.hideProgressBar(uzVideo.getProgressBar());
    }

    protected void showProgress() {
        LUIUtil.showProgressBar(uzVideo.getProgressBar());
    }

    protected ExoPlaybackException getExoPlaybackException() {
        return exoPlaybackException;
    }

    private void onFirstStateReady() {
        if (uzVideo != null) {
            long durationInS = uzVideo.getDuration() / 1000;
            TmpParamData.getInstance().setEntityDuration(durationInS + "");
            TmpParamData.getInstance().setEntitySourceDuration(durationInS + "");
            uzVideo.removeVideoCover(false);
        }
    }

    private class UZPlayerEventListener implements Player.EventListener {
        private long timestampRebufferStart;

        //This is called when the current playlist changes
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            if (uzVideo != null && uzVideo.eventListener != null) {
                uzVideo.eventListener.onTimelineChanged(timeline, manifest, reason);
            }
        }

        //This is called when the available or selected tracks change
        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            if (debugCallback != null) {
                debugCallback.onUpdateButtonVisibilities();
            }
            if (uzVideo != null && uzVideo.eventListener != null) {
                uzVideo.eventListener.onTracksChanged(trackGroups, trackSelections);
            }
        }

        //This is called when ExoPlayer starts or stops loading sources(TS files, fMP4 files…)
        @Override
        public void onLoadingChanged(boolean isLoading) {
            if (uzVideo != null && uzVideo.eventListener != null) {
                uzVideo.eventListener.onLoadingChanged(isLoading);
            }
        }

        //This is called when either playWhenReady or playbackState changes
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case Player.STATE_BUFFERING:
                    showProgress();
                    if (uzVideo != null) {
                        if (playWhenReady) {
                            TmpParamData.getInstance().setViewRebufferDuration(System.currentTimeMillis() - timestampRebufferStart);
                            timestampRebufferStart = 0;
                            uzVideo.addTrackingMuiza(Constants.MUIZA_EVENT_REBUFFEREND);
                        } else {
                            timestampRebufferStart = System.currentTimeMillis();
                            uzVideo.addTrackingMuiza(Constants.MUIZA_EVENT_REBUFFERSTART);
                            TmpParamData.getInstance().addViewRebufferCount();
                            uzVideo.addTrackingMuiza(Constants.MUIZA_EVENT_WAITING);
                        }
                    }
                    break;
                case Player.STATE_ENDED:
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
                    if (playWhenReady) {
                        // media actually playing
                        if (uzVideo != null) {
                            uzVideo.addTrackingMuiza(Constants.MUIZA_EVENT_PLAYING);
                            uzVideo.hideLayoutMsg();
                            uzVideo.resetCountTryLinkPlayError();
                        }
                        if (uzTimebar != null) {
                            uzTimebar.hidePreview();
                        }
                    }
                    if (!isFirstStateReady) {
                        onFirstStateReady();
                        isFirstStateReady = true;
                    }
                    break;
            }
            if (debugCallback != null) {
                debugCallback.onUpdateButtonVisibilities();
            }
            if (progressCallback != null) {
                progressCallback.onPlayerStateChanged(playWhenReady, playbackState);
            }
            if (uzVideo != null && uzVideo.eventListener != null) {
                uzVideo.eventListener.onPlayerStateChanged(playWhenReady, playbackState);
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            if (uzVideo != null && uzVideo.eventListener != null) {
                uzVideo.eventListener.onRepeatModeChanged(repeatMode);
            }
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            if (uzVideo != null && uzVideo.eventListener != null) {
                uzVideo.eventListener.onShuffleModeEnabledChanged(shuffleModeEnabled);
            }
        }

        //This is called then a error happens
        @Override
        public void onPlayerError(ExoPlaybackException error) {
            if (error == null) {
                return;
            }
            LLog.e(TAG, "onPlayerError " + error.toString() + " - " + error.getMessage());
            if (error.type == ExoPlaybackException.TYPE_SOURCE) {
                LLog.e(TAG, "onPlayerError TYPE_SOURCE");
            } else if (error.type == ExoPlaybackException.TYPE_RENDERER) {
                LLog.e(TAG, "onPlayerError TYPE_RENDERER");
            } else if (error.type == ExoPlaybackException.TYPE_UNEXPECTED) {
                LLog.e(TAG, "onPlayerError TYPE_UNEXPECTED");
            }
            error.printStackTrace();
            exoPlaybackException = error;
            if (debugCallback != null) {
                debugCallback.onUpdateButtonVisibilities();
            }
            if (uzVideo == null) {
                return;
            }
            uzVideo.handleError(UZExceptionUtil.getExceptionPlayback());
            //LLog.d(TAG, "onPlayerError isConnected: " + LConnectivityUtil.isConnected(context));
            if (LConnectivityUtil.isConnected(context)) {
                uzVideo.tryNextLinkPlay();
            } else {
                uzVideo.pauseVideo();
            }
            if (uzVideo != null && uzVideo.eventListener != null) {
                uzVideo.eventListener.onPlayerError(error);
            }
        }

        //This is called when a position discontinuity occurs without a change to the timeline
        @Override
        public void onPositionDiscontinuity(int reason) {
            if (uzVideo != null && uzVideo.eventListener != null) {
                uzVideo.eventListener.onPositionDiscontinuity(reason);
            }
        }

        //This is called when the current playback parameters change
        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            if (uzVideo != null && uzVideo.eventListener != null) {
                uzVideo.eventListener.onPlaybackParametersChanged(playbackParameters);
            }
        }

        //This is called when seek finishes
        @Override
        public void onSeekProcessed() {
            if (uzVideo != null && uzVideo.eventListener != null) {
                uzVideo.eventListener.onSeekProcessed();
            }
        }
    }

    private class UZAudioEventListener implements AudioListener {
        @Override
        public void onAudioSessionId(int audioSessionId) {
            if (uzVideo != null && uzVideo.audioListener != null) {
                uzVideo.audioListener.onAudioSessionId(audioSessionId);
            }
        }

        @Override
        public void onAudioAttributesChanged(AudioAttributes audioAttributes) {
            if (uzVideo != null && uzVideo.audioListener != null) {
                uzVideo.audioListener.onAudioAttributesChanged(audioAttributes);
            }
        }

        @Override
        public void onVolumeChanged(float volume) {
            if (uzVideo != null && uzVideo.audioListener != null) {
                uzVideo.audioListener.onVolumeChanged(volume);
            }
        }
    }

    protected int getVideoW() {
        return videoW;
    }

    protected int getVideoH() {
        return videoH;
    }

    private class UZVideoEventListener implements VideoListener {
        //This is called when the video size changes
        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            videoW = width;
            videoH = height;
            TmpParamData.getInstance().setEntitySourceWidth(width);
            TmpParamData.getInstance().setEntitySourceHeight(height);
            if (uzVideo != null && uzVideo.videoListener != null) {
                uzVideo.videoListener.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
            }
        }

        @Override
        public void onSurfaceSizeChanged(int width, int height) {
            if (uzVideo != null && uzVideo.videoListener != null) {
                uzVideo.videoListener.onSurfaceSizeChanged(width, height);
            }
        }

        //This is called when first frame is rendered
        @Override
        public void onRenderedFirstFrame() {
            exoPlaybackException = null;
            if (uzVideo != null && uzVideo.videoListener != null) {
                uzVideo.videoListener.onRenderedFirstFrame();
            }
        }
    }

    private class UZMetadataOutputListener implements MetadataOutput {

        //This is called when there is metadata associated with current playback time
        @Override
        public void onMetadata(Metadata metadata) {
            if (uzVideo != null && uzVideo.metadataOutput != null) {
                uzVideo.metadataOutput.onMetadata(metadata);
            }
        }
    }

    private class UZTextOutputListener implements TextOutput {

        @Override
        public void onCues(List<Cue> cues) {
            if (uzVideo != null && uzVideo.textOutput != null) {
                uzVideo.textOutput.onCues(cues);
            }
        }
    }

    private class UZVideoAdPlayerListener implements VideoAdPlayer.VideoAdPlayerCallback {
        private final String TAG = UZVideoAdPlayerListener.class.getSimpleName();
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


    protected void toggleVolumeMute(UZImageButton exoVolume) {
        if (!isPlayerValid() || exoVolume == null) {
            return;
        }
        if (getVolume() == 0f) {
            setVolume(volumeToggle);
            exoVolume.setSrcDrawableEnabled();
        } else {
            volumeToggle = getVolume();
            setVolume(0f);
            exoVolume.setSrcDrawableDisabledCanTouch();
        }
    }

    protected SimpleExoPlayer getPlayer() {
        return playerHelper.getPlayer();
    }

    protected void setVolume(float volume) {
        if (!isPlayerValid()) return;
        playerHelper.setVolume(volume);
        if (uzVideo == null) return;
        uzVideo.addTrackingMuiza(Constants.MUIZA_EVENT_VOLUMECHANGE);
        if (uzVideo.getIbVolumeIcon() != null) {
            if (getVolume() != 0f) {
                uzVideo.getIbVolumeIcon().setSrcDrawableEnabled();
            } else {
                uzVideo.getIbVolumeIcon().setSrcDrawableDisabledCanTouch();
            }
        }
    }

    protected float getVolume() {
        return playerHelper.getVolume();
    }

    protected boolean isPlayerValid() {
        return playerHelper != null && playerHelper.isPlayerValid();
    }

    protected void setPlayWhenReady(boolean ready) {
        playerHelper.setPlayWhenReady(ready);
    }

    protected boolean seekTo(long positionMs) {
        return playerHelper.seekTo(positionMs);
    }

    //forward  10000mls
    protected void seekToForward(long forward) {
        playerHelper.seekToForward(forward);
    }

    //next 10000mls
    protected void seekToBackward(long backward) {
        playerHelper.seekToBackward(backward);
    }

    public interface DebugCallback {
        void onUpdateButtonVisibilities();
    }

    public void setDebugCallback(DebugCallback debugCallback) {
        this.debugCallback = debugCallback;
    }

    //if player is playing then turn off connection -> player is error -> store current position
    //then if connection is connected again, resume position
    public void setResumeIfConnectionError() {
        contentPosition = mls;
    }

    protected long getCurrentPosition() {
        return playerHelper.getCurrentPosition();
    }

    protected long getDuration() {
        return playerHelper.getDuration();
    }

    protected boolean isVOD() {
        return playerHelper.isVOD();
    }

    protected boolean isLIVE() {
        return playerHelper.isLIVE();
    }

    protected String getDebugString() {
        return getPlayerStateString() + getVideoString() + getAudioString();
    }

    /**
     * Returns a string containing player state debugging information.
     */
    protected String getPlayerStateString() {
        return playerHelper.getPlayerStateString();
    }

    /**
     * Returns a string containing video debugging information.
     */
    protected String getVideoString() {
        return playerHelper.getVideoString();
    }

    protected int getVideoProfileW() {
        return playerHelper.getVideoProfileW();
    }

    protected int getVideoProfileH() {
        return playerHelper.getVideoProfileH();
    }

    /**
     * Returns a string containing audio debugging information.
     */
    protected String getAudioString() {
        return playerHelper.getAudioString();
    }

    protected String getDecoderCountersBufferCountString(DecoderCounters counters) {
        return playerHelper.getDecoderCountersBufferCountString(counters);
    }

    protected String getPixelAspectRatioString(float pixelAspectRatio) {
        return playerHelper.getPixelAspectRatioString(pixelAspectRatio);
    }
}
