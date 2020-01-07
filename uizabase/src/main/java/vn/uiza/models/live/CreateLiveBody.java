package vn.uiza.models.live;

import com.google.gson.annotations.SerializedName;

public class CreateLiveBody {

    @SerializedName("name")
    String name;
    @SerializedName("description")
    String description;
    @SerializedName("region")
    String region;
    @SerializedName("dvr")
    boolean dvr;

    public CreateLiveBody(String name, String description, String region){
        this(name, description, region, true);
    }

    public CreateLiveBody(String name, String description, String region, boolean dvr) {
        this.name = name;
        this.description = description;
        this.region = region;
        this.dvr = dvr;
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

    public boolean isDvr() {
        return dvr;
    }
}
