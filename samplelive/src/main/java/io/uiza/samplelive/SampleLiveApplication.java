package io.uiza.samplelive;

import androidx.multidex.MultiDexApplication;

import timber.log.Timber;
import vn.uiza.core.common.Constants;
import vn.uiza.restapi.restclient.UizaClientFactory;

public class SampleLiveApplication extends MultiDexApplication {

    public static final String EXTRA_STREAM_ENDPOINT = "uiza_live_extra_stream_endpoint";


    private static final String DEV_HOST = "https://development-api.uizadev.io";

    public static final String LIVE_URL = "rtmp://679b139b89-in.streamwiz.dev/transcode";
    public static final String STREAM_KEY = "live_ljNx4GLp3F";

    private static final String APP_SECRET = "uap-c1ffbff4db954ddcb050c6af0b43ba56-41193b64";
    public static final String APP_ID = "duyqt-app";
    public static final String USER_ID = "duyqt1";


    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        Constants.setDebugMode(false);
        Constants.setApiVersion(Constants.API_VERSION_5);
        UizaClientFactory.setup(DEV_HOST, APP_SECRET, Constants.ENVIRONMENT.DEV);
    }

    public static String getLiveEndpoint() {
        return LIVE_URL + "/" + STREAM_KEY;
    }

}
