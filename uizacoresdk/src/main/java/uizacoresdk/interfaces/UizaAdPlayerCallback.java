package uizacoresdk.interfaces;

/**
 * Created by MinhNguyen on 14/06/2019.
 * nguyen.thanh.minhb@framgia.com
 */
public interface UizaAdPlayerCallback {
    void onPlay();

    void onVolumeChanged(int i);

    void onPause();

    void onLoaded();

    void onResume();

    void onEnded();

    void onError();

    void onBuffering();
}
