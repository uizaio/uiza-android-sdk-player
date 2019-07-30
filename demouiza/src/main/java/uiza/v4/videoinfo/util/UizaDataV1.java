package uiza.v4.videoinfo.util;

import android.content.Context;
import android.content.pm.ResolveInfo;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import uizacoresdk.R;
import uizacoresdk.util.UZOsUtil;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LDateUtils;
import vn.uiza.core.utilities.LLog;
import vn.uiza.restapi.uiza.model.tracking.UizaTracking;
import vn.uiza.restapi.uiza.model.v2.auth.Auth;

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

    public UizaInputV1 getUizaInputPrev() {
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
    }

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
        uizaTracking.setViewerUserId("");
        //user_agent
        uizaTracking.setUserAgent(Constants.USER_AGENT);
        //referrer
        uizaTracking.setReferrer("");
        //device_id
        uizaTracking.setDeviceId(UZOsUtil.getDeviceId(context));
        //timestamp
        uizaTracking.setTimestamp(LDateUtils.getCurrent(LDateUtils.FORMAT_1));
        //uizaTracking.setTimestamp("2018-01-11T07:46:06.176Z");
        //player_id
        uizaTracking.setPlayerId(currentPlayerId + "");
        uizaTracking.setPlayerName("UizaAndroidSDKV3");
        uizaTracking.setPlayerVersion("1.0.3");
        //entity_id
        uizaTracking.setEntityId(uizaInputV1 == null ? "null" : uizaInputV1.getEntityId());
        //entity_name
        uizaTracking.setEntityName(uizaInputV1 == null ? "null" : uizaInputV1.getEntityName());
        uizaTracking.setEntitySeries("");
        uizaTracking.setEntityProducer("");
        uizaTracking.setEntityContentType("video");
        uizaTracking.setEntityLanguageCode("");
        uizaTracking.setEntityVariantName("");
        uizaTracking.setEntityVariantId("");
        uizaTracking.setEntityDuration("0");
        uizaTracking.setEntityStreamType("on-demand");
        uizaTracking.setEntityEncodingVariant("");
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
