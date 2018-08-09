package vn.loitp.uizavideov3.util;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.provider.Settings;

import com.google.android.gms.cast.MediaTrack;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import vn.loitp.chromecast.Casty;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LDateUtils;
import vn.loitp.core.utilities.LLog;
import vn.loitp.restapi.restclient.RestClientTracking;
import vn.loitp.restapi.restclient.RestClientV3;
import vn.loitp.restapi.restclient.RestClientV3GetLinkPlay;
import vn.loitp.restapi.uiza.model.tracking.UizaTracking;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.loitp.utils.util.Utils;

/**
 * Created by loitp on 4/28/2018.
 */

public class UizaDataV3 {
    private final String TAG = getClass().getSimpleName();
    private static final UizaDataV3 ourInstance = new UizaDataV3();

    public static UizaDataV3 getInstance() {
        return ourInstance;
    }

    private UizaDataV3() {
    }

    private String currentPlayerId = Constants.PLAYER_ID_SKIN_1;

    public String getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(String currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
        UizaUtil.setSlideUizaVideoEnabled(Utils.getContext(), true);
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
            throw new NullPointerException("You must init Casty with acitivy before using Chromecast. Tips: put 'UizaDataV3.getInstance().setCasty(Casty.create(this));' to your onStart() or onCreate()");
        }
        return casty;
    }

    public void initSDK(String domainAPI, String token, String appId) {
        initSDK(domainAPI, token, appId, Constants.ENVIRONMENT_PROD);
    }

    public void initSDK(String domainAPI, String token, String appId, int environment) {
        mDomainAPI = domainAPI;
        mToken = token;
        mAppId = appId;

        RestClientV3.init(Constants.PREFIXS + domainAPI, token);
        UizaUtil.setToken(Utils.getContext(), token);

        if (environment == Constants.ENVIRONMENT_DEV) {
            RestClientV3GetLinkPlay.init(Constants.URL_GET_LINK_PLAY_DEV);
            initTracking(Constants.URL_TRACKING_DEV);
        } else if (environment == Constants.ENVIRONMENT_STAG) {
            RestClientV3GetLinkPlay.init(Constants.URL_GET_LINK_PLAY_STAG);
            initTracking(Constants.URL_TRACKING_STAG);
        } else if (environment == Constants.ENVIRONMENT_PROD) {
            RestClientV3GetLinkPlay.init(Constants.URL_GET_LINK_PLAY_PROD);
            initTracking(Constants.URL_TRACKING_PROD);
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

    private void initTracking(String domainAPITracking) {
        mDomainAPITracking = domainAPITracking;
        RestClientTracking.init(domainAPITracking);
        UizaUtil.setApiTrackEndPoint(Utils.getContext(), domainAPITracking);
    }

    private List<UizaInputV3> uizaInputV3List = new ArrayList<>();

    public List<UizaInputV3> getUizaInputV3List() {
        return uizaInputV3List;
    }

    /*public void setUizaInputList(List<UizaInput> uizaInputV3List) {
        this.uizaInputV3List = uizaInputV3List;
    }*/

    public UizaInputV3 getUizaInputPrev() {
        //LLog.d(TAG, "getUizaInputPrev " + uizaInputV3List.size());
        if (uizaInputV3List.isEmpty() || uizaInputV3List.size() <= 1) {
            return null;
        } else {
            UizaInputV3 uizaInputV3 = uizaInputV3List.get(uizaInputV3List.size() - 2);//-1: current, -2 previous item;
            uizaInputV3List.remove(uizaInputV3List.size() - 1);
            return uizaInputV3;
        }
    }

    private UizaInputV3 uizaInputV3;

    public UizaInputV3 getUizaInputV3() {
        return uizaInputV3;
    }

    private boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed;

    public boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed() {
        return isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed;
    }

    public void setUizaInput(UizaInputV3 uizaInputV3) {
        setUizaInput(uizaInputV3, false);
    }

    public void setUizaInput(UizaInputV3 uizaInputV3, boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed) {
        this.uizaInputV3 = uizaInputV3;
        this.isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed = isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed;

        //add new uiza input to last position
        //remove the first item
        //uizaInputV3List is always have 2 item everytime

        int existAt = Constants.NOT_FOUND;
        for (int i = 0; i < uizaInputV3List.size(); i++) {
            if (uizaInputV3.getData().getId().equals(uizaInputV3List.get(i).getData().getId())) {
                existAt = i;
                break;
            }
        }
        LLog.d(TAG, "setUizaInput existAt " + existAt);
        if (existAt != Constants.NOT_FOUND) {
            uizaInputV3List.remove(existAt);
        }
        uizaInputV3List.add(uizaInputV3);
        if (uizaInputV3List.size() > 2) {
            uizaInputV3List.remove(0);
        }
        if (Constants.IS_DEBUG) {
            String x = "";
            for (UizaInputV3 u : uizaInputV3List) {
                x += " > " + u.getData().getEntityName();
            }
            LLog.d(TAG, "setUizaInput " + x);
        }
    }

    public boolean isLivestream() {
        if (uizaInputV3 == null) {
            return false;
        }
        return uizaInputV3.isLivestream();
    }

    public String getEntityId() {
        if (uizaInputV3 == null || uizaInputV3.getData() == null) {
            return null;
        }
        return uizaInputV3.getData().getId();
    }

    public String getEntityName() {
        if (uizaInputV3 == null || uizaInputV3.getData() == null) {
            return " - ";
        }
        return uizaInputV3.getData().getName();
    }

    public String getThumbnail() {
        if (uizaInputV3 == null || uizaInputV3.getData() == null) {
            return null;
        }
        return uizaInputV3.getData().getThumbnail();
    }

    public String getChannelName() {
        if (uizaInputV3 == null || uizaInputV3.getData() == null) {
            return null;
        }
        return uizaInputV3.getData().getChannelName();
    }

    public String getUrlIMAAd() {
        if (uizaInputV3 == null) {
            return null;
        }
        return uizaInputV3.getUrlIMAAd();
    }

    public String getUrlThumnailsPreviewSeekbar() {
        if (uizaInputV3 == null) {
            return null;
        }
        return uizaInputV3.getUrlThumnailsPreviewSeekbar();
    }

    public String getLastFeedId() {
        if (uizaInputV3 == null || uizaInputV3.getData() == null) {
            return null;
        }
        return uizaInputV3.getData().getLastFeedId();
    }


    /*public void removeLastUizaInput() {
        if (uizaInputV3List != null && !uizaInputV3List.isEmpty()) {
            uizaInputV3List.remove(uizaInputV3List.size() - 1);
        }
    }*/

    public void clear() {
        uizaInputV3 = null;
        uizaInputV3List.clear();
    }

    public UizaTracking createTrackingInputV3(Context context, String eventType) {
        return createTrackingInputV3(context, "0", eventType);
    }

    //playerId id of skin
    public UizaTracking createTrackingInputV3(Context context, String playThrough, String eventType) {
        UizaTracking uizaTracking = new UizaTracking();
        //app_id
        uizaTracking.setAppId(UizaDataV3.getInstance().getAppId());
        //page_type
        uizaTracking.setPageType("app");
        //TODO viewer_user_id
        uizaTracking.setViewerUserId("");
        //user_agent
        uizaTracking.setUserAgent(context.getPackageName());
        //referrer
        uizaTracking.setReferrer("");
        //device_id
        uizaTracking.setDeviceId(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        //timestamp
        uizaTracking.setTimestamp(LDateUtils.getCurrent(LDateUtils.FORMAT_1));
        //uizaTracking.setTimestamp("2018-01-11T07:46:06.176Z");
        //player_id
        uizaTracking.setPlayerId(currentPlayerId);
        //TODO player_name
        uizaTracking.setPlayerName("UizaAndroidSDKV3");
        //TODO player_version
        uizaTracking.setPlayerVersion("1.0.3 V3");
        //entity_id
        uizaTracking.setEntityId(uizaInputV3 == null ? "null" : uizaInputV3.getData().getId());
        //entity_name
        uizaTracking.setEntityName(uizaInputV3 == null ? "null" : uizaInputV3.getData().getName());
        //TODO entity_series
        uizaTracking.setEntitySeries("");
        //TODO entity_producer
        uizaTracking.setEntityProducer("");
        //TODO entity_content_type
        uizaTracking.setEntityContentType("video");
        //TODO entity_language_code
        uizaTracking.setEntityLanguageCode("");
        //TODO entity_variant_name
        uizaTracking.setEntityVariantName("");
        //TODO entity_variant_id
        uizaTracking.setEntityVariantId("");
        //TODO entity_duration
        uizaTracking.setEntityDuration("0");
        //TODO entity_stream_type
        uizaTracking.setEntityStreamType("on-demand");
        //TODO entity_encoding_variant
        uizaTracking.setEntityEncodingVariant("");
        //TODO entity_cdn
        uizaTracking.setEntityCdn("");
        //play_through
        uizaTracking.setPlayThrough(playThrough);
        //event_type
        uizaTracking.setEventType(eventType);
        if (Constants.IS_DEBUG) {
            Gson gson = new Gson();
            LLog.d(TAG, "createTrackingInput " + gson.toJson(uizaTracking));
        }
        return uizaTracking;
    }

    //dialog share
    private List<ResolveInfo> resolveInfoList;

    public List<ResolveInfo> getResolveInfoList() {
        return resolveInfoList;
    }

    public void setResolveInfoList(List<ResolveInfo> resolveInfoList) {
        this.resolveInfoList = resolveInfoList;
    }
    //end dialog share

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

    //start singleton data if play entity
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void clearDataForEntity() {
        data = null;
    }
    //end singleton data if play entity

    //start singleton data if play playlist folder
    private List<Data> dataList;
    private int currentPositionOfDataList = 0;

    /*
    **Trả về true nếu playlist folder
    * Trả về flase nếu play entity
     */
    public boolean isPlayWithPlaylistFolder() {
        if (dataList == null) {
            return true;
        }
        return false;
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
}
