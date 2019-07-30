package vn.uiza.core.utilities;

import android.app.UiModeManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;

public class LDeviceUtil {

    /**
     * Vibrate the device in specific duration
     * @param context the context
     * @param duration the duration in milliseconds
     */
    public static void vibrate(Context context, long duration) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(duration);
    }

    /**
     * Vibrate device in 300 milliseconds
     * @see LDeviceUtil#vibrate(Context, long)
     */
    public static void vibrate(Context context) {
        vibrate(context, 300);
    }

    /**
     * Check if device is tablet or not
     * @param context the context
     * @return true if device is tablet, otherwise false
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * Check if device is Television or not
     * @param context the context
     * @return true if device is Television, otherwise false
     */
    public static boolean isTV(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager != null && uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION;
    }

    /**
     * Check if the device is set auto rotation or not
     * @param context the context
     * @return true if device is set auto rotation, otherwise false
     */
    public static boolean isRotationPossible(Context context) {
        if (context == null) {
            return false;
        }
        boolean hasAccelerometer = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
        return (hasAccelerometer && Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1);
    }

    /**
     * Check if the app has Overlay permission or not
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
     * Get current device's volume percentage
     * @param context the context
     * @param streamType the stream type whose volume index is returned. Ex. {@link AudioManager#STREAM_MUSIC}
     * @return the current device's volume percentage
     */
    public static int getVolumePercentage(@NonNull Context context, int streamType) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audioManager.getStreamVolume(streamType);
        int maxVolume = audioManager.getStreamMaxVolume(streamType);
        return Math.round(currentVolume * 1f / maxVolume * 100);
    }
}
