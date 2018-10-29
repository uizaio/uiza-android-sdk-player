package uizacoresdk.model;

public class SDTUZCustomLinkPlay {
    private static final SDTUZCustomLinkPlay ourInstance = new SDTUZCustomLinkPlay();

    public static SDTUZCustomLinkPlay getInstance() {
        return ourInstance;
    }

    private SDTUZCustomLinkPlay() {
    }

    private UZCustomLinkPlay uzCustomLinkPlay;

    public UZCustomLinkPlay getUzCustomLinkPlay() {
        return uzCustomLinkPlay;
    }

    public void setUzCustomLinkPlay(UZCustomLinkPlay uzCustomLinkPlay) {
        this.uzCustomLinkPlay = uzCustomLinkPlay;
    }
}
