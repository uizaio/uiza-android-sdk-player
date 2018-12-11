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
    private final String DF_DOMAIN_API = "input";
    private final String DF_TOKEN = "input";
    private final String DF_APP_ID = "input";
    public static String entityIdDefaultVOD = "input";
    public static String entityIdDefaultLIVE = "input";
    public static String metadataDefault0 = "input";
    private final int env = Constants.ENVIRONMENT_PROD;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (gson == null) {
            gson = new Gson();
        }
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
