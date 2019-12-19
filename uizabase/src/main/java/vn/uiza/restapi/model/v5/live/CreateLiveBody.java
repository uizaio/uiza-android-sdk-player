package vn.uiza.restapi.model.v5.live;

import com.google.gson.annotations.SerializedName;

public class CreateLiveBody {

    @SerializedName("name")
    String name;
    @SerializedName("description")
    String description;
    @SerializedName("region")
    String region;

    public CreateLiveBody(String name, String description, String region) {
        this.name = name;
        this.description = description;
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRegion() {
        return region;
    }
}
