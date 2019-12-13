
package vn.uiza.restapi.uiza.model.v2.getplayerinfo;

import com.squareup.moshi.Json;

public class SocialSharing {

    @Json(name = "allow")
    private String allow;
    @Json(name = "controller")
    private Controller controller;

    public String getAllow() {
        return allow;
    }

    public void setAllow(String allow) {
        this.allow = allow;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

}
