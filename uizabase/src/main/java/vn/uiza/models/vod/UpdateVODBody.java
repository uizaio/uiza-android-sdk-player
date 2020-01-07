package vn.uiza.models.vod;

import com.google.gson.annotations.SerializedName;

public class UpdateVODBody {

    /**
     * The video's name, meant to be displayable to end users.
     */
    @SerializedName("name")
    String name;
    /**
     * The video's description, meant to be displayable to end users. Can contain up to 65,535 characters.
     */
    @SerializedName("description")
    String description;

    public UpdateVODBody() {
    }

    public UpdateVODBody(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
