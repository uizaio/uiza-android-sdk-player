package test.loitp.samplelivestream;

import android.support.multidex.MultiDexApplication;

import uizalivestream.uiza.UZUtil;
import vn.uiza.core.common.Constants;

public class App extends MultiDexApplication {
    //TODO input information of your workspace
    public static final String DF_DOMAIN_API = "veep.uiza.co";
    public static final String DF_TOKEN = "uap-4f892058d35e46dd8a3a660481583b79-6a1319cb";
    public static final String DF_APP_ID = "4f892058d35e46dd8a3a660481583b79";
    public static String entityIdDefaultLIVE_TRANSCODE = "bd8f7e6d-f3e2-4c10-b4aa-0b12de17131e";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "d1ef74b5-7d6b-4ce0-a680-fdc1495b0b84";

    @Override
    public void onCreate() {
        super.onCreate();
        Constants.setDebugMode(true);
        UZUtil.initWorkspace(this, DF_DOMAIN_API, DF_TOKEN, DF_APP_ID);
    }
}
