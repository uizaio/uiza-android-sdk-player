package io.uiza.core.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.uiza.core.BuildConfig;
import io.uiza.core.util.constant.Constants;

public final class UzCoreUtil {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private UzCoreUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(Context context) {
        UzCoreUtil.context = context.getApplicationContext();
        initSentry(context, BuildConfig.BUILD_TYPE.equalsIgnoreCase(Constants.RELEASE));
    }

    public static Context getContext() {
        if (context != null) {
            return context;
        }
        throw new NullPointerException("context should be init first");
    }

    private static void initSentry(@NonNull Context context, boolean isReleaseBuild) {
        String sentryDsn;
        if (isReleaseBuild) {
            sentryDsn = Constants.SENTRY_DSN + Constants.SENTRY_ENVIRONMENT_GA;
        } else {
            sentryDsn = Constants.SENTRY_DSN + Constants.SENTRY_ENVIRONMENT_STAG;
        }
        Sentry.init(sentryDsn, new AndroidSentryClientFactory(context));
    }
}
