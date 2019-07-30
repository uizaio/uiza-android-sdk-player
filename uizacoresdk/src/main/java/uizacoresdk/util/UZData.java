package uizacoresdk.util;

import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import uizacoresdk.R;
import uizacoresdk.chromecast.Casty;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LDateUtils;
import vn.uiza.core.utilities.LLog;
import vn.uiza.restapi.UZAPIMaster;
import vn.uiza.restapi.restclient.UZRestClient;
import vn.uiza.restapi.restclient.UZRestClientGetLinkPlay;
import vn.uiza.restapi.restclient.UZRestClientHeartBeat;
import vn.uiza.restapi.restclient.UZRestClientTracking;
import vn.uiza.restapi.uiza.UZService;
import vn.uiza.restapi.uiza.model.UTCTime;
import vn.uiza.restapi.uiza.model.tracking.UizaTracking;
import vn.uiza.restapi.uiza.model.tracking.muiza.Muiza;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming.ResultGetTokenStreaming;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.rxandroid.ApiSubscriber;
import vn.uiza.utils.util.Utils;

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
        UZRestClient.init(Constants.PREFIXS + domainAPI, token);
        UZUtil.setToken(Utils.getContext(), token);
        syncCurrentUTCTime();// for synchronize server time
        if (environment == Constants.ENVIRONMENT_DEV) {
            UZRestClientGetLinkPlay.init(Constants.URL_GET_LINK_PLAY_DEV);
            UZRestClientHeartBeat.init(Constants.URL_HEART_BEAT_DEV);
            initTracking(Constants.URL_TRACKING_DEV, Constants.TRACKING_ACCESS_TOKEN_DEV);
        } else if (environment == Constants.ENVIRONMENT_STAG) {
            UZRestClientGetLinkPlay.init(Constants.URL_GET_LINK_PLAY_STAG);
            UZRestClientHeartBeat.init(Constants.URL_HEART_BEAT_STAG);
            initTracking(Constants.URL_TRACKING_STAG, Constants.TRACKING_ACCESS_TOKEN_STAG);
        } else if (environment == Constants.ENVIRONMENT_PROD) {
            UZRestClientGetLinkPlay.init(Constants.URL_GET_LINK_PLAY_PROD);
            UZRestClientHeartBeat.init(Constants.URL_HEART_BEAT_PROD);
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
        UZService service = UZRestClient.createService(UZService.class);
        final long startAPICallTime = System.currentTimeMillis();
        UZAPIMaster.getInstance().subscribe(service.getCurrentUTCTime(), new ApiSubscriber<UTCTime>() {
            @Override
            public void onSuccess(UTCTime result) {
                long apiTime = (System.currentTimeMillis() - startAPICallTime) / 2;
                long currentTime = result.getCurrentDateTimeMs() + apiTime;
                Log.i(TAG, "sync server time success " + currentTime);
                UZUtil.saveLastServerTime(Utils.getContext(), currentTime);
                UZUtil.saveLastElapsedTime(Utils.getContext(), SystemClock.elapsedRealtime());
            }

            @Override
            public void onFail(Throwable e) {
                Log.e(TAG, "sync server time failed");
                UZUtil.saveLastServerTime(Utils.getContext(), System.currentTimeMillis());
                UZUtil.saveLastElapsedTime(Utils.getContext(), SystemClock.elapsedRealtime());
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
        UZRestClientTracking.init(domainAPITracking);
        UZRestClientTracking.addAccessToken(accessToken);
        UZUtil.setApiTrackEndPoint(Utils.getContext(), domainAPITracking);
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

    public ResultGetTokenStreaming getResultGetTokenStreaming() {
        if (uzInput == null) {
            return null;
        }
        return uzInput.getResultGetTokenStreaming();
    }

    public ResultGetLinkPlay getResultGetLinkPlay() {
        if (uzInput == null) {
            return null;
        }
        return uzInput.getResultGetLinkPlay();
    }

    public void setResultGetLinkPlay(ResultGetLinkPlay resultGetLinkPlay) {
        uzInput.setResultGetLinkPlay(resultGetLinkPlay);
    }

    //==================================================================================================================START TRACKING
    public UizaTracking createTrackingInput(Context context, String eventType) {
        return createTrackingInput(context, "0", eventType);
    }

    public UizaTracking createTrackingInput(Context context, String playThrough, String eventType) {
        if (context == null) {
            return null;
        }
        UizaTracking uizaTracking = new UizaTracking();
        uizaTracking.setAppId(getAppId());
        uizaTracking.setPageType(PAGE_TYPE);
        uizaTracking.setViewerUserId(UZOsUtil.getDeviceId(context));
        uizaTracking.setUserAgent(Constants.USER_AGENT);
        uizaTracking.setReferrer(TmpParamData.getInstance().getReferrer());
        uizaTracking.setDeviceId(UZOsUtil.getDeviceId(context));
        //timestamp
        uizaTracking.setTimestamp(LDateUtils.getCurrent(LDateUtils.FORMAT_1));
        //uizaTracking.setTimestamp("2018-01-11T07:46:06.176Z");
        uizaTracking.setPlayerId(currentPlayerId + "");
        uizaTracking.setPlayerName(Constants.PLAYER_NAME);
        uizaTracking.setPlayerVersion(Constants.PLAYER_SDK_VERSION);
        //entity_id, entity_name
        if (uzInput == null || uzInput.getData() == null) {
            uizaTracking.setEntityId(NULL);
            uizaTracking.setEntityName(NULL);
        } else {
            uizaTracking.setEntityId(uzInput.getData().getId());
            uizaTracking.setEntityName(uzInput.getData().getName());
        }
        uizaTracking.setEntitySeries(TmpParamData.getInstance().getEntitySeries());
        uizaTracking.setEntityProducer(TmpParamData.getInstance().getEntityProducer());
        uizaTracking.setEntityContentType(TmpParamData.getInstance().getEntityContentType());
        uizaTracking.setEntityLanguageCode(TmpParamData.getInstance().getEntityLanguageCode());
        uizaTracking.setEntityVariantName(TmpParamData.getInstance().getEntityVariantName());
        uizaTracking.setEntityVariantId(TmpParamData.getInstance().getEntityVariantId());
        uizaTracking.setEntityDuration(TmpParamData.getInstance().getEntityDuration());
        uizaTracking.setEntityStreamType(TmpParamData.getInstance().getEntityStreamType());
        uizaTracking.setEntityEncodingVariant(TmpParamData.getInstance().getEntityEncodingVariant());
        uizaTracking.setEntityCdn(TmpParamData.getInstance().getEntityCnd());
        uizaTracking.setPlayThrough(playThrough);
        uizaTracking.setEventType(eventType);
        return uizaTracking;
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

    public void addTrackingMuiza(Context context, String event, UZException e) {
        if (context == null || event == null || event.isEmpty()) {
            return;
        }
        TmpParamData.getInstance().addPlayerSequenceNumber();
        TmpParamData.getInstance().addViewSequenceNumber();
        Muiza muiza = new Muiza();
        muiza.setBeaconDomain(mDomainAPITracking);
        muiza.setEntityCdn(TmpParamData.getInstance().getEntityCnd());
        muiza.setEntityContentType(TmpParamData.getInstance().getEntityContentType());
        muiza.setEntityDuration(TmpParamData.getInstance().getEntityDuration());
        muiza.setEntityEncodingVariant(TmpParamData.getInstance().getEntityEncodingVariant());
        muiza.setEntityLanguageCode(TmpParamData.getInstance().getEntityLanguageCode());
        if (uzInput == null || uzInput.getData() == null) {
            muiza.setEntityId(NULL);
            muiza.setEntityName(NULL);
        } else {
            muiza.setEntityId(uzInput.getData().getId());
            muiza.setEntityName(uzInput.getData().getName());
        }
        muiza.setEntityPosterUrl(TmpParamData.getInstance().getEntityPosterUrl());
        muiza.setEntityProducer(TmpParamData.getInstance().getEntityProducer());
        muiza.setEntitySeries(TmpParamData.getInstance().getEntitySeries());
        muiza.setEntitySourceDomain(TmpParamData.getInstance().getEntitySourceDomain());
        muiza.setEntitySourceDuration(TmpParamData.getInstance().getEntitySourceDuration());
        muiza.setEntitySourceHeight(TmpParamData.getInstance().getEntitySourceHeight());
        muiza.setEntitySourceHostname(TmpParamData.getInstance().getEntitySourceHostname());
        muiza.setEntitySourceIsLive(isLivestream());
        muiza.setEntitySourceMimeType(TmpParamData.getInstance().getEntitySourceMimeType());
        muiza.setEntitySourceUrl(TmpParamData.getInstance().getEntitySourceUrl());
        muiza.setEntitySourceWidth(TmpParamData.getInstance().getEntitySourceWidth());
        muiza.setEntityStreamType(TmpParamData.getInstance().getEntityStreamType());
        muiza.setEntityVariantId(TmpParamData.getInstance().getEntityVariantId());
        muiza.setEntityVariantName(TmpParamData.getInstance().getEntityVariantName());
        muiza.setPageType(PAGE_TYPE);
        muiza.setPageUrl(TmpParamData.getInstance().getPageUrl());
        muiza.setPlayerAutoplayOn(TmpParamData.getInstance().isPlayerAutoplayOn());
        muiza.setPlayerHeight(TmpParamData.getInstance().getPlayerHeight());
        muiza.setPlayerIsFullscreen(TmpParamData.getInstance().isPlayerIsFullscreen());
        muiza.setPlayerIsPaused(TmpParamData.getInstance().isPlayerIsPaused());
        muiza.setPlayerLanguageCode(TmpParamData.getInstance().getPlayerLanguageCode());
        muiza.setPlayerName(Constants.PLAYER_NAME);
        muiza.setPlayerPlayheadTime(TmpParamData.getInstance().getPlayerPlayheadTime());
        muiza.setPlayerPreloadOn(TmpParamData.getInstance().getPlayerPreloadOn());
        muiza.setPlayerSequenceNumber(TmpParamData.getInstance().getPlayerSequenceNumber());
        muiza.setPlayerSoftwareName(TmpParamData.getInstance().getPlayerSoftwareName());
        muiza.setPlayerSoftwareVersion(TmpParamData.getInstance().getPlayerSoftwareVersion());
        muiza.setPlayerVersion(Constants.PLAYER_SDK_VERSION);
        muiza.setPlayerWidth(TmpParamData.getInstance().getPlayerWidth());
        muiza.setSessionExpires(System.currentTimeMillis() + 5 * 60 * 1000);
        muiza.setSessionId(TmpParamData.getInstance().getSessionId());
        muiza.setTimestamp(LDateUtils.getCurrent(LDateUtils.FORMAT_1));
        //muiza.setTimestamp("2018-01-11T07:46:06.176Z");
        muiza.setViewId(UZOsUtil.getDeviceId(context));
        muiza.setViewSequenceNumber(TmpParamData.getInstance().getViewSequenceNumber());
        muiza.setViewerApplicationEngine(TmpParamData.getInstance().getViewerApplicationEngine());
        muiza.setViewerApplicationName(TmpParamData.getInstance().getViewerApplicationName());
        muiza.setViewerApplicationVersion(TmpParamData.getInstance().getViewerApplicationVersion());
        muiza.setViewerDeviceManufacturer(android.os.Build.MANUFACTURER);
        muiza.setViewerDeviceName(android.os.Build.MODEL);
        muiza.setViewerOsArchitecture(UZOsUtil.getViewerOsArchitecture());
        muiza.setViewerOsFamily(ANDROID + Build.VERSION.RELEASE);
        muiza.setViewerOsVersion(API_LEVEL + Build.VERSION.SDK_INT);
        muiza.setViewerTime(System.currentTimeMillis());
        muiza.setViewerUserId(UZOsUtil.getDeviceId(context));
        muiza.setAppId(getAppId());
        muiza.setReferrer(TmpParamData.getInstance().getReferrer());
        muiza.setPageLoadTime(TmpParamData.getInstance().getPageLoadTime());
        muiza.setPlayerId(String.valueOf(currentPlayerId));
        muiza.setPlayerInitTime(TmpParamData.getInstance().getPlayerInitTime());
        muiza.setPlayerStartupTime(TmpParamData.getInstance().getPlayerStartupTime());
        muiza.setSessionStart(TmpParamData.getInstance().getSessionStart());
        muiza.setPlayerViewCount(TmpParamData.getInstance().getPlayerViewCount());
        muiza.setViewStart(TmpParamData.getInstance().getViewStart());
        muiza.setViewWatchTime(TmpParamData.getInstance().getViewWatchTime());
        muiza.setViewTimeToFirstFrame(TmpParamData.getInstance().getViewTimeToFirstFrame());
        muiza.setViewAggregateStartupTime(TmpParamData.getInstance().getViewStart() + TmpParamData.getInstance().getViewWatchTime());
        muiza.setViewAggregateStartupTotalTime(TmpParamData.getInstance().getViewTimeToFirstFrame() + (TmpParamData.getInstance().getPlayerInitTime() - TmpParamData.getInstance().getTimeFromInitEntityIdToAllApiCalledSuccess()));
        muiza.setEvent(event);
        switch (event) {
            case Constants.MUIZA_EVENT_ERROR:
                if (e != null) {
                    muiza.setPlayerErrorCode(e.getErrorCode());
                    muiza.setPlayerErrorMessage(e.getMessage());
                }
                break;
            case Constants.MUIZA_EVENT_SEEKING:
            case Constants.MUIZA_EVENT_SEEKED:
                muiza.setViewSeekCount(TmpParamData.getInstance().getViewSeekCount());
                muiza.setViewSeekDuration(TmpParamData.getInstance().getViewSeekDuration());
                muiza.setViewMaxSeekTime(TmpParamData.getInstance().getViewMaxSeekTime());
                break;
            case Constants.MUIZA_EVENT_REBUFFERSTART:
            case Constants.MUIZA_EVENT_REBUFFEREND:
                muiza.setViewRebufferCount(TmpParamData.getInstance().getViewRebufferCount());
                muiza.setViewRebufferDuration(TmpParamData.getInstance().getViewRebufferDuration());
                if (TmpParamData.getInstance().getViewWatchTime() == 0) {
                    muiza.setViewRebufferFrequency(0);
                    muiza.setViewRebufferPercentage(0);
                } else {
                    muiza.setViewRebufferFrequency(((float) TmpParamData.getInstance().getViewRebufferCount() / (float) TmpParamData.getInstance().getViewWatchTime()));
                    muiza.setViewRebufferPercentage(((float) TmpParamData.getInstance().getViewRebufferDuration() / (float) TmpParamData.getInstance().getViewWatchTime()));
                }
                break;
        }
        muizaList.add(muiza);
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
    private List<Data> dataList;
    private int currentPositionOfDataList = 0;

    /**
     * true neu playlist folder
     * tra ve false neu play entity
     */
    public boolean isPlayWithPlaylistFolder() {
        return dataList != null;
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public int getCurrentPositionOfDataList() {
        return currentPositionOfDataList;
    }

    public void setCurrentPositionOfDataList(int currentPositionOfDataList) {
        this.currentPositionOfDataList = currentPositionOfDataList;
    }

    public Data getDataWithPositionOfDataList(int position) {
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

    public Data getData() {
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
