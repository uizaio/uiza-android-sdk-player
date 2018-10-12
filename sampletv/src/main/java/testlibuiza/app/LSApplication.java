package testlibuiza.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.gson.Gson;

import vn.uiza.core.common.Constants;
import vn.uiza.data.ActivityData;
import vn.uiza.uzv3.util.UZUtil;

public class LSApplication extends MultiDexApplication {
    private static LSApplication instance;
    private Gson gson;

    private final String DF_DOMAIN_API = "veep.uiza.co";
    private final String DF_TOKEN = "uap-4f892058d35e46dd8a3a660481583b79-6a1319cb";
    private final String DF_APP_ID = "4f892058d35e46dd8a3a660481583b79";
    public static String entityIdDefaultVOD = "aa0874b7-16fd-4ede-aa5a-a6b3a4f3ba79";
    public static String entityIdDefaultLIVE = "64d0665e-9593-491d-b849-a8624ba47c73";
    public static String metadataDefault0 = "a8d02fcd-d5b8-42f5-b0c4-628533ec8a9c";
    private final int env = Constants.ENVIRONMENT_PROD;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (gson == null) {
            gson = new Gson();
        }
        //config activity transition default
        ActivityData.getInstance().setType(Constants.TYPE_ACTIVITY_TRANSITION_SLIDELEFT);
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
