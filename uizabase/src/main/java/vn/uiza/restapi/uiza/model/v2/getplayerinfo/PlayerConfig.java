
package vn.uiza.restapi.uiza.model.v2.getplayerinfo;

import com.squareup.moshi.Json;

public class PlayerConfig {

    @Json(name = "setting")
    private Setting setting;
    @Json(name = "styling")
    private Styling styling;
    @Json(name = "socialSharing")
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
