package vn.uiza.restapi.uiza.model.v5;

import com.google.gson.annotations.SerializedName;

public class CreateLiveEntityBody {

    @SerializedName("name")
    String name;
    @SerializedName("description")
    String description;
    @SerializedName("region")
    String region;
    @SerializedName("app_id")
    String appId;
    @SerializedName("user_id")
    String userId;

    public CreateLiveEntityBody(String name, String description, String region, String appId, String userId) {
        this.name = name;
        this.description = description;
        this.region = region;
        this.appId = appId;
        this.userId = userId;
    }
}
