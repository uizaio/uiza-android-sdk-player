package vn.uiza.uzv1.view.util;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.provider.Settings;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import loitp.core.R;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LDateUtils;
import vn.uiza.core.utilities.LLog;
import vn.uiza.restapi.uiza.model.tracking.UizaTracking;
import vn.uiza.restapi.uiza.model.v2.auth.Auth;
import vn.uiza.uzv3.util.UZUtil;

/**
 * Created by loitp on 4/28/2018.
 */

public class UizaDataV1 {
    private final String TAG = getClass().getSimpleName();
    private static final UizaDataV1 ourInstance = new UizaDataV1();

    public static UizaDataV1 getInstance() {
        return ourInstance;
    }

    private UizaDataV1() {
    }

    private int currentPlayerId = R.layout.uz_player_skin_1;

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    private List<UizaInputV1> uizaInputV1List = new ArrayList<>();

    public List<UizaInputV1> getUizaInputV1List() {
        return uizaInputV1List;
    }

    /*public void setUizaInputList(List<UizaInputV1> uizaInputV1List) {
        this.uizaInputV1List = uizaInputV1List;
    }*/

    public UizaInputV1 getUizaInputPrev() {
        //LLog.d(TAG, "getUizaInputPrev " + uizaInputV1List.size());
        if (uizaInputV1List.isEmpty() || uizaInputV1List.size() <= 1) {
            return null;
        } else {
            UizaInputV1 uizaInputV1 = uizaInputV1List.get(uizaInputV1List.size() - 2);//-1: current, -2 previous item;
            uizaInputV1List.remove(uizaInputV1List.size() - 1);
            return uizaInputV1;
        }
    }

    private UizaInputV1 uizaInputV1;

    public UizaInputV1 getUizaInputV1() {
        return uizaInputV1;
    }

    private boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed;

    public boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed() {
        return isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed;
    }

    public void setUizaInput(UizaInputV1 uizaInputV1, boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed) {
        this.uizaInputV1 = uizaInputV1;
        this.isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed = isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed;

        //add new uiza input to last position
        //remove the first item
        //uizaInputV1List is always have 2 item everytime

        int existAt = Constants.NOT_FOUND;
        for (int i = 0; i < uizaInputV1List.size(); i++) {
            if (uizaInputV1.getEntityId().equals(uizaInputV1List.get(i).getEntityId())) {
                existAt = i;
                break;
            }
        }
        if (existAt != Constants.NOT_FOUND) {
            uizaInputV1List.remove(existAt);
        }
        uizaInputV1List.add(uizaInputV1);
        if (uizaInputV1List.size() > 2) {
            uizaInputV1List.remove(0);
        }
        /*if (Constants.IS_DEBUG) {
            String x = "";
            for (UizaInputV1 u : uizaInputV1List) {
                x += " > " + u.getEntityName();
            }
        }*/
    }

    /*public void removeLastUizaInput() {
        if (uizaInputV1List != null && !uizaInputV1List.isEmpty()) {
            uizaInputV1List.remove(uizaInputV1List.size() - 1);
        }
    }*/

    public void clear() {
        uizaInputV1 = null;
        uizaInputV1List.clear();
    }

    public UizaTracking createTrackingInput(Context context, String eventType) {
        return createTrackingInput(context, "0", eventType);
    }

    //playerId id of skin
    public UizaTracking createTrackingInput(Context context, String playThrough, String eventType) {
        UizaTracking uizaTracking = new UizaTracking();
        //app_id
        Gson gson = new Gson();
        Auth auth = UZUtil.getAuth(context, gson);
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
        uizaTracking.setEntityId(uizaInputV1 == null ? "null" : uizaInputV1.getEntityId());
        //entity_name
        uizaTracking.setEntityName(uizaInputV1 == null ? "null" : uizaInputV1.getEntityName());
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
        uizaTracking.setEntityId(uizaInputV1 == null ? "null" : uizaInputV1.getEntityId());
        //entity_name
        uizaTracking.setEntityName(uizaInputV1 == null ? "null" : uizaInputV1.getEntityName());
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
