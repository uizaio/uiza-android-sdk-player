package uizacoresdk.util;

import uizacoresdk.model.UZCustomLinkPlay;

public class UZDataCLP {
    private static final UZDataCLP ourInstance = new UZDataCLP();

    public static UZDataCLP getInstance() {
        return ourInstance;
    }

    private UZDataCLP() {
    }

    private UZCustomLinkPlay uzCustomLinkPlay;

    public UZCustomLinkPlay getUzCustomLinkPlay() {
        return uzCustomLinkPlay;
    }

    public void setUzCustomLinkPlay(UZCustomLinkPlay uzCustomLinkPlay) {
        this.uzCustomLinkPlay = uzCustomLinkPlay;
    }

    public void clearData() {
        uzCustomLinkPlay = null;
    }
}
