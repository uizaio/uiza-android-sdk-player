package uizacoresdk.util;

import vn.uiza.restapi.model.v5.UizaPlayback;

public class UZDataCLP {
    private static final UZDataCLP ourInstance = new UZDataCLP();

    public static UZDataCLP getInstance() {
        return ourInstance;
    }

    private UZDataCLP() {
    }

    private UizaPlayback playback;

    public UizaPlayback getPlayback() {
        return playback;
    }

    public void setPlayback(UizaPlayback playback) {
        this.playback = playback;
    }

    public void clearData() {
        playback = null;
    }
}
