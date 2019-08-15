package io.uiza.player;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.view.View;
import io.uiza.core.api.UzApiMaster;
import io.uiza.core.api.UzServiceApi;
import io.uiza.core.api.client.UzRestClient;
import io.uiza.core.api.client.UzRestClientGetLinkPlay;
import io.uiza.core.api.client.UzRestClientHeartBeat;
import io.uiza.core.api.response.UtcTime;
import io.uiza.core.api.util.ApiSubscriber;
import io.uiza.core.exception.UzException;
import io.uiza.core.model.CustomLinkPlay;
import io.uiza.core.model.UzCustomLinkPlayData;
import io.uiza.core.util.LLog;
import io.uiza.core.util.UzCommonUtil;
import io.uiza.core.util.UzCoreUtil;
import io.uiza.core.util.connection.UzConnectivityUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.player.cast.Casty;
import io.uiza.player.mini.pip.PipHelper;
import io.uiza.player.util.UzPlayerData;

public class UzPlayerConfig {

    public static final String TAG = UzPlayerConfig.class.getSimpleName();

    /**
     * Init the workspace for playing vod or livestream video.
     *
     * @param context the context
     * @param apiVersion the api version
     * @param apiDomain the api domain url
     * @param token the token
     * @param appId the app id
     * @param env the environment of workspace (pro, stag...)
     * @param playerSkinRes the player skin of {@link UzPlayer}
     * @return true if init workspace success, otherwise false
     */
    public static boolean initWorkspace(Context context, int apiVersion, String apiDomain,
            String token, String appId, int env, int playerSkinRes) {
        if (context == null) {
            throw new NullPointerException(UzException.ERR_15);
        }
        if (apiDomain == null || apiDomain.isEmpty()) {
            throw new NullPointerException(UzException.ERR_16);
        }
        if (token == null || token.isEmpty()) {
            throw new NullPointerException(UzException.ERR_17);
        }
        if (appId == null || appId.isEmpty()) {
            throw new NullPointerException(UzException.ERR_18);
        }
        if (!UzCommonUtil.isExoDependencyAvailable()) {
            throw new NoClassDefFoundError(UzException.ERR_504);
        }

        UzCoreUtil.init(context.getApplicationContext());
        setCurrentSkinRes(playerSkinRes);
        return initUzSdk(apiVersion, apiDomain, token, appId, env);
    }

    /**
     * Init Uiza SDK.
     *
     * @param apiVersion the api version
     * @param apiDomain the api domain url
     * @param token the token
     * @param appId the app id
     * @param environment the environment of workspace (pro, stag...)
     * @return true if init Uiza SDK success, otherwise false
     */
    public static boolean initUzSdk(int apiVersion, String apiDomain, String token, String appId,
            int environment) {
        if (apiDomain == null || apiDomain.isEmpty() || apiDomain.contains(" ")
                || token == null || token.isEmpty() || token.contains(" ")
                || appId == null || appId.isEmpty() || appId.contains(" ")) {
            return false;
        }
        UzPlayerData data = UzPlayerData.getInstance();
        data.setApiVersion(apiVersion);
        data.setApiDomain(apiDomain);
        data.setApiToken(token);
        data.setAppId(appId);
        UzRestClient.init(Constants.PREFIXS + apiDomain, token);
        UzPlayerData.setToken(UzCoreUtil.getContext(), token);
        syncCurrentUtcTime();// for synchronize server time
        if (environment == Constants.ENVIRONMENT_DEV) {
            UzRestClientGetLinkPlay.init(Constants.URL_GET_LINK_PLAY_DEV);
            UzRestClientHeartBeat.init(Constants.URL_HEART_BEAT_DEV);
            data.initTracking(Constants.URL_TRACKING_DEV, Constants.TRACKING_ACCESS_TOKEN_DEV);
        } else if (environment == Constants.ENVIRONMENT_STAG) {
            UzRestClientGetLinkPlay.init(Constants.URL_GET_LINK_PLAY_STAG);
            UzRestClientHeartBeat.init(Constants.URL_HEART_BEAT_STAG);
            data.initTracking(Constants.URL_TRACKING_STAG, Constants.TRACKING_ACCESS_TOKEN_STAG);
        } else if (environment == Constants.ENVIRONMENT_PROD) {
            UzRestClientGetLinkPlay.init(Constants.URL_GET_LINK_PLAY_PROD);
            UzRestClientHeartBeat.init(Constants.URL_HEART_BEAT_PROD);
            data.initTracking(Constants.URL_TRACKING_PROD, Constants.TRACKING_ACCESS_TOKEN_PROD);
        } else {
            return false;
        }
        return true;
    }

