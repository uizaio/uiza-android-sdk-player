
package vn.uiza.restapi.model.v2.getplayerinfo;


import com.google.gson.annotations.SerializedName;

public class PlayerConfig {

    @SerializedName("setting")
    private Setting setting;
    @SerializedName("styling")
    private Styling styling;
    @SerializedName("socialSharing")
    private SocialSharing socialSharing;

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

    public SocialSharing getSocialSharing() {
        return socialSharing;
    }

    public void setSocialSharing(SocialSharing socialSharing) {
        this.socialSharing = socialSharing;
    }

}
