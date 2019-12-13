package vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming;

/**
 * Created by LENOVO on 7/2/2018.
 */

import com.squareup.moshi.Json;

public class SendGetTokenStreaming {
    public final static String STREAM = "stream";
    public final static String STATIC = "static";
    public final static String CATCHUP = "catchup";
    public final static String LIVE = "live";
    @Json(name = "entity_id")
    private String entityId;
    @Json(name = "app_id")
    private String appId;
    @Json(name = "content_type")
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