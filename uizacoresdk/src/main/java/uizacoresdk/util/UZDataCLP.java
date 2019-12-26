package uizacoresdk.util;

import vn.uiza.restapi.model.v5.PlaybackInfo;

public class UZDataCLP {
    private static final UZDataCLP ourInstance = new UZDataCLP();

    public static UZDataCLP getInstance() {
        return ourInstance;
    }

    private UZDataCLP() {
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
