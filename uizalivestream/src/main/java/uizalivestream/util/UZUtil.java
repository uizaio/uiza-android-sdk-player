package uizalivestream.util;

import android.content.Context;
import uizalivestream.data.UZLivestreamData;
import vn.uiza.core.common.Constants;
import vn.uiza.restapi.restclient.UZRestClient;
import vn.uiza.utils.util.Utils;

/**
 * Created by loitp on 4/11/2018.
 */

public class UZUtil {
    private final static String TAG = UZUtil.class.getSimpleName();

    public static void initWorkspace(Context context, int apiVersion, String domainApi, String token, String appId) {
        if (context == null) {
            throw new NullPointerException("Error: Context cannot be null");
        }
        if (domainApi == null || domainApi.isEmpty()) {
            throw new NullPointerException("Domain api cannot be null or empty");
        }
        if (token == null || token.isEmpty()) {
            throw new NullPointerException("Token cannot be null or empty");
        }
        if (appId == null || appId.isEmpty()) {
            throw new NullPointerException("App id be null or empty");
        }
        Utils.init(context.getApplicationContext());
        //UZUtil.setCurrentPlayerId(currentPlayerId);

        //UZData.getInstance().initSDK(domainApi, token, appId, env);
        UZRestClient.init(Constants.PREFIXS + domainApi, token);
        //UZUtil.setToken(Utils.getContext(), token);
        UZLivestreamData.getInstance().setAppId(appId);
        UZLivestreamData.getInstance().setAPIVersion(apiVersion);
    }
}
