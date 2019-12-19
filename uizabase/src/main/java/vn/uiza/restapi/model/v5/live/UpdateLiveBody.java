package vn.uiza.restapi.model.v5.live;

import com.google.gson.annotations.SerializedName;

public class UpdateLiveBody {
    @SerializedName("name")
    String name;
    @SerializedName("description")
    String description;

    public UpdateLiveBody(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
