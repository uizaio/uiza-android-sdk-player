package io.uiza.core.api.response.playerinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Setting {

    @SerializedName("autoStart")
    @Expose
    private boolean autoStart;
    @SerializedName("showQuality")
    @Expose
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
