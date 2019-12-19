package vn.uiza.restapi.model.v5.vod;

import com.google.gson.annotations.SerializedName;

public class DeleteVODResponse {
    /**
     * Unique identifier for the object.
     */
    @SerializedName("id")
    String id;
    /**
     * Return true if VOD deleted successful
     */
    @SerializedName("deleted")
    boolean deleted;
}
