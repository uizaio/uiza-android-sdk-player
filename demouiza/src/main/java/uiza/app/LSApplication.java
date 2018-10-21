package uiza.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.gson.Gson;

import vn.uiza.core.common.Constants;
import vn.uiza.data.ActivityData;
import uizacoresdk.util.UZUtil;

public class LSApplication extends MultiDexApplication {
    private final String TAG = LSApplication.class.getSimpleName();
    private static LSApplication instance;
    private Gson gson;

    public static final String DF_DOMAIN_API = "loctbprod01.uiza.co";
    public static final String DF_TOKEN = "uap-9816792bb84642f09d843af4f93fb748-b94fcbd1";
    public static final String DF_APP_ID = "9816792bb84642f09d843af4f93fb748";

    /*public static final String DF_DOMAIN_API = "byterange01-api.uizadev.io";
    public static String DF_TOKEN = "uap-50ee0c6a7e104583bc8aa0944a59ab7a-6dede367";
    public static String DF_APP_ID = "50ee0c6a7e104583bc8aa0944a59ab7a";*/

    public final static int ENVIRONMENT = Constants.ENVIRONMENT_PROD;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (gson == null) {
            gson = new Gson();
        }
        //Utils.init(this);
        ActivityData.getInstance().setType(Constants.TYPE_ACTIVITY_TRANSITION_FADE);

        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/

        UZUtil.initWorkspace(this, DF_DOMAIN_API, DF_TOKEN, DF_APP_ID, ENVIRONMENT);
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
