package uizacoresdk.util;

import vn.uiza.restapi.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.model.v3.linkplay.gettokenstreaming.ResultGetTokenStreaming;
import vn.uiza.restapi.model.v5.UizaPlayback;

/**
 * Created by loitp on 18/1/2019.
 */

public class UZInput {
    private String urlIMAAd = "";
    private String urlThumnailsPreviewSeekbar = "";
    private UizaPlayback playback;
    private ResultGetTokenStreaming resultGetTokenStreaming;
    private ResultGetLinkPlay resultGetLinkPlay;

    public String getUrlIMAAd() {
        return urlIMAAd;
    }

    public void setUrlIMAAd(String urlIMAAd) {
        this.urlIMAAd = urlIMAAd;
    }

    public String getUrlThumnailsPreviewSeekbar() {
        return urlThumnailsPreviewSeekbar;
    }

    public void setUrlThumnailsPreviewSeekbar(String urlThumnailsPreviewSeekbar) {
        this.urlThumnailsPreviewSeekbar = urlThumnailsPreviewSeekbar;
    }

    public UizaPlayback getPlayback() {
        return playback;
    }

    public void setPlayback(UizaPlayback playback) {
        this.playback = playback;
    }

    public boolean isLivestream() {
        return playback != null;
    }

    public ResultGetTokenStreaming getResultGetTokenStreaming() {
        return resultGetTokenStreaming;
    }

    public void setResultGetTokenStreaming(ResultGetTokenStreaming resultGetTokenStreaming) {
        this.resultGetTokenStreaming = resultGetTokenStreaming;
    }

    public ResultGetLinkPlay getResultGetLinkPlay() {
        return resultGetLinkPlay;
    }

    public void setResultGetLinkPlay(ResultGetLinkPlay resultGetLinkPlay) {
        this.resultGetLinkPlay = resultGetLinkPlay;
    }
}
