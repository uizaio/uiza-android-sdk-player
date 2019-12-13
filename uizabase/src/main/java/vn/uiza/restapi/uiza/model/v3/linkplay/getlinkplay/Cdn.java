
package vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay;

import com.squareup.moshi.Json;

public class Cdn {

    @Json(name = "host")
    private String host;
    @Json(name = "priority")
    private Integer priority;
    @Json(name = "region")
    private String region;
    @Json(name = "app_id")
    private String appId;
    @Json(name = "entity_id")
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
