
package vn.uiza.restapi.model.v3.livestreaming.retrievealiveevent;


import com.google.gson.annotations.SerializedName;

public class LastPullInfo {

    @SerializedName("primaryInputUri")
    private String primaryInputUri;
    @SerializedName("secondaryInputUri")
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
