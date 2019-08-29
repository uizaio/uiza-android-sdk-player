package io.uiza.player.interfaces;

public interface UzLiveInfoListener {

    void onStartTimeUpdate(long duration, String elapsedTime);

    void onCurrentViewUpdate(long watchNow);

    void onLivestreamUnAvailable();
}
