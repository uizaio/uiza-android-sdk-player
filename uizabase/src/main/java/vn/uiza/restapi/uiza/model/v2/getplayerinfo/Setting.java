
package vn.uiza.restapi.uiza.model.v2.getplayerinfo;

import com.squareup.moshi.Json;

public class Setting {

    @Json(name = "autoStart")
    private String autoStart;
    @Json(name = "allowFullscreen")
    private String allowFullscreen;
    @Json(name = "showQuality")
    private String showQuality;
    @Json(name = "displayPlaylist")
    private String displayPlaylist;

    public String getAutoStart() {
        return autoStart;
    }

    public void setAutoStart(String autoStart) {
        this.autoStart = autoStart;
    }

    public String getAllowFullscreen() {
        return allowFullscreen;
    }

    public void setAllowFullscreen(String allowFullscreen) {
        this.allowFullscreen = allowFullscreen;
    }

    public String getShowQuality() {
        return showQuality;
    }

    public void setShowQuality(String showQuality) {
        this.showQuality = showQuality;
    }

    public String getDisplayPlaylist() {
        return displayPlaylist;
    }

    public void setDisplayPlaylist(String displayPlaylist) {
        this.displayPlaylist = displayPlaylist;
    }

}
