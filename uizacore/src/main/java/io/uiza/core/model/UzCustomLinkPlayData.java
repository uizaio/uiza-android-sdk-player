package io.uiza.core.model;

public class UzCustomLinkPlayData {

    private static final UzCustomLinkPlayData ourInstance = new UzCustomLinkPlayData();

    public static UzCustomLinkPlayData getInstance() {
        return ourInstance;
    }

    private UzCustomLinkPlayData() {
    }

    private CustomLinkPlay customLinkPlay;

    public CustomLinkPlay getCustomLinkPlay() {
        return customLinkPlay;
    }

    public void setCustomLinkPlay(CustomLinkPlay customLinkPlay) {
        this.customLinkPlay = customLinkPlay;
    }

    public void clearCustomLinkData() {
        customLinkPlay = null;
    }
}
