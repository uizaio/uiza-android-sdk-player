
package vn.uiza.restapi.uiza.model.v3.skin.listskin;

import com.squareup.moshi.Json;

public class Datum {

    @Json(name = "id")
    private String id;
    @Json(name = "status")
    private long status;
    @Json(name = "adminUserId")
    private String adminUserId;
    @Json(name = "name")
    private String name;
    @Json(name = "shortDescription")
    private Object shortDescription;
    @Json(name = "description")
    private String description;
    @Json(name = "preloadVideo")
    private String preloadVideo;
    @Json(name = "autoStart")
    private long autoStart;
    @Json(name = "allowedDomain")
    private Object allowedDomain;
    @Json(name = "allowFullscreen")
    private long allowFullscreen;
    @Json(name = "endscreen")
    private Object endscreen;
    @Json(name = "allowSharing")
    private Object allowSharing;
    @Json(name = "qualitySelector")
    private long qualitySelector;
    @Json(name = "displayPlaylist")
    private long displayPlaylist;
    @Json(name = "sizing")
    private String sizing;
    @Json(name = "aspect")
    private String aspect;
    @Json(name = "customAspect")
    private Object customAspect;
    @Json(name = "platform")
    private String platform;
    @Json(name = "version")
    private Object version;
    @Json(name = "updateMode")
    private String updateMode;
    @Json(name = "skinId")
    private String skinId;
    @Json(name = "plugin")
    private Object plugin;
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
