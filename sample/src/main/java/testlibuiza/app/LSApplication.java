package testlibuiza.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.gson.Gson;

import testlibuiza.R;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.common.Constants;

public class LSApplication extends MultiDexApplication {
    private static LSApplication instance;
    private Gson gson;
    //TODO input information of your workspace
    public static final String DF_DOMAIN_API = "edm.uiza.co";
    public static final String DF_TOKEN = "uap-a9383d04d7d0420bae10dbf96bb27d9b-60d5c644";
    public static final String DF_APP_ID = "a9383d04d7d0420bae10dbf96bb27d9b";
    public static final int currentPlayerId = R.layout.uz_player_skin_1;
    public static final String entityIdDefaultVODLongtime = "85527b12-ae9a-4102-af60-c83054ffa213";
    public static final String entityIdDefaultVOD = "ffc91430-c46f-47cb-97e4-f83c4fd0fe21";
    public static final String thumbEntityIdDefaultVOD = "http://teamplayer-static.uizacdn.net/01e137ad1b534004ad822035bf89b29f-static/2018/09/24/7699e10e-5ce3-4dab-a5ad-a615a711101e/thumbnail-10-8-720.jpeg";
    public static final String entityIdDefaultVOD_21_9 = "85527b12-ae9a-4102-af60-c83054ffa213";
    public static final String entityIdDefaultVODportrait = "2732b570-6dc2-42df-bd58-3f7a0cac5683";
    public static final String entityIdDefaultLIVE = "bbe5b36e-155a-4f77-8e0f-f358c4f1f46a";
    public static final String metadataDefault0 = "53c2e63e-6ddf-4259-8159-cb43371943d1";
    public static String entityIdDefaultLIVE_TRANSCODE = "58240424-62f6-4f2b-9ddb-b3a49a3074a5";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "8ae1be2b-3cd5-478c-94c0-6598d5cad4f8";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (gson == null) {
            gson = new Gson();
        }
        Constants.setDebugMode(true);
        UZUtil.initWorkspace(this, DF_DOMAIN_API, DF_TOKEN, DF_APP_ID);
    }

    public Gson getGson() {
        return gson;
    }

    public static LSApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
