package io.uiza.samplelive;

import android.content.SharedPreferences;

import androidx.multidex.MultiDexApplication;
import androidx.preference.PreferenceManager;

import timber.log.Timber;
import vn.uiza.restapi.UizaClientFactory;

public class SampleLiveApplication extends MultiDexApplication {

    public static final String EXTRA_STREAM_ENDPOINT = "uiza_live_extra_stream_endpoint";
    public static final String EXTRA_STREAM_ID = "uiza_live_extra_stream_id";

    private static final String DEV_HOST = "development-api.uizadev.io";

    public static final String LIVE_URL = "rtmp://679b139b89-in.streamwiz.dev/transcode";
    public static final String STREAM_KEY = "live_ljNx4GLp3F";

    SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String host = preferences.getString("api_base_url_key", DEV_HOST);
        String appId = preferences.getString("app_id_key", "");
        UizaClientFactory.setup(this,host, appId);
    }

    public static String getLiveEndpoint() {
        return LIVE_URL + "/" + STREAM_KEY;
    }
}
