package io.uiza.player.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import io.uiza.core.api.request.tracking.muiza.Muiza;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.streaming.StreamingToken;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.model.UzLinkPlayData;
import io.uiza.core.util.SharedPreferenceUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.player.R;
import io.uiza.player.analytic.muiza.MuizaEvent;
import io.uiza.player.analytic.muiza.MuizaTrackingManager;
import io.uiza.player.cast.Casty;
import java.util.List;

public class UzPlayerData {

    private static final String PREFERENCES_FILE_NAME = "UzData";
    private static final String API_TRACK_END_POINT = "API_TRACK_END_POINT";
    private static final String TOKEN = "TOKEN";
    private static final String IS_INIT_PLAYLIST_FOLDER = "IS_INIT_PLAYLIST_FOLDER";
    private static final String LAST_ELAPSED_TIME = "LAST_ELAPSED_TIME";
    private static final String LAST_SYNCED_SERVER_TIME = "LAST_SYNCED_SERVER_TIME";

    private static final UzPlayerData ourInstance = new UzPlayerData();
    private int apiVersion = Constants.API_VERSION_4;
    private String apiDomain = "";
    private String trackingDomain = "";
    private String apiToken = "";
    private String appId = "";
    private Casty casty;
    private UzLinkPlayData uzLinkPlayData;
    private boolean useDraggableLayout;
    private int currentSkinRes = R.layout.uz_player_skin_1; //id of layout xml
    private String playerInfoId; //player id from workspace uiza
    private MuizaTrackingManager muizaTrackingManager = new MuizaTrackingManager();
    private boolean isSettingPlayer;
    private List<VideoData> playlistData;
    private int positionInPlaylist = 0;

    public static UzPlayerData getInstance() {
        return ourInstance;
    }

    private UzPlayerData() {
    }

    public int getCurrentSkinRes() {
        return currentSkinRes;
    }

    public void setCurrentSkinRes(int currentSkinRes) {
        this.currentSkinRes = currentSkinRes;
    }

    public String getPlayerInfoId() {
        return playerInfoId;
    }

    public void setPlayerInfoId(String playerInfoId) {
        this.playerInfoId = playerInfoId;
    }

    public void setCasty(Casty casty) {
        this.casty = casty;
    }

    public Casty getCasty() {
        return casty;
    }

