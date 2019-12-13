
package vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata;

import com.squareup.moshi.Json;

import java.util.Date;
import java.util.List;

import vn.uiza.core.common.Constants;
import vn.uiza.restapi.uiza.model.v3.livestreaming.retrievealiveevent.LastPullInfo;
import vn.uiza.restapi.uiza.model.v3.livestreaming.retrievealiveevent.LastPushInfo;

public class Data {

    @Json(name = "entityId")
    private String entityId;

    @Json(name = "entityName")
    private String entityName;

    @Json(name = "feedId")
    private String feedId;

    @Json(name = "startTime")
    private String startTime;

    @Json(name = "endTime")
    private Object endTime;

    @Json(name = "length")
    private Object length;

    @Json(name = "process")
    private String process;

    @Json(name = "deletedEntity")
    private Object deletedEntity;

    @Json(name = "pullInfo")
    private String pullInfo;

    @Json(name = "pushInfo")
    private String pushInfo;

    @Json(name = "mode")
    private String mode;

    @Json(name = "resourceMode")
    private String resourceMode;

    @Json(name = "encode")
    private long encode;

    @Json(name = "channelName")
    private String channelName;

    @Json(name = "lastPresetId")
    private Object lastPresetId;

    @Json(name = "lastFeedId")
    private String lastFeedId;

    @Json(name = "linkPublishSocial")
    private String linkPublishSocial;

    @Json(name = "linkStream")
    private String linkStream;

    @Json(name = "lastPullInfo")
    private LastPullInfo lastPullInfo;

    @Json(name = "lastPushInfo")
    private List<LastPushInfo> lastPushInfo = null;

    @Json(name = "lastProcess")
    private String lastProcess;

    @Json(name = "eventType")
    private String eventType;

    @Json(name = "slug")
    private String slug;

    @Json(name = "orderNumber")
    private Integer orderNumber;

    @Json(name = "icon")
    private String icon;

    @Json(name = "status")
    private int status;

    @Json(name = "id")
    private String id;

    @Json(name = "name")
    private String name;

    @Json(name = "description")
    private String description;

    @Json(name = "shortDescription")
    private String shortDescription;

    @Json(name = "view")
    private long view;

    @Json(name = "poster")
    private String poster;

    @Json(name = "thumbnail")
    private String thumbnail;

    @Json(name = "type")
    private String type;

    @Json(name = "duration")
    private String duration;

    @Json(name = "embedMetadata")
    private Object embedMetadata;

    /*@Json(name = "extendMetadata")
    @Expose
    private ExtendMetadata extendMetadata;*/

    @Json(name = "createdAt")
    private String createdAt;

    @Json(name = "updatedAt")
    private String updatedAt;

    @Json(name = "publishToCdn")
    private String publishToCdn;

    @Json(name = "inputType")
    private String inputType;

    @Json(name = "url")
    private String url;

    @Json(name = "readyToPublish")
    private String readyToPublish;

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public long getView() {
        return view;
    }

    public void setView(long view) {
        this.view = view;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getThumbnail() {
        if (thumbnail == null || thumbnail.isEmpty()) {
            return Constants.URL_IMG_THUMBNAIL;
        }
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

    public Object getEmbedMetadata() {
        return embedMetadata;
    }

    public void setEmbedMetadata(Object embedMetadata) {
        this.embedMetadata = embedMetadata;
    }

    /*public ExtendMetadata getExtendMetadata() {
        return extendMetadata;
    }

    public void setExtendMetadata(ExtendMetadata extendMetadata) {
        this.extendMetadata = extendMetadata;
    }*/

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

    public String getLinkPublishSocial() {
        return linkPublishSocial;
    }

    public void setLinkPublishSocial(String linkPublishSocial) {
        this.linkPublishSocial = linkPublishSocial;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getLinkStream() {
        return linkStream;
    }

    public void setLinkStream(String linkStream) {
        this.linkStream = linkStream;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Object getEndTime() {
        return endTime;
    }

    public void setEndTime(Object endTime) {
        this.endTime = endTime;
    }

    public Object getLength() {
        return length;
    }

    public void setLength(Object length) {
        this.length = length;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Object getDeletedEntity() {
        return deletedEntity;
    }

    public void setDeletedEntity(Object deletedEntity) {
        this.deletedEntity = deletedEntity;
    }

    public String getPullInfo() {
        return pullInfo;
    }

    public void setPullInfo(String pullInfo) {
        this.pullInfo = pullInfo;
    }

    public String getPushInfo() {
        return pushInfo;
    }

    public void setPushInfo(String pushInfo) {
        this.pushInfo = pushInfo;
    }

    public String getPublishToCdn() {
        return publishToCdn;
    }

    public void setPublishToCdn(String publishToCdn) {
        this.publishToCdn = publishToCdn;
    }
}
