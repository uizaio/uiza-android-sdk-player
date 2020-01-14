package uizacoresdk.util;

import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;

import androidx.annotation.LayoutRes;

import com.google.android.gms.cast.MediaTrack;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;
import uizacoresdk.R;
import uizacoresdk.chromecast.Casty;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UizaException;
import vn.uiza.models.PlaybackInfo;
import vn.uiza.models.tracking.Muiza;
import vn.uiza.models.tracking.UizaTracking;
import vn.uiza.restapi.RxBinder;
import vn.uiza.restapi.UizaClientFactory;
import vn.uiza.restapi.UizaEventService;
import vn.uiza.utils.AppUtils;
import vn.uiza.utils.LDateUtils;
import vn.uiza.utils.LDeviceUtil;
import vn.uiza.utils.ListUtil;

/**
 * Created by loitp on 4/28/2018.
 */

public class UZData {
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

    @LayoutRes
    private int currentPlayerId = R.layout.uz_player_skin_1;//id of layout xml
    private String playerInforId;//player id from workspace uiza

    @LayoutRes
    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(@LayoutRes int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public String getPlayerInforId() {
        return playerInforId;
    }

    public void setPlayerInforId(String playerInforId) {
        this.playerInforId = playerInforId;
    }

    private String mDomainAPI = "";
    private String mDomainAPITracking = "";
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
            Timber.e("getCasty null");
            throw new NullPointerException("You must init Casty with acitivy before using Chromecast. Tips: put 'UZUtil.setCasty(this);' to your onStart() or onCreate()");
        }
        return casty;
    }

    /**
     * InitSDK
     *
     * @param context     Context
     * @param domainAPI   Base Url of API
     * @param appId       App Id
     * @param environment One if {@link Constants.ENVIRONMENT#DEV},
     *                    {@link Constants.ENVIRONMENT#STAG} or {@link Constants.ENVIRONMENT#PROD}
     * @return true if success init or false
     */
    public boolean initSDK(Context context, String domainAPI, String appId, int environment) {
        if (TextUtils.isEmpty(domainAPI)) {
            return false;
        }
        mAppId = appId;
        mDomainAPI = domainAPI;
        UizaClientFactory.setup(context, mDomainAPI, appId, environment);
        UZUtil.setAppId(appId);
        syncCurrentUTCTime();// for synchronize server time
        if (environment == Constants.ENVIRONMENT.DEV) {
            mDomainAPITracking = Constants.URL_TRACKING_DEV;
        } else if (environment == Constants.ENVIRONMENT.STAG) {
            mDomainAPITracking = Constants.URL_TRACKING_STAG;
        } else if (environment == Constants.ENVIRONMENT.PROD) {
            mDomainAPITracking = Constants.URL_TRACKING_PROD;
        } else {
            return false;
        }
        UZUtil.setApiTrackEndPoint(mDomainAPITracking);
        return true;
    }

    /**
     * Because the time from client is un-trust for calculating the HLS latency, so get & save the UTC time from
     * <a href=http://worldtimeapi.org/api/timezone/Etc/UTC>this free api</a>
     */
    private void syncCurrentUTCTime() {
        UizaEventService service = UizaClientFactory.getEventService();
        final long startAPICallTime = System.currentTimeMillis();
        RxBinder.bind(service.getCurrentUTCTime(), result -> {
            long apiTime = (System.currentTimeMillis() - startAPICallTime) / 2;
            long currentTime = result.getCurrentDateTimeMs() + apiTime;
            Timber.i("sync server time success :%d", currentTime);
            UZUtil.saveLastServerTime(currentTime);
            UZUtil.saveLastElapsedTime(SystemClock.elapsedRealtime());
        }, throwable -> {
            Timber.e("sync server time failed");
            UZUtil.saveLastServerTime(System.currentTimeMillis());
            UZUtil.saveLastElapsedTime(SystemClock.elapsedRealtime());
        });
    }

    public String getDomainAPI() {
        return mDomainAPI;
    }

    public String getmDomainAPITracking() {
        return mDomainAPITracking;
    }

    public String getAppId() {
        return mAppId;
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
        if (uzInput == null || uzInput.getPlaybackInfo() == null) {
            return null;
        }
        return uzInput.getPlaybackInfo().getId();
    }

    public String getEntityName() {
        if (uzInput == null || uzInput.getPlaybackInfo() == null) {
            return "";
        }
        return uzInput.getPlaybackInfo().getName();
    }

    public String getThumbnail() {
        if (uzInput == null || uzInput.getPlaybackInfo() == null) {
            return null;
        }
        return uzInput.getPlaybackInfo().getThumbnail();
    }

    public String getChannelName() {
        if (uzInput == null || uzInput.getPlaybackInfo() == null) {
            return null;
        }
        return uzInput.getPlaybackInfo().getChannelName();
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
        if (uzInput == null || uzInput.getPlaybackInfo() == null) {
            return null;
        }
        return uzInput.getPlaybackInfo().getLastFeedId();
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
        uizaTracking.setPageType(PAGE_TYPE);
        uizaTracking.setViewerUserId(LDeviceUtil.getDeviceId(context));
        uizaTracking.setUserAgent(Constants.USER_AGENT);
        uizaTracking.setReferrer(TmpParamData.getInstance().getReferrer());
        uizaTracking.setDeviceId(LDeviceUtil.getDeviceId(context));
        //timestamp
        uizaTracking.setTimestamp(LDateUtils.getCurrent(LDateUtils.FORMAT_1));
        //uizaTracking.setTimestamp("2018-01-11T07:46:06.176Z");
        uizaTracking.setPlayerId(currentPlayerId + "");
        uizaTracking.setPlayerName(Constants.PLAYER_NAME);
        uizaTracking.setPlayerVersion(Constants.PLAYER_SDK_VERSION);
        //entity_id, entity_name
        if (uzInput == null || uzInput.getPlaybackInfo() == null) {
            uizaTracking.setEntityId(NULL);
            uizaTracking.setEntityName(NULL);
        } else {
            uizaTracking.setEntityId(uzInput.getPlaybackInfo().getId());
            uizaTracking.setEntityName(uzInput.getPlaybackInfo().getName());
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

    public void addTrackingMuiza(Context context, String event, UizaException e) {
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
        if (uzInput == null || uzInput.getPlaybackInfo() == null) {
            muiza.setEntityId(NULL);
            muiza.setEntityName(NULL);
        } else {
            muiza.setEntityId(uzInput.getPlaybackInfo().getId());
            muiza.setEntityName(uzInput.getPlaybackInfo().getName());
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
        muiza.setViewId(LDeviceUtil.getDeviceId(context));
        muiza.setViewSequenceNumber(TmpParamData.getInstance().getViewSequenceNumber());
        muiza.setViewerApplicationEngine(TmpParamData.getInstance().getViewerApplicationEngine());
        muiza.setViewerApplicationName(TmpParamData.getInstance().getViewerApplicationName());
        muiza.setViewerApplicationVersion(TmpParamData.getInstance().getViewerApplicationVersion());
        muiza.setViewerDeviceManufacturer(android.os.Build.MANUFACTURER);
        muiza.setViewerDeviceName(android.os.Build.MODEL);
        muiza.setViewerOsArchitecture(LDeviceUtil.getViewerOsArchitecture());
        muiza.setViewerOsFamily(ANDROID + Build.VERSION.RELEASE);
        muiza.setViewerOsVersion(API_LEVEL + Build.VERSION.SDK_INT);
        muiza.setViewerTime(System.currentTimeMillis());
        muiza.setViewerUserId(LDeviceUtil.getDeviceId(context));
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

    public MediaTrack buildTrack(long id, String type, String subType, String contentId, String name, String language) {
        if (!AppUtils.checkChromeCastAvailable()) {
            throw new NoClassDefFoundError(UizaException.ERR_505);
        }
        int trackType = MediaTrack.TYPE_UNKNOWN;
        if (TEXT.equals(type)) {
            trackType = MediaTrack.TYPE_TEXT;
        } else if (VIDEO.equals(type)) {
            trackType = MediaTrack.TYPE_VIDEO;
        } else if (AUDIO.equals(type)) {
            trackType = MediaTrack.TYPE_AUDIO;
        }

        int trackSubType = MediaTrack.SUBTYPE_NONE;
        if (subType != null) {
            if (CAPTIONS.equals(type)) {
                trackSubType = MediaTrack.SUBTYPE_CAPTIONS;
            } else if (SUBTITLE.equals(type)) {
                trackSubType = MediaTrack.SUBTYPE_SUBTITLES;
            }
        }

        return new MediaTrack.Builder(id, trackType)
                .setName(name)
                .setSubtype(trackSubType)
                .setContentId(contentId)
                .setLanguage(language).build();
    }

    //start singleton data if play playlist folder
    private List<PlaybackInfo> playList;
    private int currentPositionOfPlayList = 0;

    /**
     * true neu playlist folder
     * tra ve false neu play entity
     */
    public boolean isPlayWithPlaylistFolder() {
        return playList != null;
    }

    public void setPlayList(List<PlaybackInfo> playlist) {
        this.playList = playlist;
    }

    public List<PlaybackInfo> getPlayList() {
        return playList;
    }

    public int getCurrentPositionOfPlayList() {
        return currentPositionOfPlayList;
    }

    public void setCurrentPositionOfPlayList(int currentPositionOfPlayList) {
        this.currentPositionOfPlayList = currentPositionOfPlayList;
    }

    public PlaybackInfo getDataWithPositionOfPlayList(int position) {
        if (ListUtil.isEmpty(playList)) {
            return null;
        }
        return playList.get(position);
    }

    public void clearDataForPlaylistFolder() {
        playList = null;
        currentPositionOfPlayList = 0;
    }
    //end singleton data if play playlist folder

    public PlaybackInfo getPlaybackInfo() {
        if (uzInput == null) {
            return null;
        }
        return uzInput.getPlaybackInfo();
    }

    private boolean isUseWithVDHView;

    public boolean isUseWithVDHView() {
        return isUseWithVDHView;
    }

    public void setUseWithVDHView(boolean isUseWithVDHView) {
        this.isUseWithVDHView = isUseWithVDHView;
    }
}
