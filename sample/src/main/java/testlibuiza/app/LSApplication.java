package testlibuiza.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.gson.Gson;

import vn.loitp.core.common.Constants;
import vn.loitp.restapi.restclient.RestClientTracking;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.utils.util.Utils;

public class LSApplication extends MultiDexApplication {
    private static LSApplication instance;
    private Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (gson == null) {
            gson = new Gson();
        }
        Utils.init(this);
        //config activity transition default
        //ActivityData.getInstance().setType(Constants.TYPE_ACTIVITY_TRANSITION_FADE);

        //init uiza
        /*RestClientV2.init(Constants.URL_DEV_UIZA_VERSION_2_DEMO);
        RestClientTracking.init(Constants.URL_TRACKING_PROD);
        Auth auth = getDummyAuthDemo();*/

        RestClientV2.init(Constants.URL_DEV_UIZA_VERSION_2_STAG);
        RestClientTracking.init(Constants.URL_TRACKING_STAG);
        //Auth auth = getDummyAuthDev();

        //RestClientV2.addAuthorization(auth.getData().getToken());
        //LPref.setAuth(getContext(), auth, gson);

        Constants.setDebugMode(false);
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

    /*private Auth getDummyAuthDev() {
        String json = "{\"code\":200,\"data\":{\"appId\":\"a204e9cdeca44948a33e0d012ef74e90\",\"expired\":\"18/07/2018 07:33:16\",\"token\":\"d44d6205-0be9-4795-b8b4-14872a31f65d\"},\"datetime\":\"2018-06-18T07:33:16.843Z\",\"message\":\"ok\",\"name\":\"Resource\",\"type\":\"SUCCESS\",\"version\":2}";
        return gson.fromJson(json, Auth.class);
    }

    private Auth getDummyAuthDemo() {
        String json = "{\"code\":200,\"data\":{\"appId\":\"0fa01cc4bc264023850069c3e07a0a38\",\"expired\":\"01/06/2018 10:43:17\",\"token\":\"3fcc5411-399e-4607-9991-6ab5d1c99e6e\"},\"datetime\":\"2018-05-02T10:43:17.180Z\",\"message\":\"ok\",\"name\":\"Resource\",\"type\":\"SUCCESS\",\"version\":2}";
        return gson.fromJson(json, Auth.class);
    }*/
}
