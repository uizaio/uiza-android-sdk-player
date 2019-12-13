
package vn.uiza.restapi.uiza.model.v4.playerinfo;

import com.squareup.moshi.Json;

public class Setting {

    @Json(name = "autoStart")
    private boolean autoStart;
    @Json(name = "showQuality")
    private boolean showQuality;

    public boolean isAutoStart() {
        return autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    public boolean isShowQuality() {
        return showQuality;
    }

    public void setShowQuality(boolean showQuality) {
        this.showQuality = showQuality;
    }

}
