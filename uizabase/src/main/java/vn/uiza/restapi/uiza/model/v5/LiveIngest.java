package vn.uiza.restapi.uiza.model.v5;

import com.squareup.moshi.Json;

public class LiveIngest {
    @Json(name = "stream_url") String streamUrl;
    @Json(name = "stream_key") String streamKey;

    public String getStreamKey() {
        return streamKey;
    }

    public String getStreamUrl() {
        return streamUrl;
    }
}
