package vn.uiza.models.live;

import com.google.gson.annotations.SerializedName;

public class DeleteLiveEntityResponse {

    @SerializedName("id")
    String id;
    @SerializedName("deleted")
    boolean deleted;

    public String getId() {
        return id;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
