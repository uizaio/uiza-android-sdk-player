package test.loitp.samplelivestream;

import android.support.multidex.MultiDexApplication;

import uizalivestream.util.UZUtil;
import vn.uiza.core.common.Constants;

public class App extends MultiDexApplication {
    //TODO input information of your workspace
    public static final String DF_DOMAIN_API = "stag-ap-southeast-1-api.uizadev.io";
    public static final String DF_TOKEN = "uap-998a1a17138644428ce028d2de20c5a0-53d927eb";
    public static final String DF_APP_ID = "998a1a17138644428ce028d2de20c5a0";
    public static String entityIdDefaultLIVE_TRANSCODE = "7cb5b495-bf41-41ac-be67-5307cc1cb1a9";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "34dfc318-9f6a-4540-a382-4574d67ecefc";

    @Override
    public void onCreate() {
        super.onCreate();
        Constants.setDebugMode(false);
        UZUtil.initWorkspace(this, Constants.API_VERSION_4, DF_DOMAIN_API, DF_TOKEN, DF_APP_ID);
    }
}