    public void setApiVersion(int apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getApiVersion() {
        return "v" + apiVersion;
    }

    public void setApiDomain(String apiDomain) {
        this.apiDomain = apiDomain;
    }

    public String getApiDomain() {
        return apiDomain;
    }

    public void setTrackingDomain(String trackingDomain) {
        this.trackingDomain = trackingDomain;
    }

    public String getTrackingDomain() {
        return trackingDomain;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    public void initTracking(String trackingDomain, String accessToken) {
        setTrackingDomain(trackingDomain);
        muizaTrackingManager.initTracking(trackingDomain, accessToken);
    }

    public UzLinkPlayData getUzLinkPlayData() {
        return uzLinkPlayData;
    }

    public void setUzLinkPlayData(UzLinkPlayData uzLinkPlayData) {
        this.uzLinkPlayData = uzLinkPlayData;
    }

    public void clearUzLinkPlayData() {
        this.uzLinkPlayData = null;
    }

    public boolean isLivestream() {
        if (uzLinkPlayData == null) {
            return false;
        }
        return uzLinkPlayData.isLivestream();
    }

    public String getEntityId() {
        if (uzLinkPlayData == null) {
            return null;
        }
        if (uzLinkPlayData.getVideoData() == null) {
            return null;
        }
        return uzLinkPlayData.getVideoData().getId();
    }

    public String getEntityName() {
        if (uzLinkPlayData == null || uzLinkPlayData.getVideoData() == null) {
            return "";
        }
        return uzLinkPlayData.getVideoData().getName();
    }

    public String getThumbnail() {
        if (uzLinkPlayData == null || uzLinkPlayData.getVideoData() == null) {
            return null;
        }
        return uzLinkPlayData.getVideoData().getThumbnail();
    }

    public String getChannelName() {
        if (uzLinkPlayData == null || uzLinkPlayData.getVideoData() == null) {
            return null;
        }
        return uzLinkPlayData.getVideoData().getChannelName();
    }

    public String getImaAdUrl() {
        if (uzLinkPlayData == null) {
            return null;
        }
        return uzLinkPlayData.getImaAdUrl();
    }

    public String getUrlThumnailsPreviewSeekbar() {
        if (uzLinkPlayData == null) {
            return null;
        }
        return uzLinkPlayData.getSeekbarPreviewThumbUrl();
    }

    public String getLastFeedId() {
        if (uzLinkPlayData == null || uzLinkPlayData.getVideoData() == null) {
            return null;
        }
        return uzLinkPlayData.getVideoData().getLastFeedId();
    }

    public StreamingToken getStreamingToken() {
        if (uzLinkPlayData == null) {
            return null;
        }
        return uzLinkPlayData.getStreamingToken();
    }

    public LinkPlay getLinkPlay() {
        if (uzLinkPlayData == null) {
            return null;
        }
        return uzLinkPlayData.getLinkPlay();
    }

    public void setLinkPlay(LinkPlay linkPlay) {
        if (uzLinkPlayData == null) {
            return;
        }
        uzLinkPlayData.setLinkPlay(linkPlay);
    }

    public List<Muiza> getMuizaList() {
        return muizaTrackingManager.getMuizaList();
    }

    public boolean isMuizaListEmpty() {
        return muizaTrackingManager.isMuizaListEmpty();
    }

    public void clearMuizaList() {
        muizaTrackingManager.clearMuizaList();
    }

    public void addListTrackingMuiza(List<Muiza> muizas) {
        muizaTrackingManager.addListTrackingMuiza(muizas);
    }

    public void addTrackingMuiza(Context context, @MuizaEvent String event) {
        addTrackingMuiza(context, event, null);
    }

    public void addTrackingMuiza(Context context, @MuizaEvent String event, UzException e) {
        muizaTrackingManager.addTrackingMuiza(context, event, e);
    }

    public boolean isSettingPlayer() {
        return isSettingPlayer;
    }

    public void setSettingPlayer(boolean settingPlayer) {
        isSettingPlayer = settingPlayer;
    }

    public boolean isPlayWithPlaylistFolder() {
        return playlistData != null;
    }

    public void setPlaylistData(List<VideoData> playlistData) {
        this.playlistData = playlistData;
    }

    public List<VideoData> getPlaylistData() {
        return playlistData;
    }

    public int getPositionInPlaylist() {
        return positionInPlaylist;
    }

    public void setPositionInPlaylist(int positionInPlaylist) {
        this.positionInPlaylist = positionInPlaylist;
    }

    public VideoData getDataWithPositionOfDataList(int position) {
        if (playlistData == null || playlistData.isEmpty() || playlistData.get(position) == null) {
            return null;
        }
        return playlistData.get(position);
    }

    public void clearDataForPlaylistFolder() {
        playlistData = null;
        positionInPlaylist = 0;
    }

    public VideoData getVideoData() {
        if (uzLinkPlayData == null) {
            return null;
        }
        return uzLinkPlayData.getVideoData();
    }

    public boolean isUseDraggableLayout() {
        return useDraggableLayout;
    }

    public void setUseDraggableLayout(boolean value) {
        this.useDraggableLayout = value;
    }

    private static SharedPreferences getPrivatePreference(Context context) {
        return context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
    }

    public static String getApiTrackEndPoint(Context context) {
        return (String) SharedPreferenceUtil
                .get(getPrivatePreference(context), API_TRACK_END_POINT, "");
    }

    public static void setApiTrackEndPoint(Context context, String value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), API_TRACK_END_POINT, value);
    }

    public static String getToken(Context context) {
        return (String) SharedPreferenceUtil.get(getPrivatePreference(context), TOKEN, "");
    }

    public static void setToken(Context context, String value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), TOKEN, value);
    }

    public static Boolean isInitPlaylistFolder(Context context) {
        return (Boolean) SharedPreferenceUtil
                .get(getPrivatePreference(context), IS_INIT_PLAYLIST_FOLDER, false);
    }

    public static void setIsInitPlaylistFolder(Context context, Boolean value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), IS_INIT_PLAYLIST_FOLDER, value);
    }

    public static void saveLastServerTime(Context context, long currentTimeMillis) {
        SharedPreferenceUtil.put(getPrivatePreference(context), LAST_SYNCED_SERVER_TIME,
                currentTimeMillis);
    }

    public static long getLastServerTime(Context context) {
        return (long) SharedPreferenceUtil.get(getPrivatePreference(context),
                LAST_SYNCED_SERVER_TIME, System.currentTimeMillis());
    }

    public static void saveLastElapsedTime(Context context, long elapsedTime) {
        SharedPreferenceUtil.put(getPrivatePreference(context), LAST_ELAPSED_TIME, elapsedTime);
    }

    public static long getLastElapsedTime(Context context) {
        return (long) SharedPreferenceUtil.get(getPrivatePreference(context), LAST_ELAPSED_TIME,
                SystemClock.elapsedRealtime());
    }


}
