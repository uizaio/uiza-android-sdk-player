package io.uiza.player.mini.pip;

import android.os.Handler;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import io.uiza.core.api.response.subtitle.Subtitle;
import io.uiza.core.util.LLog;
import io.uiza.core.util.constant.Constants;
import java.util.List;

public final class PipNoAdsPlayerManager extends PipPlayerManagerAbs {

    public PipNoAdsPlayerManager(final UzPipPlayer uzPipPlayer, String linkPlay,
            String thumbnailsUrl, List<Subtitle> subtitleList) {
        this.timestampPlayed = System.currentTimeMillis();
        isCanAddViewWatchTime = true;
        this.context = uzPipPlayer.getContext();
        this.uzPipPlayer = uzPipPlayer;
        this.linkPlay = linkPlay;
        this.subtitleList = subtitleList;
        this.videoWidth = 0;
        this.videoHeight = 0;

        manifestDataSourceFactory = new DefaultDataSourceFactory(context, Constants.USER_AGENT);
        mediaDataSourceFactory =
                new DefaultDataSourceFactory(context, Constants.USER_AGENT,
                        new DefaultBandwidthMeter());
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (uzPipPlayer.getPlayerView() != null) {
                    handleVideoProgress();
                    if (handler != null && runnable != null) {
                        handler.postDelayed(runnable, 1000);
                    }
                }
            }
        };
        handler.postDelayed(runnable, 0);
        uzPipPlayer.getPlayerView().setControllerShowTimeoutMs(0);
    }

    @Override
    public void init(boolean isLivestream, long contentPosition) {
        LLog.d(TAG, "miniplayer STEP 1 FUZPLayerManager init isLivestream "
                + isLivestream
                + ", contentPosition "
                + contentPosition);
        reset();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        uzPipPlayer.getPlayerView().setPlayer(player);
        MediaSource mediaSourceVideo = createMediaSourceVideo();
        //merge title to media source video
        //SUBTITLE
        MediaSource mediaSourceWithSubtitle = createMediaSourceWithSubtitle(mediaSourceVideo);
        //merge ads to media source subtitle
        //IMA ADS
        // Compose the content media source into a new AdsMediaSource with both ads and content.
        //Prepare the player with the source.
        player.addListener(new PipPlayerEventListener());
        player.addVideoListener(new PipVideoListener());

        player.prepare(mediaSourceWithSubtitle);
        //setVolumeOff();
        if (isLivestream) {
            player.seekToDefaultPosition();
        } else {
            seekTo(contentPosition);
        }
        player.setPlayWhenReady(true);
    }
}
