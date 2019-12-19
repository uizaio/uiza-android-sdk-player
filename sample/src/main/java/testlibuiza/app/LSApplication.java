package testlibuiza.app;

import android.content.SharedPreferences;

import androidx.multidex.MultiDexApplication;
import androidx.preference.PreferenceManager;

import testlibuiza.BuildConfig;
import timber.log.Timber;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.common.Constants;
import vn.uiza.utils.EncryptUtil;

public class LSApplication extends MultiDexApplication {

    public static final String DF_DOMAIN_API = "ap-southeast-1-api.uiza.co";
    public static final String DF_TOKEN = "uap-f785bc511967473fbe6048ee5fb7ea59-69fefb79";
    public static final String DF_APP_ID = "f785bc511967473fbe6048ee5fb7ea59";
    public static final String entityIdDefaultVOD = "9940516b-c2d3-42d0-80e1-2340f9265277";
    public static final String entityIdDefaultVOD_21_9 = "33812ed9-4b02-408d-aab4-e77c12d16bb0";
    public static final String entityIdDefaultVODportrait = "33812ed9-4b02-408d-aab4-e77c12d16bb0";
    public static final String entityIdDefaultLIVE = "7b1b7cf3-d451-48c9-a57a-73e44e87f032";
    public static final String metadataDefault0 = "-";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "-";
    public static String PLAYER_INFOR_ID = "702ea04c-61d9-42ad-b3e0-5ec376b4d2a4";

    private static final String DEV_HOST = "development-api.uizadev.io";
    private static final String APP_SECRET = "uap-c1ffbff4db954ddcb050c6af0b43ba56-41193b64";


    private static final String DOMAIN_API = "https://development-api.uizadev.io";
    private static final String API_TOKEN = "uap-445a3a1bc75440aa8caccafcc6f9c425-08509e15";
    SharedPreferences preferences;
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        Constants.setDebugMode(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String apiToken = preferences.getString("api_token_key", API_TOKEN);
        UZUtil.initV5Workspace(this, DOMAIN_API, apiToken);

    }

}
