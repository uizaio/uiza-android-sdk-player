package io.uiza.samplelive;

import android.content.SharedPreferences;

import androidx.multidex.MultiDexApplication;
import androidx.preference.PreferenceManager;

import timber.log.Timber;
import vn.uiza.core.common.Constants;
import vn.uiza.restapi.restclient.UizaClientFactory;

public class SampleLiveApplication extends MultiDexApplication {

    public static final String EXTRA_STREAM_ENDPOINT = "uiza_live_extra_stream_endpoint";


    private static final String DEV_HOST = "https://development-api.uizadev.io";

    public static final String LIVE_URL = "rtmp://679b139b89-in.streamwiz.dev/transcode";
    public static final String STREAM_KEY = "live_ljNx4GLp3F";

    private static final String API_TOKEN = "uap-445a3a1bc75440aa8caccafcc6f9c425-08509e15"; // dynamic
    SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Constants.setDebugMode(false);
        Constants.setApiVersion(Constants.API_VERSION_5);
        String apiToken = preferences.getString("api_token_key", API_TOKEN);
        UizaClientFactory.setup(DEV_HOST, apiToken);
    }

    public static String getLiveEndpoint() {
        return LIVE_URL + "/" + STREAM_KEY;
    }

}
