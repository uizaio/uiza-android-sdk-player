package vn.uiza.restapi.uiza.model.v5;

import com.squareup.moshi.Json;

import java.util.Date;

public class LiveEntity {
    @Json(name = "id")
    private String id;
    @Json(name = "name")
    String name;
    @Json(name = "description")
    String description;
    @Json(name = "ingest")
    LiveIngest ingest;
    @Json(name = "playback")
    LivePlayback playback;
    @Json(name = "region")
    String region;
    @Json(name = "status")
    String status;
    @Json(name = "broadcast")
    String broadcast;
    @Json(name = "created_at")
    Date createdAt;
    @Json(name = "updated_at")
    Date updatedAt;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LiveIngest getIngest() {
        return ingest;
    }

    public LivePlayback getPlayback() {
        return playback;
    }

    public String getRegion() {
        return region;
    }

    public String getStatus() {
        return status;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
