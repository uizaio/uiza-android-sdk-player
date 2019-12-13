package vn.uiza.restapi.uiza.model;

import com.squareup.moshi.Json;

public class UTCTime {
    @Json(name = "unixtime")
    private long unixtime; // seconds format
    @Json(name = "timezone")
    private String timezone;
    @Json(name = "utc_datetime")
    private String utcDatetime;

    public long getCurrentDateTimeMs() {
        return unixtime * 1000;
    }

    public String getCurrentDateTimeStr() {
        return utcDatetime;
    }
}
