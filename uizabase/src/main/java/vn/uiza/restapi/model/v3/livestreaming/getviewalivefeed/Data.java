
package vn.uiza.restapi.model.v3.livestreaming.getviewalivefeed;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("watchnow")
    private long watchnow;

    public long getWatchnow() {
        return watchnow;
    }

    public void setWatchnow(long watchnow) {
        this.watchnow = watchnow;
    }

}
