package vn.uiza.models.vod;

import com.google.gson.annotations.SerializedName;

public enum VODType {
    @SerializedName("0")
    UNKNOWN(0),
    @SerializedName("1")
    VOD(1),
    @SerializedName("2")
    AOD(2);

    private final int value;

    VODType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
