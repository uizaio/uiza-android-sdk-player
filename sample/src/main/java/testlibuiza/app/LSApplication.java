package testlibuiza.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.gson.Gson;

import vn.uiza.core.common.Constants;
import vn.uiza.restapi.restclient.RestClientTracking;
import vn.uiza.restapi.restclient.RestClientV2;
import vn.uiza.uzv3.util.UZUtil;

public class LSApplication extends MultiDexApplication {
    private static LSApplication instance;
    private Gson gson;

    /*chivas69.uizadev.io
    trunglh@uiza.io / 123456789*/

    /*https://loctbprod01.uiza.co
    loctb@uiza.io / 123456789*/

    /*public static final String metadataDefault0 = "00932b61-1d39-45d2-8c7d-3d99ad9ea95a";
    public static String entityIdDefaultLIVE_TRANSCODE = "ebc8e874-afbb-4fd0-b7cf-9e34d14de4ac";
    private final String DF_TOKEN = "uap-16f8e65d8e2643ffa3ff5ee9f4f9ba03-a07716a6";
    private final String DF_APP_ID = "16f8e65d8e2643ffa3ff5ee9f4f9ba03";
    private final int env = Constants.ENVIRONMENT_STAG;
    public static final String entityIdDefaultVOD = "b7297b29-c6c4-4bd6-a74f-b60d0118d275";
    public static final String entityIdDefaultLIVE = "45a908f7-a62e-4eaf-8ce2-dc5699f33406";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "9a6dc8d6-b5ef-443e-81d1-560368b2c197";
    private final String currentPlayerId = Constants.PLAYER_ID_SKIN_1;
    private final String DF_DOMAIN_API = "android-api.uiza.co";*/

    //workspace prod
    private final String DF_DOMAIN_API = "loctbprod01.uiza.co";
    private final String DF_TOKEN = "uap-9816792bb84642f09d843af4f93fb748-b94fcbd1";
    private final String DF_APP_ID = "9816792bb84642f09d843af4f93fb748";
    private final int currentPlayerId = loitp.core.R.layout.uz_player_skin_1;
    public static String entityIdDefaultVOD = "fe0c0ba1-dc1c-4d1e-96dd-9c5558c104bf";
    public static String entityIdDefaultLIVE = "6e736441-750b-4351-b7b0-561982c6554f";
    public static String metadataDefault0 = "0e87adaa-49ef-4b6e-a827-6c68a63796b4";
    public static String entityIdDefaultLIVE_TRANSCODE = "04db355b-80d5-456e-aedb-aff1f8579e03";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "52128efd-59a7-4a3c-a2ec-988d031ccbe4";
    private final int env = Constants.ENVIRONMENT_PROD;

    /*private final String DF_DOMAIN_API = "vingroup-api.uiza.co";
    private final String DF_TOKEN = "uap-a905fa990e5844c2ac92262cc8ee7a3f-4b52b053";
    private final String DF_APP_ID = "a905fa990e5844c2ac92262cc8ee7a3f";
    private final int env = Constants.ENVIRONMENT_PROD;
    public static final String entityIdDefaultVOD = "8bbb46b8-37e0-49a7-8061-5bfeb7e577dd";
    public static final String entityIdDefaultLIVE = "";
    public static final String metadataDefault0 = "";*/

    /*private final String DF_DOMAIN_API = "dev-api.uizadev.io";
    private final String DF_TOKEN = "5e1e1afb4bf71936f26ec0ee6832b44f9ae0176f2a0334a0eadf09d4fa6da1f2e0a595baa46d20983c62f30d274724ca5bad4beaf0f4251db32f9651075ecc3b";
    private final String DF_APP_ID = "152a5e7cca9a41948c88dbb96e705aaf";
    private final int currentPlayerId = Constants.PLAYER_ID_SKIN_1;
    public static String entityIdDefaultVOD = "fe0c0ba1-dc1c-4d1e-96dd-9c5558c104bf";
    public static String entityIdDefaultLIVE = "";
    public static String metadataDefault0 = "";
    public static String entityIdDefaultLIVE_TRANSCODE = "";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "";
    private final int env = Constants.ENVIRONMENT_DEV;*/

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (gson == null) {
            gson = new Gson();
        }
        //Utils.init(this);
        //config activity transition default
        //ActivityData.getInstance().setType(Constants.TYPE_ACTIVITY_TRANSITION_FADE);

        RestClientV2.init(Constants.URL_DEV_UIZA_VERSION_2_STAG);
        RestClientTracking.init(Constants.URL_TRACKING_STAG);
        Constants.setDebugMode(true);

        //initWorkspace();
        //UZUtil.initWorkspace(DF_DOMAIN_API, DF_TOKEN, DF_APP_ID, env, currentPlayerId);
        UZUtil.initWorkspace(this, DF_DOMAIN_API, DF_TOKEN, DF_APP_ID);
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
