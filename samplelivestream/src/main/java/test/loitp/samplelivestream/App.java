package test.loitp.samplelivestream;

import android.support.multidex.MultiDexApplication;

import com.google.gson.Gson;

import uizalivestream.uiza.UZUtil;
import vn.uiza.core.common.Constants;

public class App extends MultiDexApplication {
    /*public static final String DF_DOMAIN_API = "loctbprod01.uiza.co";
    public static final String DF_TOKEN = "uap-9816792bb84642f09d843af4f93fb748-b94fcbd1";
    public static final String DF_APP_ID = "9816792bb84642f09d843af4f93fb748";
    public static String entityIdDefaultLIVE_TRANSCODE = "04db355b-80d5-456e-aedb-aff1f8579e03";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "52128efd-59a7-4a3c-a2ec-988d031ccbe4";*/

    public static final String DF_DOMAIN_API = "teamplayer-api.uiza.co";
    public static final String DF_TOKEN = "uap-01e137ad1b534004ad822035bf89b29f-b9b31f29";
    public static final String DF_APP_ID = "01e137ad1b534004ad822035bf89b29f";
    public static String entityIdDefaultLIVE_TRANSCODE = "b8df8cdb-5d1b-40c1-b3e5-2879fb6c9625";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "1b811944-51c0-4592-81d7-73389002164e";

    private final int env = Constants.ENVIRONMENT_PROD;

    @Override
    public void onCreate() {
        super.onCreate();
        Constants.setDebugMode(true);
        UZUtil.initWorkspace(this, DF_DOMAIN_API, DF_TOKEN, DF_APP_ID);
    }
}
