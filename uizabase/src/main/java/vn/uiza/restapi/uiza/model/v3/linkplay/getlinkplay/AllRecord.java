
package vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay;

import com.squareup.moshi.Json;

public class AllRecord {

    @Json(name = "processed_time")
    private Integer processedTime;
    @Json(name = "action")
    private String action;
    @Json(name = "source_uri")
    private String sourceUri;
    @Json(name = "region_type")
    private String regionType;
    @Json(name = "uploaded_size")
    private String uploadedSize;
    @Json(name = "status")
    private String status;
    @Json(name = "createdAt")
    private String createdAt;
    @Json(name = "app_id")
    private String appId;
    @Json(name = "destination_uri")
    private String destinationUri;
    @Json(name = "entity_id")
    private String entityId;
    @Json(name = "task_id")
    private String taskId;
    @Json(name = "output_transcode_path")
    private String outputTranscodePath;
    @Json(name = "updatedAt")
    private String updatedAt;
    @Json(name = "progress")
    private Integer progress;
    @Json(name = "type_content")
    private String typeContent;
    @Json(name = "publish_id")
    private String publishId;

    public Integer getProcessedTime() {
        return processedTime;
    }

    public void setProcessedTime(Integer processedTime) {
        this.processedTime = processedTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSourceUri() {
        return sourceUri;
    }

    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }

    public String getRegionType() {
        return regionType;
    }

    public void setRegionType(String regionType) {
        this.regionType = regionType;
    }

    public String getUploadedSize() {
        return uploadedSize;
    }

    public void setUploadedSize(String uploadedSize) {
        this.uploadedSize = uploadedSize;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDestinationUri() {
        return destinationUri;
    }

    public void setDestinationUri(String destinationUri) {
        this.destinationUri = destinationUri;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getOutputTranscodePath() {
        return outputTranscodePath;
    }

    public void setOutputTranscodePath(String outputTranscodePath) {
        this.outputTranscodePath = outputTranscodePath;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getTypeContent() {
        return typeContent;
    }

    public void setTypeContent(String typeContent) {
        this.typeContent = typeContent;
    }

    public String getPublishId() {
        return publishId;
    }

    public void setPublishId(String publishId) {
        this.publishId = publishId;
    }

}
