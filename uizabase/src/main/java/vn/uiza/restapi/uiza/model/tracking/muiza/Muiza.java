
package vn.uiza.restapi.uiza.model.tracking.muiza;

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

    public String getBeaconDomain() {
        return beaconDomain;
    }

    public void setBeaconDomain(String beaconDomain) {
        this.beaconDomain = beaconDomain;
    }

    public String getEntityCdn() {
        return entityCdn;
    }

    public void setEntityCdn(String entityCdn) {
        this.entityCdn = entityCdn;
    }

    public String getEntityContentType() {
        return entityContentType;
    }

    public void setEntityContentType(String entityContentType) {
        this.entityContentType = entityContentType;
    }

    public String getEntityDuration() {
        return entityDuration;
    }

    public void setEntityDuration(String entityDuration) {
        this.entityDuration = entityDuration;
    }

    public String getEntityEncodingVariant() {
        return entityEncodingVariant;
    }

    public void setEntityEncodingVariant(String entityEncodingVariant) {
        this.entityEncodingVariant = entityEncodingVariant;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityLanguageCode() {
        return entityLanguageCode;
    }

    public void setEntityLanguageCode(String entityLanguageCode) {
        this.entityLanguageCode = entityLanguageCode;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityPosterUrl() {
        return entityPosterUrl;
    }

    public void setEntityPosterUrl(String entityPosterUrl) {
        this.entityPosterUrl = entityPosterUrl;
    }

    public String getEntityProducer() {
        return entityProducer;
    }

    public void setEntityProducer(String entityProducer) {
        this.entityProducer = entityProducer;
    }

    public String getEntitySeries() {
        return entitySeries;
    }

    public void setEntitySeries(String entitySeries) {
        this.entitySeries = entitySeries;
    }

    public String getEntitySourceDomain() {
        return entitySourceDomain;
    }

    public void setEntitySourceDomain(String entitySourceDomain) {
        this.entitySourceDomain = entitySourceDomain;
    }

    public String getEntitySourceDuration() {
        return entitySourceDuration;
    }

    public void setEntitySourceDuration(String entitySourceDuration) {
        this.entitySourceDuration = entitySourceDuration;
    }

    public int getEntitySourceHeight() {
        return entitySourceHeight;
    }

    public void setEntitySourceHeight(int entitySourceHeight) {
        this.entitySourceHeight = entitySourceHeight;
    }

    public String getEntitySourceHostname() {
        return entitySourceHostname;
    }

    public void setEntitySourceHostname(String entitySourceHostname) {
        this.entitySourceHostname = entitySourceHostname;
    }

    public boolean isEntitySourceIsLive() {
        return entitySourceIsLive;
    }

    public void setEntitySourceIsLive(boolean entitySourceIsLive) {
        this.entitySourceIsLive = entitySourceIsLive;
    }

    public String getEntitySourceMimeType() {
        return entitySourceMimeType;
    }

    public void setEntitySourceMimeType(String entitySourceMimeType) {
        this.entitySourceMimeType = entitySourceMimeType;
    }

    public int getEntitySourceWidth() {
        return entitySourceWidth;
    }

    public void setEntitySourceWidth(int entitySourceWidth) {
        this.entitySourceWidth = entitySourceWidth;
    }

    public String getEntityStreamType() {
        return entityStreamType;
    }

    public void setEntityStreamType(String entityStreamType) {
        this.entityStreamType = entityStreamType;
    }

    public String getEntityVariantId() {
        return entityVariantId;
    }

    public void setEntityVariantId(String entityVariantId) {
        this.entityVariantId = entityVariantId;
    }

    public String getEntityVariantName() {
        return entityVariantName;
    }

    public void setEntityVariantName(String entityVariantName) {
        this.entityVariantName = entityVariantName;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public boolean isPlayerAutoplayOn() {
        return playerAutoplayOn;
    }

    public void setPlayerAutoplayOn(boolean playerAutoplayOn) {
        this.playerAutoplayOn = playerAutoplayOn;
    }

    public int getPlayerHeight() {
        return playerHeight;
    }

    public void setPlayerHeight(int playerHeight) {
        this.playerHeight = playerHeight;
    }

    public boolean isPlayerIsFullscreen() {
        return playerIsFullscreen;
    }

    public void setPlayerIsFullscreen(boolean playerIsFullscreen) {
        this.playerIsFullscreen = playerIsFullscreen;
    }

    public boolean isPlayerIsPaused() {
        return playerIsPaused;
    }

    public void setPlayerIsPaused(boolean playerIsPaused) {
        this.playerIsPaused = playerIsPaused;
    }

    public String getPlayerLanguageCode() {
        return playerLanguageCode;
    }

    public void setPlayerLanguageCode(String playerLanguageCode) {
        this.playerLanguageCode = playerLanguageCode;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public long getPlayerPlayheadTime() {
        return playerPlayheadTime;
    }

    public void setPlayerPlayheadTime(long playerPlayheadTime) {
        this.playerPlayheadTime = playerPlayheadTime;
    }

    public String getPlayerPreloadOn() {
        return playerPreloadOn;
    }

    public void setPlayerPreloadOn(String playerPreloadOn) {
        this.playerPreloadOn = playerPreloadOn;
    }

    public long getPlayerSequenceNumber() {
        return playerSequenceNumber;
    }

    public void setPlayerSequenceNumber(long playerSequenceNumber) {
        this.playerSequenceNumber = playerSequenceNumber;
    }

    public String getPlayerSoftwareName() {
        return playerSoftwareName;
    }

    public void setPlayerSoftwareName(String playerSoftwareName) {
        this.playerSoftwareName = playerSoftwareName;
    }

    public String getPlayerSoftwareVersion() {
        return playerSoftwareVersion;
    }

    public void setPlayerSoftwareVersion(String playerSoftwareVersion) {
        this.playerSoftwareVersion = playerSoftwareVersion;
    }

    public String getPlayerVersion() {
        return playerVersion;
    }

    public void setPlayerVersion(String playerVersion) {
        this.playerVersion = playerVersion;
    }

    public long getPlayerWidth() {
        return playerWidth;
    }

    public void setPlayerWidth(long playerWidth) {
        this.playerWidth = playerWidth;
    }

    public long getSessionExpires() {
        return sessionExpires;
    }

    public void setSessionExpires(long sessionExpires) {
        this.sessionExpires = sessionExpires;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSubPropertyId() {
        return subPropertyId;
    }

    public void setSubPropertyId(String subPropertyId) {
        this.subPropertyId = subPropertyId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public long getViewSequenceNumber() {
        return viewSequenceNumber;
    }

    public void setViewSequenceNumber(long viewSequenceNumber) {
        this.viewSequenceNumber = viewSequenceNumber;
    }

    public String getViewerApplicationEngine() {
        return viewerApplicationEngine;
    }

    public void setViewerApplicationEngine(String viewerApplicationEngine) {
        this.viewerApplicationEngine = viewerApplicationEngine;
    }

    public String getViewerApplicationName() {
        return viewerApplicationName;
    }

    public void setViewerApplicationName(String viewerApplicationName) {
        this.viewerApplicationName = viewerApplicationName;
    }

    public String getViewerApplicationVersion() {
        return viewerApplicationVersion;
    }

    public void setViewerApplicationVersion(String viewerApplicationVersion) {
        this.viewerApplicationVersion = viewerApplicationVersion;
    }

    public String getViewerDeviceCategory() {
        return viewerDeviceCategory;
    }

    public void setViewerDeviceCategory(String viewerDeviceCategory) {
        this.viewerDeviceCategory = viewerDeviceCategory;
    }

    public Object getViewerDeviceManufacturer() {
        return viewerDeviceManufacturer;
    }

    public void setViewerDeviceManufacturer(Object viewerDeviceManufacturer) {
        this.viewerDeviceManufacturer = viewerDeviceManufacturer;
    }

    public Object getViewerDeviceName() {
        return viewerDeviceName;
    }

    public void setViewerDeviceName(Object viewerDeviceName) {
        this.viewerDeviceName = viewerDeviceName;
    }

    public int getViewerOsArchitecture() {
        return viewerOsArchitecture;
    }

    public void setViewerOsArchitecture(int viewerOsArchitecture) {
        this.viewerOsArchitecture = viewerOsArchitecture;
    }

    public String getViewerOsFamily() {
        return viewerOsFamily;
    }

    public void setViewerOsFamily(String viewerOsFamily) {
        this.viewerOsFamily = viewerOsFamily;
    }

    public String getViewerOsVersion() {
        return viewerOsVersion;
    }

    public void setViewerOsVersion(String viewerOsVersion) {
        this.viewerOsVersion = viewerOsVersion;
    }

    public long getViewerTime() {
        return viewerTime;
    }

    public void setViewerTime(long viewerTime) {
        this.viewerTime = viewerTime;
    }

    public String getViewerUserId() {
        return viewerUserId;
    }

    public void setViewerUserId(String viewerUserId) {
        this.viewerUserId = viewerUserId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public long getPageLoadTime() {
        return pageLoadTime;
    }

    public void setPageLoadTime(long pageLoadTime) {
        this.pageLoadTime = pageLoadTime;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public long getPlayerInitTime() {
        return playerInitTime;
    }

    public void setPlayerInitTime(long playerInitTime) {
        this.playerInitTime = playerInitTime;
    }

    public long getPlayerStartupTime() {
        return playerStartupTime;
    }

    public void setPlayerStartupTime(long playerStartupTime) {
        this.playerStartupTime = playerStartupTime;
    }

    public long getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(long sessionStart) {
        this.sessionStart = sessionStart;
    }

    public long getViewWatchTime() {
        return viewWatchTime;
    }

    public void setViewWatchTime(long viewWatchTime) {
        this.viewWatchTime = viewWatchTime;
    }

    public String getEntitySourceUrl() {
        return entitySourceUrl;
    }

    public void setEntitySourceUrl(String entitySourceUrl) {
        this.entitySourceUrl = entitySourceUrl;
    }

    public int getPlayerViewCount() {
        return playerViewCount;
    }

    public void setPlayerViewCount(int playerViewCount) {
        this.playerViewCount = playerViewCount;
    }

    public long getViewStart() {
        return viewStart;
    }

    public void setViewStart(long viewStart) {
        this.viewStart = viewStart;
    }

    public long getViewTimeToFirstFrame() {
        return viewTimeToFirstFrame;
    }

    public void setViewTimeToFirstFrame(long viewTimeToFirstFrame) {
        this.viewTimeToFirstFrame = viewTimeToFirstFrame;
    }

    public long getViewAggregateStartupTime() {
        return viewAggregateStartupTime;
    }

    public void setViewAggregateStartupTime(long viewAggregateStartupTime) {
        this.viewAggregateStartupTime = viewAggregateStartupTime;
    }

    public long getViewAggregateStartupTotalTime() {
        return viewAggregateStartupTotalTime;
    }

    public void setViewAggregateStartupTotalTime(long viewAggregateStartupTotalTime) {
        this.viewAggregateStartupTotalTime = viewAggregateStartupTotalTime;
    }

    public int getPlayerErrorCode() {
        return playerErrorCode;
    }

    public void setPlayerErrorCode(int playerErrorCode) {
        this.playerErrorCode = playerErrorCode;
    }

    public String getPlayerErrorMessage() {
        return playerErrorMessage;
    }

    public void setPlayerErrorMessage(String playerErrorMessage) {
        this.playerErrorMessage = playerErrorMessage;
    }

    public int getViewSeekCount() {
        return viewSeekCount;
    }

    public void setViewSeekCount(int viewSeekCount) {
        this.viewSeekCount = viewSeekCount;
    }

    public long getViewSeekDuration() {
        return viewSeekDuration;
    }

    public void setViewSeekDuration(long viewSeekDuration) {
        this.viewSeekDuration = viewSeekDuration;
    }

    public long getViewMaxSeekTime() {
        return viewMaxSeekTime;
    }

    public void setViewMaxSeekTime(long viewMaxSeekTime) {
        this.viewMaxSeekTime = viewMaxSeekTime;
    }

    public int getViewRebufferCount() {
        return viewRebufferCount;
    }

    public void setViewRebufferCount(int viewRebufferCount) {
        this.viewRebufferCount = viewRebufferCount;
    }

    public long getViewRebufferDuration() {
        return viewRebufferDuration;
    }

    public void setViewRebufferDuration(long viewRebufferDuration) {
        this.viewRebufferDuration = viewRebufferDuration;
    }

    public float getViewRebufferFrequency() {
        return viewRebufferFrequency;
    }

    public void setViewRebufferFrequency(float viewRebufferFrequency) {
        this.viewRebufferFrequency = viewRebufferFrequency;
    }

    public float getViewRebufferPercentage() {
        return viewRebufferPercentage;
    }

    public void setViewRebufferPercentage(float viewRebufferPercentage) {
        this.viewRebufferPercentage = viewRebufferPercentage;
    }
}
