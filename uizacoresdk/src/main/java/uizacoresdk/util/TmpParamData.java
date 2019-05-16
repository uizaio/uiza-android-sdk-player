package uizacoresdk.util;

import vn.uiza.core.common.Constants;

public class TmpParamData {
    private final String TAG = getClass().getSimpleName();
    private static final TmpParamData ourInstance = new TmpParamData();

    public static TmpParamData getInstance() {
        return ourInstance;
    }

    private TmpParamData() {
    }

    public void clearAll() {
        //TODO correct
        entityCnd = "";
        entityDuration = "0";
        entityPosterUrl = "";
        entitySourceDomain = "";
        entitySourceDuration = "";
        entitySourceHeight = 0;
        entitySourceHostname = "";
        entitySourceUrl = "";
        entitySourceWidth = 0;
        playerAutoplayOn = Constants.DF_PLAYER_IS_AUTO_START;
        playerHeight = 0;
        playerIsFullscreen = false;
        playerIsPaused = false;
        playerSequenceNumber = 0;
        playerWidth = 0;
        sessionId = "";
        viewSequenceNumber = 0;
        pageLoadTime = 0;
        playerInitTime = 0;
        playerStartupTime = 0;
        sessionStart = 0;
        playerViewCount = 0;
        viewStart = 0;
        viewWatchTime = 0;
        viewTimeToFirstFrame = 0;
        timeFromInitEntityIdToAllApiCalledSuccess = 0;
        viewSeekCount = 0;
        viewSeekDuration = 0;
        viewMaxSeekTime = 0;
        viewRebufferCount = 0;
        viewRebufferDuration = 0;
    }

    private String entityCnd = "";
    private String entityContentType = "video";
    private String entityDuration = "0";
    private String entityEncodingVariant = "";//TODO correct
    private String entityLanguageCode = "en-us";//TODO correct
    private String entityPosterUrl = "";
    private String entityProducer = "";//TODO correct
    private String entitySeries = "";//TODO correct
    private String entitySourceDomain = "";
    private String entitySourceDuration = "";
    private int entitySourceHeight = 0;
    private String entitySourceHostname = "";
    private String entitySourceMimeType = "";//TODO correct
    private String entitySourceUrl = "";
    private int entitySourceWidth = 0;
    private String entityStreamType = "";//TODO correct
    private String entityVariantId;//TODO correct
    private String entityVariantName;//TODO correct
    private String pageUrl;//TODO correct
    private boolean playerAutoplayOn = Constants.DF_PLAYER_IS_AUTO_START;
    private int playerHeight = 0;
    private boolean playerIsFullscreen = false;
    private boolean playerIsPaused = false;
    private String playerLanguageCode = "en-us";
    private long playerPlayheadTime;//TODO correct
    private String playerPreloadOn = "metadata";//TODO correct
    private int playerSequenceNumber = 0;
    private String playerSoftwareName = Constants.PLAYER_NAME;
    private String playerSoftwareVersion = Constants.PLAYER_SDK_VERSION;
    private long playerWidth = 0;
    private String sessionId = "";
    private int viewSequenceNumber = 0;
    private String viewerApplicationEngine = "";
    private String viewerApplicationName = "";
    private String viewerApplicationVersion = "";
    private String referrer = "";//TODO correct
    private long pageLoadTime = 0;
    private long playerInitTime = 0;
    private long playerStartupTime = 0;
    private long sessionStart = 0;
    private int playerViewCount = 0;
    private long viewStart = 0;
    private long viewWatchTime = 0;
    private long viewTimeToFirstFrame = 0;
    private long timeFromInitEntityIdToAllApiCalledSuccess = 0;
    private int viewSeekCount = 0;
    private long viewSeekDuration = 0;
    private long viewMaxSeekTime = 0;
    private int viewRebufferCount = 0;
    private long viewRebufferDuration = 0;

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

    public void addPlayerSequenceNumber() {
        this.playerSequenceNumber++;
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

    public long getPlayerWidth() {
        return playerWidth;
    }

    public void setPlayerWidth(long playerWidth) {
        this.playerWidth = playerWidth;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getViewSequenceNumber() {
        return viewSequenceNumber;
    }

    public void addViewSequenceNumber() {
        this.viewSequenceNumber++;
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

    public int getPlayerViewCount() {
        return playerViewCount;
    }

    public void addPlayerViewCount() {
        this.playerViewCount++;
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

    public long getTimeFromInitEntityIdToAllApiCalledSuccess() {
        return timeFromInitEntityIdToAllApiCalledSuccess;
    }

    public void setTimeFromInitEntityIdToAllApiCalledSuccess(long timeFromInitEntityIdToAllApiCalledSuccess) {
        this.timeFromInitEntityIdToAllApiCalledSuccess = timeFromInitEntityIdToAllApiCalledSuccess;
    }

    public int getViewSeekCount() {
        return viewSeekCount;
    }

    public void addViewSeekCount() {
        this.viewSeekCount++;
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

    public void addViewRebufferCount() {
        this.viewRebufferCount++;
    }

    public long getViewRebufferDuration() {
        return viewRebufferDuration;
    }

    public void setViewRebufferDuration(long viewRebufferDuration) {
        this.viewRebufferDuration = viewRebufferDuration;
    }

    public long getViewWatchTime() {
        return viewWatchTime;
    }

    public void addViewWatchTime(long addViewWatchTime) {
        this.viewWatchTime += addViewWatchTime;
    }
}