    public static void initWorkspace(Context context, int apiVersion, String apiDomain,
            String token, String appId, int playerSkinRes) {
        initWorkspace(context, apiVersion, apiDomain, token, appId, Constants.ENVIRONMENT_PROD,
                playerSkinRes);
    }

    public static void initWorkspace(Context context, int apiVersion, String apiDomain,
            String token, String appId) {
        initWorkspace(context, apiVersion, apiDomain, token, appId, Constants.ENVIRONMENT_PROD,
                R.layout.uz_player_skin_1);
    }

    /**
     * Init to play a provided video link.
     *
     * @param uzPlayer the {@link UzPlayer} instance.
     * @param linkPlay the {@link CustomLinkPlay} instance.
     */
    public static void initCustomLinkPlay(UzPlayer uzPlayer, CustomLinkPlay linkPlay) {
        if (uzPlayer == null) {
            throw new NullPointerException(UzException.ERR_13);
        }
        if (linkPlay == null) {
            LLog.e(TAG, UzException.ERR_14);
            return;
        }
        Context context = uzPlayer.getContext();
        if (!UzConnectivityUtil.isConnected(context)) {
            LLog.e(TAG, UzException.ERR_0);
            return;
        }
        if (PipHelper.getClickedPip(context)) {
            LLog.d(TAG, "miniplayer STEP 6 initLinkPlay");
            playCustomLinkPlay(uzPlayer, linkPlay);
        } else {
            PipHelper.stopMiniPlayer(context);
            playCustomLinkPlay(uzPlayer, linkPlay);
        }
        UzPlayerData.setIsInitPlaylistFolder(context, false);
    }

    private static void playCustomLinkPlay(final UzPlayer uzVideo, final CustomLinkPlay linkPlay) {
        UzPlayerData.getInstance().setSettingPlayer(false);
        uzVideo.post(new Runnable() {
            @Override
            public void run() {
                uzVideo.initCustomLinkPlay(linkPlay.getLinkPlay(), linkPlay.isLivestream());
            }
        });
    }

    /**
     * Init to play the livestream video.
     *
     * @param uzPlayer the {@link UzPlayer} instance.
     * @param entityId the livestream entity id.
     */
    public static void initLiveEntity(UzPlayer uzPlayer, String entityId) {
        initEntity(uzPlayer, entityId, true);
    }

    /**
     * Init to play the VOD video.
     *
     * @param uzPlayer the {@link UzPlayer} instance.
     * @param entityId the VOD entity id.
     */
    public static void initVodEntity(UzPlayer uzPlayer, String entityId) {
        initEntity(uzPlayer, entityId, false);
    }

    /**
     * Init the casty.
     *
     * @param activity the current activity
     */
    public static void setCasty(Activity activity) {
        if (activity == null) {
            throw new NullPointerException(UzException.ERR_12);
        }
        if (UzCommonUtil.isTV(activity)) {
            return;
        }
        if (!UzCommonUtil.isCastDependencyAvailable()) {
            throw new NoClassDefFoundError(UzException.ERR_505);
        }
        UzPlayerData.getInstance().setCasty(Casty.create(activity));
    }

    private static void initEntity(UzPlayer uzPlayer, String entityId, boolean isLive) {
        if (uzPlayer == null) {
            throw new NullPointerException(UzException.ERR_13);
        }
        Context context = uzPlayer.getContext();
        if (entityId != null) {
            PipHelper.setClickedPip(context, false);
        }
        if (!UzConnectivityUtil.isConnected(context)) {
            LLog.e(TAG, UzException.ERR_0);
            return;
        }
        if (PipHelper.getClickedPip(context)) {
            play(uzPlayer, null, isLive);
        } else {
            //check if play entity
            PipHelper.stopMiniPlayer(context);
            if (entityId != null) {
                play(uzPlayer, entityId, isLive);
            }
        }
        UzPlayerData.setIsInitPlaylistFolder(context, false);
        UzCustomLinkPlayData.getInstance().clearCustomLinkData();
    }

