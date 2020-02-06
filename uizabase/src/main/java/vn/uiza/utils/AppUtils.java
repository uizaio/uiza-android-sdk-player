package vn.uiza.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import java.io.Closeable;
import java.io.IOException;

import timber.log.Timber;

public final class AppUtils {

    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    public static boolean checkChromeCastAvailable() {
        return AppUtils.isDependencyAvailable("com.google.android.gms.cast.framework.OptionsProvider")
                && AppUtils.isDependencyAvailable("androidx.mediarouter.app.MediaRouteButton");
    }

    public static boolean isAdsDependencyAvailable() {
        return AppUtils.isDependencyAvailable("com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer");
    }

    public static boolean isDependencyAvailable(String dependencyClass) {
        try {
            Class.forName(dependencyClass);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String getAppName(@NonNull Context context) {
        return getAppName(context.getPackageManager(), context.getPackageName());
    }

    public static String getAppName(PackageManager pm, String packageName) {
        if (isSpace(packageName)) return null;
        try {
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e);
            return null;
        }
    }


    public static boolean isSpace(String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static void closeIO(Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}