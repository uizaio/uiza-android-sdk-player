
package vn.uiza.models.playerinfo;

import com.google.gson.annotations.SerializedName;

public class Setting {

    @SerializedName("autoStart")
    private boolean autoStart;
    @SerializedName("showQuality")
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