package uizacoresdk.util;

import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import io.uiza.core.api.UzApiMaster;
import io.uiza.core.api.UzServiceApi;
import io.uiza.core.api.client.UzRestClient;
import io.uiza.core.api.client.UzRestClientGetLinkPlay;
import io.uiza.core.api.client.UzRestClientHeartBeat;
import io.uiza.core.api.client.UzRestClientTracking;
import io.uiza.core.api.request.tracking.UizaTracking;
import io.uiza.core.api.request.tracking.muiza.Muiza;
import io.uiza.core.api.response.UtcTime;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.streaming.StreamingToken;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.api.util.ApiSubscriber;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.LLog;
import io.uiza.core.util.UzCoreUtil;
import io.uiza.core.util.UzDateTimeUtil;
import io.uiza.core.util.constant.Constants;
import java.util.ArrayList;
import java.util.List;
import uizacoresdk.R;
import uizacoresdk.chromecast.Casty;

/**
 * Created by loitp on 4/28/2018.
 */

public class UZData {
    private final String TAG = getClass().getSimpleName();
    private static final UZData ourInstance = new UZData();
    private static final String PAGE_TYPE = "app";
    private static final String NULL = "null";
    private static final String ANDROID = "Android ";
    private static final String API_LEVEL = "Api level ";
    private static final String TEXT = "text";
    private static final String VIDEO = "video";
    private static final String AUDIO = "audio";
    private static final String CAPTIONS = "captions";
    private static final String SUBTITLE = "subtitle";

    public static UZData getInstance() {
        return ourInstance;
    }

    private UZData() {
    }

    private int currentPlayerId = R.layout.uz_player_skin_1;//id of layout xml
    private String playerInforId;//player id from workspace uiza

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public String getPlayerInforId() {
        return playerInforId;
    }

    public void setPlayerInforId(String playerInforId) {
        this.playerInforId = playerInforId;
    }

    private int mAPIVersion = Constants.API_VERSION_3;
    private String mDomainAPI = "";
    private String mDomainAPITracking = "";
    private String mToken = "";
    private String mAppId = "";

    private Casty casty;

    public void setCasty(Casty casty) {
        this.casty = casty;
    }

    public Casty getCasty() {
        /*if (casty == null) {
            //TODO bug if use mini controller
            //casty = Casty.create(baseActivity).withMiniController();
            casty = Casty.create(baseActivity);
        }*/
        if (casty == null) {
            LLog.e(TAG, "getCasty null");
            throw new NullPointerException("You must init Casty with acitivy before using Chromecast. Tips: put 'UZUtil.setCasty(this);' to your onStart() or onCreate()");
        }
        return casty;
    }

    public boolean initSDK(int apiVersion, String domainAPI, String token, String appId, int environment) {
        if (domainAPI == null || domainAPI.isEmpty() || domainAPI.contains(" ")
                || token == null || token.isEmpty() || token.contains(" ")
                || appId == null || appId.isEmpty() || appId.contains(" ")) {
            return false;
        }
        mAPIVersion = apiVersion;
        mDomainAPI = domainAPI;
        mToken = token;
        mAppId = appId;
        UzRestClient.init(Constants.PREFIXS + domainAPI, token);
        UZUtil.setToken(UzCoreUtil.getContext(), token);
        syncCurrentUTCTime();// for synchronize server time
        if (environment == Constants.ENVIRONMENT_DEV) {
            UzRestClientGetLinkPlay.init(Constants.URL_GET_LINK_PLAY_DEV);
            UzRestClientHeartBeat.init(Constants.URL_HEART_BEAT_DEV);
            initTracking(Constants.URL_TRACKING_DEV, Constants.TRACKING_ACCESS_TOKEN_DEV);
        } else if (environment == Constants.ENVIRONMENT_STAG) {
            UzRestClientGetLinkPlay.init(Constants.URL_GET_LINK_PLAY_STAG);
            UzRestClientHeartBeat.init(Constants.URL_HEART_BEAT_STAG);
            initTracking(Constants.URL_TRACKING_STAG, Constants.TRACKING_ACCESS_TOKEN_STAG);
        } else if (environment == Constants.ENVIRONMENT_PROD) {
            UzRestClientGetLinkPlay.init(Constants.URL_GET_LINK_PLAY_PROD);
            UzRestClientHeartBeat.init(Constants.URL_HEART_BEAT_PROD);
            initTracking(Constants.URL_TRACKING_PROD, Constants.TRACKING_ACCESS_TOKEN_PROD);
        } else {
            return false;
        }
        return true;
    }

