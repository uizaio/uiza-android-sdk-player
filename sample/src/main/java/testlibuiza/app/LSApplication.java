package testlibuiza.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.gson.Gson;

import testlibuiza.R;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.common.Constants;
import vn.uiza.restapi.restclient.RestClientTracking;
import vn.uiza.restapi.restclient.RestClientV2;

public class LSApplication extends MultiDexApplication {
    private static LSApplication instance;
    private Gson gson;
    //TODO input information of your workspace
    public static final String DF_DOMAIN_API = "azui.uiza.co";
    public static final String DF_TOKEN = "uap-598eff5dfedd434d99d8b957488aa7d3-42115bd5";
    public static final String DF_APP_ID = "598eff5dfedd434d99d8b957488aa7d3";
    public static final int currentPlayerId = R.layout.uz_player_skin_1;
    public static final String entityIdDefaultVODLongtime = "85527b12-ae9a-4102-af60-c83054ffa213";
    public static final String entityIdDefaultVOD = "678cbc03-dad2-461d-ab25-8c3c9d0e2611";
    public static final String entityIdDefaultVOD_21_9 = "85527b12-ae9a-4102-af60-c83054ffa213";
    public static final String entityIdDefaultVODportrait = "2732b570-6dc2-42df-bd58-3f7a0cac5683";
    public static final String entityIdDefaultLIVE = "1759f642-e062-4e88-b5f2-e3022bd03b57";
    public static final String metadataDefault0 = "53c2e63e-6ddf-4259-8159-cb43371943d1";
    public static String entityIdDefaultLIVE_TRANSCODE = "58240424-62f6-4f2b-9ddb-b3a49a3074a5";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "8ae1be2b-3cd5-478c-94c0-6598d5cad4f8";


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (gson == null) {
            gson = new Gson();
        }
        RestClientV2.init(Constants.URL_DEV_UIZA_VERSION_2_STAG);
        RestClientTracking.init(Constants.URL_TRACKING_STAG);
        Constants.setDebugMode(false);
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
