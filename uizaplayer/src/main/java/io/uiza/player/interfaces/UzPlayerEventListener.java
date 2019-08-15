package io.uiza.player.interfaces;

import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;

public interface UzPlayerEventListener {

    void onDataInitialized(boolean initSuccess, boolean getDataSuccess, LinkPlay linkPlay,
            VideoData data);

    void onPlayerStateChanged(boolean playWhenReady, int playbackState);

    void onVideoProgress(long currentMls, int s, long duration, int percent);

    void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration);

    void onVideoEnded();

    void onPlayerError(UzException exception);
}
