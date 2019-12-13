package vn.uiza.restapi.uiza.model.tracking;

/**
 * Created by loitp on 18/1/2019.
 */

import com.squareup.moshi.Json;

public class UizaTracking {

    @Json(name = "hours_watched")
    private String hoursWatched;
    @Json(name = "publisher_id")
    private String publisherId;
    @Json(name = "player_init_time")
    private String playerInitTime;
    @Json(name = "sub_property_id")
    private String subPropertyId;
    @Json(name = "property_key")
    private String propertyKey;
    @Json(name = "experiment_name")
    private String experimentName;
    @Json(name = "app_id")
    private String appId;
    @Json(name = "page_type")
    private String pageType;
    @Json(name = "viewer_user_id")
    private String viewerUserId;
    @Json(name = "user_agent")
    private String userAgent;
    @Json(name = "referrer")
    private String referrer;
    @Json(name = "device_id")
    private String deviceId;
    @Json(name = "player_id")
    private String playerId;
    @Json(name = "player_name")
    private String playerName;
    @Json(name = "player_version")
    private String playerVersion;
    @Json(name = "entity_id")
    private String entityId;
    @Json(name = "entity_name")
    private String entityName;
    @Json(name = "entity_series")
    private String entitySeries;
    @Json(name = "entity_producer")
    private String entityProducer;
    @Json(name = "entity_content_type")
    private String entityContentType;
    @Json(name = "entity_language_code")
    private String entityLanguageCode;
    @Json(name = "entity_variant_name")
    private String entityVariantName;
    @Json(name = "entity_variant_id")
    private String entityVariantId;
    @Json(name = "entity_duration")
    private String entityDuration;
    @Json(name = "entity_stream_type")
    private String entityStreamType;
    @Json(name = "entity_encoding_variant")
    private String entityEncodingVariant;
    @Json(name = "entity_cdn")
    private String entityCdn;
    @Json(name = "play_through")
    private String playThrough;
    @Json(name = "event_type")
    private String eventType;
    @Json(name = "timestamp")
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

