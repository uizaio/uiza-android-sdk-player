package io.uiza.core.util;

import android.util.Log;
import io.uiza.core.util.constant.Constants;

public class LLog {
    public static void d(String tag, String msg) {
        if (Constants.IS_DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (Constants.IS_DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (Constants.IS_DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (Constants.IS_DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (Constants.IS_DEBUG) {
            Log.w(tag, msg);
        }
    }
}