
package vn.uiza.restapi.uiza.model.v3.authentication.gettoken;

import com.squareup.moshi.Json;

public class Data {

    @Json(name = "token")
    private String token;
    @Json(name = "expired")
    private long expired;
    @Json(name = "appId")
    private String appId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public long getExpired() {
        return expired;
    }

    public void setExpired(long expired) {
        this.expired = expired;
    }
}
