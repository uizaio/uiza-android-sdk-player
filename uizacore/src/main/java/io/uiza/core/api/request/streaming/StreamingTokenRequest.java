package io.uiza.core.api.request.streaming;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StreamingTokenRequest {
    public static final String STREAM = "stream";
    public static final String STATIC = "static";
    public static final String CATCHUP = "catchup";
    public static final String LIVE = "live";
    @SerializedName("entity_id")
    @Expose
    private String entityId;
    @SerializedName("app_id")
    @Expose
    private String appId;
    @SerializedName("content_type")
    @Expose
    private String contentType;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}