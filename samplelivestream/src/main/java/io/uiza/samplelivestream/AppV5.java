package io.uiza.samplelivestream;

import androidx.multidex.MultiDexApplication;

import uizalivestream.util.UZUtil;
import vn.uiza.core.common.Constants;

/**
 * V5
 */

public class AppV5 extends MultiDexApplication {
    //TODO input information of your workspace
    public static final String DF_DOMAIN_API = "teamplayer-api.uiza.co";
    public static final String DF_TOKEN = "uap-01e137ad1b534004ad822035bf89b29f-b9b31f29";
    public static final String DF_APP_ID = "01e137ad1b534004ad822035bf89b29f";
    public static String entityIdDefaultLIVE_TRANSCODE = "ee375e25-5373-4b7e-8f5d-4a70eb539a5a";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "aab8b32e-438a-4f86-8478-f82139de8902";

    @Override
    public void onCreate() {
        super.onCreate();
        Constants.setDebugMode(false);
        UZUtil.initWorkspace(this, DF_DOMAIN_API, DF_TOKEN, DF_APP_ID);
    }
}
