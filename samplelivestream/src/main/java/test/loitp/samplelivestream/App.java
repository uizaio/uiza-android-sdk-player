package test.loitp.samplelivestream;

import android.support.multidex.MultiDexApplication;

import uizalivestream.util.UZUtil;
import vn.uiza.core.common.Constants;

public class App extends MultiDexApplication {
    //TODO input information of your workspace
    public static final String DF_DOMAIN_API = "ap-southeast-1-api.uiza.co";
    public static final String DF_TOKEN = "uap-5c4390970136424dae1c69248e826916-14746733";
    public static final String DF_APP_ID = "5c4390970136424dae1c69248e826916";
    public static String entityIdDefaultLIVE_TRANSCODE = "c599c9a0-0bd5-4ec3-b5a6-515ebaa09383";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "34dfc318-9f6a-4540-a382-4574d67ecefc";

    @Override
    public void onCreate() {
        super.onCreate();
        Constants.setDebugMode(false);
        UZUtil.initWorkspace(this, Constants.API_VERSION_4, DF_DOMAIN_API, DF_TOKEN, DF_APP_ID);
    }
}
