package vn.uiza.restapi.model.v5.live;

import com.google.gson.annotations.SerializedName;

public enum LiveStatus {

    @SerializedName("init")
    INIT("init"),
    @SerializedName("ready")
    READY("ready"),
    @SerializedName("broadcasting")
    BROADCASTING("broadcasting");

    private final String value;

    LiveStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
