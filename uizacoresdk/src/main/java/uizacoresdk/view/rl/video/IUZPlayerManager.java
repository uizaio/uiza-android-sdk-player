package uizacoresdk.view.rl.video;

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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import uizacoresdk.glide.GlideThumbnailTransformationPB;
import uizacoresdk.interfaces.UZBufferCallback;
import uizacoresdk.listerner.ProgressCallback;
import uizacoresdk.util.TmpParamData;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.rl.timebar.UZTimebar;

abstract class IUZPlayerManager implements PreviewLoader {
    protected final String TAG = "TAG" + getClass().getSimpleName();
    private static final String EXT_X_PROGRAM_DATE_TIME = "#EXT-X-PROGRAM-DATE-TIME:";
    private static final String EXTINF = "#EXTINF:";
    private static final long INVALID_PROGRAM_DATE_TIME = 0;
    protected Context context;
    protected UZVideo uzVideo;
    private final DataSource.Factory manifestDataSourceFactory;
    private final DataSource.Factory mediaDataSourceFactory;
    protected long contentPosition;
    protected SimpleExoPlayer player;
    protected UZPlayerHelper playerHelper;
    private String linkPlay;
    private List<Subtitle> subtitleList;
    private boolean isFirstStateReady;
    private UZTimebar uzTimebar;
    private String thumbnailsUrl;
    private ImageView imageView;
    protected Handler handler;
    protected Runnable runnable;
    private boolean isCanAddViewWatchTime;
    private long timestampPlayed;
    protected ProgressCallback progressCallback;
    protected UZBufferCallback bufferCallback;
    protected long mls = 0;
    protected long duration = 0;
    protected int percent = 0;
    protected int s = 0;
    private long bufferPosition = 0;
    private int bufferPercentage = 0;
    private int videoW = 0;
    private int videoH = 0;
    protected DefaultTrackSelector trackSelector;
    private float volumeToggle;
    private DebugCallback debugCallback;
    private ExoPlaybackException exoPlaybackException;
    private DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

