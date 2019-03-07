
package vn.uiza.restapi.uiza.model.v4.playerinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("setting")
    @Expose
    private Setting setting;
    @SerializedName("styling")
    @Expose
    private Styling styling;
    @SerializedName("endscreen")
    @Expose
    private Endscreen endscreen;
    @SerializedName("logo")
    @Expose
    private Logo logo;
    @SerializedName("name")
    @Expose
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
