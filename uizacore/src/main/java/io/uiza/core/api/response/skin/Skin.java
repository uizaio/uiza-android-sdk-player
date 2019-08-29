
package io.uiza.core.api.response.skin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Skin {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private long status;
    @SerializedName("adminUserId")
    @Expose
    private String adminUserId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("shortDescription")
    @Expose
    private Object shortDescription;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("preloadVideo")
    @Expose
    private String preloadVideo;
    @SerializedName("autoStart")
    @Expose
    private long autoStart;
    @SerializedName("allowedDomain")
    @Expose
    private Object allowedDomain;
    @SerializedName("allowFullscreen")
    @Expose
    private long allowFullscreen;
    @SerializedName("endscreen")
    @Expose
    private Object endscreen;
    @SerializedName("allowSharing")
    @Expose
    private Object allowSharing;
    @SerializedName("qualitySelector")
    @Expose
    private long qualitySelector;
    @SerializedName("displayPlaylist")
    @Expose
    private long displayPlaylist;
    @SerializedName("sizing")
    @Expose
    private String sizing;
    @SerializedName("aspect")
    @Expose
    private String aspect;
    @SerializedName("customAspect")
    @Expose
    private Object customAspect;
    @SerializedName("platform")
    @Expose
    private String platform;
    @SerializedName("version")
    @Expose
    private Object version;
    @SerializedName("updateMode")
    @Expose
    private String updateMode;
    @SerializedName("skinId")
    @Expose
    private String skinId;
    @SerializedName("plugin")
    @Expose
    private Object plugin;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(Object shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPreloadVideo() {
        return preloadVideo;
    }

    public void setPreloadVideo(String preloadVideo) {
        this.preloadVideo = preloadVideo;
    }

    public long getAutoStart() {
        return autoStart;
    }

    public void setAutoStart(long autoStart) {
        this.autoStart = autoStart;
    }

    public Object getAllowedDomain() {
        return allowedDomain;
    }

    public void setAllowedDomain(Object allowedDomain) {
        this.allowedDomain = allowedDomain;
    }

    public long getAllowFullscreen() {
        return allowFullscreen;
    }

    public void setAllowFullscreen(long allowFullscreen) {
        this.allowFullscreen = allowFullscreen;
    }

    public Object getEndscreen() {
        return endscreen;
    }

    public void setEndscreen(Object endscreen) {
        this.endscreen = endscreen;
    }

    public Object getAllowSharing() {
        return allowSharing;
    }

    public void setAllowSharing(Object allowSharing) {
        this.allowSharing = allowSharing;
    }

    public long getQualitySelector() {
        return qualitySelector;
    }

    public void setQualitySelector(long qualitySelector) {
        this.qualitySelector = qualitySelector;
    }

    public long getDisplayPlaylist() {
        return displayPlaylist;
    }

    public void setDisplayPlaylist(long displayPlaylist) {
        this.displayPlaylist = displayPlaylist;
    }

    public String getSizing() {
        return sizing;
    }

    public void setSizing(String sizing) {
        this.sizing = sizing;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public Object getCustomAspect() {
        return customAspect;
    }

    public void setCustomAspect(Object customAspect) {
        this.customAspect = customAspect;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Object getVersion() {
        return version;
    }

    public void setVersion(Object version) {
        this.version = version;
    }

    public String getUpdateMode() {
        return updateMode;
    }

    public void setUpdateMode(String updateMode) {
        this.updateMode = updateMode;
    }

    public String getSkinId() {
        return skinId;
    }

    public void setSkinId(String skinId) {
        this.skinId = skinId;
    }

    public Object getPlugin() {
        return plugin;
    }

    public void setPlugin(Object plugin) {
        this.plugin = plugin;
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
