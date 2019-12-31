package vn.uiza.restapi.restclient;

import vn.uiza.core.common.Constants;
import vn.uiza.restapi.UizaLiveService;
import vn.uiza.restapi.UizaVodService;
import vn.uiza.restapi.heartbeat.UizaHeartBeatService;
import vn.uiza.restapi.linkplay.UizaLinkPlayService;
import vn.uiza.restapi.tracking.UizaTrackingService;
import vn.uiza.restapi.uiza.UZService;

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
            UZRestClientGetLinkPlay.getInstance().init(Constants.URL_GET_LINK_PLAY_DEV, "");
            UZRestClientHeartBeat.getInstance().init(Constants.URL_HEART_BEAT_DEV, "");
            UZRestClientTracking.getInstance().init(Constants.URL_TRACKING_DEV, Constants.TRACKING_ACCESS_TOKEN_DEV);
        } else if (environment == Constants.ENVIRONMENT.STAG) {
            UZRestClientGetLinkPlay.getInstance().init(Constants.URL_GET_LINK_PLAY_STAG, "");
            UZRestClientHeartBeat.getInstance().init(Constants.URL_HEART_BEAT_STAG, "");
            UZRestClientTracking.getInstance().init(Constants.URL_TRACKING_STAG, Constants.TRACKING_ACCESS_TOKEN_STAG);
        } else if (environment == Constants.ENVIRONMENT.PROD) {
            UZRestClientGetLinkPlay.getInstance().init(Constants.URL_GET_LINK_PLAY_PROD, "");
            UZRestClientHeartBeat.getInstance().init(Constants.URL_HEART_BEAT_PROD, "");
            UZRestClientTracking.getInstance().init(Constants.URL_TRACKING_PROD, Constants.TRACKING_ACCESS_TOKEN_PROD);
        }
    }

    public static void changeAPIToken(String token) {
        UizaRestClient.getInstance().changeAuthorization(token);
    }

    public static UizaRestClient getUizaClient() {
        return UizaRestClient.getInstance();
    }

    public static UZRestClientGetLinkPlay getLinkPlayClient() {
        return UZRestClientGetLinkPlay.getInstance();
    }

    // optional
    @Deprecated
    public static UZService getUizaService() {
        return UizaRestClient.getInstance().createService(UZService.class);
    }

    // V5
    public static UizaLiveService getLiveService() {
        return UizaRestClient.getInstance().createService(UizaLiveService.class);
    }

    public static UizaVodService getVideoService() {
        return UizaRestClient.getInstance().createService(UizaVodService.class);
    }

    public static UizaLinkPlayService getLinkPlayService() {
        return UZRestClientGetLinkPlay.getInstance().createService(UizaLinkPlayService.class);
    }

    public static UizaTrackingService getTrackingService() {
        return UZRestClientTracking.getInstance().createService(UizaTrackingService.class);
    }

    public static UizaHeartBeatService getHeartBeatService() {
        return UZRestClientHeartBeat.getInstance().createService(UizaHeartBeatService.class);
    }

}
