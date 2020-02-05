package uizacoresdk;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import uizacoresdk.chromecast.Casty;
import uizacoresdk.util.UizaUtil;
import uizacoresdk.util.UizaCLPData;
import uizacoresdk.util.UizaData;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UizaException;
import vn.uiza.models.PlaybackInfo;
import vn.uiza.restapi.UizaClientFactory;
import vn.uiza.utils.AppUtils;
import vn.uiza.utils.LDeviceUtil;

public class UizaCoreSDK {
    private UizaCoreSDK() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * InitSDK
     *
     * @param context         see {@link Context}
     * @param domainAPI       Base Url of API
     * @param appId           App Id
     * @param environment     One if {@link Constants.ENVIRONMENT#DEV},
     *                        {@link Constants.ENVIRONMENT#STAG} or {@link Constants.ENVIRONMENT#PROD}
     * @param currentPlayerId Skin of player
     * @return true if success init or false
     */
    public static boolean initWorkspace(@NonNull Context context,
                                        String domainAPI,
                                        String appId,
                                        int environment,
                                        @LayoutRes int currentPlayerId) {
        if (TextUtils.isEmpty(domainAPI)) {
            throw new NullPointerException(UizaException.ERR_16);
        }
        if (!AppUtils.isDependencyAvailable("com.google.android.exoplayer2.SimpleExoPlayer")) {
            throw new NoClassDefFoundError(UizaException.ERR_504);
        }
        UizaUtil.init(context);
        setCurrentPlayerId(currentPlayerId);
        return UizaData.getInstance().initSDK(context, domainAPI, appId, environment);
    }

    /**
     * InitSDK for API_VERSION_5
     * environment {@link Constants.ENVIRONMENT#PROD}
     * and player skin {@link R.layout#uz_player_skin_1}
     *
     * @param context   see {@link Context}
     * @param domainAPI Base Url of API
     * @param appId     App Id
     * @return true if success init or false
     */
    public static boolean initWorkspace(@NonNull Context context, String domainAPI, String appId) {
        return initWorkspace(context, domainAPI, appId, Constants.ENVIRONMENT.PROD, R.layout.uz_player_skin_1);
    }

    public static void changeAppId(String appId) {
        UizaUtil.setAppId(appId);
        UizaClientFactory.changeAppId(appId);
    }

    /**
     * set Casty
     *
     * @param activity: Activity
     */
    public static void setCasty(@NonNull Activity activity) {
        if (LDeviceUtil.isTV(activity)) {
            return;
        }
        if (!AppUtils.checkChromeCastAvailable()) {
            throw new NoClassDefFoundError(UizaException.ERR_505);
        }
        UizaData.getInstance().setCasty(Casty.create(activity));
    }

    /**
     * set Player Id
     *
     * @param resLayoutMain: id of layout xml
     */
    public static void setCurrentPlayerId(@LayoutRes int resLayoutMain) {
        UizaData.getInstance().setCurrentPlayerId(resLayoutMain);
    }

    /**
     * user with VDHView
     *
     * @param isUseWithVDHView: boolean
     */
    public static void setUseWithVDHView(boolean isUseWithVDHView) {
        UizaData.getInstance().setUseWithVDHView(isUseWithVDHView);
    }

    /**
     * Current player InfoId
     *
     * @param playerInfoId: string
     */
    public static void setCurrentPlayerInfoId(String playerInfoId) {
        UizaData.getInstance().setPlayerInfoId(playerInfoId);
    }

    /**
     * set current PlayBackInfo for Custom Link Play
     *
     * @param playback: {@link PlaybackInfo}
     */
    public static void setCurrentPlaybackInfo(PlaybackInfo playback) {
        UizaCLPData.getInstance().setPlaybackInfo(playback);
    }
}
