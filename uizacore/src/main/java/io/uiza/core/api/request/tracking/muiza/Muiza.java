package io.uiza.core.api.request.tracking.muiza;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Muiza {

    @SerializedName("beacon_domain")
    @Expose
    private String beaconDomain;
    @SerializedName("entity_cdn")
    @Expose
    private String entityCdn;
    @SerializedName("entity_content_type")
    @Expose
    private String entityContentType;
    @SerializedName("entity_duration")
    @Expose
    private String entityDuration;
    @SerializedName("entity_encoding_variant")
    @Expose
    private String entityEncodingVariant;
    @SerializedName("entity_id")
    @Expose
    private String entityId;
    @SerializedName("entity_language_code")
    @Expose
    private String entityLanguageCode;
    @SerializedName("entity_name")
    @Expose
    private String entityName;
    @SerializedName("entity_poster_url")
    @Expose
    private String entityPosterUrl;
    @SerializedName("entity_producer")
    @Expose
    private String entityProducer;
    @SerializedName("entity_series")
    @Expose
    private String entitySeries;
    @SerializedName("entity_source_domain")
    @Expose
    private String entitySourceDomain;
    @SerializedName("entity_source_duration")
    @Expose
    private String entitySourceDuration;
    @SerializedName("entity_source_height")
    @Expose
    private int entitySourceHeight;
    @SerializedName("entity_source_hostname")
    @Expose
    private String entitySourceHostname;
    @SerializedName("entity_source_is_live")
    @Expose
    private boolean entitySourceIsLive;
    @SerializedName("entity_source_mime_type")
    @Expose
    private String entitySourceMimeType;
    @SerializedName("entity_source_width")
    @Expose
    private int entitySourceWidth;
    @SerializedName("entity_stream_type")
    @Expose
    private String entityStreamType;
    @SerializedName("entity_variant_id")
    @Expose
    private String entityVariantId;
    @SerializedName("entity_variant_name")
    @Expose
    private String entityVariantName;
    @SerializedName("event")
    @Expose
    private String event;
    @SerializedName("experiment_name")
    @Expose
    private String experimentName;
    @SerializedName("page_type")
    @Expose
    private String pageType;
    @SerializedName("page_url")
    @Expose
    private String pageUrl;
    @SerializedName("player_autoplay_on")
    @Expose
    private boolean playerAutoplayOn;
    @SerializedName("player_height")
    @Expose
    private int playerHeight;
    @SerializedName("player_is_fullscreen")
    @Expose
    private boolean playerIsFullscreen;
    @SerializedName("player_is_paused")
    @Expose
    private boolean playerIsPaused;
    @SerializedName("player_language_code")
    @Expose
    private String playerLanguageCode;
    @SerializedName("player_name")
    @Expose
    private String playerName;
    @SerializedName("player_playhead_time")
    @Expose
    private long playerPlayheadTime;
    @SerializedName("player_preload_on")
    @Expose
    private String playerPreloadOn;
    @SerializedName("player_sequence_number")
    @Expose
    private long playerSequenceNumber;
    @SerializedName("player_software_name")
    @Expose
    private String playerSoftwareName;
    @SerializedName("player_software_version")
    @Expose
    private String playerSoftwareVersion;
    @SerializedName("player_version")
    @Expose
    private String playerVersion;
    @SerializedName("player_width")
    @Expose
    private long playerWidth;
    @SerializedName("session_expires")
    @Expose
    private long sessionExpires;
    @SerializedName("session_id")
    @Expose
    private String sessionId;
    @SerializedName("sub_property_id")
    @Expose
    private String subPropertyId;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("view_id")
    @Expose
    private String viewId;
    @SerializedName("view_sequence_number")
    @Expose
    private long viewSequenceNumber;
    @SerializedName("viewer_application_engine")
    @Expose
    private String viewerApplicationEngine;
    @SerializedName("viewer_application_name")
    @Expose
    private String viewerApplicationName;
    @SerializedName("viewer_application_version")
    @Expose
    private String viewerApplicationVersion;
    @SerializedName("viewer_device_category")
    @Expose
    private String viewerDeviceCategory;
    @SerializedName("viewer_device_manufacturer")
    @Expose
    private Object viewerDeviceManufacturer;
    @SerializedName("viewer_device_name")
    @Expose
    private Object viewerDeviceName;
    @SerializedName("viewer_os_architecture")
    @Expose
    private int viewerOsArchitecture;
    @SerializedName("viewer_os_family")
    @Expose
    private String viewerOsFamily;
    @SerializedName("viewer_os_version")
    @Expose
    private String viewerOsVersion;
    @SerializedName("viewer_time")
    @Expose
    private long viewerTime;
    @SerializedName("viewer_user_id")
    @Expose
    private String viewerUserId;
    @SerializedName("app_id")
    @Expose
    private String appId;
    @SerializedName("referrer")
    @Expose
    private String referrer;
    @SerializedName("page_load_time")
    @Expose
    private long pageLoadTime;
    @SerializedName("player_id")
    @Expose
    private String playerId;
    @SerializedName("player_init_time")
    @Expose
    private long playerInitTime;
    @SerializedName("player_startup_time")
    @Expose
    private long playerStartupTime;
    @SerializedName("session_start")
    @Expose
    private long sessionStart;
    @SerializedName("view_watch_time")
    @Expose
    private long viewWatchTime;
    @SerializedName("entity_source_url")
    @Expose
    private String entitySourceUrl;

    @SerializedName("player_view_count")
    @Expose
    private int playerViewCount;

    @SerializedName("view_start")
    @Expose
    private long viewStart;

    @SerializedName("view_time_to_first_frame")
    @Expose
    private long viewTimeToFirstFrame;

    @SerializedName("view_aggregate_startup_time")
    @Expose
    private long viewAggregateStartupTime;

    @SerializedName("view_aggregate_startup_total_time")
    @Expose
    private long viewAggregateStartupTotalTime;

    @SerializedName("player_error_code")
    @Expose
    private Integer playerErrorCode;

    @SerializedName("player_error_message")
    @Expose
    private String playerErrorMessage;

    @SerializedName("view_seek_count")
    @Expose
    private Integer viewSeekCount;

    @SerializedName("view_seek_duration")
    @Expose
    private Long viewSeekDuration;

    @SerializedName("view_max_seek_time")
    @Expose
    private Long viewMaxSeekTime;

    @SerializedName("view_rebuffer_count")
    @Expose
    private Integer viewRebufferCount;

    @SerializedName("view_rebuffer_duration")
    @Expose
    private Long viewRebufferDuration;

    @SerializedName("view_rebuffer_frequency")
    @Expose
    private Float viewRebufferFrequency;

    @SerializedName("view_rebuffer_percentage")
    @Expose
    private Float viewRebufferPercentage;

    private Muiza(Builder builder) {
        beaconDomain = builder.beaconDomain;
        entityCdn = builder.entityCdn;
        entityContentType = builder.entityContentType;
        entityDuration = builder.entityDuration;
        entityEncodingVariant = builder.entityEncodingVariant;
        entityId = builder.entityId;
        entityLanguageCode = builder.entityLanguageCode;
        entityName = builder.entityName;
        entityPosterUrl = builder.entityPosterUrl;
        entityProducer = builder.entityProducer;
        entitySeries = builder.entitySeries;
        entitySourceDomain = builder.entitySourceDomain;
        entitySourceDuration = builder.entitySourceDuration;
        entitySourceHeight = builder.entitySourceHeight;
        entitySourceHostname = builder.entitySourceHostname;
        entitySourceIsLive = builder.entitySourceIsLive;
        entitySourceMimeType = builder.entitySourceMimeType;
        entitySourceWidth = builder.entitySourceWidth;
        entityStreamType = builder.entityStreamType;
        entityVariantId = builder.entityVariantId;
        entityVariantName = builder.entityVariantName;
        event = builder.event;
        experimentName = builder.experimentName;
        pageType = builder.pageType;
        pageUrl = builder.pageUrl;
        playerAutoplayOn = builder.playerAutoplayOn;
        playerHeight = builder.playerHeight;
        playerIsFullscreen = builder.playerIsFullscreen;
        playerIsPaused = builder.playerIsPaused;
        playerLanguageCode = builder.playerLanguageCode;
        playerName = builder.playerName;
        playerPlayheadTime = builder.playerPlayheadTime;
        playerPreloadOn = builder.playerPreloadOn;
        playerSequenceNumber = builder.playerSequenceNumber;
        playerSoftwareName = builder.playerSoftwareName;
        playerSoftwareVersion = builder.playerSoftwareVersion;
        playerVersion = builder.playerVersion;
        playerWidth = builder.playerWidth;
        sessionExpires = builder.sessionExpires;
        sessionId = builder.sessionId;
        subPropertyId = builder.subPropertyId;
        timestamp = builder.timestamp;
        viewId = builder.viewId;
        viewSequenceNumber = builder.viewSequenceNumber;
        viewerApplicationEngine = builder.viewerApplicationEngine;
        viewerApplicationName = builder.viewerApplicationName;
        viewerApplicationVersion = builder.viewerApplicationVersion;
        viewerDeviceCategory = builder.viewerDeviceCategory;
        viewerDeviceManufacturer = builder.viewerDeviceManufacturer;
        viewerDeviceName = builder.viewerDeviceName;
        viewerOsArchitecture = builder.viewerOsArchitecture;
        viewerOsFamily = builder.viewerOsFamily;
        viewerOsVersion = builder.viewerOsVersion;
        viewerTime = builder.viewerTime;
        viewerUserId = builder.viewerUserId;
        appId = builder.appId;
        referrer = builder.referrer;
        pageLoadTime = builder.pageLoadTime;
        playerId = builder.playerId;
        playerInitTime = builder.playerInitTime;
        playerStartupTime = builder.playerStartupTime;
        sessionStart = builder.sessionStart;
        viewWatchTime = builder.viewWatchTime;
        entitySourceUrl = builder.entitySourceUrl;
        playerViewCount = builder.playerViewCount;
        viewStart = builder.viewStart;
        viewTimeToFirstFrame = builder.viewTimeToFirstFrame;
        viewAggregateStartupTime = builder.viewAggregateStartupTime;
        viewAggregateStartupTotalTime = builder.viewAggregateStartupTotalTime;
        playerErrorCode = builder.playerErrorCode;
        playerErrorMessage = builder.playerErrorMessage;
        viewSeekCount = builder.viewSeekCount;
        viewSeekDuration = builder.viewSeekDuration;
        viewMaxSeekTime = builder.viewMaxSeekTime;
        viewRebufferCount = builder.viewRebufferCount;
        viewRebufferDuration = builder.viewRebufferDuration;
        viewRebufferFrequency = builder.viewRebufferFrequency;
        viewRebufferPercentage = builder.viewRebufferPercentage;
    }

    public static final class Builder {

        private String beaconDomain;
        private String entityCdn;
        private String entityContentType;
        private String entityDuration;
        private String entityEncodingVariant;
        private String entityId;
        private String entityLanguageCode;
        private String entityName;
        private String entityPosterUrl;
        private String entityProducer;
        private String entitySeries;
        private String entitySourceDomain;
        private String entitySourceDuration;
        private int entitySourceHeight;
        private String entitySourceHostname;
        private boolean entitySourceIsLive;
        private String entitySourceMimeType;
        private int entitySourceWidth;
        private String entityStreamType;
        private String entityVariantId;
        private String entityVariantName;
        private String event;
        private String experimentName;
        private String pageType;
        private String pageUrl;
        private boolean playerAutoplayOn;
        private int playerHeight;
        private boolean playerIsFullscreen;
        private boolean playerIsPaused;
        private String playerLanguageCode;
        private String playerName;
        private long playerPlayheadTime;
        private String playerPreloadOn;
        private long playerSequenceNumber;
        private String playerSoftwareName;
        private String playerSoftwareVersion;
        private String playerVersion;
        private long playerWidth;
        private long sessionExpires;
        private String sessionId;
        private String subPropertyId;
        private String timestamp;
        private String viewId;
        private long viewSequenceNumber;
        private String viewerApplicationEngine;
        private String viewerApplicationName;
        private String viewerApplicationVersion;
        private String viewerDeviceCategory;
        private Object viewerDeviceManufacturer;
        private Object viewerDeviceName;
        private int viewerOsArchitecture;
        private String viewerOsFamily;
        private String viewerOsVersion;
        private long viewerTime;
        private String viewerUserId;
        private String appId;
        private String referrer;
        private long pageLoadTime;
        private String playerId;
        private long playerInitTime;
        private long playerStartupTime;
        private long sessionStart;
        private long viewWatchTime;
        private String entitySourceUrl;
        private int playerViewCount;
        private long viewStart;
        private long viewTimeToFirstFrame;
        private long viewAggregateStartupTime;
        private long viewAggregateStartupTotalTime;
        private Integer playerErrorCode;
        private String playerErrorMessage;
        private Integer viewSeekCount;
        private Long viewSeekDuration;
        private Long viewMaxSeekTime;
        private Integer viewRebufferCount;
        private Long viewRebufferDuration;
        private Float viewRebufferFrequency;
        private Float viewRebufferPercentage;

        public Builder() {
        }

        public Builder beaconDomain(String val) {
            beaconDomain = val;
            return this;
        }

        public Builder entityCdn(String val) {
            entityCdn = val;
            return this;
        }

        public Builder entityContentType(String val) {
            entityContentType = val;
            return this;
        }

        public Builder entityDuration(String val) {
            entityDuration = val;
            return this;
        }

        public Builder entityEncodingVariant(String val) {
            entityEncodingVariant = val;
            return this;
        }

        public Builder entityId(String val) {
            entityId = val;
            return this;
        }

        public Builder entityLanguageCode(String val) {
            entityLanguageCode = val;
            return this;
        }

        public Builder entityName(String val) {
            entityName = val;
            return this;
        }

        public Builder entityPosterUrl(String val) {
            entityPosterUrl = val;
            return this;
        }

        public Builder entityProducer(String val) {
            entityProducer = val;
            return this;
        }

        public Builder entitySeries(String val) {
            entitySeries = val;
            return this;
        }

        public Builder entitySourceDomain(String val) {
            entitySourceDomain = val;
            return this;
        }

        public Builder entitySourceDuration(String val) {
            entitySourceDuration = val;
            return this;
        }

        public Builder entitySourceHeight(int val) {
            entitySourceHeight = val;
            return this;
        }

        public Builder entitySourceHostname(String val) {
            entitySourceHostname = val;
            return this;
        }

        public Builder entitySourceIsLive(boolean val) {
            entitySourceIsLive = val;
            return this;
        }

        public Builder entitySourceMimeType(String val) {
            entitySourceMimeType = val;
            return this;
        }

        public Builder entitySourceWidth(int val) {
            entitySourceWidth = val;
            return this;
        }

        public Builder entityStreamType(String val) {
            entityStreamType = val;
            return this;
        }

        public Builder entityVariantId(String val) {
            entityVariantId = val;
            return this;
        }

        public Builder entityVariantName(String val) {
            entityVariantName = val;
            return this;
        }

        public Builder event(String val) {
            event = val;
            return this;
        }

        public Builder experimentName(String val) {
            experimentName = val;
            return this;
        }

        public Builder pageType(String val) {
            pageType = val;
            return this;
        }

        public Builder pageUrl(String val) {
            pageUrl = val;
            return this;
        }

        public Builder playerAutoplayOn(boolean val) {
            playerAutoplayOn = val;
            return this;
        }

        public Builder playerHeight(int val) {
            playerHeight = val;
            return this;
        }

        public Builder playerIsFullscreen(boolean val) {
            playerIsFullscreen = val;
            return this;
        }

        public Builder playerIsPaused(boolean val) {
            playerIsPaused = val;
            return this;
        }

        public Builder playerLanguageCode(String val) {
            playerLanguageCode = val;
            return this;
        }

        public Builder playerName(String val) {
            playerName = val;
            return this;
        }

        public Builder playerPlayheadTime(long val) {
            playerPlayheadTime = val;
            return this;
        }

        public Builder playerPreloadOn(String val) {
            playerPreloadOn = val;
            return this;
        }

        public Builder playerSequenceNumber(long val) {
            playerSequenceNumber = val;
            return this;
        }

        public Builder playerSoftwareName(String val) {
            playerSoftwareName = val;
            return this;
        }

        public Builder playerSoftwareVersion(String val) {
            playerSoftwareVersion = val;
            return this;
        }

        public Builder playerVersion(String val) {
            playerVersion = val;
            return this;
        }

        public Builder playerWidth(long val) {
            playerWidth = val;
            return this;
        }

        public Builder sessionExpires(long val) {
            sessionExpires = val;
            return this;
        }

        public Builder sessionId(String val) {
            sessionId = val;
            return this;
        }

        public Builder subPropertyId(String val) {
            subPropertyId = val;
            return this;
        }

        public Builder timestamp(String val) {
            timestamp = val;
            return this;
        }

        public Builder viewId(String val) {
            viewId = val;
            return this;
        }

        public Builder viewSequenceNumber(long val) {
            viewSequenceNumber = val;
            return this;
        }

        public Builder viewerApplicationEngine(String val) {
            viewerApplicationEngine = val;
            return this;
        }

        public Builder viewerApplicationName(String val) {
            viewerApplicationName = val;
            return this;
        }

        public Builder viewerApplicationVersion(String val) {
            viewerApplicationVersion = val;
            return this;
        }

        public Builder viewerDeviceCategory(String val) {
            viewerDeviceCategory = val;
            return this;
        }

        public Builder viewerDeviceManufacturer(Object val) {
            viewerDeviceManufacturer = val;
            return this;
        }

        public Builder viewerDeviceName(Object val) {
            viewerDeviceName = val;
            return this;
        }

        public Builder viewerOsArchitecture(int val) {
            viewerOsArchitecture = val;
            return this;
        }

        public Builder viewerOsFamily(String val) {
            viewerOsFamily = val;
            return this;
        }

        public Builder viewerOsVersion(String val) {
            viewerOsVersion = val;
            return this;
        }

        public Builder viewerTime(long val) {
            viewerTime = val;
            return this;
        }

        public Builder viewerUserId(String val) {
            viewerUserId = val;
            return this;
        }

        public Builder appId(String val) {
            appId = val;
            return this;
        }

        public Builder referrer(String val) {
            referrer = val;
            return this;
        }

        public Builder pageLoadTime(long val) {
            pageLoadTime = val;
            return this;
        }

        public Builder playerId(String val) {
            playerId = val;
            return this;
        }

        public Builder playerInitTime(long val) {
            playerInitTime = val;
            return this;
        }

        public Builder playerStartupTime(long val) {
            playerStartupTime = val;
            return this;
        }

        public Builder sessionStart(long val) {
            sessionStart = val;
            return this;
        }

        public Builder viewWatchTime(long val) {
            viewWatchTime = val;
            return this;
        }

        public Builder entitySourceUrl(String val) {
            entitySourceUrl = val;
            return this;
        }

        public Builder playerViewCount(int val) {
            playerViewCount = val;
            return this;
        }

        public Builder viewStart(long val) {
            viewStart = val;
            return this;
        }

        public Builder viewTimeToFirstFrame(long val) {
            viewTimeToFirstFrame = val;
            return this;
        }

        public Builder viewAggregateStartupTime(long val) {
            viewAggregateStartupTime = val;
            return this;
        }

        public Builder viewAggregateStartupTotalTime(long val) {
            viewAggregateStartupTotalTime = val;
            return this;
        }

        public Builder playerErrorCode(Integer val) {
            playerErrorCode = val;
            return this;
        }

        public Builder playerErrorMessage(String val) {
            playerErrorMessage = val;
            return this;
        }

        public Builder viewSeekCount(Integer val) {
            viewSeekCount = val;
            return this;
        }

        public Builder viewSeekDuration(Long val) {
            viewSeekDuration = val;
            return this;
        }

        public Builder viewMaxSeekTime(Long val) {
            viewMaxSeekTime = val;
            return this;
        }

        public Builder viewRebufferCount(Integer val) {
            viewRebufferCount = val;
            return this;
        }

        public Builder viewRebufferDuration(Long val) {
            viewRebufferDuration = val;
            return this;
        }

        public Builder viewRebufferFrequency(Float val) {
            viewRebufferFrequency = val;
            return this;
        }

        public Builder viewRebufferPercentage(Float val) {
            viewRebufferPercentage = val;
            return this;
        }

        public Muiza build() {
            return new Muiza(this);
        }
    }
}
