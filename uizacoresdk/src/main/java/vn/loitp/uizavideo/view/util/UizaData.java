package vn.loitp.uizavideo.view.util;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.provider.Settings;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LDateUtils;
import vn.loitp.core.utilities.LLog;
import vn.loitp.restapi.uiza.model.tracking.UizaTracking;
import vn.loitp.restapi.uiza.model.v2.auth.Auth;
import vn.loitp.uizavideov3.util.UizaUtil;

/**
 * Created by loitp on 4/28/2018.
 */

public class UizaData {
    private final String TAG = getClass().getSimpleName();
    private static final UizaData ourInstance = new UizaData();

    public static UizaData getInstance() {
        return ourInstance;
    }

    private UizaData() {
    }

    private int currentPlayerId = Constants.PLAYER_ID_SKIN_0;

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    private List<UizaInput> uizaInputList = new ArrayList<>();

    public List<UizaInput> getUizaInputList() {
        return uizaInputList;
    }

    /*public void setUizaInputList(List<UizaInput> uizaInputList) {
        this.uizaInputList = uizaInputList;
    }*/

    public UizaInput getUizaInputPrev() {
        //LLog.d(TAG, "getUizaInputPrev " + uizaInputList.size());
        if (uizaInputList.isEmpty() || uizaInputList.size() <= 1) {
            return null;
        } else {
            UizaInput uizaInput = uizaInputList.get(uizaInputList.size() - 2);//-1: current, -2 previous item;
            uizaInputList.remove(uizaInputList.size() - 1);
            return uizaInput;
        }
    }

    private UizaInput uizaInput;

    public UizaInput getUizaInput() {
        return uizaInput;
    }

    private boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed;

    public boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed() {
        return isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed;
    }

    public void setUizaInput(UizaInput uizaInput, boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed) {
        this.uizaInput = uizaInput;
        this.isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed = isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed;

        //add new uiza input to last position
        //remove the first item
        //uizaInputList is always have 2 item everytime

        int existAt = Constants.NOT_FOUND;
        for (int i = 0; i < uizaInputList.size(); i++) {
            if (uizaInput.getEntityId().equals(uizaInputList.get(i).getEntityId())) {
                existAt = i;
                break;
            }
        }
        if (existAt != Constants.NOT_FOUND) {
            uizaInputList.remove(existAt);
        }
        uizaInputList.add(uizaInput);
        if (uizaInputList.size() > 2) {
            uizaInputList.remove(0);
        }
        /*if (Constants.IS_DEBUG) {
            String x = "";
            for (UizaInput u : uizaInputList) {
                x += " > " + u.getEntityName();
            }
        }*/
    }

    /*public void removeLastUizaInput() {
        if (uizaInputList != null && !uizaInputList.isEmpty()) {
            uizaInputList.remove(uizaInputList.size() - 1);
        }
    }*/

    public void clear() {
        uizaInput = null;
        uizaInputList.clear();
    }

    public UizaTracking createTrackingInput(Context context, String eventType) {
        return createTrackingInput(context, "0", eventType);
    }

    //playerId id of skin
    public UizaTracking createTrackingInput(Context context, String playThrough, String eventType) {
        UizaTracking uizaTracking = new UizaTracking();
        //app_id
        Gson gson = new Gson();
        Auth auth = UizaUtil.getAuth(context, gson);
        if (auth != null) {
            uizaTracking.setAppId(auth.getData().getAppId());
        }
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
        uizaTracking.setPlayerVersion("1.0.3");
        //entity_id
        uizaTracking.setEntityId(uizaInput == null ? "null" : uizaInput.getEntityId());
        //entity_name
        uizaTracking.setEntityName(uizaInput == null ? "null" : uizaInput.getEntityName());
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
            LLog.d(TAG, "createTrackingInput " + gson.toJson(uizaTracking));
        }
        return uizaTracking;
    }

    /*public UizaTracking createTrackingInputV3(Context context, String eventType) {
        return createTrackingInputV3(context, "0", eventType);
    }*/

    //playerId id of skin
    /*public UizaTracking createTrackingInputV3(Context context, String playThrough, String eventType) {
        UizaTracking uizaTracking = new UizaTracking();
        //app_id
        ResultGetToken resultGetToken = UizaPref.getResultGetToken(context);
        uizaTracking.setAppId(resultGetToken.getData().getAppId());
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
        uizaTracking.setEntityId(uizaInput == null ? "null" : uizaInput.getEntityId());
        //entity_name
        uizaTracking.setEntityName(uizaInput == null ? "null" : uizaInput.getEntityName());
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
    }*/

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
}
