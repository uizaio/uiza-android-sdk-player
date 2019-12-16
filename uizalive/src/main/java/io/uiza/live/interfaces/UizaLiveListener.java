package io.uiza.live.interfaces;

public interface UizaLiveListener {

    void onInit(boolean success);

    void onConnectionSuccess();

    void onConnectionFailed(String reason);

    void onRetryConnection(long delay);

    void onNewBitrate(long bitrate);

    void onDisconnect();

    void onAuthError();

    void onAuthSuccess();

    void surfaceCreated();

    void surfaceChanged(
            int format, int width, int
            height
    );

    void surfaceDestroyed();
}
