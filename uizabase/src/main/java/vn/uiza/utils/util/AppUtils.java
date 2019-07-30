package vn.uiza.utils.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

public final class AppUtils {

    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String getAppPackageName() {
        return Utils.getContext().getPackageName();
    }

    public static String getAppName() {
        return getAppName(Utils.getContext().getPackageName());
    }

    public static String getAppName(String packageName) {
        if (hasSpace(packageName)) return null;
        try {
            PackageManager pm = Utils.getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            SentryUtils.captureException(e);
            return null;
        }
    }

    public static int getAppVersionCode() {
        return getAppVersionCode(Utils.getContext().getPackageName());
    }

    public static int getAppVersionCode(String packageName) {
        if (hasSpace(packageName)) return -1;
        try {
            PackageManager pm = Utils.getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            SentryUtils.captureException(e);
            return -1;
        }
    }

    public static boolean hasSpace(String str) {
        if (str == null) return true;
        for (int i = 0, len = str.length(); i < len; ++i) {
            if (Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static void openUrlInBrowser(Context context, String url) {
        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}