
package vn.uiza.restapi.uiza.model.v3.livestreaming.retrievealiveevent;

import com.squareup.moshi.Json;

public class LastPushInfo {

    @Json(name = "streamUrl")
    private String streamUrl;
    @Json(name = "streamKey")
    private String streamKey;

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getStreamKey() {
        return streamKey;
    }

    public void setStreamKey(String streamKey) {
        this.streamKey = streamKey;
    }

}
