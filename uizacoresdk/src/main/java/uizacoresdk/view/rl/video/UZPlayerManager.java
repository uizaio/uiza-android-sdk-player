package uizacoresdk.view.rl.video;

import android.net.Uri;
import android.os.Handler;
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import io.uiza.core.api.response.subtitle.Subtitle;
import io.uiza.core.util.LLog;
import io.uiza.core.util.constant.Constants;
import java.util.List;
import uizacoresdk.util.UZUtil;

/**
 * Manages the {@link ExoPlayer}, the IMA plugin and all video playback.
 */
//https://medium.com/@takusemba/understands-callbacks-of-exoplayer-c05ac3c322c2
public final class UZPlayerManager extends IUZPlayerManager implements AdsMediaSource.MediaSourceFactory {

    private ImaAdsLoader adsLoader = null;
    private boolean isOnAdEnded;
    private UZAdPlayerCallback uzAdPlayerCallback;
    private UZVideoAdPlayerListener uzVideoAdPlayerListener = new UZVideoAdPlayerListener();

    public UZPlayerManager(final UZVideo uzVideo, String linkPlay, String urlIMAAd, String thumbnailsUrl,
            List<Subtitle> subtitleList) {
        super(uzVideo, linkPlay, thumbnailsUrl, subtitleList);

        if (urlIMAAd != null && !urlIMAAd.isEmpty()) {
            if (UZUtil.getClickedPip(context)) {
                LLog.e(TAG, "UZPlayerManager don't init urlIMAAd because called from PIP again");
            } else {
                adsLoader = new ImaAdsLoader(context, Uri.parse(urlIMAAd));
            }
        }

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

    @Override
    protected boolean isPlayingAd() {
        if (uzVideoAdPlayerListener == null) {
            return false;
        }
        return uzVideoAdPlayerListener.isPlayingAd();
    }

    @Override
    public void setRunnable() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (uzVideo == null || uzVideo.getUzPlayerView() == null) {
                    return;
                }
                if (uzVideoAdPlayerListener.isEnded()) {
                    onAdEnded();
                }
                if (isPlayingAd()) {
                    handleAdProgress();
                } else {
                    handleVideoProgress();
                }
                if (uzVideo.getDebugTextView() != null) {
                    uzVideo.getDebugTextView().setText(getDebugString());
                }
                if (handler != null && runnable != null) {
                    handler.postDelayed(runnable, 1000);
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    private void handleAdProgress() {
        hideProgress();
        isOnAdEnded = false;
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
    }

    @Override
    void initSource() {
        isOnAdEnded = false;
        String drmScheme = Constants.DRM_SCHEME_NULL;
        DefaultDrmSessionManager<FrameworkMediaCrypto> drmSessionManager = buildDrmSessionManager(drmScheme);
        if (drmScheme != Constants.DRM_SCHEME_NULL && drmSessionManager == null) return;

        player = buildPlayer(drmSessionManager);
        playerHelper = new UZPlayerHelper(player);
        uzVideo.getUzPlayerView().setPlayer(player);

        MediaSource mediaSourceVideo = createMediaSourceVideo();
        // merge title to media source video
        MediaSource mediaSourceWithSubtitle = createMediaSourceWithSubtitle(mediaSourceVideo);
        // merge ads to media source subtitle
        // Compose the content media source into a new AdsMediaSource with both ads and content.
        MediaSource mediaSourceWithAds = createMediaSourceWithAds(mediaSourceWithSubtitle);
        // Prepare the player with the source.

        addPlayerListener();
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
        notifyUpdateButtonVisibility();
    }

    private MediaSource createMediaSourceWithAds(MediaSource mediaSource) {
        if (adsLoader == null) {
            return mediaSource;
        }
        return new AdsMediaSource(mediaSource, this, adsLoader,
                uzVideo.getUzPlayerView().getOverlayFrameLayout(), null, null);
    }

    @Override
    public void release() {
        super.release();
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
        return new int[] { C.TYPE_DASH, C.TYPE_HLS, C.TYPE_OTHER };
    }

    private class UZVideoAdPlayerListener implements VideoAdPlayer.VideoAdPlayerCallback {
        private boolean isPlayingAd;
        private boolean isEnded;

        @Override
        public void onPlay() {
            isPlayingAd = true;
            if (uzAdPlayerCallback != null) uzAdPlayerCallback.onPlay();
        }

        @Override
        public void onVolumeChanged(int i) {
            if (uzAdPlayerCallback != null) uzAdPlayerCallback.onVolumeChanged(i);
        }

        @Override
        public void onPause() {
            isPlayingAd = false;
            if (uzAdPlayerCallback != null) uzAdPlayerCallback.onPause();
        }

        @Override
        public void onLoaded() {
            if (uzAdPlayerCallback != null) uzAdPlayerCallback.onLoaded();
        }

        @Override
        public void onResume() {
            isPlayingAd = true;
            isEnded = false;
            if (uzAdPlayerCallback != null) uzAdPlayerCallback.onResume();
        }

        @Override
        public void onEnded() {
            isPlayingAd = false;
            isEnded = true;
            if (uzAdPlayerCallback != null) uzAdPlayerCallback.onEnded();
        }

        @Override
        public void onError() {
            isPlayingAd = false;
            if (uzAdPlayerCallback != null) uzAdPlayerCallback.onError();
        }

        @Override
        public void onBuffering() {
            if (uzAdPlayerCallback != null) uzAdPlayerCallback.onBuffering();
        }

        public boolean isPlayingAd() {
            return isPlayingAd;
        }

        public boolean isEnded() {
            return isEnded;
        }
    }

    public void addAdPlayerCallback(UZAdPlayerCallback uzAdPlayerCallback) {
        this.uzAdPlayerCallback = uzAdPlayerCallback;
    }
}
