
package vn.uiza.restapi.uiza.model.v3.livestreaming.getviewalivefeed;

import com.squareup.moshi.Json;

public class Data {

    @Json(name = "watchnow")
    private long watchnow;

    public long getWatchnow() {
        return watchnow;
    }

    public void setWatchnow(long watchnow) {
        this.watchnow = watchnow;
    }

}
