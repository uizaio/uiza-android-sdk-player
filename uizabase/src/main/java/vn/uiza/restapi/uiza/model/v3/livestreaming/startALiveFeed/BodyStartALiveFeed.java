package vn.uiza.restapi.uiza.model.v3.livestreaming.startALiveFeed;

import com.squareup.moshi.Json;

public class BodyStartALiveFeed {
    @Json(name = "id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
