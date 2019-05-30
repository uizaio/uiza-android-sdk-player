package uizacoresdk.listerner;

import uizacoresdk.interfaces.UZAdStateChangedListener;
import uizacoresdk.interfaces.UZVideoStateChangedListener;

/**
 * @deprecated use {@link UZAdStateChangedListener} or {@link UZVideoStateChangedListener} instead
 */
@Deprecated
public interface ProgressCallback {
    @Deprecated
    void onAdProgress(int s, int duration, int percent);

    @Deprecated
    void onAdEnded();

    @Deprecated
    void onVideoProgress(long currentMls, int s, long duration, int percent);

    @Deprecated
    void onPlayerStateChanged(boolean playWhenReady, int playbackState);

    @Deprecated
    void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration);
}
