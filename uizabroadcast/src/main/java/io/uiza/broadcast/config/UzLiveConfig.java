package io.uiza.broadcast.config;

import android.content.Context;
import io.uiza.core.api.client.UzRestClient;
import io.uiza.core.util.UzCoreUtil;
import io.uiza.core.util.constant.Constants;

public final class UzLiveConfig {

    private static final UzLiveConfig liveConfig = new UzLiveConfig();
    private String appId;
    private int apiVersion = Constants.API_VERSION_4;

    private UzLiveConfig() {
    }

    /**
     * Gets current live config.
     *
     * @return the current live config
     */
    public static UzLiveConfig getConfig() {
        if (liveConfig.appId == null) {
            throw new IllegalStateException(
                    "Init your live workspace first via UzLiveConfig.initWorkspace()!");
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
     * @param apiDomain the domain (without http or https prefix)
     * @param token the token (see SDK docs for how to get it)
     * @param appId the app id (see SDK docs for how to get it)
     */
    public static void initWorkspace(Context context, int apiVersion, String apiDomain,
            String token, String appId) {
        if (context == null) {
            throw new NullPointerException("Error: Context cannot be null");
        }
        if (apiDomain == null || apiDomain.isEmpty()) {
            throw new NullPointerException("Domain api cannot be null or empty");
        }
        if (token == null || token.isEmpty()) {
            throw new NullPointerException("Token cannot be null or empty");
        }
        if (appId == null || appId.isEmpty()) {
            throw new NullPointerException("App id be null or empty");
        }
        UzCoreUtil.init(context.getApplicationContext());
        UzRestClient.init(Constants.PREFIXS + apiDomain, token);
        liveConfig.setAppId(appId);
        liveConfig.setApiVersion(apiVersion);
    }
}
