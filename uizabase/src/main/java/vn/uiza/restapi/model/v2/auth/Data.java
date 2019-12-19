
package vn.uiza.restapi.model.v2.auth;

import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("token")
    private String token;
    @SerializedName("expired")
    private String expired;
    @SerializedName("appId")
    private String appId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

}