    /**
     * Because the time from client is un-trust for calculating the HLS latency, so get & save the UTC time from
     * <a href=http://worldtimeapi.org/api/timezone/Etc/UTC>this free api</a>
     */
    private void syncCurrentUTCTime() {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        final long startAPICallTime = System.currentTimeMillis();
        UzApiMaster.getInstance().subscribe(service.getCurrentUTCTime(), new ApiSubscriber<UtcTime>() {
            @Override
            public void onSuccess(UtcTime result) {
                long apiTime = (System.currentTimeMillis() - startAPICallTime) / 2;
                long currentTime = result.getCurrentDateTimeMs() + apiTime;
                Log.i(TAG, "sync server time success " + currentTime);
                UZUtil.saveLastServerTime(UzCoreUtil.getContext(), currentTime);
                UZUtil.saveLastElapsedTime(UzCoreUtil.getContext(), SystemClock.elapsedRealtime());
            }

            @Override
            public void onFail(Throwable e) {
                Log.e(TAG, "sync server time failed");
                UZUtil.saveLastServerTime(UzCoreUtil.getContext(), System.currentTimeMillis());
                UZUtil.saveLastElapsedTime(UzCoreUtil.getContext(), SystemClock.elapsedRealtime());
            }
        });
    }

    public String getAPIVersion() {
        return "v" + mAPIVersion;
    }

    public String getDomainAPI() {
        return mDomainAPI;
    }

    public String getmDomainAPITracking() {
        return mDomainAPITracking;
    }

    public String getToken() {
        return mToken;
    }

    public String getAppId() {
        return mAppId;
    }

    private void initTracking(String domainAPITracking, String accessToken) {
        mDomainAPITracking = domainAPITracking;
        UzRestClientTracking.init(domainAPITracking);
        UzRestClientTracking.addAccessToken(accessToken);
        UZUtil.setApiTrackEndPoint(UzCoreUtil.getContext(), domainAPITracking);
    }

    private UZInput uzInput;

    public UZInput getUzInput() {
        return uzInput;
    }

    public void setUizaInput(UZInput uzInput) {
        this.uzInput = uzInput;
    }

    public void clearUizaInput() {
        this.uzInput = null;
    }

    public boolean isLivestream() {
        if (uzInput == null) {
            return false;
        }
        return uzInput.isLivestream();
    }

    public String getEntityId() {
        if (uzInput == null) {
            return null;
        }
        if (uzInput.getData() == null) {
            return null;
        }
        return uzInput.getData().getId();
    }

    public String getEntityName() {
        if (uzInput == null || uzInput.getData() == null) {
            return "";
        }
        return uzInput.getData().getName();
    }

    public String getThumbnail() {
        if (uzInput == null || uzInput.getData() == null) {
            return null;
        }
        return uzInput.getData().getThumbnail();
    }

    public String getChannelName() {
        if (uzInput == null || uzInput.getData() == null) {
            return null;
        }
        return uzInput.getData().getChannelName();
    }

    public String getUrlIMAAd() {
        if (uzInput == null) {
            return null;
        }
        return uzInput.getUrlIMAAd();
    }

    public String getUrlThumnailsPreviewSeekbar() {
        if (uzInput == null) {
            return null;
        }
        return uzInput.getUrlThumnailsPreviewSeekbar();
    }

    public String getLastFeedId() {
        if (uzInput == null || uzInput.getData() == null) {
            return null;
        }
        return uzInput.getData().getLastFeedId();
    }

    public StreamingToken getStreamingToken() {
        if (uzInput == null) {
            return null;
        }
        return uzInput.getStreamingToken();
    }

