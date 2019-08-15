package uiza.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import com.google.gson.Gson;
import io.uiza.core.util.constant.Constants;
import uizacoresdk.util.UZUtil;

public class LSApplication extends MultiDexApplication {
    private final String TAG = LSApplication.class.getSimpleName();
    private static LSApplication instance;
    private Gson gson;
    //TODO input information of your workspace
    public static final String DF_DOMAIN_API = "ap-southeast-1-api.uiza.co";
    public static final String DF_TOKEN = "uap-0dba39c3ea9d44d9b6b4e016ce10a703-46605f92";
    public static final String DF_APP_ID = "0dba39c3ea9d44d9b6b4e016ce10a703";
    public final static int ENVIRONMENT = Constants.ENVIRONMENT_PROD;
    public final static int API_VERSION = Constants.API_VERSION_4;

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
        UZUtil.initWorkspace(this, API_VERSION, DF_DOMAIN_API, DF_TOKEN, DF_APP_ID, ENVIRONMENT);
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
