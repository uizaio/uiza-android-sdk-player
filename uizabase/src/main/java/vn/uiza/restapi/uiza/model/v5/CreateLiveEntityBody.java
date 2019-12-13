package vn.uiza.restapi.uiza.model.v5;

import com.squareup.moshi.Json;

public class CreateLiveEntityBody {

    @Json(name = "name")
    String name;
    @Json(name = "description")
    String description;
    @Json(name = "region")
    String region;
    @Json(name = "app_id")
    String appId;
    @Json(name = "user_id")
    String userId;

    public CreateLiveEntityBody(String name, String description, String region, String appId, String userId) {
        this.name = name;
        this.description = description;
        this.region = region;
        this.appId = appId;
        this.userId = userId;
    }
}
