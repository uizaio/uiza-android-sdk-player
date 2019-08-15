package io.uiza.player.interfaces;

public interface UzPlayerUiEventListener {

    void onSkinChanged();

    void onStateMiniPlayer(boolean success);

    void onPlayerRotated(boolean isLandscape);
}
