package testlibuiza.app;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import testlibuiza.BuildConfig;
import timber.log.Timber;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.common.Constants;
import vn.uiza.utils.EncryptUtil;

public class LSApplication extends MultiDexApplication {
    private static LSApplication instance;

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


//    private static final String NEW_APPID = "c2c2808817ca4fea82f5b325fe0e02b7";
    private static final String DOMAIN_API = "api.uiza.io";
    private static final String NEW_API_KEY = "uap-c2c2808817ca4fea82f5b325fe0e02b7-ef605d7d";
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        instance = this;
        Constants.setDebugMode(true);
        int apiVersion = Constants.API_VERSION_4;
        Constants.setApiVersion(apiVersion);
        UZUtil.initWorkspace(this, apiVersion, DF_DOMAIN_API, DF_TOKEN, DF_APP_ID);
        //UZUtil.initWorkspace(this, apiVersion, DF_DOMAIN_API, DF_TOKEN, DF_APP_ID);
        //UZUtil.setCurrentPlayerInforId(PLAYER_INFOR_ID);
        String s = EncryptUtil.getAppSigned(getApplicationContext());
        Timber.e("s = %s", s);
        String hmac = EncryptUtil.hmacSHA256("namnd", "namdeptrai");
        Timber.e("hmac = %s", hmac);
    }

    public static LSApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
