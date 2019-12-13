package vn.uiza.restapi.uiza.model.v5;

import com.squareup.moshi.Json;

public class LivePlayback {

    @Json(name = "hls")
    String hls;

    public String getHls() {
        return hls;
    }
}
