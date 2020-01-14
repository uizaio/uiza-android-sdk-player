package testlibuiza.app;

import android.content.SharedPreferences;

import androidx.multidex.MultiDexApplication;
import androidx.preference.PreferenceManager;

import testlibuiza.BuildConfig;
import timber.log.Timber;
import uizacoresdk.UizaCoreSDK;
import vn.uiza.core.common.Constants;

public class LSApplication extends MultiDexApplication {

    private static final String DEV_HOST = "development-api.uizadev.io";
    private static final String APP_ID = "8e5dddcd6abe4904ac2f996a36fda7ee";
    public static final String DEFAULT_REGION = "asia-south1";

    SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        Constants.setDebugMode(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String host = preferences.getString("api_base_url_key", DEV_HOST);
        String apiToken = preferences.getString("api_token_key", "");
        UizaCoreSDK.initWorkspace(this, host, apiToken);
    }

}
