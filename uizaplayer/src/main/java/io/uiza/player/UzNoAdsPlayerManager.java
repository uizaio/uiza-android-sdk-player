package io.uiza.player;

import android.os.Handler;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.source.MediaSource;
import io.uiza.core.api.response.subtitle.Subtitle;
import io.uiza.core.util.constant.Constants;
import java.util.List;

public final class UzNoAdsPlayerManager extends UzPlayerManagerAbs {

    public UzNoAdsPlayerManager(final UzPlayer uzPlayer, String linkPlay, String thumbnailsUrl,
            List<Subtitle> subtitleList) {
        super(uzPlayer, linkPlay, thumbnailsUrl, subtitleList);
        setRunnable();
    }

    @Override
    public void setRunnable() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (uzPlayer == null || uzPlayer.getUzPlayerView() == null) {
                    return;
                }
                handleVideoProgress();

                if (uzPlayer.getDebugTextView() != null) {
                    uzPlayer.getDebugTextView().setText(getDebugString());
                }
                if (handler != null && runnable != null) {
                    handler.postDelayed(runnable, 1000);
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    @Override
    void initSource() {
        String drmScheme = Constants.DRM_SCHEME_NULL;
        DefaultDrmSessionManager<FrameworkMediaCrypto> drmSessionManager = buildDrmSessionManager(
                drmScheme);
        // TODO: currently, we still not support DRM
        if (drmScheme != Constants.DRM_SCHEME_NULL && drmSessionManager == null) {
            return;
        }

        exoPlayer = buildPlayer(drmSessionManager);
        uzPlayerHelper = new UzPlayerHelper(exoPlayer);
        uzPlayer.getUzPlayerView().setPlayer(exoPlayer);

        MediaSource mediaSourceVideo = createMediaSourceVideo();
        // merge title to media source video
        MediaSource mediaSourceWithSubtitle = createMediaSourceWithSubtitle(mediaSourceVideo);
        // Prepare the player with the source.
        addPlayerListener();

        exoPlayer.prepare(mediaSourceWithSubtitle);
        setPlayWhenReady(uzPlayer.isAutoStart());
        if (uzPlayer.isLivestream()) {
            uzPlayerHelper.seekToDefaultPosition();
        } else {
            seekTo(contentPosition);
        }
        notifyUpdateButtonVisibility();
    }
}
