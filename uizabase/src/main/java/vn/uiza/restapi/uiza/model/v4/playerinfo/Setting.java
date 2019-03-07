
package vn.uiza.restapi.uiza.model.v4.playerinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Setting {

    @SerializedName("autoStart")
    @Expose
    private String autoStart;
    @SerializedName("showQuality")
    @Expose
    private String showQuality;

    public String getAutoStart() {
        return autoStart;
    }

    public void setAutoStart(String autoStart) {
        this.autoStart = autoStart;
    }

    public String getShowQuality() {
        return showQuality;
    }

    public void setShowQuality(String showQuality) {
        this.showQuality = showQuality;
    }

}
