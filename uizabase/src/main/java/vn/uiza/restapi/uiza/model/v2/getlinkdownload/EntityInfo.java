
package vn.uiza.restapi.uiza.model.v2.getlinkdownload;

import com.squareup.moshi.Json;

import java.util.List;

public class EntityInfo {

    @Json(name = "id")
    private String id;
    @Json(name = "referenceId")
    private String referenceId;
    @Json(name = "name")
    private String name;
    @Json(name = "description")
    private String description;
    @Json(name = "shortDescription")
    private String shortDescription;
    @Json(name = "ingestMetadataId")
    private String ingestMetadataId;
    @Json(name = "adminUserId")
    private String adminUserId;
    @Json(name = "poster")
    private String poster;
    @Json(name = "view")
    private int view;
    @Json(name = "status")
    private int status;
    @Json(name = "thumbnail")
    private String thumbnail;
    @Json(name = "duration")
    private String duration;
    @Json(name = "extendData")
    private Object extendData;
    @Json(name = "ownerName")
    private String ownerName;
    @Json(name = "ownerEmail")
    private String ownerEmail;
    @Json(name = "ownerAvatar")
    private String ownerAvatar;
    @Json(name = "ownerFullName")
    private String ownerFullName;
    @Json(name = "metadata")
    private List<Object> metadata = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getIngestMetadataId() {
        return ingestMetadataId;
    }

    public void setIngestMetadataId(String ingestMetadataId) {
        this.ingestMetadataId = ingestMetadataId;
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Object getExtendData() {
        return extendData;
    }

    public void setExtendData(Object extendData) {
        this.extendData = extendData;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerAvatar() {
        return ownerAvatar;
    }

    public void setOwnerAvatar(String ownerAvatar) {
        this.ownerAvatar = ownerAvatar;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public List<Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<Object> metadata) {
        this.metadata = metadata;
    }

}
