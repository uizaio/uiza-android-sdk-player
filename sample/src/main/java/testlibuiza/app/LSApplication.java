package testlibuiza.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.gson.Gson;

import testlibuiza.R;
import uizacoresdk.cache.UZCacheHelper;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.common.Constants;

public class LSApplication extends MultiDexApplication {
    private static LSApplication instance;
    private Gson gson;
    //TODO input information of your workspace
    public static final String DF_DOMAIN_API = "teamplayer.uiza.co";
    public static final String DF_TOKEN = "uap-01e137ad1b534004ad822035bf89b29f-b9b31f29";
    public static final String DF_APP_ID = "01e137ad1b534004ad822035bf89b29f";
    public static final int currentPlayerId = R.layout.uz_player_skin_1;
    public static final String entityIdDefaultVODLongtime = "85527b12-ae9a-4102-af60-c83054ffa213";
    public static final String entityIdDefaultVOD = "7699e10e-5ce3-4dab-a5ad-a615a711101e";
    public static final String thumbEntityIdDefaultVOD = "http://teamplayer-static.uizacdn.net/01e137ad1b534004ad822035bf89b29f-static/2018/09/24/7699e10e-5ce3-4dab-a5ad-a615a711101e/thumbnail-10-8-720.jpeg";
    public static final String entityIdDefaultVOD_21_9 = "85527b12-ae9a-4102-af60-c83054ffa213";
    public static final String entityIdDefaultVODportrait = "2732b570-6dc2-42df-bd58-3f7a0cac5683";
    public static final String entityIdDefaultLIVE = "8064b2ba-b228-4ea9-91ad-3736d96c3b24";
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
        Constants.setDebugMode(false);
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

    public DownloadManager getDownloadManager(){
        return UZCacheHelper.get().getDownloadManager();
    }
}
