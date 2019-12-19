
package vn.uiza.restapi.model.v3.linkplay.gettokenstreaming;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
