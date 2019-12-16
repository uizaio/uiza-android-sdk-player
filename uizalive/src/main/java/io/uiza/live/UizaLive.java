package io.uiza.live;

import timber.log.Timber;

public class UizaLive {
    private UizaLive() {
    }

    private static class UizaLiveHelper {
        private static final UizaLive INSTANCE = new UizaLive();
    }

    public static UizaLive get() {
        return UizaLiveHelper.INSTANCE;
    }

    // Library init
    public void init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
