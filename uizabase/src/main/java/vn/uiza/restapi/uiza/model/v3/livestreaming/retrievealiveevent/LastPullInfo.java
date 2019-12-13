
package vn.uiza.restapi.uiza.model.v3.livestreaming.retrievealiveevent;

import com.squareup.moshi.Json;

public class LastPullInfo {

    @Json(name = "primaryInputUri")
    private String primaryInputUri;
    @Json(name = "secondaryInputUri")
    private Object secondaryInputUri;

    public String getPrimaryInputUri() {
        return primaryInputUri;
    }

    public void setPrimaryInputUri(String primaryInputUri) {
        this.primaryInputUri = primaryInputUri;
    }

    public Object getSecondaryInputUri() {
        return secondaryInputUri;
    }

    public void setSecondaryInputUri(Object secondaryInputUri) {
        this.secondaryInputUri = secondaryInputUri;
    }

}
