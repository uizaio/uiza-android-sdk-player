
package vn.uiza.restapi.uiza.model.v2.getlinkdownload;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EntityInfo {

    @SerializedName("id")
    private String id;
    @SerializedName("referenceId")
    private String referenceId;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("shortDescription")
    private String shortDescription;
    @SerializedName("ingestMetadataId")
    private String ingestMetadataId;
    @SerializedName("adminUserId")
    private String adminUserId;
    @SerializedName("poster")
    private String poster;
    @SerializedName("view")
    private int view;
    @SerializedName("status")
    private int status;
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("duration")
    private String duration;
    @SerializedName("extendData")
    private Object extendData;
    @SerializedName("ownerName")
    private String ownerName;
    @SerializedName("ownerEmail")
    private String ownerEmail;
    @SerializedName("ownerAvatar")
    private String ownerAvatar;
    @SerializedName("ownerFullName")
    private String ownerFullName;
    @SerializedName("metadata")
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
