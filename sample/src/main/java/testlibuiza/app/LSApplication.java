package testlibuiza.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.gson.Gson;

import vn.uiza.core.common.Constants;
import vn.uiza.restapi.restclient.RestClientTracking;
import vn.uiza.restapi.restclient.RestClientV2;
import vn.uiza.uzv3.util.UZUtil;

public class LSApplication extends MultiDexApplication {
    private static LSApplication instance;
    private Gson gson;

    //workspace prod
    private final String DF_DOMAIN_API = "teamplayer.uiza.co";
    private final String DF_TOKEN = "uap-01e137ad1b534004ad822035bf89b29f-b9b31f29";
    private final String DF_APP_ID = "01e137ad1b534004ad822035bf89b29f";
    private final int currentPlayerId = loitp.core.R.layout.uz_player_skin_1;
    public static String entityIdDefaultVOD = "7699e10e-5ce3-4dab-a5ad-a615a711101e";
    public static String entityIdDefaultLIVE = "1759f642-e062-4e88-b5f2-e3022bd03b57";
    public static String metadataDefault0 = "53c2e63e-6ddf-4259-8159-cb43371943d1";
    public static String entityIdDefaultLIVE_TRANSCODE = "b8df8cdb-5d1b-40c1-b3e5-2879fb6c9625";
    public static String entityIdDefaultLIVE_NO_TRANSCODE = "1b811944-51c0-4592-81d7-73389002164e";
    private final int env = Constants.ENVIRONMENT_PROD;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (gson == null) {
            gson = new Gson();
        }
        //config activity transition default
        //ActivityData.getInstance().setType(Constants.TYPE_ACTIVITY_TRANSITION_FADE);

        RestClientV2.init(Constants.URL_DEV_UIZA_VERSION_2_STAG);
        RestClientTracking.init(Constants.URL_TRACKING_STAG);
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
}
