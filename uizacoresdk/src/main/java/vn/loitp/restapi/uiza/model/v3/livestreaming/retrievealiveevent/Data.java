
package vn.loitp.restapi.uiza.model.v3.livestreaming.retrievealiveevent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("resourceMode")
    @Expose
    private String resourceMode;
    @SerializedName("encode")
    @Expose
    private long encode;
    @SerializedName("channelName")
    @Expose
    private String channelName;
    @SerializedName("lastPresetId")
    @Expose
    private Object lastPresetId;
    @SerializedName("lastFeedId")
    @Expose
    private String lastFeedId;
    @SerializedName("poster")
    @Expose
    private String poster;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("linkPublishSocial")
    @Expose
    private String linkPublishSocial;
    @SerializedName("linkStream")
    @Expose
    private String linkStream;
    @SerializedName("lastPullInfo")
    @Expose
    private LastPullInfo lastPullInfo;
    @SerializedName("lastPushInfo")
    @Expose
    private List<LastPushInfo> lastPushInfo = null;
    @SerializedName("lastProcess")
    @Expose
    private String lastProcess;
    @SerializedName("eventType")
    @Expose
    private String eventType;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getResourceMode() {
        return resourceMode;
    }

    public void setResourceMode(String resourceMode) {
        this.resourceMode = resourceMode;
    }

    public long getEncode() {
        return encode;
    }

    public void setEncode(long encode) {
        this.encode = encode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Object getLastPresetId() {
        return lastPresetId;
    }

    public void setLastPresetId(Object lastPresetId) {
        this.lastPresetId = lastPresetId;
    }

    public String getLastFeedId() {
        return lastFeedId;
    }

    public void setLastFeedId(String lastFeedId) {
        this.lastFeedId = lastFeedId;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLinkPublishSocial() {
        return linkPublishSocial;
    }

    public void setLinkPublishSocial(String linkPublishSocial) {
        this.linkPublishSocial = linkPublishSocial;
    }

    public String getLinkStream() {
        return linkStream;
    }

    public void setLinkStream(String linkStream) {
        this.linkStream = linkStream;
    }

    public LastPullInfo getLastPullInfo() {
        return lastPullInfo;
    }

    public void setLastPullInfo(LastPullInfo lastPullInfo) {
        this.lastPullInfo = lastPullInfo;
    }

    public List<LastPushInfo> getLastPushInfo() {
        return lastPushInfo;
    }

    public void setLastPushInfo(List<LastPushInfo> lastPushInfo) {
        this.lastPushInfo = lastPushInfo;
    }

    public String getLastProcess() {
        return lastProcess;
    }

    public void setLastProcess(String lastProcess) {
        this.lastProcess = lastProcess;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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
