
package vn.uiza.restapi.uiza.model.v2.getplayerinfo;


import com.google.gson.annotations.SerializedName;

public class SocialSharing {

    @SerializedName("allow")
    private String allow;
    @SerializedName("controller")
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
