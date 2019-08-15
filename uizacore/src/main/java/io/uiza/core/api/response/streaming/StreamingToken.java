
package io.uiza.core.api.response.streaming;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StreamingToken {

    @SerializedName("token")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
