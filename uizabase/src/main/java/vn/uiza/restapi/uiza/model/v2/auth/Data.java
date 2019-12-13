
package vn.uiza.restapi.uiza.model.v2.auth;

import com.squareup.moshi.Json;


public class Data {

    @Json(name = "token")
    private String token;
    @Json(name = "expired")
    private String expired;
    @Json(name = "appId")
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
