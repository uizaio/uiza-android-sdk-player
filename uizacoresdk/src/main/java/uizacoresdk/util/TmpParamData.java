package uizacoresdk.util;

import vn.uiza.core.common.Constants;

public class TmpParamData {
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
    }

    private String entityCnd = "";
    private String entityContentType = "video";//TODO correct
    private String entityDuration = "0";
    private String entityEncodingVariant = "";//TODO correct
    private String entityLanguageCode = "";//TODO correct
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
    private boolean playerIsFullscreen;
    private boolean playerIsPaused;
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
    private String referrer;//TODO correct
    private long pageLoadTime;//TODO correct
    private long playerInitTime;//TODO correct
    private long playerStartupTime;//TODO correct
    private long sessionStart;//TODO correct
    private int playerViewCount;//TODO correct
    private String viewStart;//TODO correct
    private String viewTimeToFirstFrame;//TODO correct
    private long viewWatchTime;//TODO correct
    private String viewAggregateStartupTime;//TODO correct
    private String viewAggregateStartupTotalTime;//TODO correct
    private int playerErrorCode;//TODO correct
    private String playerErrorMessage;//TODO correct
    private String viewSeekCount;//TODO correct
    private String viewSeekDuration;//TODO correct
    private String viewMaxSeekTime;//TODO correct
    private String viewRebufferCount;//TODO correct
    private String viewRebufferDuration;//TODO correct
    private String viewRebufferFrequency;//TODO correct
    private String viewRebufferPercentage;//TODO correct

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

    public void setPlayerViewCount(int playerViewCount) {
        this.playerViewCount = playerViewCount;
    }

    public String getViewStart() {
        return viewStart;
    }

    public void setViewStart(String viewStart) {
        this.viewStart = viewStart;
    }

    public String getViewTimeToFirstFrame() {
        return viewTimeToFirstFrame;
    }

    public void setViewTimeToFirstFrame(String viewTimeToFirstFrame) {
        this.viewTimeToFirstFrame = viewTimeToFirstFrame;
    }

    public String getViewAggregateStartupTime() {
        return viewAggregateStartupTime;
    }

    public void setViewAggregateStartupTime(String viewAggregateStartupTime) {
        this.viewAggregateStartupTime = viewAggregateStartupTime;
    }

    public String getViewAggregateStartupTotalTime() {
        return viewAggregateStartupTotalTime;
    }

    public void setViewAggregateStartupTotalTime(String viewAggregateStartupTotalTime) {
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

    public String getViewSeekCount() {
        return viewSeekCount;
    }

    public void setViewSeekCount(String viewSeekCount) {
        this.viewSeekCount = viewSeekCount;
    }

    public String getViewSeekDuration() {
        return viewSeekDuration;
    }

    public void setViewSeekDuration(String viewSeekDuration) {
        this.viewSeekDuration = viewSeekDuration;
    }

    public String getViewMaxSeekTime() {
        return viewMaxSeekTime;
    }

    public void setViewMaxSeekTime(String viewMaxSeekTime) {
        this.viewMaxSeekTime = viewMaxSeekTime;
    }

    public String getViewRebufferCount() {
        return viewRebufferCount;
    }

    public void setViewRebufferCount(String viewRebufferCount) {
        this.viewRebufferCount = viewRebufferCount;
    }

    public String getViewRebufferDuration() {
        return viewRebufferDuration;
    }

    public void setViewRebufferDuration(String viewRebufferDuration) {
        this.viewRebufferDuration = viewRebufferDuration;
    }

    public String getViewRebufferFrequency() {
        return viewRebufferFrequency;
    }

    public void setViewRebufferFrequency(String viewRebufferFrequency) {
        this.viewRebufferFrequency = viewRebufferFrequency;
    }

    public String getViewRebufferPercentage() {
        return viewRebufferPercentage;
    }

    public void setViewRebufferPercentage(String viewRebufferPercentage) {
        this.viewRebufferPercentage = viewRebufferPercentage;
    }

    public long getViewWatchTime() {
        return viewWatchTime;
    }

    public void setViewWatchTime(long viewWatchTime) {
        this.viewWatchTime = viewWatchTime;
    }
}
