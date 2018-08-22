package testlibuiza.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.gson.Gson;

import vn.loitp.core.common.Constants;
import vn.loitp.restapi.restclient.RestClientTracking;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.uizavideov3.util.UizaDataV3;
import vn.loitp.utils.util.Utils;

public class LSApplication extends MultiDexApplication {
    private static LSApplication instance;
    private Gson gson;

    /*https://loctbprod01.uiza.co
    loctb@uiza.io / 123456789*/

    //workspace stag
    /*private final String DF_DOMAIN_API = "android-api.uiza.co";
    private final String DF_TOKEN = "uap-16f8e65d8e2643ffa3ff5ee9f4f9ba03-a07716a6";
    private final String DF_APP_ID = "16f8e65d8e2643ffa3ff5ee9f4f9ba03";
    private final int env = Constants.ENVIRONMENT_STAG;
    public static final String entityIdDefaultVOD = "b7297b29-c6c4-4bd6-a74f-b60d0118d275";
    public static final String entityIdDefaultLIVE = "45a908f7-a62e-4eaf-8ce2-dc5699f33406";
    public static final String metadataDefault0 = "00932b61-1d39-45d2-8c7d-3d99ad9ea95a";*/

    //workspace prod
    private final String DF_DOMAIN_API = "loctbprod01.uiza.co";
    private final String DF_TOKEN = "uap-9816792bb84642f09d843af4f93fb748-b94fcbd1";
    private final String DF_APP_ID = "9816792bb84642f09d843af4f93fb748";
    public static String entityIdDefaultVOD = "71472a9b-662d-4eee-837e-3ad98b99140a";
    public static String entityIdDefaultLIVE = "6356e2c3-00af-495e-b60c-361f976b4084";
    public static String metadataDefault0 = "0e87adaa-49ef-4b6e-a827-6c68a63796b4";
    public static String entityIdDefaultLIVE_TRANSCODE = "26a409a2-0177-4a84-8459-4feb2d131d35";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "c504ce3a-39d0-4b96-8b56-d6d87636974b";
    private final String currentPlayerId = Constants.PLAYER_ID_SKIN_1;
    private final int env = Constants.ENVIRONMENT_PROD;

    /*private final String DF_DOMAIN_API = "vingroup-api.uiza.co";
    private final String DF_TOKEN = "uap-a905fa990e5844c2ac92262cc8ee7a3f-4b52b053";
    private final String DF_APP_ID = "a905fa990e5844c2ac92262cc8ee7a3f";
    private final int env = Constants.ENVIRONMENT_PROD;
    public static final String entityIdDefaultVOD = "8bbb46b8-37e0-49a7-8061-5bfeb7e577dd";
    public static final String entityIdDefaultLIVE = "";
    public static final String metadataDefault0 = "";*/

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (gson == null) {
            gson = new Gson();
        }
        Utils.init(this);
        //config activity transition default
        //ActivityData.getInstance().setType(Constants.TYPE_ACTIVITY_TRANSITION_FADE);

        RestClientV2.init(Constants.URL_DEV_UIZA_VERSION_2_STAG);
        RestClientTracking.init(Constants.URL_TRACKING_STAG);
        Constants.setDebugMode(true);

        initWorkspace();
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

    private void initWorkspace() {
        UizaDataV3.getInstance().setCurrentPlayerId(currentPlayerId);
        UizaDataV3.getInstance().initSDK(DF_DOMAIN_API, DF_TOKEN, DF_APP_ID, env);
    }
}
