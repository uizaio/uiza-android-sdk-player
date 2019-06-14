package uizacoresdk.view.rl.video;

/**
 * Created by MinhNguyen on 14/06/2019.
 * nguyen.thanh.minhb@framgia.com
 */
public interface UZAdPlayerCallback {
    void onPlay();

    void onVolumeChanged(int i);

    void onPause();

    void onLoaded();

    void onResume();

    void onEnded();

    void onError();

    void onBuffering();
}
