package uizacoresdk.model;

import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.Url;

public class UZCustomLinkPlay {
    private Url urlPlay;
    private boolean isLivestream;

    public Url getUrlPlay() {
        return urlPlay;
    }

    public void setUrlPlay(Url urlPlay) {
        this.urlPlay = urlPlay;
    }

    public boolean isLivestream() {
        return isLivestream;
    }

    public void setLivestream(boolean livestream) {
        isLivestream = livestream;
    }
}
