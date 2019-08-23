package io.uiza.player;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.github.rubensousa.previewseekbar.PreviewLoader;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
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
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsManifest;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
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
import io.uiza.core.api.response.subtitle.Subtitle;
import io.uiza.core.exception.UzExceptionUtil;
import io.uiza.core.util.LLog;
import io.uiza.core.util.SentryUtil;
import io.uiza.core.util.UzDateTimeUtil;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.connection.UzConnectivityUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.core.view.autosize.UzImageButton;
import io.uiza.player.analytic.TmpParamData;
import io.uiza.player.analytic.muiza.MuizaEvent;
import io.uiza.player.interfaces.UzAdEventListener;
import io.uiza.player.interfaces.UzPlayerBufferChangedListener;
import io.uiza.player.util.PreviewThumbnailTransform;
import io.uiza.player.util.UzPlayerData;
import io.uiza.player.view.UzTimeBar;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class UzPlayerManagerAbs implements PreviewLoader {

    protected static final String TAG = UzPlayerManagerAbs.class.getSimpleName();
    private static final String EXT_X_PROGRAM_DATE_TIME = "#EXT-X-PROGRAM-DATE-TIME:";
    private static final String EXTINF = "#EXTINF:";
    private static final long INVALID_PROGRAM_DATE_TIME = 0;
    protected Context context;
    protected UzPlayer uzPlayer;
    private final DataSource.Factory manifestDataSourceFactory;
    private final DataSource.Factory mediaDataSourceFactory;
    protected long contentPosition;
    protected SimpleExoPlayer exoPlayer;
    protected UzPlayerHelper uzPlayerHelper;
    private String linkPlay;
    private List<Subtitle> subtitleList;
    private boolean isFirstStateReady;
    private UzTimeBar uzTimebar;
    private String thumbnailsUrl;
    private ImageView imageView;
    protected Handler handler;
    protected Runnable runnable;
    private boolean isCanAddViewWatchTime;
    private long timestampPlayed;
    protected io.uiza.player.interfaces.UzPlayerEventListener playerEventListener;
    protected UzAdEventListener adEventListener;
    protected UzPlayerBufferChangedListener bufferChangedListener;
    protected long mls;
    protected long duration = 0;
    protected int percent = 0;
    protected int s = 0;
    private long bufferPosition;
    private int bufferPercentage;
    private int videoW;
    private int videoH;
    protected DefaultTrackSelector trackSelector;
    private float volumeToggle;
    private DebugCallback debugCallback;
    private ExoPlaybackException exoPlaybackException;
    private DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

    UzPlayerManagerAbs(final UzPlayer uzPlayer, String linkPlay, String thumbnailsUrl,
            List<Subtitle> subtitleList) {
        TmpParamData.getInstance().setPlayerInitTime(System.currentTimeMillis());
        this.timestampPlayed = System.currentTimeMillis();
        this.isCanAddViewWatchTime = true;
        this.context = uzPlayer.getContext();
        this.videoW = 0;
        this.videoH = 0;
        this.mls = 0;
        this.bufferPosition = 0;
        this.bufferPercentage = 0;
        this.uzPlayer = uzPlayer;
        this.linkPlay = processLinkPlay(linkPlay);
        this.subtitleList = subtitleList;
        this.isFirstStateReady = false;

        // OPTION 1 OK
        /* manifestDataSourceFactory = new DefaultDataSourceFactory(context, userAgent);
        mediaDataSourceFactory = new DefaultDataSourceFactory(
                context,
                userAgent,
                new DefaultBandwidthMeter());*/

        // OPTION 2 PLAY LINK REDIRECT
        // Default parameters, except allowCrossProtocolRedirects is true
        this.manifestDataSourceFactory =
                new DefaultHttpDataSourceFactory(Constants.USER_AGENT, null /* listener */,
                        DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                        DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                        true /* allowCrossProtocolRedirects */);
        this.mediaDataSourceFactory =
                new DefaultDataSourceFactory(context, null /* listener */,
                        manifestDataSourceFactory);

        //SETUP OTHER
        this.imageView = uzPlayer.getIvThumbnail();
        this.uzTimebar = uzPlayer.getUzTimeBar();
        this.thumbnailsUrl = thumbnailsUrl;
    }

    public void init() {
        reset();
        initSource();
    }

    public void initWithoutReset() {
        initSource();
    }

    public List<Subtitle> getSubtitleList() {
        return subtitleList;
    }

    public String getLinkPlay() {
        return linkPlay;
    }

    public void setPlayerEventListener(
            io.uiza.player.interfaces.UzPlayerEventListener playerEventListener) {
        this.playerEventListener = playerEventListener;
    }

    public void setAdEventListener(UzAdEventListener adEventListener) {
        this.adEventListener = adEventListener;
    }

    public void setBufferChangedListener(UzPlayerBufferChangedListener bufferChangedListener) {
        this.bufferChangedListener = bufferChangedListener;
    }

    public void release() {
        if (isPlayerValid()) {
            uzPlayerHelper.release();
            handler = null;
            runnable = null;
        }
    }

    public void loadPreview(long currentPosition, long max) {
        if (!isPlayerValid()) {
            return;
        }
        setPlayWhenReady(false);
        if (thumbnailsUrl != null) {
            Glide.with(imageView)
                    .load(thumbnailsUrl)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .transform(new PreviewThumbnailTransform(currentPosition))
                    .into(imageView);
        }
    }

    public DefaultTrackSelector getTrackSelector() {
        return trackSelector;
    }

    public void setDebugCallback(DebugCallback debugCallback) {
        this.debugCallback = debugCallback;
    }

    //if player is playing then turn off connection -> player is error -> store current position
    //then if connection is connected again, resume position
    public void setResumeIfConnectionError() {
        contentPosition = mls;
    }

    protected void resumeVideo() {
        setPlayWhenReady(true);
        timestampPlayed = System.currentTimeMillis();
        isCanAddViewWatchTime = true;
    }

    protected void pauseVideo() {
        if (!isPlayerValid()) {
            return;
        }
        setPlayWhenReady(false);
        if (isCanAddViewWatchTime) {
            long durationWatched = System.currentTimeMillis() - timestampPlayed;
            TmpParamData.getInstance().addViewWatchTime(durationWatched);
            isCanAddViewWatchTime = false;
        }
    }

    protected void reset() {
        if (!isPlayerValid()) {
            return;
        }
        contentPosition = uzPlayerHelper.getContentPosition();
        uzPlayerHelper.release();
        handler = null;
        runnable = null;
    }

    protected boolean isPlayingAd() {
        return false;
    }

    protected void hideProgress() {
        if (uzPlayer.isCasting()) {
            return;
        }
        UzDisplayUtil.hideProgressBar(uzPlayer.getProgressBar());
    }

    protected void showProgress() {
        UzDisplayUtil.showProgressBar(uzPlayer.getProgressBar());
    }

    protected SimpleExoPlayer getExoPlayer() {
        return uzPlayerHelper.getExoPlayer();
    }

    protected ExoPlaybackException getExoPlaybackException() {
        return exoPlaybackException;
    }

    protected boolean isPlayerValid() {
        return uzPlayerHelper != null && uzPlayerHelper.isPlayerValid();
    }

    protected int getVideoWidth() {
        return videoW;
    }

    protected int getVideoHeight() {
        return videoH;
    }

    protected void toggleVolumeMute(UzImageButton exoVolume) {
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

    protected void setVolume(float volume) {
        if (!isPlayerValid()) {
            return;
        }
        uzPlayerHelper.setVolume(volume);
        if (uzPlayer == null) {
            return;
        }
        uzPlayer.addTrackingMuiza(MuizaEvent.MUIZA_EVENT_VOLUMECHANGE);
        if (uzPlayer.getIbVolumeIcon() != null) {
            if (getVolume() != 0f) {
                uzPlayer.getIbVolumeIcon().setSrcDrawableEnabled();
            } else {
                uzPlayer.getIbVolumeIcon().setSrcDrawableDisabledCanTouch();
            }
        }
    }

    protected float getVolume() {
        return uzPlayerHelper.getVolume();
    }

    protected void setPlayWhenReady(boolean ready) {
        uzPlayerHelper.setPlayWhenReady(ready);
    }

    protected boolean seekTo(long positionMs) {
        return uzPlayerHelper.seekTo(positionMs);
    }

    //forward  10000mls
    protected void seekToForward(long forward) {
        uzPlayerHelper.seekToForward(forward);
    }

    //next 10000mls
    protected void seekToBackward(long backward) {
        uzPlayerHelper.seekToBackward(backward);
    }

    protected long getCurrentPosition() {
        return uzPlayerHelper.getCurrentPosition();
    }

    protected long getDuration() {
        return uzPlayerHelper.getDuration();
    }

    protected boolean isVod() {
        return uzPlayerHelper.isVod();
    }

    protected boolean isLive() {
        return uzPlayerHelper.isLive();
    }

    protected String getDebugString() {
        return getPlayerStateString() + getVideoString() + getAudioString();
    }

    /**
     * Returns a string containing player state debugging information.
     */
    protected String getPlayerStateString() {
        return uzPlayerHelper.getPlayerStateString();
    }

    /**
     * Returns a string containing video debugging information.
     */
    protected String getVideoString() {
        return uzPlayerHelper.getVideoString();
    }

    protected int getVideoProfileWidth() {
        return uzPlayerHelper.getVideoProfileW();
    }

    protected int getVideoProfileHeight() {
        return uzPlayerHelper.getVideoProfileH();
    }

    /**
     * Returns a string containing audio debugging information.
     */
    protected String getAudioString() {
        return uzPlayerHelper.getAudioString();
    }

    protected String getDecoderCountersBufferCountString(DecoderCounters counters) {
        return uzPlayerHelper.getDecoderCountersBufferCountString(counters);
    }

    protected String getPixelAspectRatioString(float pixelAspectRatio) {
        return uzPlayerHelper.getPixelAspectRatioString(pixelAspectRatio);
    }

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

    void handleVideoProgress() {
        if (playerEventListener != null && isPlayerValid()) {
            mls = getCurrentPosition();
            duration = getDuration();
            if (mls >= duration) {
                mls = duration;
            }
            if (duration != 0) {
                percent = (int) (mls * 100 / duration);
            }
            s = Math.round(mls / 1000.0f);
            playerEventListener.onVideoProgress(mls, s, duration, percent);
            //buffer changing
            if (bufferPosition != uzPlayer.getBufferedPosition()
                    || bufferPercentage != uzPlayer.getBufferedPercentage()) {
                bufferPosition = uzPlayer.getBufferedPosition();
                bufferPercentage = uzPlayer.getBufferedPercentage();
                playerEventListener.onBufferProgress(bufferPosition, bufferPercentage, duration);
            }
        }
    }

    void notifyUpdateButtonVisibility() {
        if (debugCallback != null) {
            debugCallback.onUpdateButtonVisibilities();
        }
    }

    MediaSource createMediaSourceVideo() {
        return buildMediaSource(Uri.parse(linkPlay));
    }

    SimpleExoPlayer buildPlayer(DefaultDrmSessionManager<FrameworkMediaCrypto> drmSessionManager) {
        @DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode =
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
        DefaultRenderersFactory renderersFactory =
                new DefaultRenderersFactory(context)
                        .setExtensionRendererMode(extensionRendererMode);
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        return ExoPlayerFactory.newSimpleInstance(context, renderersFactory, trackSelector,
                new DefaultLoadControl() {
                    @Override
                    public boolean shouldContinueLoading(long bufferedDurationUs,
                            float playbackSpeed) {
                        if (bufferChangedListener != null) {
                            bufferChangedListener.onBufferChanged(bufferedDurationUs, playbackSpeed);
                        }
                        return super.shouldContinueLoading(bufferedDurationUs, playbackSpeed);
                    }
                }, drmSessionManager);
    }

    // TODO: Implement drm for UzPlayer
    DefaultDrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManager(String drmScheme) {
        DefaultDrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
        if (drmScheme != Constants.DRM_SCHEME_NULL) {
            String drmLicenseUrl = Constants.DRM_LICENSE_URL;
            String[] keyRequestPropertiesArray = null;
            boolean multiSession = false;
            String errorStringId = "An unknown DRM error occurred";
            if (Util.SDK_INT < 18) {
                errorStringId = "Protected content not supported on API levels below 18";
            } else {
                try {
                    UUID drmSchemeUuid = Util.getDrmUuid(drmScheme);
                    drmSessionManager =
                            buildDrmSessionManagerV18(drmSchemeUuid, drmLicenseUrl,
                                    keyRequestPropertiesArray,
                                    multiSession);
                } catch (UnsupportedDrmException e) {
                    LLog.e(TAG, "UnsupportedDrmException " + e.toString());
                    SentryUtil.captureException(e);
                }
            }
            if (drmSessionManager == null) {
                LLog.e(TAG, "Error drmSessionManager: " + errorStringId);
            }
        }
        return drmSessionManager;
    }

    void addPlayerListener() {
        exoPlayer.addListener(new UzPlayerEventListener());
        exoPlayer.addAudioListener(new UzAudioEventListener());
        exoPlayer.addVideoListener(new UzVideoEventListener());
        exoPlayer.addMetadataOutput(new UzMetadataOutputListener());
        exoPlayer.addTextOutput(new UzTextOutputListener());
    }

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

    /**
     * Force link play to http if version is below 22.
     *
     * @return the link play of vod or live, note that we force use http if android device version
     * is below 22.
     */
    private String processLinkPlay(String linkPlay) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1 && linkPlay
                .startsWith(Constants.PREFIXS)) {
            return linkPlay.replace(Constants.PREFIXS, Constants.PREFIX);
        }
        return linkPlay;
    }

    private void onFirstStateReady() {
        if (uzPlayer == null) {
            return;
        }
        long durationInS = uzPlayer.getDuration() / 1000;
        TmpParamData.getInstance().setEntityDuration(durationInS + "");
        TmpParamData.getInstance().setEntitySourceDuration(durationInS + "");
        uzPlayer.removeVideoCover(false);
    }

    private DefaultDrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManagerV18(UUID uuid,
            String licenseUrl, String[] keyRequestPropertiesArray, boolean multiSession)
            throws UnsupportedDrmException {

        HttpDataSource.Factory licenseDataSourceFactory = buildHttpDataSourceFactory();
        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl,
                licenseDataSourceFactory);
        if (keyRequestPropertiesArray != null) {
            for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i],
                        keyRequestPropertiesArray[i + 1]);
            }
        }
        return new DefaultDrmSessionManager<>(uuid, FrameworkMediaDrm.newInstance(uuid),
                drmCallback, null,
                multiSession);
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory() {
        return new DefaultHttpDataSourceFactory(Constants.USER_AGENT);
    }

    class UzAudioEventListener implements AudioListener {

        @Override
        public void onAudioSessionId(int audioSessionId) {
            if (uzPlayer != null && uzPlayer.audioListener != null) {
                uzPlayer.audioListener.onAudioSessionId(audioSessionId);
            }
        }

        @Override
        public void onAudioAttributesChanged(AudioAttributes audioAttributes) {
            if (uzPlayer != null && uzPlayer.audioListener != null) {
                uzPlayer.audioListener.onAudioAttributesChanged(audioAttributes);
            }
        }

        @Override
        public void onVolumeChanged(float volume) {
            if (uzPlayer != null && uzPlayer.audioListener != null) {
                uzPlayer.audioListener.onVolumeChanged(volume);
            }
        }
    }

    class UzVideoEventListener implements VideoListener {

        //This is called when the video size changes
        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                float pixelWidthHeightRatio) {
            videoW = width;
            videoH = height;
            TmpParamData.getInstance().setEntitySourceWidth(width);
            TmpParamData.getInstance().setEntitySourceHeight(height);
            if (uzPlayer != null && uzPlayer.videoListener != null) {
                uzPlayer.videoListener.onVideoSizeChanged(width, height, unappliedRotationDegrees,
                        pixelWidthHeightRatio);
            }
        }

        @Override
        public void onSurfaceSizeChanged(int width, int height) {
            if (uzPlayer != null && uzPlayer.videoListener != null) {
                uzPlayer.videoListener.onSurfaceSizeChanged(width, height);
            }
        }

        //This is called when first frame is rendered
        @Override
        public void onRenderedFirstFrame() {
            exoPlaybackException = null;
            if (uzPlayer != null && uzPlayer.videoListener != null) {
                uzPlayer.videoListener.onRenderedFirstFrame();
            }
        }
    }

    class UzMetadataOutputListener implements MetadataOutput {

        //This is called when there is metadata associated with current playback time
        @Override
        public void onMetadata(Metadata metadata) {
            if (uzPlayer != null && uzPlayer.metadataOutput != null) {
                uzPlayer.metadataOutput.onMetadata(metadata);
            }
        }
    }

    class UzTextOutputListener implements TextOutput {

        @Override
        public void onCues(List<Cue> cues) {
            if (uzPlayer != null && uzPlayer.textOutput != null) {
                uzPlayer.textOutput.onCues(cues);
            }
        }
    }

    class UzPlayerEventListener implements Player.EventListener {

        private long timestampRebufferStart;

        //This is called when the current playlist changes
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            if (uzPlayer == null) {
                return;
            }
            if (uzPlayer.eventListener != null) {
                uzPlayer.eventListener.onTimelineChanged(timeline, manifest, reason);
            }
            if (manifest instanceof HlsManifest) {
                HlsMediaPlaylist playlist = ((HlsManifest) manifest).mediaPlaylist;
                // From the current playing frame to end time of chunk
                long timeToEndChunk = exoPlayer.getDuration() - exoPlayer.getCurrentPosition();
                long extProgramDateTime = getProgramDateTimeValue(playlist, timeToEndChunk);

                if (extProgramDateTime == INVALID_PROGRAM_DATE_TIME) {
                    uzPlayer.hideTextLiveStreamLatency();
                    return;
                }

                long elapsedTime =
                        SystemClock.elapsedRealtime() - UzPlayerData.getLastElapsedTime(context);
                long currentTime = UzPlayerData.getLastServerTime(context) + elapsedTime;

                long latency = currentTime - extProgramDateTime;
                uzPlayer.updateLiveStreamLatency(latency);
            } else {
                uzPlayer.hideTextLiveStreamLatency();
            }
        }

        private long getProgramDateTimeValue(HlsMediaPlaylist playlist, long timeToEndChunk) {
            if (playlist == null || playlist.tags == null || playlist.tags.isEmpty()) {
                return INVALID_PROGRAM_DATE_TIME;
            }
            final String emptyStr = "";
            final int tagSize = playlist.tags.size();

            long totalTime = 0;
            int playingIndex = tagSize;

            // Find the playing frame index
            while (playingIndex > 0) {
                String tag = playlist.tags.get(playingIndex - 1);
                if (tag.contains(EXTINF)) {
                    totalTime +=
                            Double.parseDouble(tag.replace(",", emptyStr).replace(EXTINF, emptyStr))
                                    * 1000;
                    if (totalTime >= timeToEndChunk) {
                        break;
                    }
                }
                playingIndex--;
            }
            if (playingIndex >= tagSize) {
                // That means the livestream latency is larger than 1 segment (duration).
                // we should skip to calc latency in this case
                return INVALID_PROGRAM_DATE_TIME;
            }

            // Find the playing frame EXT_X_PROGRAM_DATE_TIME
            String playingDateTime = emptyStr;
            for (int i = playingIndex; i < tagSize; i++) {
                String tag = playlist.tags.get(i);
                if (tag.contains(EXT_X_PROGRAM_DATE_TIME)) {
                    playingDateTime = tag.replace(EXT_X_PROGRAM_DATE_TIME, emptyStr);
                    break;
                }
            }

            if (TextUtils.isEmpty(playingDateTime)) {
                // That means something wrong with the format, check with server
                // we should skip to calc latency in this case
                return INVALID_PROGRAM_DATE_TIME;
            }
            // int list of frame, we get the EXT_X_PROGRAM_DATE_TIME of current playing frame
            return UzDateTimeUtil.convertUtcMs(playingDateTime);
        }

        //This is called when the available or selected tracks change
        @Override
        public void onTracksChanged(TrackGroupArray trackGroups,
                TrackSelectionArray trackSelections) {
            notifyUpdateButtonVisibility();
            if (uzPlayer != null && uzPlayer.eventListener != null) {
                uzPlayer.eventListener.onTracksChanged(trackGroups, trackSelections);
            }
        }

        //This is called when ExoPlayer starts or stops loading sources(TS files, fMP4 files…)
        @Override
        public void onLoadingChanged(boolean isLoading) {
            if (uzPlayer != null && uzPlayer.eventListener != null) {
                uzPlayer.eventListener.onLoadingChanged(isLoading);
            }
        }

        //This is called when either playWhenReady or playbackState changes
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case Player.STATE_BUFFERING:
                    showProgress();
                    if (uzPlayer != null) {
                        if (playWhenReady) {
                            TmpParamData.getInstance().setViewRebufferDuration(
                                    System.currentTimeMillis() - timestampRebufferStart);
                            timestampRebufferStart = 0;
                            uzPlayer.addTrackingMuiza(MuizaEvent.MUIZA_EVENT_REBUFFEREND);
                        } else {
                            timestampRebufferStart = System.currentTimeMillis();
                            uzPlayer.addTrackingMuiza(MuizaEvent.MUIZA_EVENT_REBUFFERSTART);
                            TmpParamData.getInstance().addViewRebufferCount();
                            uzPlayer.addTrackingMuiza(MuizaEvent.MUIZA_EVENT_WAITING);
                        }
                    }
                    break;
                case Player.STATE_ENDED:
                    if (uzPlayer != null) {
                        uzPlayer.onPlayerEnded();
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
                        if (uzPlayer != null) {
                            uzPlayer.addTrackingMuiza(MuizaEvent.MUIZA_EVENT_PLAYING);
                            uzPlayer.hideLayoutMsg();
                            uzPlayer.resetCountTryLinkPlayError();
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
            notifyUpdateButtonVisibility();
            if (playerEventListener != null) {
                playerEventListener.onPlayerStateChanged(playWhenReady, playbackState);
            }
            if (uzPlayer != null && uzPlayer.eventListener != null) {
                uzPlayer.eventListener.onPlayerStateChanged(playWhenReady, playbackState);
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            if (uzPlayer != null && uzPlayer.eventListener != null) {
                uzPlayer.eventListener.onRepeatModeChanged(repeatMode);
            }
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            if (uzPlayer != null && uzPlayer.eventListener != null) {
                uzPlayer.eventListener.onShuffleModeEnabledChanged(shuffleModeEnabled);
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
            notifyUpdateButtonVisibility();
            if (uzPlayer == null) {
                return;
            }
            uzPlayer.handleError(UzExceptionUtil.getExceptionPlayback());
            //LLog.d(TAG, "onPlayerError isConnected: " + UzConnectivityUtil.isConnected(context));
            if (UzConnectivityUtil.isConnected(context)) {
                uzPlayer.tryNextLinkPlay();
            } else {
                uzPlayer.pause();
            }
            if (uzPlayer != null && uzPlayer.eventListener != null) {
                uzPlayer.eventListener.onPlayerError(error);
            }
        }

        //This is called when a position discontinuity occurs without a change to the timeline
        @Override
        public void onPositionDiscontinuity(int reason) {
            if (uzPlayer != null && uzPlayer.eventListener != null) {
                uzPlayer.eventListener.onPositionDiscontinuity(reason);
            }
        }

        //This is called when the current playback parameters change
        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            if (uzPlayer != null && uzPlayer.eventListener != null) {
                uzPlayer.eventListener.onPlaybackParametersChanged(playbackParameters);
            }
        }

        //This is called when seek finishes
        @Override
        public void onSeekProcessed() {
            if (uzPlayer != null && uzPlayer.eventListener != null) {
                uzPlayer.eventListener.onSeekProcessed();
            }
        }
    }

    abstract void initSource();

    abstract void setRunnable();
}
