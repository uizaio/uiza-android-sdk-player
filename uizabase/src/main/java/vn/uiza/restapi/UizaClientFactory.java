package vn.uiza.restapi;

import vn.uiza.core.common.Constants;
import vn.uiza.restapi.UizaEventService;
import vn.uiza.restapi.UizaHeartBeatService;
import vn.uiza.restapi.UizaLiveService;
import vn.uiza.restapi.UizaTrackingService;
import vn.uiza.restapi.UizaVideoService;
import vn.uiza.restapi.restclient.UizaHeartBeatClient;
import vn.uiza.restapi.restclient.UizaRestClient;
import vn.uiza.restapi.restclient.UizaTrackingClient;

public class UizaClientFactory {

    private UizaClientFactory() {
    }

    /**
     * setup for API_VERSION_5
     * environment {@link Constants.ENVIRONMENT#DEV}
     *
     * @param baseApiUrl Base Url of API
     * @param token      API Token
     */
    public static void setup(String baseApiUrl, String token) {
        setup(baseApiUrl, token, Constants.ENVIRONMENT.DEV);
    }

    /**
     * setup for API_VERSION_5
     *
     * @param baseApiUrl  Base Url of API
     * @param token       API Token
     * @param environment One if {@link Constants.ENVIRONMENT#DEV},
     *                    {@link Constants.ENVIRONMENT#STAG} or {@link Constants.ENVIRONMENT#PROD}
     */
    public static void setup(String baseApiUrl, String token, @Constants.ENVIRONMENT int environment) {
        UizaRestClient.getInstance().init(baseApiUrl, token);
        if (environment == Constants.ENVIRONMENT.DEV) {
            UizaHeartBeatClient.getInstance().init(Constants.URL_HEART_BEAT_DEV, "");
            UizaTrackingClient.getInstance().init(Constants.URL_TRACKING_DEV, Constants.TRACKING_ACCESS_TOKEN_DEV);
        } else if (environment == Constants.ENVIRONMENT.STAG) {
            UizaHeartBeatClient.getInstance().init(Constants.URL_HEART_BEAT_STAG, "");
            UizaTrackingClient.getInstance().init(Constants.URL_TRACKING_STAG, Constants.TRACKING_ACCESS_TOKEN_STAG);
        } else if (environment == Constants.ENVIRONMENT.PROD) {
            UizaHeartBeatClient.getInstance().init(Constants.URL_HEART_BEAT_PROD, "");
            UizaTrackingClient.getInstance().init(Constants.URL_TRACKING_PROD, Constants.TRACKING_ACCESS_TOKEN_PROD);
        }
    }

    public static void changeAPIToken(String token) {
        UizaRestClient.getInstance().changeAuthorization(token);
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
