package testlibuiza.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.gson.Gson;

import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LPref;
import vn.loitp.data.ActivityData;
import vn.loitp.restapi.restclient.RestClientTracking;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.restapi.uiza.model.v2.auth.Auth;
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
        ActivityData.getInstance().setType(Constants.TYPE_ACTIVITY_TRANSITION_SLIDEUP);

        //init uiza
        RestClientV2.init(Constants.URL_DEV_UIZA_VERSION_2);
        RestClientTracking.init(Constants.URL_TRACKING_DEV);
        Auth auth = getDummyAuth();
        RestClientV2.addAuthorization(auth.getData().getToken());
        LPref.setAuth(getContext(), auth, gson);
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

    public Auth getDummyAuth() {
        String json = "{\"data\":{\"token\":\"01faafee-6bc2-45ff-a116-b3cd82130ea0\",\"expired\":\"27/05/2018 08:21:59\",\"appId\":\"a204e9cdeca44948a33e0d012ef74e90\"},\"version\":2,\"datetime\":\"2018-04-27T08:21:59.407Z\",\"name\":\"Resource\",\"message\":\"ok\",\"code\":200,\"type\":\"SUCCESS\"}";
        return gson.fromJson(json, Auth.class);
    }
}
