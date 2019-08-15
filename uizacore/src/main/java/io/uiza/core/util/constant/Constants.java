package io.uiza.core.util.constant;

public class Constants {
    public static boolean IS_DEBUG = false;
    public static final String PLAYER_NAME = "UZSDK";
    public static final String USER_AGENT = "UizaSDK-Android";
    public static final String PLAYER_SDK_VERSION = "4.0.27";
    public static final int API_VERSION_3 = 3;
    public static final int API_VERSION_4 = 4;

    public static void setDebugMode(boolean isDebugEnable) {
        IS_DEBUG = isDebugEnable;
    }

    public static final int NOT_FOUND = -404;
    public static final int UNKNOWN = -400;

    public static final String KEY_UIZA_ENTITY_ID = "KEY_UIZA_ENTITY_ID";
    public static final String KEY_UIZA_IS_LIVE = "KEY_UIZA_IS_LIVE";
    public static final String KEY_UIZA_METADATA_ENTITY_ID = "KEY_UIZA_METADATA_ENTITY_ID";
    public static final String KEY_UIZA_THUMBNAIL = "KEY_UIZA_THUMBNAIL";

    public static final String URL_IMG_POSTER = "https://static.uiza.io/2017/11/27/uiza-logo-demo-mobile.png";
    public static final String URL_IMG_THUMBNAIL_BLACK = "https://static.uiza.io/black_1px.jpg";
    public static final String URL_IMG_THUMBNAIL = "https://static.uiza.io/2017/11/27/uiza-logo-demo-mobile.png";
    public static final String URL_IMG_THUMBNAIL_2 = "https://static.uiza.io/2017/11/27/uiza-logo-1511755911349_1511755913189.png";

    public static final String PREFIX = "http://";
    public static final String PREFIXS = "https://";

    public static final String URL_GET_LINK_PLAY_DEV = "https://dev-ucc.uizadev.io/";
    public static final String URL_GET_LINK_PLAY_STAG = "https://stag-ucc.uizadev.io/";
    public static final String URL_GET_LINK_PLAY_PROD = "https://ucc.uiza.io/";

    public static final String URL_TRACKING_DEV = "https://dev-tracking.uizadev.io/analytic-tracking/";
    public static final String URL_TRACKING_STAG = "https://stag-tracking.uiza.io/analytic-tracking/";
    public static final String URL_TRACKING_PROD = "https://tracking.uiza.io/analytic-tracking/";

    public static final String TRACKING_ACCESS_TOKEN_DEV = "kv8O7hLkeDtN3EBviXLD01gzNz2RP2nA";
    public static final String TRACKING_ACCESS_TOKEN_STAG = "082c2cbf515648db96069fa660523247";
    public static final String TRACKING_ACCESS_TOKEN_PROD = "27cdc337bd65420f8a88cfbd9cf8577a";

    public static final String URL_HEART_BEAT_DEV = "https://dev-heartbeat.uizadev.io/";
    public static final String URL_HEART_BEAT_STAG = "https://stag-heartbeat.uizadev.io/";
    public static final String URL_HEART_BEAT_PROD = "https://heartbeat.uiza.io/";

    public static final String URL_DEV_UIZA_VERSION_2 = "http://dev-api.uiza.io/";
    public static final String URL_DEV_UIZA_VERSION_2_STAG = "https://uqc-api.uiza.io/";
    public static final String URL_DEV_UIZA_VERSION_2_DEMO = "https://demo-api.uiza.io/";

    public static final int ENVIRONMENT_DEV = 1;
    public static final int ENVIRONMENT_STAG = 2;
    public static final int ENVIRONMENT_PROD = 3;

    public static final int PLAYTHROUGH_25 = 25;
    public static final int PLAYTHROUGH_50 = 50;
    public static final int PLAYTHROUGH_75 = 75;
    public static final int PLAYTHROUGH_100 = 98;

    public static final String FLOAT_USER_USE_CUSTOM_LINK_PLAY = "FLOAT_USER_USE_CUSTOM_LINK_PLAY";
    public static final String FLOAT_LINK_PLAY = "FLOAT_LINK_PLAY";
    public static final String FLOAT_CONTENT_POSITION = "FLOAT_CONTENT_POSITION";
    public static final String FLOAT_PROGRESS_BAR_COLOR = "FLOAT_PROGRESS_BAR_COLOR";
    public static final String FLOAT_IS_LIVESTREAM = "FLOAT_IS_LIVESTREAM";
    public static final String FLOAT_UUID = "FLOAT_UUID";
    public static final String FLOAT_CONTROL_SKIN_ID = "FLOAT_CONTROL_SKIN_ID";

    public static final int ANIMATION_DURATION = 200;

    public static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 6969;

    public static final int RATIO_LAND_TABLET = 24;
    public static final int RATIO_PORTRAIT_TABLET = 20;

    public static final int RATIO_LAND_MOBILE = 18;
    public static final int RATIO_PORTRAIT_MOBILE = 12;

    public static final String LAST_PROCESS_START = "start";
    public static final String LAST_PROCESS_STOP = "stop";

    public static final String MODE_PULL = "pull";
    public static final String MODE_PUSH = "push";

    public static final String SUCCESS = "success";
    public static final String NOT_READY = "not-ready";

    public static final String PLATFORM_ANDROID = "android";

    public static final float RATIO_9_16 = 9f / 16f;

    public static final String DRM_SCHEME_NULL = null;
    public static final String DRM_LICENSE_URL = "https://wv.service.expressplay.com/hms/wv/rights/?ExpressPlayToken=BAAaXbkVKbEAAABg_0gifyfSLlqtjYGc9boiYUIudGi445e5xHzay2CzEazC0uj6GWg79k_yexpv7t2GmjWF10ehecUV2kqV5MBWM-7kURuaQcSJ368ocXFpcoT4l2EXQO8_9R67vZC3Y9lDqLE-9_FTTIqg7C-oWLoXZgWAmJQ";

    public static final int W_320 = 320;
    public static final int W_180 = 180;

    public static final boolean DF_PLAYER_IS_AUTO_START = true;

    // FIXME: SentryDSN, try to save dsn more securely
    public static final String SENTRY_ENVIRONMENT_STAG = "STAG";
    public static final String SENTRY_ENVIRONMENT_GA = "GA";
    public static final String RELEASE = "release";
    public static final String SENTRY_DSN = "https://dcc976635e4c4732a16d984e7dfade70@sentry.io/1453020?environment=";

    public static final String PACKAGE = "package";
    public static final String CPU_INFO_FILENAME = "/proc/cpuinfo";
    public static final String AARCH64 = "aarch64";

    // type of subtitle that we support
    public static final String TYPE_VTT = ".VTT";
    public static final String TYPE_SRT = ".SRT";

}
