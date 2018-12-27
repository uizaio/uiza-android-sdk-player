package test.loitp.samplelivestream;

import android.support.multidex.MultiDexApplication;

import uizalivestream.util.UZUtil;
import vn.uiza.core.common.Constants;

public class App extends MultiDexApplication {
    //TODO input information of your workspace
    public static final String DF_DOMAIN_API = "teamplayer-api.uiza.co";
    public static final String DF_TOKEN = "uap-01e137ad1b534004ad822035bf89b29f-b9b31f29";
    public static final String DF_APP_ID = "01e137ad1b534004ad822035bf89b29f";
    public static String entityIdDefaultLIVE_TRANSCODE = "58240424-62f6-4f2b-9ddb-b3a49a3074a5";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "8ae1be2b-3cd5-478c-94c0-6598d5cad4f8";

    @Override
    public void onCreate() {
        super.onCreate();
        Constants.setDebugMode(true);
        UZUtil.initWorkspace(this, DF_DOMAIN_API, DF_TOKEN, DF_APP_ID);
    }
}
