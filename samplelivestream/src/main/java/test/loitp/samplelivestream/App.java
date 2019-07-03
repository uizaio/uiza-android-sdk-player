package test.loitp.samplelivestream;

import android.support.multidex.MultiDexApplication;

import uizalivestream.util.UZUtil;
import vn.uiza.core.common.Constants;

public class App extends MultiDexApplication {
    //TODO input information of your workspace
    public static final String DF_DOMAIN_API = "ap-southeast-1-api.uiza.co/";
    public static final String DF_TOKEN = "uap-b99d6b58428043ffbbc2091054ef3442-dae7e075";
    public static final String DF_APP_ID = "b99d6b58428043ffbbc2091054ef3442";
    public static String entityIdDefaultLIVE_TRANSCODE = "b21e1ecb-dab9-4435-b492-6681f2b1ff5d";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "aab8b32e-438a-4f86-8478-f82139de8902";

    @Override
    public void onCreate() {
        super.onCreate();
        Constants.setDebugMode(false);
        UZUtil.initWorkspace(this, Constants.API_VERSION_4, DF_DOMAIN_API, DF_TOKEN, DF_APP_ID);
    }
}
