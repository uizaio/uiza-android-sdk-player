package io.uiza.core.api.response.playerinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Logo {

    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("display")
    @Expose
    private boolean display;
    @SerializedName("url")
    @Expose
    private String url;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
