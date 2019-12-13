
package vn.uiza.restapi.uiza.model.v4.playerinfo;

import com.squareup.moshi.Json;

public class Logo {

    @Json(name = "logo")
    private String logo;
    @Json(name = "position")
    private String position;
    @Json(name = "display")
    private boolean display;
    @Json(name = "url")
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
