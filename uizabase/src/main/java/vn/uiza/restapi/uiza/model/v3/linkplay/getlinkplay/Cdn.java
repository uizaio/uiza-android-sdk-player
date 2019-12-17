
package vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay;

import com.google.gson.annotations.SerializedName;

public class Cdn {

    @SerializedName("host")
    private String host;
    @SerializedName("priority")
    private Integer priority;
    @SerializedName("region")
    private String region;
    @SerializedName("app_id")
    private String appId;
    @SerializedName("entity_id")
    private String entityId;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

}
