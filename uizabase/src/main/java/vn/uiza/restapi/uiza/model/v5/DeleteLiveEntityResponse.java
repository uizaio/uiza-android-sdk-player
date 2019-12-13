package vn.uiza.restapi.uiza.model.v5;

import com.squareup.moshi.Json;

public class DeleteLiveEntityResponse {

    @Json(name = "id")
    String id;
    @Json(name = "deleted")
    boolean deleted;

    public String getId() {
        return id;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
