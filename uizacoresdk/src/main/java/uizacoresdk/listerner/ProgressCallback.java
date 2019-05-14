package uizacoresdk.listerner;

/**
 * Created by loitp on 4/12/2018.
 */

public interface ProgressCallback {
    void onAdProgress(int s, int duration, int percent);

    void onAdEnded();

    void onVideoProgress(long currentMls, int s, long duration, int percent);

    void onPlayerStateChanged(boolean playWhenReady, int playbackState);

    void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration);
}
