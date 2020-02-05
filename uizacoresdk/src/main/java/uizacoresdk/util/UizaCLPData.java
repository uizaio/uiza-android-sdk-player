package uizacoresdk.util;


import vn.uiza.models.PlaybackInfo;

/**
 * Uiza Custom link play Data
 */
public class UizaCLPData {
    private static final UizaCLPData ourInstance = new UizaCLPData();

    public static UizaCLPData getInstance() {
        return ourInstance;
    }

    private UizaCLPData() {
    }

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
