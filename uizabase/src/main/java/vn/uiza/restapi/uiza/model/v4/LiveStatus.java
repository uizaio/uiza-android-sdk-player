package vn.uiza.restapi.uiza.model.v4;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public enum LiveStatus {
    @SerializedName("offline")
    @Expose
    OFFLINE("offline"),
    @SerializedName("available")
    @Expose
    AVAILABLE("available");

    private String value;

    LiveStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