    IUZPlayerManager(final UZVideo uzVideo, String linkPlay, String thumbnailsUrl,
            List<Subtitle> subtitleList) {
        TmpParamData.getInstance().setPlayerInitTime(System.currentTimeMillis());
        this.timestampPlayed = System.currentTimeMillis();
        this.isCanAddViewWatchTime = true;
        this.context = uzVideo.getContext();
        this.videoW = 0;
        this.videoH = 0;
        this.mls = 0;
        this.bufferPosition = 0;
        this.bufferPercentage = 0;
        this.uzVideo = uzVideo;
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
                new DefaultDataSourceFactory(context, null /* listener */, manifestDataSourceFactory);

        //SETUP OTHER
        this.imageView = uzVideo.getIvThumbnail();
        this.uzTimebar = uzVideo.getUZTimeBar();
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

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    public void setBufferCallback(UZBufferCallback bufferCallback) {
        this.bufferCallback = bufferCallback;
    }

    public void release() {
        if (isPlayerValid()) {
            playerHelper.release();
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
                    .transform(new GlideThumbnailTransformationPB(currentPosition))
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
        if (!isPlayerValid()) return;
        setPlayWhenReady(false);
        if (isCanAddViewWatchTime) {
            long durationWatched = System.currentTimeMillis() - timestampPlayed;
            TmpParamData.getInstance().addViewWatchTime(durationWatched);
            isCanAddViewWatchTime = false;
        }
    }

    protected void reset() {
        if (!isPlayerValid()) return;
        contentPosition = playerHelper.getContentPosition();
        playerHelper.release();
        handler = null;
        runnable = null;
    }

    protected boolean isPlayingAd() {
        return false;
    }

    protected void hideProgress() {
        if (uzVideo.isCastingChromecast()) {
            return;
        }
        UzDisplayUtil.hideProgressBar(uzVideo.getProgressBar());
    }

    protected void showProgress() {
        UzDisplayUtil.showProgressBar(uzVideo.getProgressBar());
    }

    protected SimpleExoPlayer getPlayer() {
        return playerHelper.getPlayer();
    }

    protected ExoPlaybackException getExoPlaybackException() {
        return exoPlaybackException;
    }

    protected boolean isPlayerValid() {
        return playerHelper != null && playerHelper.isPlayerValid();
    }

    protected int getVideoW() {
        return videoW;
    }

    protected int getVideoH() {
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

    MediaSource buildMediaSource(Uri uri) {
        @C.ContentType int type = Util.inferContentType(uri);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        manifestDataSourceFactory).createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(new DefaultSsChunkSource.Factory(mediaDataSourceFactory),
                        manifestDataSourceFactory).createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    void handleVideoProgress() {
        if (progressCallback != null && isPlayerValid()) {
            mls = getCurrentPosition();
            duration = getDuration();
            if (mls >= duration) {
                mls = duration;
            }
            if (duration != 0) {
                percent = (int) (mls * 100 / duration);
            }
            s = Math.round(mls / 1000.0f);
            progressCallback.onVideoProgress(mls, s, duration, percent);
            //buffer changing
            if (bufferPosition != uzVideo.getBufferedPosition()
                    || bufferPercentage != uzVideo.getBufferedPercentage()) {
                bufferPosition = uzVideo.getBufferedPosition();
                bufferPercentage = uzVideo.getBufferedPercentage();
                progressCallback.onBufferProgress(bufferPosition, bufferPercentage, duration);
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
                new DefaultRenderersFactory(context).setExtensionRendererMode(extensionRendererMode);
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        return ExoPlayerFactory.newSimpleInstance(context, renderersFactory, trackSelector,
                new DefaultLoadControl() {
                    @Override
                    public boolean shouldContinueLoading(long bufferedDurationUs, float playbackSpeed) {
                        if (bufferCallback != null) {
                            bufferCallback.onBufferChanged(bufferedDurationUs, playbackSpeed);
                        }
                        return super.shouldContinueLoading(bufferedDurationUs, playbackSpeed);
                    }
                }, drmSessionManager);
    }

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
                            buildDrmSessionManagerV18(drmSchemeUuid, drmLicenseUrl, keyRequestPropertiesArray,
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
        player.addListener(new UZPlayerEventListener());
        player.addAudioListener(new UZAudioEventListener());
        player.addVideoListener(new UZVideoEventListener());
        player.addMetadataOutput(new UZMetadataOutputListener());
        player.addTextOutput(new UZTextOutputListener());
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
                textFormat = Format.createTextSampleFormat(null, sampleMimeType, null, Format.NO_VALUE,
                        Format.NO_VALUE, subtitle.getLanguage(), null, Format.OFFSET_SAMPLE_RELATIVE);
            } else {
                // TextFormat with custom label
                textFormat = Format.createTextContainerFormat(null, subtitle.getName(), null, sampleMimeType, null,
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
     * @return the link play of vod or live, note that we force use http if android device version is below 22
     * <code>(Build.VERSION_CODES.LOLLIPOP_MR1)</code>
     */
    private String processLinkPlay(String linkPlay) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1 && linkPlay.startsWith(Constants.PREFIXS)) {
            return linkPlay.replace(Constants.PREFIXS, Constants.PREFIX);
        }
        return linkPlay;
    }

    private void onFirstStateReady() {
        if (uzVideo == null) return;
        long durationInS = uzVideo.getDuration() / 1000;
        TmpParamData.getInstance().setEntityDuration(durationInS + "");
        TmpParamData.getInstance().setEntitySourceDuration(durationInS + "");
        uzVideo.removeVideoCover(false);
    }

    private DefaultDrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManagerV18(UUID uuid,
            String licenseUrl, String[] keyRequestPropertiesArray, boolean multiSession)
            throws UnsupportedDrmException {

        HttpDataSource.Factory licenseDataSourceFactory = buildHttpDataSourceFactory();
        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl, licenseDataSourceFactory);
        if (keyRequestPropertiesArray != null) {
            for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i],
                        keyRequestPropertiesArray[i + 1]);
            }
        }
        return new DefaultDrmSessionManager<>(uuid, FrameworkMediaDrm.newInstance(uuid), drmCallback, null,
                multiSession);
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory() {
        return new DefaultHttpDataSourceFactory(Constants.USER_AGENT);
    }

    class UZAudioEventListener implements AudioListener {
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

    class UZVideoEventListener implements VideoListener {
        //This is called when the video size changes
        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                float pixelWidthHeightRatio) {
            videoW = width;
            videoH = height;
            TmpParamData.getInstance().setEntitySourceWidth(width);
            TmpParamData.getInstance().setEntitySourceHeight(height);
            if (uzVideo != null && uzVideo.videoListener != null) {
                uzVideo.videoListener.onVideoSizeChanged(width, height, unappliedRotationDegrees,
                        pixelWidthHeightRatio);
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

    class UZMetadataOutputListener implements MetadataOutput {

        //This is called when there is metadata associated with current playback time
        @Override
        public void onMetadata(Metadata metadata) {
            if (uzVideo != null && uzVideo.metadataOutput != null) {
                uzVideo.metadataOutput.onMetadata(metadata);
            }
        }
    }

    class UZTextOutputListener implements TextOutput {

        @Override
        public void onCues(List<Cue> cues) {
            if (uzVideo != null && uzVideo.textOutput != null) {
                uzVideo.textOutput.onCues(cues);
            }
        }
    }

    class UZPlayerEventListener implements Player.EventListener {
        private long timestampRebufferStart;

        //This is called when the current playlist changes
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            if (uzVideo == null) return;
            if (uzVideo.eventListener != null) {
                uzVideo.eventListener.onTimelineChanged(timeline, manifest, reason);
            }
            if (manifest instanceof HlsManifest) {
                HlsMediaPlaylist playlist = ((HlsManifest) manifest).mediaPlaylist;
                // From the current playing frame to end time of chunk
                long timeToEndChunk = player.getDuration() - player.getCurrentPosition();
                long extProgramDateTime = getProgramDateTimeValue(playlist, timeToEndChunk);

                if (extProgramDateTime == INVALID_PROGRAM_DATE_TIME) {
                    uzVideo.hideTextLiveStreamLatency();
                    return;
                }

                long elapsedTime = SystemClock.elapsedRealtime() - UZUtil.getLastElapsedTime(context);
                long currentTime = UZUtil.getLastServerTime(context) + elapsedTime;

                long latency = currentTime - extProgramDateTime;
                uzVideo.updateLiveStreamLatency(latency);
            } else {
                uzVideo.hideTextLiveStreamLatency();
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
                            Double.parseDouble(tag.replace(",", emptyStr).replace(EXTINF, emptyStr)) * 1000;
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
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            notifyUpdateButtonVisibility();
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
                            TmpParamData.getInstance()
                                    .setViewRebufferDuration(
                                            System.currentTimeMillis() - timestampRebufferStart);
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
            notifyUpdateButtonVisibility();
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
            notifyUpdateButtonVisibility();
            if (uzVideo == null) {
                return;
            }
            uzVideo.handleError(UzExceptionUtil.getExceptionPlayback());
            //LLog.d(TAG, "onPlayerError isConnected: " + UzConnectivityUtil.isConnected(context));
            if (UzConnectivityUtil.isConnected(context)) {
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

    abstract void initSource();

    abstract void setRunnable();
}
