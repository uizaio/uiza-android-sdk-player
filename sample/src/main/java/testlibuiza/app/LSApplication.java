package testlibuiza.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.gson.Gson;

import uizacoresdk.util.UZUtil;
import vn.uiza.core.common.Constants;
import vn.uiza.restapi.restclient.RestClientTracking;
import vn.uiza.restapi.restclient.RestClientV2;

public class LSApplication extends MultiDexApplication {
    private static LSApplication instance;
    private Gson gson;
    //TODO input information of your workspace
    private final String DF_DOMAIN_API = "input";
    private final String DF_TOKEN = "input";
    private final String DF_APP_ID = "input";
    public static String entityIdDefaultVODLongtime = "input";
    public static String entityIdDefaultVOD = "input";
    public static String entityIdDefaultVOD_21_9 = "input";
    public static String entityIdDefaultVODportrait = "input";
    public static String entityIdDefaultLIVE = "input";
    public static String metadataDefault0 = "input";
    public static String entityIdDefaultLIVE_TRANSCODE = "input";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "input";
    private final int env = Constants.ENVIRONMENT_PROD;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (gson == null) {
            gson = new Gson();
        }
        //config activity transition default
        //ActivityData.getInstance().setType(Constants.TYPE_ACTIVITY_TRANSITION_FADE);

        RestClientV2.init(Constants.URL_DEV_UIZA_VERSION_2_STAG);
        RestClientTracking.init(Constants.URL_TRACKING_STAG);
        Constants.setDebugMode(true);

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