    public LinkPlay getLinkPlay() {
        if (uzInput == null) {
            return null;
        }
        return uzInput.getLinkPlay();
    }

    public void setLinkPlay(LinkPlay linkPlay) {
        if (uzInput == null) {
            return;
        }
        uzInput.setLinkPlay(linkPlay);
    }

    //==================================================================================================================START TRACKING
    public UizaTracking createTrackingInput(Context context, String eventType) {
        return createTrackingInput(context, "0", eventType);
    }

    public UizaTracking createTrackingInput(Context context, String playThrough, String eventType) {
        if (context == null) {
            return null;
        }
        UizaTracking.Builder builder = new UizaTracking.Builder();
        builder.appId(getAppId()).pageType(PAGE_TYPE)
                .viewerUserId(UZOsUtil.getDeviceId(context)).userAgent(Constants.USER_AGENT)
                .referrer(TmpParamData.getInstance().getReferrer())
                .deviceId(UZOsUtil.getDeviceId(context))
                .timestamp(UzDateTimeUtil.getCurrent(UzDateTimeUtil.FORMAT_1))
                .playerId(currentPlayerId + "").playerName(Constants.PLAYER_NAME)
                .playerVersion(Constants.PLAYER_SDK_VERSION);
        if (uzInput == null || uzInput.getData() == null) {
            builder.entityId(NULL).entityName(NULL);
        } else {
            builder.entityId(uzInput.getData().getId())
                    .entityName(uzInput.getData().getName());
        }
        builder.entitySeries(TmpParamData.getInstance().getEntitySeries())
                .entityProducer(TmpParamData.getInstance().getEntityProducer())
                .entityContentType(TmpParamData.getInstance().getEntityContentType())
                .entityLanguageCode(TmpParamData.getInstance().getEntityLanguageCode())
                .entityVariantName(TmpParamData.getInstance().getEntityVariantName())
                .entityVariantId(TmpParamData.getInstance().getEntityVariantId())
                .entityDuration(TmpParamData.getInstance().getEntityDuration())
                .entityStreamType(TmpParamData.getInstance().getEntityStreamType())
                .entityEncodingVariant(TmpParamData.getInstance().getEntityEncodingVariant())
                .entityCdn(TmpParamData.getInstance().getEntityCnd()).playThrough(playThrough)
                .eventType(eventType);
        return builder.build();
    }

    private List<Muiza> muizaList = new ArrayList<>();

    public List<Muiza> getMuizaList() {
        return muizaList;
    }

    public boolean isMuizaListEmpty() {
        return muizaList.isEmpty();
    }

    public void clearMuizaList() {
        muizaList.clear();
    }

    public void addListTrackingMuiza(List<Muiza> muizas) {
        this.muizaList.addAll(muizas);
    }

    public void addTrackingMuiza(Context context, String event) {
        addTrackingMuiza(context, event, null);
    }

