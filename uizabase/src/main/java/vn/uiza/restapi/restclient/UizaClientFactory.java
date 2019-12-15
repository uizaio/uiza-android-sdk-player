package vn.uiza.restapi.restclient;

import vn.uiza.core.common.Constants;

public class UizaClientFactory {

    private UizaClientFactory() {
    }

    private static void setup(String baseApiUrl, String token, ClientType type) {
        switch (type) {
            case NORMAL:
                UZRestClient.getInstance().init(baseApiUrl, token);
                break;
            case LINKPLAY:
                UZRestClientGetLinkPlay.getInstance().init(baseApiUrl, token);
                break;
            case HEARTBEAT:
                UZRestClientHeartBeat.getInstance().init(baseApiUrl, token);
                break;
            case TRACKING:
                UZRestClientTracking.getInstance().init(baseApiUrl, token);
                break;
            default:
                throw new UnsupportedOperationException("No support " + type.toString() + " type");
        }
    }

    public static void setup(String baseApiUrl, String token, @Constants.ENVIRONMENT int environment) {
        setup(baseApiUrl, token, ClientType.NORMAL);
        if (environment == Constants.ENVIRONMENT.DEV) {
            setup(Constants.URL_GET_LINK_PLAY_DEV, "", ClientType.LINKPLAY);
            setup(Constants.URL_HEART_BEAT_DEV, "", ClientType.HEARTBEAT);
            setup(Constants.URL_TRACKING_DEV, Constants.TRACKING_ACCESS_TOKEN_DEV, ClientType.TRACKING);
        } else if (environment == Constants.ENVIRONMENT.STAG) {
            setup(Constants.URL_GET_LINK_PLAY_STAG, "", ClientType.LINKPLAY);
            setup(Constants.URL_HEART_BEAT_STAG, "", ClientType.HEARTBEAT);
            setup(Constants.URL_TRACKING_STAG, Constants.TRACKING_ACCESS_TOKEN_STAG, ClientType.TRACKING);
        } else if (environment == Constants.ENVIRONMENT.PROD) {
            setup(Constants.URL_GET_LINK_PLAY_PROD, "", ClientType.LINKPLAY);
            setup(Constants.URL_HEART_BEAT_PROD, "", ClientType.HEARTBEAT);
            setup(Constants.URL_TRACKING_PROD, Constants.TRACKING_ACCESS_TOKEN_PROD, ClientType.TRACKING);
        }
    }

    public static RestClient getClient(ClientType type) {
        switch (type) {
            case NORMAL:
                return UZRestClient.getInstance();
            case LINKPLAY:
                return UZRestClientGetLinkPlay.getInstance();
            case HEARTBEAT:
                return UZRestClientHeartBeat.getInstance();
            case TRACKING:
                return UZRestClientTracking.getInstance();
            default:
                throw new UnsupportedOperationException("No support " + type.toString() + " type");
        }
    }

    public static RestClient getClient() {
        return UZRestClient.getInstance();
    }

    public static <S> S createService(Class<S> serviceClass, ClientType type) {
        return getClient(type).createService(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass) {
        return UZRestClient.getInstance().createService(serviceClass);
    }

}
