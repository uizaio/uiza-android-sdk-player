package vn.uiza.restapi.model.v5.live;

import com.google.gson.annotations.SerializedName;

public class UpdateLiveBody {
    @SerializedName("name")
    String name;
    @SerializedName("description")
    String description;
    @SerializedName("dvr")
    boolean dvr;

    public UpdateLiveBody(String name, String description) {
        this(name, description, true);
    }

    public UpdateLiveBody(String name, String description, boolean dvr) {
        this.name = name;
        this.description = description;
        this.dvr = dvr;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDvr() {
        return dvr;
    }
}
