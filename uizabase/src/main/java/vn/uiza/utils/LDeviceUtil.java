package vn.uiza.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.UiModeManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import vn.uiza.core.common.Constants;
import vn.uiza.views.LToast;

import static android.content.Context.UI_MODE_SERVICE;


/**
 * File created on 11/14/2016.
 *
 * @author loitp
 */
public class LDeviceUtil {
    private static final String COPY_LABEL = "Copy";

    public static boolean isTablet(Activity activity) {
        return (activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public boolean isNavigationBarAvailable() {
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
        return (!(hasBackKey && hasHomeKey));
    }


    public static void setClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(COPY_LABEL, text);
        clipboard.setPrimaryClip(clip);
        LToast.show(context, "Copied!");
    }

    public static void vibrate(Context context, int length) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(length);
    }

    public static void vibrate(Context context) {
        vibrate(context, 300);
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isTV(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(UI_MODE_SERVICE);
        return uiModeManager != null && uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION;
    }

    //return true if device is set auto switch rotation on
    //return false if device is set auto switch rotation off
    public static boolean isRotationPossible(Context context) {
        if (context == null) {
            return false;
        }
        boolean hasAccelerometer = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
        return (hasAccelerometer && android.provider.Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1);
    }

    public static boolean isCanOverlay(Context context) {
        if (context == null) {
            return false;
        }
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context);
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(@NonNull Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static int getViewerOsArchitecture() {
        try {
            boolean isArm64 = false;
            BufferedReader localBufferedReader = new BufferedReader(new FileReader(Constants.CPU_INFO_FILENAME));
            if (localBufferedReader.readLine().contains(Constants.AARCH64)) {
                isArm64 = true;
            }
            localBufferedReader.close();
            return isArm64 ? 64 : 32;
        } catch (IOException e) {
            SentryUtils.captureException(e);
        }
        return 0;
    }
}
