
package vn.uiza.restapi.uiza.model.v3.ad;

import com.squareup.moshi.Json;

public class Ad {

    @Json(name = "id")
    private String id;
    @Json(name = "status")
    private long status;
    @Json(name = "adminUserId")
    private String adminUserId;
    @Json(name = "isDeleted")
    private long isDeleted;
    @Json(name = "entityId")
    private String entityId;
    @Json(name = "campaignId")
    private String campaignId;
    @Json(name = "name")
    private String name;
    @Json(name = "type")
    private String type;
    @Json(name = "time")
    private long time;
    @Json(name = "link")
    private String link;
    @Json(name = "createdAt")
    private String createdAt;
    @Json(name = "updatedAt")
    private String updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

    public long getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(long isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
