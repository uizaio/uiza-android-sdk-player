package io.uiza.core.api.request.tracking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UizaTracking {

    @SerializedName("hours_watched")
    @Expose
    private String hoursWatched;
    @SerializedName("publisher_id")
    @Expose
    private String publisherId;
    @SerializedName("player_init_time")
    @Expose
    private String playerInitTime;
    @SerializedName("sub_property_id")
    @Expose
    private String subPropertyId;
    @SerializedName("property_key")
    @Expose
    private String propertyKey;
    @SerializedName("experiment_name")
    @Expose
    private String experimentName;
    @SerializedName("app_id")
    @Expose
    private String appId;
    @SerializedName("page_type")
    @Expose
    private String pageType;
    @SerializedName("viewer_user_id")
    @Expose
    private String viewerUserId;
    @SerializedName("user_agent")
    @Expose
    private String userAgent;
    @SerializedName("referrer")
    @Expose
    private String referrer;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("player_id")
    @Expose
    private String playerId;
    @SerializedName("player_name")
    @Expose
    private String playerName;
    @SerializedName("player_version")
    @Expose
    private String playerVersion;
    @SerializedName("entity_id")
    @Expose
    private String entityId;
    @SerializedName("entity_name")
    @Expose
    private String entityName;
    @SerializedName("entity_series")
    @Expose
    private String entitySeries;
    @SerializedName("entity_producer")
    @Expose
    private String entityProducer;
    @SerializedName("entity_content_type")
    @Expose
    private String entityContentType;
    @SerializedName("entity_language_code")
    @Expose
    private String entityLanguageCode;
    @SerializedName("entity_variant_name")
    @Expose
    private String entityVariantName;
    @SerializedName("entity_variant_id")
    @Expose
    private String entityVariantId;
    @SerializedName("entity_duration")
    @Expose
    private String entityDuration;
    @SerializedName("entity_stream_type")
    @Expose
    private String entityStreamType;
    @SerializedName("entity_encoding_variant")
    @Expose
    private String entityEncodingVariant;
    @SerializedName("entity_cdn")
    @Expose
    private String entityCdn;
    @SerializedName("play_through")
    @Expose
    private String playThrough;
    @SerializedName("event_type")
    @Expose
    private String eventType;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    private UizaTracking(Builder builder) {
        hoursWatched = builder.hoursWatched;
        publisherId = builder.publisherId;
        playerInitTime = builder.playerInitTime;
        subPropertyId = builder.subPropertyId;
        propertyKey = builder.propertyKey;
        experimentName = builder.experimentName;
        appId = builder.appId;
        pageType = builder.pageType;
        viewerUserId = builder.viewerUserId;
        userAgent = builder.userAgent;
        referrer = builder.referrer;
        deviceId = builder.deviceId;
        playerId = builder.playerId;
        playerName = builder.playerName;
        playerVersion = builder.playerVersion;
        entityId = builder.entityId;
        entityName = builder.entityName;
        entitySeries = builder.entitySeries;
        entityProducer = builder.entityProducer;
        entityContentType = builder.entityContentType;
        entityLanguageCode = builder.entityLanguageCode;
        entityVariantName = builder.entityVariantName;
        entityVariantId = builder.entityVariantId;
        entityDuration = builder.entityDuration;
        entityStreamType = builder.entityStreamType;
        entityEncodingVariant = builder.entityEncodingVariant;
        entityCdn = builder.entityCdn;
        playThrough = builder.playThrough;
        eventType = builder.eventType;
        timestamp = builder.timestamp;
    }

    public static final class Builder {

        private String hoursWatched;
        private String publisherId;
        private String playerInitTime;
        private String subPropertyId;
        private String propertyKey;
        private String experimentName;
        private String appId;
        private String pageType;
        private String viewerUserId;
        private String userAgent;
        private String referrer;
        private String deviceId;
        private String playerId;
        private String playerName;
        private String playerVersion;
        private String entityId;
        private String entityName;
        private String entitySeries;
        private String entityProducer;
        private String entityContentType;
        private String entityLanguageCode;
        private String entityVariantName;
        private String entityVariantId;
        private String entityDuration;
        private String entityStreamType;
        private String entityEncodingVariant;
        private String entityCdn;
        private String playThrough;
        private String eventType;
        private String timestamp;

        public Builder() {
        }

        public Builder hoursWatched(String val) {
            hoursWatched = val;
            return this;
        }

        public Builder publisherId(String val) {
            publisherId = val;
            return this;
        }

        public Builder playerInitTime(String val) {
            playerInitTime = val;
            return this;
        }

        public Builder subPropertyId(String val) {
            subPropertyId = val;
            return this;
        }

        public Builder propertyKey(String val) {
            propertyKey = val;
            return this;
        }

        public Builder experimentName(String val) {
            experimentName = val;
            return this;
        }

        public Builder appId(String val) {
            appId = val;
            return this;
        }

        public Builder pageType(String val) {
            pageType = val;
            return this;
        }

        public Builder viewerUserId(String val) {
            viewerUserId = val;
            return this;
        }

        public Builder userAgent(String val) {
            userAgent = val;
            return this;
        }

        public Builder referrer(String val) {
            referrer = val;
            return this;
        }

        public Builder deviceId(String val) {
            deviceId = val;
            return this;
        }

        public Builder playerId(String val) {
            playerId = val;
            return this;
        }

        public Builder playerName(String val) {
            playerName = val;
            return this;
        }

        public Builder playerVersion(String val) {
            playerVersion = val;
            return this;
        }

        public Builder entityId(String val) {
            entityId = val;
            return this;
        }

        public Builder entityName(String val) {
            entityName = val;
            return this;
        }

        public Builder entitySeries(String val) {
            entitySeries = val;
            return this;
        }

        public Builder entityProducer(String val) {
            entityProducer = val;
            return this;
        }

        public Builder entityContentType(String val) {
            entityContentType = val;
            return this;
        }

        public Builder entityLanguageCode(String val) {
            entityLanguageCode = val;
            return this;
        }

        public Builder entityVariantName(String val) {
            entityVariantName = val;
            return this;
        }

        public Builder entityVariantId(String val) {
            entityVariantId = val;
            return this;
        }

        public Builder entityDuration(String val) {
            entityDuration = val;
            return this;
        }

        public Builder entityStreamType(String val) {
            entityStreamType = val;
            return this;
        }

        public Builder entityEncodingVariant(String val) {
            entityEncodingVariant = val;
            return this;
        }

        public Builder entityCdn(String val) {
            entityCdn = val;
            return this;
        }

        public Builder playThrough(String val) {
            playThrough = val;
            return this;
        }

        public Builder eventType(String val) {
            eventType = val;
            return this;
        }

        public Builder timestamp(String val) {
            timestamp = val;
            return this;
        }

        public UizaTracking build() {
            return new UizaTracking(this);
        }
    }
}
