package vn.uiza;

import timber.log.Timber;
import vn.uiza.core.common.Constants;

public class UizaBase {
    private UizaBase() {
    }

    private static class UizaBaseHelper {
        private static final UizaBase INSTANCE = new UizaBase();
    }

    public static UizaBase getInstance() {
        return UizaBaseHelper.INSTANCE;
    }

    // call init in onCreate of Application
    public void init() {
        if (Constants.IS_DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
