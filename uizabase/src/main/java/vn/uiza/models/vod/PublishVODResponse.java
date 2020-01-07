package vn.uiza.models.vod;

import com.google.gson.annotations.SerializedName;

public class PublishVODResponse {
    @SerializedName("id")
    String id;
    @SerializedName("message")
    String message;
    @SerializedName("entity_id")
    String entityId;

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getEntityId() {
        return entityId;
    }
}
