package testlibuiza.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.gson.Gson;

import uizacoresdk.util.UZUtil;
import vn.uiza.core.common.Constants;

public class LSApplication extends MultiDexApplication {
    private static LSApplication instance;
    private Gson gson;
    //TODO input information of your workspace
    /*public static final String DF_DOMAIN_API = "teamplayer.uiza.co";
    public static final String DF_TOKEN = "uap-01e137ad1b534004ad822035bf89b29f-b9b31f29";
    public static final String DF_APP_ID = "01e137ad1b534004ad822035bf89b29f";
    public static final int currentPlayerId = R.layout.uz_player_skin_1;
    public static final String entityIdDefaultVODLongtime = "85527b12-ae9a-4102-af60-c83054ffa213";
    public static final String entityIdDefaultVOD = "7699e10e-5ce3-4dab-a5ad-a615a711101e";
    public static final String entityIdDefaultVOD_21_9 = "85527b12-ae9a-4102-af60-c83054ffa213";
    public static final String entityIdDefaultVODportrait = "2732b570-6dc2-42df-bd58-3f7a0cac5683";
    public static final String entityIdDefaultLIVE = "bbe5b36e-155a-4f77-8e0f-f358c4f1f46a";
    public static final String metadataDefault0 = "53c2e63e-6ddf-4259-8159-cb43371943d1";
    public static String entityIdDefaultLIVE_TRANSCODE = "58240424-62f6-4f2b-9ddb-b3a49a3074a5";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "8ae1be2b-3cd5-478c-94c0-6598d5cad4f8";*/

    /*public static final String DF_DOMAIN_API = "stag-ap-southeast-1-api.uizadev.io";
    public static final String DF_TOKEN = "uap-37828391444345ebb02bbb4e95437307-af0c77c0";
    public static final String DF_APP_ID = "37828391444345ebb02bbb4e95437307";
    public static final String entityIdDefaultVOD = "0abef046-a1bc-4f12-9304-2cad3e0f60c4";
    public static final String entityIdDefaultVOD_21_9 = "0abef046-a1bc-4f12-9304-2cad3e0f60c4";
    public static final String entityIdDefaultVODportrait = "0abef046-a1bc-4f12-9304-2cad3e0f60c4";
    public static final String entityIdDefaultLIVE = "0abef046-a1bc-4f12-9304-2cad3e0f60c4";
    public static final String metadataDefault0 = "53c2e63e-6ddf-4259-8159-cb43371943d1";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "8ae1be2b-3cd5-478c-94c0-6598d5cad4f8";*/

    public static final String DF_DOMAIN_API = "dev-ap-southeast-1-api.uizadev.io";
    public static final String DF_TOKEN = "uap-10d71cf534914f38add1a163365f62c5-2d0d0cca";
    public static final String DF_APP_ID = "10d71cf534914f38add1a163365f62c5";
    public static final String entityIdDefaultVOD = "3a11d84c-8ee4-4d6a-9a75-2ec6a067f9d9";
    public static final String entityIdDefaultVOD_21_9 = "3a11d84c-8ee4-4d6a-9a75-2ec6a067f9d9";
    public static final String entityIdDefaultVODportrait = "3a11d84c-8ee4-4d6a-9a75-2ec6a067f9d9";
    public static final String entityIdDefaultLIVE = "-";
    public static final String metadataDefault0 = "-";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "-";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (gson == null) {
            gson = new Gson();
        }
        Constants.setDebugMode(true);
        //UZUtil.initWorkspace(this, DF_DOMAIN_API, DF_TOKEN, DF_APP_ID);
        UZUtil.initWorkspace(this, DF_DOMAIN_API, DF_TOKEN, DF_APP_ID, Constants.ENVIRONMENT_DEV, uizacoresdk.R.layout.uz_player_skin_1);
    }

    public Gson getGson() {
        return gson;
    }

    public static LSApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
