package uiza.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.gson.Gson;

import uizacoresdk.util.UZUtil;
import vn.uiza.core.common.Constants;

public class LSApplication extends MultiDexApplication {
    private final String TAG = LSApplication.class.getSimpleName();
    private static LSApplication instance;
    private Gson gson;
    //TODO input information of your workspace
    /*public static final String DF_DOMAIN_API = "teamplayer-api.uiza.co";
    public static final String DF_TOKEN = "uap-01e137ad1b534004ad822035bf89b29f-b9b31f29";
    public static final String DF_APP_ID = "01e137ad1b534004ad822035bf89b29f";
    public final static int ENVIRONMENT = Constants.ENVIRONMENT_PROD;*/

    public static final String DF_DOMAIN_API = "http://chivas69.uizadev.io/";
    public static final String DF_TOKEN = "uap-152a5e7cca9a41948c88dbb96e705aaf-929e125e";
    public static final String DF_APP_ID = "152a5e7cca9a41948c88dbb96e705aaf";
    public final static int ENVIRONMENT = Constants.ENVIRONMENT_DEV;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (gson == null) {
            gson = new Gson();
        }
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
