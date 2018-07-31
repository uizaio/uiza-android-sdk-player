package vn.loitp.uizavideo.listerner;

/**
 * Created by loitp on 4/12/2018.
 */

public interface ProgressCallback {
    public void onAdProgress(float currentMls, int s, float duration, int percent);

    public void onVideoProgress(float currentMls, int s, float duration, int percent);
}
