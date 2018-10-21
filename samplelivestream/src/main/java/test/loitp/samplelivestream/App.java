package test.loitp.samplelivestream;

import android.support.multidex.MultiDexApplication;

import uizalivestream.uiza.UZUtil;
import vn.uiza.core.common.Constants;

public class App extends MultiDexApplication {
    public static final String DF_DOMAIN_API = "loctbprod01.uiza.co";
    public static final String DF_TOKEN = "uap-9816792bb84642f09d843af4f93fb748-b94fcbd1";
    public static final String DF_APP_ID = "9816792bb84642f09d843af4f93fb748";
    public static String entityIdDefaultVOD = "93d74510-5bf9-4b3c-8686-401db4cb6811";
    public static String entityIdDefaultLIVE = "ae8e7a65-b2f8-4803-a62c-6480e282616a";
    public static String metadataDefault0 = "1ae7d4ee-8e4e-402c-af30-e49d53f43ff3";
    public static String entityIdDefaultLIVE_TRANSCODE = "04db355b-80d5-456e-aedb-aff1f8579e03";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "52128efd-59a7-4a3c-a2ec-988d031ccbe4";
    private final int env = Constants.ENVIRONMENT_PROD;

    @Override
    public void onCreate() {
        super.onCreate();
        Constants.setDebugMode(true);
        UZUtil.initWorkspace(this, DF_DOMAIN_API, DF_TOKEN, DF_APP_ID);
    }
}
