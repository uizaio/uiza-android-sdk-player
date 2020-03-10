package test.loitp.samplelivestream;

import androidx.multidex.MultiDexApplication;

import uizalivestream.util.UZUtil;
import vn.uiza.core.common.Constants;

public class App extends MultiDexApplication {
    //TODO input information of your workspace
    public static final String DF_DOMAIN_API = "ap-southeast-1-api.uiza.co";
//    public static final String DF_TOKEN = "uap-f785bc511967473fbe6048ee5fb7ea59-69fefb79";
//    public static final String DF_APP_ID = "f785bc511967473fbe6048ee5fb7ea59";
    public static final String DF_TOKEN = "uap-16d51c83b5894e33bc8fe9418bd94151-3516e4ff";
    public static final String DF_APP_ID = "16d51c83b5894e33bc8fe9418bd94151";
//    public static String entityIdDefaultLIVE_TRANSCODE = "7b1b7cf3-d451-48c9-a57a-73e44e87f032";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "6faac381-2c5d-4fb6-a3f1-29efd67ed88e";

    @Override
    public void onCreate() {
        super.onCreate();
        Constants.setDebugMode(false);
        UZUtil.initWorkspace(this, Constants.API_VERSION_4, DF_DOMAIN_API, DF_TOKEN, DF_APP_ID);
    }
}
