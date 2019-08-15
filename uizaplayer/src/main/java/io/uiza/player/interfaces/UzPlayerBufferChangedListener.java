package io.uiza.player.interfaces;

public interface UzPlayerBufferChangedListener {

    void onBufferChanged(long bufferedDurationUs, float playbackSpeed);
}