    public void addTrackingMuiza(Context context, String event, UzException e) {
        if (context == null || event == null || event.isEmpty()) {
            return;
        }
        TmpParamData.getInstance().addPlayerSequenceNumber();
        TmpParamData.getInstance().addViewSequenceNumber();
        Muiza.Builder muizaBuilder = new Muiza.Builder();
        muizaBuilder.beaconDomain(mDomainAPITracking)
                .entityCdn(TmpParamData.getInstance().getEntityCnd())
                .entityContentType(TmpParamData.getInstance().getEntityContentType())
                .entityDuration(TmpParamData.getInstance().getEntityDuration())
                .entityEncodingVariant(TmpParamData.getInstance().getEntityEncodingVariant())
                .entityLanguageCode(TmpParamData.getInstance().getEntityLanguageCode());
        if (uzInput == null || uzInput.getData() == null) {
            muizaBuilder.entityId(NULL).entityName(NULL);
        } else {
            muizaBuilder.entityId(uzInput.getData().getId())
                    .entityName(uzInput.getData().getName());
        }
        muizaBuilder.entityPosterUrl(TmpParamData.getInstance().getEntityPosterUrl())
                .entityProducer(TmpParamData.getInstance().getEntityProducer())
                .entitySeries(TmpParamData.getInstance().getEntitySeries())
                .entitySourceDomain(TmpParamData.getInstance().getEntitySourceDomain())
                .entitySourceDuration(TmpParamData.getInstance().getEntitySourceDuration())
                .entitySourceHeight(TmpParamData.getInstance().getEntitySourceHeight())
                .entitySourceHostname(TmpParamData.getInstance().getEntitySourceHostname())
                .entitySourceIsLive(isLivestream())
                .entitySourceMimeType(TmpParamData.getInstance().getEntitySourceMimeType())
                .entitySourceUrl(TmpParamData.getInstance().getEntitySourceUrl())
                .entitySourceWidth(TmpParamData.getInstance().getEntitySourceWidth())
                .entityStreamType(TmpParamData.getInstance().getEntityStreamType())
                .entityVariantId(TmpParamData.getInstance().getEntityVariantId())
                .entityVariantName(TmpParamData.getInstance().getEntityVariantName())
                .pageType(PAGE_TYPE)
                .pageUrl(TmpParamData.getInstance().getPageUrl())
                .playerAutoplayOn(TmpParamData.getInstance().isPlayerAutoplayOn())
                .playerHeight(TmpParamData.getInstance().getPlayerHeight())
                .playerIsFullscreen(TmpParamData.getInstance().isPlayerIsFullscreen())
                .playerIsPaused(TmpParamData.getInstance().isPlayerIsPaused())
                .playerLanguageCode(TmpParamData.getInstance().getPlayerLanguageCode())
                .playerName(Constants.PLAYER_NAME)
                .playerPlayheadTime(TmpParamData.getInstance().getPlayerPlayheadTime())
                .playerPreloadOn(TmpParamData.getInstance().getPlayerPreloadOn())
                .playerSequenceNumber(TmpParamData.getInstance().getPlayerSequenceNumber())
                .playerSoftwareName(TmpParamData.getInstance().getPlayerSoftwareName())
                .playerSoftwareVersion(TmpParamData.getInstance().getPlayerSoftwareVersion())
                .playerVersion(Constants.PLAYER_SDK_VERSION)
                .playerWidth(TmpParamData.getInstance().getPlayerWidth())
                .sessionExpires(System.currentTimeMillis() + 5 * 60 * 1000)
                .sessionId(TmpParamData.getInstance().getSessionId())
                .timestamp(UzDateTimeUtil.getCurrent(UzDateTimeUtil.FORMAT_1))
                .viewId(UZOsUtil.getDeviceId(context))
                .viewSequenceNumber(TmpParamData.getInstance().getViewSequenceNumber())
                .viewerApplicationEngine(TmpParamData.getInstance().getViewerApplicationEngine())
                .viewerApplicationName(TmpParamData.getInstance().getViewerApplicationName())
                .viewerApplicationVersion(TmpParamData.getInstance().getViewerApplicationVersion())
                .viewerDeviceManufacturer(android.os.Build.MANUFACTURER)
                .viewerDeviceName(android.os.Build.MODEL)
                .viewerOsArchitecture(UZOsUtil.getViewerOsArchitecture())
                .viewerOsFamily(ANDROID + Build.VERSION.RELEASE)
                .viewerOsVersion(API_LEVEL + Build.VERSION.SDK_INT)
                .viewerTime(System.currentTimeMillis())
                .viewerUserId(UZOsUtil.getDeviceId(context))
                .appId(getAppId())
                .referrer(TmpParamData.getInstance().getReferrer())
                .pageLoadTime(TmpParamData.getInstance().getPageLoadTime())
                .playerId(String.valueOf(currentPlayerId))
                .playerInitTime(TmpParamData.getInstance().getPlayerInitTime())
                .playerStartupTime(TmpParamData.getInstance().getPlayerStartupTime())
                .sessionStart(TmpParamData.getInstance().getSessionStart())
                .playerViewCount(TmpParamData.getInstance().getPlayerViewCount())
                .viewStart(TmpParamData.getInstance().getViewStart())
                .viewWatchTime(TmpParamData.getInstance().getViewWatchTime())
                .viewTimeToFirstFrame(TmpParamData.getInstance().getViewTimeToFirstFrame())
                .viewAggregateStartupTime(TmpParamData.getInstance().getViewStart()
                        + TmpParamData.getInstance().getViewWatchTime())
                .viewAggregateStartupTotalTime(TmpParamData.getInstance().getViewTimeToFirstFrame()
                        + (TmpParamData.getInstance().getPlayerInitTime()
                        - TmpParamData.getInstance().getTimeFromInitEntityIdToAllApiCalledSuccess()))
                .event(event);
        switch (event) {
            case Constants.MUIZA_EVENT_ERROR:
                if (e != null) {
                    muizaBuilder.playerErrorCode(e.getErrorCode())
                            .playerErrorMessage(e.getMessage());
                }
                break;
            case Constants.MUIZA_EVENT_SEEKING:
            case Constants.MUIZA_EVENT_SEEKED:
                muizaBuilder.viewSeekCount(TmpParamData.getInstance().getViewSeekCount())
                        .viewSeekDuration(TmpParamData.getInstance().getViewSeekDuration())
                        .viewMaxSeekTime(TmpParamData.getInstance().getViewMaxSeekTime());
                break;
            case Constants.MUIZA_EVENT_REBUFFERSTART:
            case Constants.MUIZA_EVENT_REBUFFEREND:
                muizaBuilder.viewRebufferCount(TmpParamData.getInstance().getViewRebufferCount())
                        .viewRebufferDuration(TmpParamData.getInstance().getViewRebufferDuration());
                if (TmpParamData.getInstance().getViewWatchTime() == 0) {
                    muizaBuilder.viewRebufferFrequency(0f).viewRebufferPercentage(0f);
                } else {
                    muizaBuilder.viewRebufferFrequency(
                            ((float) TmpParamData.getInstance().getViewRebufferCount()
                                    / (float) TmpParamData.getInstance().getViewWatchTime()))
                            .viewRebufferPercentage(
                                    ((float) TmpParamData.getInstance().getViewRebufferDuration()
                                            / (float) TmpParamData.getInstance()
                                            .getViewWatchTime()));
                }
                break;
        }
        muizaList.add(muizaBuilder.build());
    }
    //==================================================================================================================END TRACKING

