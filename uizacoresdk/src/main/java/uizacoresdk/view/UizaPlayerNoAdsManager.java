package uizacoresdk.view;

import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.source.MediaSource;

import java.util.List;

import vn.uiza.core.common.Constants;
import vn.uiza.models.Subtitle;

public final class UizaPlayerNoAdsManager extends IUizaPlayerManager {

    public UizaPlayerNoAdsManager(@NonNull UizaVideoView uzVideo, String linkPlay, String thumbnailsUrl,
                                  List<Subtitle> subtitleList) {
        super(uzVideo, linkPlay, thumbnailsUrl, subtitleList);
        setRunnable();
    }

    @Override
    public void setRunnable() {
        handler = new Handler();
        runnable = () -> {
            if (uzVideo == null || uzVideo.getUzPlayerView() == null) {
                return;
            }
            handleVideoProgress();
            if (handler != null && runnable != null) {
                handler.postDelayed(runnable, 1000);
            }
        };
        handler.postDelayed(runnable, 0);
    }

    @Override
    void initSource() {
        DefaultDrmSessionManager<FrameworkMediaCrypto> drmSessionManager = buildDrmSessionManager(drmScheme);
        if (drmScheme != null && drmSessionManager == null) return;

        player = buildPlayer(drmSessionManager);
        playerHelper = new UizaPlayerHelper(player);
        uzVideo.getUzPlayerView().setPlayer(player);

        MediaSource mediaSourceVideo = createMediaSourceVideo();
        // merge title to media source video
        MediaSource mediaSourceWithSubtitle = createMediaSourceWithSubtitle(mediaSourceVideo);
        // Prepare the player with the source.
        addPlayerListener();

        player.prepare(mediaSourceWithSubtitle);
        setPlayWhenReady(uzVideo.isAutoStart());
        if (uzVideo.isLivestream()) {
            playerHelper.seekToDefaultPosition();
        } else {
            seekTo(contentPosition);
        }
        notifyUpdateButtonVisibility();
    }
}
