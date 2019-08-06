package io.uiza.broadcast.config;

import android.content.Context;
import vn.uiza.core.common.Constants;
import vn.uiza.restapi.restclient.UZRestClient;
import vn.uiza.utils.util.Utils;

public final class LiveConfig {

    private static final LiveConfig liveConfig = new LiveConfig();
    private String appId;
    private int apiVersion = Constants.API_VERSION_4;

    private LiveConfig() {
    }

    /**
     * Gets current live config.
     *
     * @return the current live config
     */
    public static LiveConfig getConfig() {
        if (liveConfig.appId == null) {
            throw new IllegalStateException(
                    "Init your live workspace first via LiveConfig.initWorkspace()!");
        }
        return liveConfig;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getApiVersion() {
        return "v" + apiVersion;
    }

    public void setApiVersion(int apiVersion) {
        this.apiVersion = apiVersion;
    }

    /**
     * Init the Uiza workspace for your livestream.
     *
     * @param context the context
     * @param apiVersion the current Uiza API version
     * @param domainApi the domain (without http or https prefix)
     * @param token the token (see SDK docs for how to get it)
     * @param appId the app id (see SDK docs for how to get it)
     */
    public static void initWorkspace(Context context, int apiVersion, String domainApi,
            String token, String appId) {
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
        UZRestClient.init(Constants.PREFIXS + domainApi, token);
        liveConfig.setAppId(appId);
        liveConfig.setApiVersion(apiVersion);
    }
}