    private boolean isSettingPlayer;

    public boolean isSettingPlayer() {
        return isSettingPlayer;
    }

    public void setSettingPlayer(boolean settingPlayer) {
        isSettingPlayer = settingPlayer;
    }

    //start singleton data if play playlist folder
    private List<VideoData> dataList;
    private int currentPositionOfDataList = 0;

    /**
     * true neu playlist folder
     * tra ve false neu play entity
     */
    public boolean isPlayWithPlaylistFolder() {
        return dataList != null;
    }

    public void setDataList(List<VideoData> dataList) {
        this.dataList = dataList;
    }

    public List<VideoData> getDataList() {
        return dataList;
    }

    public int getCurrentPositionOfDataList() {
        return currentPositionOfDataList;
    }

    public void setCurrentPositionOfDataList(int currentPositionOfDataList) {
        this.currentPositionOfDataList = currentPositionOfDataList;
    }

    public VideoData getDataWithPositionOfDataList(int position) {
        if (dataList == null || dataList.isEmpty() || dataList.get(position) == null) {
            return null;
        }
        return dataList.get(position);
    }

    public void clearDataForPlaylistFolder() {
        dataList = null;
        currentPositionOfDataList = 0;
    }
    //end singleton data if play playlist folder

    public VideoData getData() {
        if (uzInput == null) {
            return null;
        }
        return uzInput.getData();
    }

    private boolean isUseWithVDHView;

    public boolean isUseWithVDHView() {
        return isUseWithVDHView;
    }

    public void setUseWithVDHView(boolean isUseWithVDHView) {
        this.isUseWithVDHView = isUseWithVDHView;
    }
}
