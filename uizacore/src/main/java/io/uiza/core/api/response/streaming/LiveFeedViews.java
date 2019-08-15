package io.uiza.core.api.response.streaming;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveFeedViews {

    @SerializedName("watchnow")
    @Expose
    private long watchnow;

    public long getWatchnow() {
        return watchnow;
    }

    public void setWatchnow(long watchnow) {
        this.watchnow = watchnow;
    }

}
