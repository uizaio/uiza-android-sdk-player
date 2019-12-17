package vn.uiza.restapi.uiza.model.v3.livestreaming.startALiveFeed;

import com.google.gson.annotations.SerializedName;

public class BodyStartALiveFeed {
    @SerializedName("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
