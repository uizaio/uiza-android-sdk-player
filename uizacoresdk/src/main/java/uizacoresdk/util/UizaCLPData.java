package uizacoresdk.util;


import vn.uiza.models.PlaybackInfo;

/**
 * Uiza Custom link play Data
 */
public class UizaCLPData {

    // Bill Pugh Singleton Implementation

    private static class UizaCLPDataHelper {
        private static final UizaCLPData INSTANCE = new UizaCLPData();
    }

    public static UizaCLPData getInstance() {
        return UizaCLPDataHelper.INSTANCE;
    }

    private UizaCLPData() {
    }

    //

    private PlaybackInfo playbackInfo;

    public PlaybackInfo getPlaybackInfo() {
        return playbackInfo;
    }

    public void setPlaybackInfo(PlaybackInfo playback) {
        this.playbackInfo = playback;
    }

    public void clearData() {
        playbackInfo = null;
    }
}
