package uizacoresdk.util;

public class TmpParamData {
    private static final TmpParamData ourInstance = new TmpParamData();

    public static TmpParamData getInstance() {
        return ourInstance;
    }

    private TmpParamData() {
    }

    public void clearAll() {
        entityCnd = "";
    }

    private String entityCnd = "";
    private String entityContentType = "video";//TODO correct
    private String entityDuration = "";//TODO correct
    private String entityEncodingVariant = "";//TODO correct
    private String entityLanguageCode = "";//TODO correct
    private String entityPosterUrl;
    private String entityProducer;//TODO correct
    private String entitySeries;//TODO correct
    private String entitySourceDomain;//TODO correct
    private String entitySourceDuration;//TODO correct
    private long entitySourceHeight;//TODO correct
    private String entitySourceHostname;//TODO correct
    private boolean entitySourceIsLive;//TODO correct
    private String entitySourceMimeType;//TODO correct
    private String entitySourceUrl;//TODO correct
    private long entitySourceWidth;//TODO correct
    private String entityStreamType;//TODO correct
    private String entityVariantId;//TODO correct
    private String entityVariantName;//TODO correct
    private String pageUrl;//TODO correct
    private boolean playerAutoplayOn;//TODO correct
    private long playerHeight;//TODO correct
    private boolean playerIsFullscreen;//TODO correct
    private boolean playerIsPaused;//TODO correct
    private String playerLanguageCode;//TODO correct
    private String playerName;//TODO correct
    private long playerPlayheadTime;//TODO correct
    private String playerPreloadOn;//TODO correct
    private int playerSequenceNumber;//TODO correct
    private String playerSoftwareName;//TODO correct
    private String playerSoftwareVersion;//TODO correct
    private String playerVersion;//TODO correct
    private long playerWidth;//TODO correct
    private long sessionExpires;//TODO correct
    private String sessionId;//TODO correct
    private String timestamp;//TODO correct
    private String viewId;//TODO correct
    private int viewSequenceNumber;//TODO correct
    private String viewerApplicationEngine;//TODO correct
    private String viewerApplicationName;//TODO correct
    private String viewerApplicationVersion;//TODO correct
    private String viewerDeviceManufacturer;//TODO correct

    public String getEntityCnd() {
        return entityCnd;
    }

    public void setEntityCnd(String entityCnd) {
        this.entityCnd = entityCnd;
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

    public String getEntityLanguageCode() {
        return entityLanguageCode;
    }

    public void setEntityLanguageCode(String entityLanguageCode) {
        this.entityLanguageCode = entityLanguageCode;
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

    public long getEntitySourceHeight() {
        return entitySourceHeight;
    }

    public void setEntitySourceHeight(long entitySourceHeight) {
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

    public String getEntitySourceUrl() {
        return entitySourceUrl;
    }

    public void setEntitySourceUrl(String entitySourceUrl) {
        this.entitySourceUrl = entitySourceUrl;
    }

    public long getEntitySourceWidth() {
        return entitySourceWidth;
    }

    public void setEntitySourceWidth(long entitySourceWidth) {
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

    public long getPlayerHeight() {
        return playerHeight;
    }

    public void setPlayerHeight(long playerHeight) {
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

    public int getPlayerSequenceNumber() {
        return playerSequenceNumber;
    }

    public void setPlayerSequenceNumber(int playerSequenceNumber) {
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

    public int getViewSequenceNumber() {
        return viewSequenceNumber;
    }

    public void setViewSequenceNumber(int viewSequenceNumber) {
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

    public String getViewerDeviceManufacturer() {
        return viewerDeviceManufacturer;
    }

    public void setViewerDeviceManufacturer(String viewerDeviceManufacturer) {
        this.viewerDeviceManufacturer = viewerDeviceManufacturer;
    }
}
