package vn.uiza.restapi;

import android.content.Context;

import vn.uiza.core.common.Constants;
import vn.uiza.core.common.EnvironmentValues;
import vn.uiza.restapi.restclient.UizaHeartBeatClient;
import vn.uiza.restapi.restclient.UizaRestClient;
import vn.uiza.restapi.restclient.UizaTrackingClient;
import vn.uiza.utils.EncryptUtils;

public class UizaClientFactory {

    private UizaClientFactory() {
    }

    /**
     * setup for API_VERSION_5
     * environment {@link Constants.ENVIRONMENT#DEV}
     *
     * @param context    Context
     * @param baseApiUrl Base Url of API
     * @param token      API Token
     */
    public static void setup(Context context, String baseApiUrl, String token) {
        setup(context, baseApiUrl, token, Constants.ENVIRONMENT.DEV);
    }

    /**
     * setup for API_VERSION_5
     *
     * @param context     Context
     * @param baseApiUrl  Base Url of API
     * @param appId       App Id
     * @param environment One if {@link Constants.ENVIRONMENT#DEV},
     *                    {@link Constants.ENVIRONMENT#STAG} or {@link Constants.ENVIRONMENT#PROD}
     */
    public static void setup(Context context, String baseApiUrl, String appId, @EnvironmentValues int environment) {
        String signed = EncryptUtils.getAppSigned(context);
        UizaRestClient.getInstance().init(baseApiUrl, appId, signed);
        if (environment == Constants.ENVIRONMENT.DEV) {
            UizaHeartBeatClient.getInstance().init(Constants.URL_HEART_BEAT_DEV);
            UizaTrackingClient.getInstance().init(Constants.URL_TRACKING_DEV, Constants.TRACKING_ACCESS_TOKEN_DEV, signed);
        } else if (environment == Constants.ENVIRONMENT.STAG) {
            UizaHeartBeatClient.getInstance().init(Constants.URL_HEART_BEAT_STAG);
            UizaTrackingClient.getInstance().init(Constants.URL_TRACKING_STAG, Constants.TRACKING_ACCESS_TOKEN_STAG, signed);
        } else if (environment == Constants.ENVIRONMENT.PROD) {
            UizaHeartBeatClient.getInstance().init(Constants.URL_HEART_BEAT_PROD);
            UizaTrackingClient.getInstance().init(Constants.URL_TRACKING_PROD, Constants.TRACKING_ACCESS_TOKEN_PROD, "");
        }
    }

    public static void changeAppId(String appId) {
        UizaRestClient.getInstance().changeAppId(appId);
    }

    // V5 get Service
    public static UizaLiveService getLiveService() {
        return UizaRestClient.getInstance().createService(UizaLiveService.class);
    }

    public static UizaVideoService getVideoService() {
        return UizaRestClient.getInstance().createService(UizaVideoService.class);
    }

    public static UizaEventService getEventService() {
        return UizaRestClient.getInstance().createService(UizaEventService.class);
    }

    public static UizaTrackingService getTrackingService() {
        return UizaTrackingClient.getInstance().createService(UizaTrackingService.class);
    }

    public static UizaHeartBeatService getHeartBeatService() {
        return UizaHeartBeatClient.getInstance().createService(UizaHeartBeatService.class);
    }
}
