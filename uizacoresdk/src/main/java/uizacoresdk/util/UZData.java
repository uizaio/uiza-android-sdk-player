package uizacoresdk.util;

import android.content.Context;
import android.provider.Settings;

import com.google.android.gms.cast.MediaTrack;

import java.util.ArrayList;
import java.util.List;

import uizacoresdk.R;
import uizacoresdk.chromecast.Casty;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LDateUtils;
import vn.uiza.core.utilities.LLog;
import vn.uiza.restapi.restclient.UZRestClient;
import vn.uiza.restapi.restclient.UZRestClientGetLinkPlay;
import vn.uiza.restapi.restclient.UZRestClientHeartBeat;
import vn.uiza.restapi.restclient.UZRestClientTracking;
import vn.uiza.restapi.uiza.model.tracking.UizaTracking;
import vn.uiza.restapi.uiza.model.tracking.muiza.Muiza;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming.ResultGetTokenStreaming;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.utils.util.Utils;

/**
 * Created by loitp on 4/28/2018.
 */

public class UZData {
    private final String TAG = getClass().getSimpleName();
    private static final UZData ourInstance = new UZData();

    public static UZData getInstance() {
        return ourInstance;
    }

    private UZData() {
    }

    private int currentPlayerId = R.layout.uz_player_skin_1;

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    private String mDomainAPI;
    private String mDomainAPITracking;
    private String mToken;
    private String mAppId;

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

    public void initSDK(String domainAPI, String token, String appId, int environment) {
        mDomainAPI = domainAPI;
        mToken = token;
        mAppId = appId;

        UZRestClient.init(Constants.PREFIXS + domainAPI, token);
        UZUtil.setToken(Utils.getContext(), token);

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
            throw new IllegalArgumentException("Please init correct environment.");
        }
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
            return " - ";
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
        UizaTracking uizaTracking = new UizaTracking();
        //app_id
        uizaTracking.setAppId(UZData.getInstance().getAppId());
        //page_type
        uizaTracking.setPageType("app");
        uizaTracking.setViewerUserId("");
        //user_agent
        uizaTracking.setUserAgent(Constants.USER_AGENT);
        //referrer
        uizaTracking.setReferrer("");
        //device_id
        uizaTracking.setDeviceId(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        //timestamp
        uizaTracking.setTimestamp(LDateUtils.getCurrent(LDateUtils.FORMAT_1));
        //uizaTracking.setTimestamp("2018-01-11T07:46:06.176Z");
        //player_id
        uizaTracking.setPlayerId(currentPlayerId + "");
        uizaTracking.setPlayerName(Constants.PLAYER_NAME);
        uizaTracking.setPlayerVersion(Constants.PLAYER_SDK_VERSION);
        //entity_id, entity_name
        if (uzInput == null || uzInput.getData() == null) {
            uizaTracking.setEntityId("null");
            uizaTracking.setEntityName("null");
        } else {
            uizaTracking.setEntityId(uzInput.getData().getId());
            uizaTracking.setEntityName(uzInput.getData().getName());
        }
        uizaTracking.setEntitySeries("");
        uizaTracking.setEntityProducer("");
        uizaTracking.setEntityContentType("video");
        uizaTracking.setEntityLanguageCode("");
        uizaTracking.setEntityVariantName("");
        uizaTracking.setEntityVariantId("");
        //TODO setEntityDuration
        uizaTracking.setEntityDuration("-");
        uizaTracking.setEntityStreamType("on-demand");
        uizaTracking.setEntityEncodingVariant("");
        uizaTracking.setEntityCdn("");
        uizaTracking.setPlayThrough(playThrough);
        uizaTracking.setEventType(eventType);
        return uizaTracking;
    }

    private List<Muiza> muizaList = new ArrayList<>();

    public void clearMuizaList() {
        muizaList.clear();
    }

    public void addTrackingMuiza(String event) {
        //TODO correct Muiza
        Muiza muiza = new Muiza();
        muiza.setEvent(event);
        muizaList.add(muiza);
    }

    public void printMuizaList() {
        if (!Constants.IS_DEBUG) {
            return;
        }
        LLog.d(TAG, "fuck------------------------------------");
        for (int i = 0; i < muizaList.size(); i++) {
            LLog.d(TAG, "fuck printMuizaList " + i + " -> " + muizaList.get(i).getEvent());
        }
        LLog.d(TAG, "fuck------------------------------------");
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
        int trackType = MediaTrack.TYPE_UNKNOWN;
        if ("text".equals(type)) {
            trackType = MediaTrack.TYPE_TEXT;
        } else if ("video".equals(type)) {
            trackType = MediaTrack.TYPE_VIDEO;
        } else if ("audio".equals(type)) {
            trackType = MediaTrack.TYPE_AUDIO;
        }

        int trackSubType = MediaTrack.SUBTYPE_NONE;
        if (subType != null) {
            if ("captions".equals(type)) {
                trackSubType = MediaTrack.SUBTYPE_CAPTIONS;
            } else if ("subtitle".equals(type)) {
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
    private List<Data> dataList;
    private int currentPositionOfDataList = 0;

    /*
     **true neu playlist folder
     * tra ve false neu play entity
     */
    public boolean isPlayWithPlaylistFolder() {
        if (dataList == null) {
            //LLog.d(TAG, "isPlayWithPlaylistFolder false");
            return false;
        }
        //LLog.d(TAG, "isPlayWithPlaylistFolder true");
        return true;
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
        /*LLog.d(TAG, "setDataList " + dataList.size());
        if (Constants.IS_DEBUG) {
            isPlayWithPlaylistFolder();
        }*/
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
        //LLog.d(TAG, "clearDataForPlaylistFolder");
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
}
