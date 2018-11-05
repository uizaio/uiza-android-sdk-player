package uizacoresdk.listerner;

/**
 * Created by loitp on 4/12/2018.
 */

public interface ProgressCallback {
    public void onAdProgress(long currentMls, int s, long duration, int percent);

    public void onAdEnded();

    public void onVideoProgress(long currentMls, int s, long duration, int percent);

    public void onPlayerStateChanged(boolean playWhenReady, int playbackState);

    public void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration);
}
