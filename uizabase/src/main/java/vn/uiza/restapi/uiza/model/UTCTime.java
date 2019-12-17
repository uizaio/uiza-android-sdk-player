package vn.uiza.restapi.uiza.model;

import com.google.gson.annotations.SerializedName;

public class UTCTime {
    @SerializedName("unixtime")
    private long unixtime; // seconds format
    @SerializedName("timezone")
    private String timezone;
    @SerializedName("utc_datetime")
    private String utcDatetime;

    public long getCurrentDateTimeMs() {
        return unixtime * 1000;
    }

    public String getCurrentDateTimeStr() {
        return utcDatetime;
    }
}
