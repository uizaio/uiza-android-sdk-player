package vn.uiza.restapi.uiza.model.v5;

import com.squareup.moshi.Json;

import java.util.Date;

public class LiveSession {

    @Json(name = "id")
    String id;
    @Json(name = "entity_id")
    String entityId;
    @Json(name = "stream_key")
    String streamKey;
    @Json(name = "server")
    String server;
    @Json(name = "duration")
    float duration;
    @Json(name = "created_at")
    Date createdAt;
    @Json(name = "updated_at")
    Date updateAt;

    public String getId() {
        return id;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getStreamKey() {
        return streamKey;
    }

    public String getServer() {
        return server;
    }

    public float getDuration() {
        return duration;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }
}
