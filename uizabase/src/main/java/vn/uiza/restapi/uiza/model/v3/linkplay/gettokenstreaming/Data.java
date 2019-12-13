
package vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming;

import com.squareup.moshi.Json;

public class Data {

    @Json(name = "token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
