package test.loitp.samplelivestream;

import android.support.multidex.MultiDexApplication;

import uizalivestream.uiza.UZUtil;
import vn.uiza.core.common.Constants;

public class App extends MultiDexApplication {
    //TODO input information of your workspace
    public static final String DF_DOMAIN_API = "input";
    public static final String DF_TOKEN = "input";
    public static final String DF_APP_ID = "input";
    public static String entityIdDefaultLIVE_TRANSCODE = "input";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "input";

    @Override
    public void onCreate() {
        super.onCreate();
        Constants.setDebugMode(false);
        UZUtil.initWorkspace(this, DF_DOMAIN_API, DF_TOKEN, DF_APP_ID);
    }
}
