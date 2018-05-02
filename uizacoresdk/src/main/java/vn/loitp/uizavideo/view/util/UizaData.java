package vn.loitp.uizavideo.view.util;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.provider.Settings;

import com.google.gson.Gson;

import java.util.List;

import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LDateUtils;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
import vn.loitp.restapi.uiza.model.tracking.UizaTracking;
import vn.loitp.restapi.uiza.model.v2.auth.Auth;

/**
 * Created by LENOVO on 4/28/2018.
 */

public class UizaData {
    private final String TAG = getClass().getSimpleName();
    private static final UizaData ourInstance = new UizaData();

    public static UizaData getInstance() {
        return ourInstance;
    }

    private UizaData() {
    }

    private String playerId = "";//id of player skin
    private String entityId = "";
    private String entityName = "";
    private String entityCover = "";
    private String urlIMAAd = "";
    private String urlThumnailsPreviewSeekbar = "";

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityCover() {
        return entityCover;
    }

    public void setEntityCover(String entityCover) {
        this.entityCover = entityCover;
    }

    public String getUrlIMAAd() {
        return urlIMAAd;
    }

    public void setUrlIMAAd(String urlIMAAd) {
        this.urlIMAAd = urlIMAAd;
    }

    public String getUrlThumnailsPreviewSeekbar() {
        return urlThumnailsPreviewSeekbar;
    }

    public void setUrlThumnailsPreviewSeekbar(String urlThumnailsPreviewSeekbar) {
        this.urlThumnailsPreviewSeekbar = urlThumnailsPreviewSeekbar;
    }

    public void clear() {
        playerId = "";
        entityId = "";
        entityName = "";
        entityCover = "";
    }

    public static final String EVENT_TYPE_DISPLAY = "display";
    public static final String EVENT_TYPE_PLAYS_REQUESTED = "plays_requested";
    public static final String EVENT_TYPE_VIDEO_STARTS = "video_starts";
    public static final String EVENT_TYPE_VIEW = "view";
    public static final String EVENT_TYPE_REPLAY = "replay";
    public static final String EVENT_TYPE_PLAY_THROUGHT = "play_through";

    public UizaTracking createTrackingInput(Context context, String eventType) {
        return createTrackingInput(context, "0", eventType);
    }

    //playerId id of skin
    public UizaTracking createTrackingInput(Context context, String playThrough, String eventType) {
        UizaTracking uizaTracking = new UizaTracking();
        //app_id
        Auth auth = LPref.getAuth(context, new Gson());
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
        uizaTracking.setPlayerId(playerId);
        //TODO player_name
        uizaTracking.setPlayerName("UizaAndroidSDKV2");
        //TODO player_version
        uizaTracking.setPlayerVersion("1.0.2");
        //entity_id
        uizaTracking.setEntityId(entityId);
        //entity_name
        uizaTracking.setEntityName(entityName);
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
}
