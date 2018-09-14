package vn.uiza.uzv3.util;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.provider.Settings;

import com.google.android.gms.cast.MediaTrack;

import java.util.ArrayList;
import java.util.List;

import loitp.core.R;
import vn.uiza.chromecast.Casty;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LDateUtils;
import vn.uiza.core.utilities.LLog;
import vn.uiza.restapi.restclient.RestClientTracking;
import vn.uiza.restapi.restclient.RestClientV3;
import vn.uiza.restapi.restclient.RestClientV3GetLinkPlay;
import vn.uiza.restapi.uiza.model.tracking.UizaTracking;
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

    private int currentPlayerId = R.layout.player_skin_1;

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
        UZUtil.setSlideUizaVideoEnabled(Utils.getContext(), true);
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
            throw new NullPointerException("You must init Casty with acitivy before using Chromecast. Tips: put 'UZData.getInstance().setCasty(Casty.create(this));' to your onStart() or onCreate()");
        }
        return casty;
    }

    public void initSDK(String domainAPI, String token, String appId, int environment) {
        mDomainAPI = domainAPI;
        mToken = token;
        mAppId = appId;

        RestClientV3.init(Constants.PREFIXS + domainAPI, token);
        UZUtil.setToken(Utils.getContext(), token);

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
        UZUtil.setApiTrackEndPoint(Utils.getContext(), domainAPITracking);
    }

    private List<UZInput> UZInputList = new ArrayList<>();

    public List<UZInput> getUZInputList() {
        return UZInputList;
    }

    /*public void setUizaInputList(List<UizaInputV1> UZInputList) {
        this.UZInputList = UZInputList;
    }*/

    public UZInput getUizaInputPrev() {
        //LLog.d(TAG, "getUizaInputPrev " + UZInputList.size());
        if (UZInputList.isEmpty() || UZInputList.size() <= 1) {
            return null;
        } else {
            UZInput UZInput = UZInputList.get(UZInputList.size() - 2);//-1: current, -2 previous item;
            UZInputList.remove(UZInputList.size() - 1);
            return UZInput;
        }
    }

    private UZInput UZInput;

    public UZInput getUZInput() {
        return UZInput;
    }

    private boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed;

    public boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed() {
        return isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed;
    }

    public void setUizaInput(UZInput UZInput) {
        setUizaInput(UZInput, false);
    }

    public void setUizaInput(UZInput UZInput, boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed) {
        this.UZInput = UZInput;
        this.isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed = isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed;

        //add new uiza input to last position
        //remove the first item
        //UZInputList is always have 2 item everytime

        int existAt = Constants.NOT_FOUND;
        for (int i = 0; i < UZInputList.size(); i++) {
            if (UZInput == null || UZInput.getData() == null || UZInput.getData().getId() == null ||
                    UZInputList.get(i) == null || UZInputList.get(i).getData() == null || UZInputList.get(i).getData().getId() == null
                    ) {
                continue;
            }
            if (UZInput.getData().getId().equals(UZInputList.get(i).getData().getId())) {
                existAt = i;
                break;
            }
        }
        //LLog.d(TAG, "setUizaInput existAt " + existAt);
        if (existAt != Constants.NOT_FOUND) {
            UZInputList.remove(existAt);
        }
        UZInputList.add(UZInput);
        if (UZInputList.size() > 2) {
            UZInputList.remove(0);
        }
        /*if (Constants.IS_DEBUG) {
            String x = "";
            for (UZInput u : UZInputList) {
                if (u != null && u.getData() != null && u.getData().getEntityName() != null) {
                    x += " > " + u.getData().getEntityName();
                }
            }
            LLog.d(TAG, "setUizaInput " + x);
        }*/
    }

    public boolean isLivestream() {
        if (UZInput == null) {
            return false;
        }
        return UZInput.isLivestream();
    }

    public String getEntityId() {
        if (UZInput == null) {
            if (data == null) {
                return null;
            }
            return data.getId();
        }
        return UZInput.getData().getId();
    }

    public String getEntityName() {
        if (UZInput == null || UZInput.getData() == null) {
            return " - ";
        }
        return UZInput.getData().getName();
    }

    public String getThumbnail() {
        /*if (UZInput == null) {
            if (data != null && getData().getThumbnail() != null) {
                return getData().getThumbnail();
            }
            return null;
        }
        return UZInput.getData().getThumbnail();*/
        if (data == null || data.getThumbnail() == null) {
            return null;
        }
        return data.getThumbnail();
    }

    public String getChannelName() {
        if (UZInput == null || UZInput.getData() == null) {
            return null;
        }
        return UZInput.getData().getChannelName();
    }

    public String getUrlIMAAd() {
        if (UZInput == null) {
            return null;
        }
        return UZInput.getUrlIMAAd();
    }

    public String getUrlThumnailsPreviewSeekbar() {
        if (UZInput == null) {
            return null;
        }
        return UZInput.getUrlThumnailsPreviewSeekbar();
    }

    public String getLastFeedId() {
        if (UZInput == null || UZInput.getData() == null) {
            return null;
        }
        return UZInput.getData().getLastFeedId();
    }


    /*public void removeLastUizaInput() {
        if (UZInputList != null && !UZInputList.isEmpty()) {
            UZInputList.remove(UZInputList.size() - 1);
        }
    }*/

    public void clearUizaInputList() {
        UZInput = null;
        UZInputList.clear();
    }

    public UizaTracking createTrackingInputV3(Context context, String eventType) {
        return createTrackingInputV3(context, "0", eventType);
    }

    //playerId id of skin
    public UizaTracking createTrackingInputV3(Context context, String playThrough, String eventType) {
        UizaTracking uizaTracking = new UizaTracking();
        //app_id
        uizaTracking.setAppId(UZData.getInstance().getAppId());
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
        uizaTracking.setPlayerId(currentPlayerId + "");
        //TODO player_name
        uizaTracking.setPlayerName("UizaAndroidSDKV3");
        //TODO player_version
        uizaTracking.setPlayerVersion("1.0.3 V3");
        //entity_id
        uizaTracking.setEntityId(UZInput == null ? "null" : UZInput.getData().getId());
        //entity_name
        uizaTracking.setEntityName(UZInput == null ? "null" : UZInput.getData().getName());
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
        /*if (Constants.IS_DEBUG) {
            Gson gson = new Gson();
            LLog.d(TAG, "createTrackingInput " + gson.toJson(uizaTracking));
        }*/
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
}
