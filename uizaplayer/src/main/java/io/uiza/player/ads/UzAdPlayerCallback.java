package io.uiza.player.ads;

public interface UzAdPlayerCallback {

    void onPlay();

    void onVolumeChanged(int level);

    void onPause();

    void onLoaded();

    void onResume();

    void onEnded();

    void onError();

    void onBuffering();
}
