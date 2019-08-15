package io.uiza.core.util;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;

public final class UzCommonUtil {

    private UzCommonUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String getAppPackageName() {
        return UzCoreUtil.getContext().getPackageName();
    }

    public static String getAppName() {
        return getAppName(UzCoreUtil.getContext().getPackageName());
    }

    public static String getAppName(String packageName) {
        if (hasSpace(packageName)) {
            return null;
        }
        try {
            PackageManager pm = UzCoreUtil.getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            SentryUtil.captureException(e);
            return null;
        }
    }

    public static int getAppVersionCode() {
        return getAppVersionCode(UzCoreUtil.getContext().getPackageName());
    }

    public static int getAppVersionCode(String packageName) {
        if (hasSpace(packageName)) {
            return -1;
        }
        try {
            PackageManager pm = UzCoreUtil.getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            SentryUtil.captureException(e);
            return -1;
        }
    }

    public static boolean hasSpace(String str) {
        if (str == null) {
            return true;
        }
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

    /**
     * Vibrate the device in specific duration.
     *
     * @param context the context
     * @param duration the duration in milliseconds
     */
    public static void vibrate(Context context, long duration) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(duration);
    }

    /**
     * Vibrate device in 300 milliseconds.
     *
     * @see UzCommonUtil#vibrate(Context, long)
     */
    public static void vibrate(Context context) {
        vibrate(context, 300);
    }

    /**
     * Check if device is tablet or not.
     *
     * @param context the context
     * @return true if device is tablet, otherwise false
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * Check if device is Television or not.
     *
     * @param context the context
     * @return true if device is Television, otherwise false
     */
    public static boolean isTV(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context
                .getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager != null
                && uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION;
    }

    /**
     * Check if the device is set auto rotation or not.
     *
     * @param context the context
     * @return true if device is set auto rotation, otherwise false
     */
    public static boolean isRotationPossible(Context context) {
        if (context == null) {
            return false;
        }
        boolean hasAccelerometer = context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
        return (hasAccelerometer && Settings.System
                .getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0)
                == 1);
    }

    /**
     * Check if the app has Overlay permission or not.
     *
     * @param context the context
     * @return true if app has Overlay permission, otherwise false
     */
    public static boolean isCanOverlay(Context context) {
        if (context == null) {
            return false;
        }
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context);
    }

    /**
     * Get current device's volume percentage.
     *
     * @param context the context
     * @param streamType the stream type whose volume index is returned. Ex. {@link
     * AudioManager#STREAM_MUSIC}
     * @return the current device's volume percentage
     */
    public static int getVolumePercentage(@NonNull Context context, int streamType) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audioManager.getStreamVolume(streamType);
        int maxVolume = audioManager.getStreamMaxVolume(streamType);
        return Math.round(currentVolume * 1f / maxVolume * 100);
    }
}