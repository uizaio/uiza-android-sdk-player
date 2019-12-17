package vn.uiza.restapi.uiza.model.tracking;

/**
 * Created by loitp on 18/1/2019.
 */

import com.google.gson.annotations.SerializedName;

public class UizaTracking {

    @SerializedName("hours_watched")
    private String hoursWatched;
    @SerializedName("publisher_id")
    private String publisherId;
    @SerializedName("player_init_time")
    private String playerInitTime;
    @SerializedName("sub_property_id")
    private String subPropertyId;
    @SerializedName("property_key")
    private String propertyKey;
    @SerializedName("experiment_name")
    private String experimentName;
    @SerializedName("app_id")
    private String appId;
    @SerializedName("page_type")
    private String pageType;
    @SerializedName("viewer_user_id")
    private String viewerUserId;
    @SerializedName("user_agent")
    private String userAgent;
    @SerializedName("referrer")
    private String referrer;
    @SerializedName("device_id")
    private String deviceId;
    @SerializedName("player_id")
    private String playerId;
    @SerializedName("player_name")
    private String playerName;
    @SerializedName("player_version")
    private String playerVersion;
    @SerializedName("entity_id")
    private String entityId;
    @SerializedName("entity_name")
    private String entityName;
    @SerializedName("entity_series")
    private String entitySeries;
    @SerializedName("entity_producer")
    private String entityProducer;
    @SerializedName("entity_content_type")
    private String entityContentType;
    @SerializedName("entity_language_code")
    private String entityLanguageCode;
    @SerializedName("entity_variant_name")
    private String entityVariantName;
    @SerializedName("entity_variant_id")
    private String entityVariantId;
    @SerializedName("entity_duration")
    private String entityDuration;
    @SerializedName("entity_stream_type")
    private String entityStreamType;
    @SerializedName("entity_encoding_variant")
    private String entityEncodingVariant;
    @SerializedName("entity_cdn")
    private String entityCdn;
    @SerializedName("play_through")
    private String playThrough;
    @SerializedName("event_type")
    private String eventType;
    @SerializedName("timestamp")
    private String timestamp;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getViewerUserId() {
        return viewerUserId;
    }

    public void setViewerUserId(String viewerUserId) {
        this.viewerUserId = viewerUserId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerVersion() {
        return playerVersion;
    }

    public void setPlayerVersion(String playerVersion) {
        this.playerVersion = playerVersion;
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

    public String getEntitySeries() {
        return entitySeries;
    }

    public void setEntitySeries(String entitySeries) {
        this.entitySeries = entitySeries;
    }

    public String getEntityProducer() {
        return entityProducer;
    }

    public void setEntityProducer(String entityProducer) {
        this.entityProducer = entityProducer;
    }

    public String getEntityContentType() {
        return entityContentType;
    }

    public void setEntityContentType(String entityContentType) {
        this.entityContentType = entityContentType;
    }

    public String getEntityLanguageCode() {
        return entityLanguageCode;
    }

    public void setEntityLanguageCode(String entityLanguageCode) {
        this.entityLanguageCode = entityLanguageCode;
    }

    public String getEntityVariantName() {
        return entityVariantName;
    }

    public void setEntityVariantName(String entityVariantName) {
        this.entityVariantName = entityVariantName;
    }

    public String getEntityVariantId() {
        return entityVariantId;
    }

    public void setEntityVariantId(String entityVariantId) {
        this.entityVariantId = entityVariantId;
    }

    public String getEntityDuration() {
        return entityDuration;
    }

    public void setEntityDuration(String entityDuration) {
        this.entityDuration = entityDuration;
    }

    public String getEntityStreamType() {
        return entityStreamType;
    }

    public void setEntityStreamType(String entityStreamType) {
        this.entityStreamType = entityStreamType;
    }

    public String getEntityEncodingVariant() {
        return entityEncodingVariant;
    }

    public void setEntityEncodingVariant(String entityEncodingVariant) {
        this.entityEncodingVariant = entityEncodingVariant;
    }

    public String getEntityCdn() {
        return entityCdn;
    }

    public void setEntityCdn(String entityCdn) {
        this.entityCdn = entityCdn;
    }

    public String getPlayThrough() {
        return playThrough;
    }

    public void setPlayThrough(String playThrough) {
        this.playThrough = playThrough;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public String getSubPropertyId() {
        return subPropertyId;
    }

    public void setSubPropertyId(String subPropertyId) {
        this.subPropertyId = subPropertyId;
    }

    public String getPlayerInitTime() {
        return playerInitTime;
    }

    public void setPlayerInitTime(String playerInitTime) {
        this.playerInitTime = playerInitTime;
    }

    public String getHoursWatched() {
        return hoursWatched;
    }

    public void setHoursWatched(String hoursWatched) {
        this.hoursWatched = hoursWatched;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }
}

