package io.uiza.core.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UtcTime {

    @SerializedName("unixtime")
    @Expose
    private long unixtime; // seconds format
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("utc_datetime")
    @Expose
    private String utcDatetime;

    public long getCurrentDateTimeMs() {
        return unixtime * 1000;
    }

    public String getCurrentDateTimeStr() {
        return utcDatetime;
    }
}
