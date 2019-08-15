package test.loitp.samplelivestream;

import android.support.multidex.MultiDexApplication;
import io.uiza.broadcast.config.LiveConfig;
import io.uiza.core.util.constant.Constants;

public class App extends MultiDexApplication {

    //TODO input information of your workspace
    public static final String DF_DOMAIN_API = "ap-southeast-1-api.uiza.co";
    public static final String DF_TOKEN = "uap-f785bc511967473fbe6048ee5fb7ea59-69fefb79";
    public static final String DF_APP_ID = "f785bc511967473fbe6048ee5fb7ea59";
    public static String entityIdDefaultLIVE_TRANSCODE = "d8286085-180d-411e-84b1-935c2ccca1a4";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "34dfc318-9f6a-4540-a382-4574d67ecefc";

    @Override
    public void onCreate() {
        super.onCreate();
        Constants.setDebugMode(false);
        LiveConfig.initWorkspace(this, Constants.API_VERSION_4, DF_DOMAIN_API, DF_TOKEN, DF_APP_ID);
    }
}
