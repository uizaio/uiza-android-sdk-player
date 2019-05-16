package uizacoresdk.util;

import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming.ResultGetTokenStreaming;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

/**
 * Created by loitp on 18/1/2019.
 */

public class UZInput {
    private String urlIMAAd = "";
    private String urlThumnailsPreviewSeekbar = "";
    private Data data;
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public boolean isLivestream() {
        return data != null && data.getLastFeedId() != null && !data.getLastFeedId().isEmpty();
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
