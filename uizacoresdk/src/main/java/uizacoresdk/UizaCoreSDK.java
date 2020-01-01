package uizacoresdk;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import uizacoresdk.chromecast.Casty;
import uizacoresdk.util.UZData;
import uizacoresdk.util.UZDataCLP;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UizaException;
import vn.uiza.restapi.model.v5.PlaybackInfo;
import vn.uiza.restapi.restclient.UizaClientFactory;
import vn.uiza.utils.AppUtils;
import vn.uiza.utils.LDeviceUtil;
import vn.uiza.utils.Utils;

public class UizaCoreSDK {
    private UizaCoreSDK() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * InitSDK
     *
     * @param context         see {@link Context}
     * @param apiVersion      One of {@link Constants#API_VERSION_5},
     *                        {@link Constants#API_VERSION_4}, or {@link Constants#API_VERSION_3}
     * @param domainAPI       Base Url of API
     * @param token           API Token
     * @param environment     One if {@link Constants.ENVIRONMENT#DEV},
     *                        {@link Constants.ENVIRONMENT#STAG} or {@link Constants.ENVIRONMENT#PROD}
     * @param currentPlayerId Skin of player
     * @return true if success init or false
     */
    public static boolean initWorkspace(@NonNull Context context,
                                        int apiVersion,
                                        String domainAPI,
                                        String token,
                                        int environment,
                                        @LayoutRes int currentPlayerId) {
        if (TextUtils.isEmpty(domainAPI)) {
            throw new NullPointerException(UizaException.ERR_16);
        }
        if (!AppUtils.isDependencyAvailable("com.google.android.exoplayer2.SimpleExoPlayer")) {
            throw new NoClassDefFoundError(UizaException.ERR_504);
        }
        Utils.init(context.getApplicationContext());
        setCurrentPlayerId(currentPlayerId);
        return UZData.getInstance().initSDK(apiVersion, domainAPI, token, environment);
    }

    /**
     * InitSDK for API_VERSION_5
     * environment {@link Constants.ENVIRONMENT#PROD}
     * and player skin {@link R.layout#uz_player_skin_1}
     *
     * @param context   see {@link Context}
     * @param domainAPI Base Url of API
     * @param token     API Token
     * @return true if success init or false
     */
    public static void initWorkspace(@NonNull Context context, String domainAPI, String token) {
        initWorkspace(context, Constants.API_VERSION_5, domainAPI, token, Constants.ENVIRONMENT.PROD, R.layout.uz_player_skin_1);
    }

    public static void changeAPIToken(String token) {
        UZUtil.setToken(Utils.getContext(), token);
        UizaClientFactory.changeAPIToken(token);
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
        UZData.getInstance().setCasty(Casty.create(activity));
    }

    /**
     * set Player Id
     *
     * @param resLayoutMain: id of layout xml
     */
    public static void setCurrentPlayerId(@LayoutRes int resLayoutMain) {
        UZData.getInstance().setCurrentPlayerId(resLayoutMain);
    }

    /**
     * user with VDHView
     *
     * @param isUseWithVDHView: boolean
     */
    public static void setUseWithVDHView(boolean isUseWithVDHView) {
        UZData.getInstance().setUseWithVDHView(isUseWithVDHView);
    }

    /**
     * Current player inforId
     *
     * @param playerInforId: string
     */
    public static void setCurrentPlayerInforId(String playerInforId) {
        UZData.getInstance().setPlayerInforId(playerInforId);
    }

    /**
     * set current playbackinfo
     *
     * @param playback: {@link PlaybackInfo}
     */
    public static void setCurrentPlaybackInfo(PlaybackInfo playback) {
        UZDataCLP.getInstance().setPlaybackInfo(playback);
    }
}