    private static void play(final UzPlayer uzVideo, final String entityId, final boolean isLive) {
        UzPlayerData.getInstance().setSettingPlayer(false);
        uzVideo.post(new Runnable() {
            @Override
            public void run() {
                if (isLive) {
                    uzVideo.initLiveEntity(entityId);
                } else {
                    uzVideo.initVodEntity(entityId);
                }
            }
        });
    }

    /**
     * Init to play a playlist folder.
     *
     * @param uzPlayer the {@link UzPlayer} instance.
     * @param metadataId the metadata id of playlist folder.
     */
    public static void initPlaylistFolder(UzPlayer uzPlayer, String metadataId) {
        if (uzPlayer == null) {
            throw new NullPointerException(UzException.ERR_13);
        }
        Context context = uzPlayer.getContext();
        if (metadataId != null) {
            PipHelper.setClickedPip(context, false);
        }
        if (!UzConnectivityUtil.isConnected(context)) {
            LLog.e(TAG, UzException.ERR_0);
            return;
        }
        if (PipHelper.getClickedPip(context)) {
            if (UzPlayerData.getInstance().isPlayWithPlaylistFolder()) {
                playPlaylist(uzPlayer, null);
            }
        } else {
            PipHelper.stopMiniPlayer(context);
            playPlaylist(uzPlayer, metadataId);
        }
        UzPlayerData.setIsInitPlaylistFolder(context, true);
        UzCustomLinkPlayData.getInstance().clearCustomLinkData();
    }

    private static void playPlaylist(final UzPlayer uzVideo, final String metadataId) {
        UzPlayerData.getInstance().setSettingPlayer(false);
        uzVideo.post(new Runnable() {
            @Override
            public void run() {
                uzVideo.initPlaylistFolder(metadataId);
            }
        });
    }

    public static void setUseDraggableLayout(boolean value) {
        UzPlayerData.getInstance().setUseDraggableLayout(value);
    }

    public static void setCurrentSkinRes(int resLayoutMain) {
        UzPlayerData.getInstance().setCurrentSkinRes(resLayoutMain);
    }

    public static void setPlayerInfoId(String playerInfoId) {
        UzPlayerData.getInstance().setPlayerInfoId(playerInfoId);
    }

    public static void updateUiFocusChange(View view, boolean isFocus) {
        updateUiFocusChange(view, isFocus, R.drawable.bkg_has_focus, R.drawable.bkg_no_focus);
    }

    public static void updateUiFocusChange(View view, boolean isFocus, int focusDrawableRes,
            int notFocusDrawableRes) {
        if (view == null) {
            return;
        }
        if (isFocus) {
            view.setBackgroundResource(focusDrawableRes);
        } else {
            view.setBackgroundResource(notFocusDrawableRes);
        }
    }

    /**
     * Because the time from client is un-trust for calculating the HLS latency, so get & save the
     * UTC time from
     * <a href=http://worldtimeapi.org/api/timezone/Etc/UTC>this free api</a>.
     */
    private static void syncCurrentUtcTime() {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        final long startApiCallTime = System.currentTimeMillis();
        UzApiMaster.getInstance()
                .subscribe(service.getCurrentUTCTime(), new ApiSubscriber<UtcTime>() {
                    @Override
                    public void onSuccess(UtcTime result) {
                        long apiTime = (System.currentTimeMillis() - startApiCallTime) / 2;
                        long currentTime = result.getCurrentDateTimeMs() + apiTime;
                        LLog.i(TAG, "sync server time success " + currentTime);
                        UzPlayerData.saveLastServerTime(UzCoreUtil.getContext(), currentTime);
                        UzPlayerData.saveLastElapsedTime(UzCoreUtil.getContext(),
                                SystemClock.elapsedRealtime());
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LLog.e(TAG, "sync server time failed");
                        UzPlayerData.saveLastServerTime(UzCoreUtil.getContext(),
                                System.currentTimeMillis());
                        UzPlayerData.saveLastElapsedTime(UzCoreUtil.getContext(),
                                SystemClock.elapsedRealtime());
                    }
                });
    }
}
