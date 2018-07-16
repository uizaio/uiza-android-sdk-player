package vn.loitp.uizavideov3.view.util;

import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

/**
 * Created by loitp on 5/18/2018.
 */

public class UizaInputV3 {
    private String urlIMAAd = "";
    private String urlThumnailsPreviewSeekbar = "";
    private Data data;

    public boolean isLivestream() {
        if (data == null || data.getLastFeedId() == null || data.getLastFeedId().isEmpty()) {
            return false;
        }
        return true;
    }

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
}
