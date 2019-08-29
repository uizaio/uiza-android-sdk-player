package io.uiza.player.interfaces;

public interface UzAdEventListener {

    void onAdProgress(int s, int duration, int percent);

    void onAdEnded();
}
