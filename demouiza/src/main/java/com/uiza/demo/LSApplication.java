package com.uiza.demo;

import android.support.multidex.MultiDexApplication;
import com.google.gson.Gson;
import io.uiza.core.util.constant.Constants;
import io.uiza.player.UzPlayerConfig;

public class LSApplication extends MultiDexApplication {

    private static LSApplication instance;
    private Gson gson;
    //TODO input information of your workspace
    public static final String DF_DOMAIN_API_STAG = "stag-ap-southeast-1-api.uizadev.io";
    public static final String DF_DOMAIN_API_PROD = "ap-southeast-1-api.uiza.co";
    public static final String DF_TOKEN = "uap-9521cff34e86473095644ba71cbd0e7f-55b150c2";
    public static final String DF_APP_ID = "9521cff34e86473095644ba71cbd0e7f";
    public static String entityIdDefaultLIVE_TRANSCODE = "2b5b2ed6-66cd-48c7-bcd8-78846904bcf6";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "2997cbf0-6a25-42ef-955e-c11469c2ec6b";
    public static final int ENVIRONMENT = Constants.ENVIRONMENT_PROD;
    public static final int API_VERSION = Constants.API_VERSION_4;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (gson == null) {
            gson = new Gson();
        }
        UzPlayerConfig.initWorkspace(this, API_VERSION, DF_DOMAIN_API_PROD, DF_TOKEN, DF_APP_ID,
                ENVIRONMENT);
    }

    public Gson getGson() {
        return gson;
    }

    public static LSApplication getInstance() {
        return instance;
    }
}
