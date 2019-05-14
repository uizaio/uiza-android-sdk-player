package vn.uiza.core.utilities;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;

import java.util.Random;

import vn.uiza.utils.util.SentryUtils;
import vn.uiza.views.LToast;

import static android.content.Context.UI_MODE_SERVICE;


/**
 * File created on 11/14/2016.
 *
 * @author loitp
 */
public class LDeviceUtil {
    private static String TAG = LDeviceUtil.class.getSimpleName();
    private static final String COPY_LABEL = "Copy";

    public static boolean isTablet(Activity activity) {
        return (activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public boolean isNavigationBarAvailable() {
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
        return (!(hasBackKey && hasHomeKey));
    }

    public static int getCurrentAndroidVersion(Activity activity) {
        int thisVersion;
        try {
            PackageInfo pi = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            thisVersion = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            thisVersion = 1;
            SentryUtils.captureException(e);
        }
        return thisVersion;
    }

    public static void setClipboard(Context context, String text) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText(COPY_LABEL, text);
            clipboard.setPrimaryClip(clip);
        }
        LToast.show(context, "Copied!");
    }

    public static void vibrate(Context context, int length) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(length);
    }

    public static void vibrate(Context context) {
        vibrate(context, 300);
    }

    public static int getRandomNumber(int max) {
        Random r = new Random();
        return r.nextInt(max);
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
}
