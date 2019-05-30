package uizacoresdk.interfaces;

public interface UZPlayerStateChangedListener {
    void onSkinChanged();

    void onStateMiniPlayer(boolean isMiniPlayerInitSuccess);

    void onScreenRotated(boolean isLandscape);
}
