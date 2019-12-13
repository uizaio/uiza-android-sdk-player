
package vn.uiza.restapi.uiza.model.v4.playerinfo;

import com.squareup.moshi.Json;

public class Data {

    @Json(name = "setting")
    private Setting setting;
    @Json(name = "styling")
    private Styling styling;
    @Json(name = "endscreen")
    private Endscreen endscreen;
    @Json(name = "logo")
    private Logo logo;
    @Json(name = "name")
    private String name;

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public Styling getStyling() {
        return styling;
    }

    public void setStyling(Styling styling) {
        this.styling = styling;
    }

    public Endscreen getEndscreen() {
        return endscreen;
    }

    public void setEndscreen(Endscreen endscreen) {
        this.endscreen = endscreen;
    }

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
