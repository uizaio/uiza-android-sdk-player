package uizacoresdk.view.rl.video;

import android.os.Handler;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.source.MediaSource;
import io.uiza.core.api.response.subtitle.Subtitle;
import io.uiza.core.util.constant.Constants;
import java.util.List;

public final class UZPlayerNoAdsManager extends IUZPlayerManager {

    public UZPlayerNoAdsManager(final UZVideo uzVideo, String linkPlay, String thumbnailsUrl,
            List<Subtitle> subtitleList) {
        super(uzVideo, linkPlay, thumbnailsUrl, subtitleList);
        setRunnable();
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
                handleVideoProgress();

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

    @Override
    void initSource() {
        String drmScheme = Constants.DRM_SCHEME_NULL;
        DefaultDrmSessionManager<FrameworkMediaCrypto> drmSessionManager = buildDrmSessionManager(drmScheme);
        if (drmScheme != Constants.DRM_SCHEME_NULL && drmSessionManager == null) return;

        player = buildPlayer(drmSessionManager);
        playerHelper = new UZPlayerHelper(player);
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
