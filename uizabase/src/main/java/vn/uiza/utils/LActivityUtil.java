package vn.uiza.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import vn.uiza.R;
import vn.uiza.core.common.Constants;
import vn.uiza.data.ActivityData;

/**
 * Created by www.muathu@gmail.com on 1/3/2018.
 */

public class LActivityUtil {

    // This snippet hides the system bars.
    public static void hideSystemUI(View mDecorView) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    public static void showSystemUI(View mDecorView) {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }


    public static void toggleFullScreen(@NonNull Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags ^= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(attrs);
    }

    /*
     **return true if ORIENTATION_LANDSCAPE->SCREEN_ORIENTATION_SENSOR_PORTRAIT
     **return false if ORIENTATION_PORTRAIT->SCREEN_ORIENTATION_SENSOR_LANDSCAPE
     */
    public static boolean toggleScreenOritation(final Activity activity) {
        int s = getScreenOrientation(activity);
        if (s == Configuration.ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            return true;
        } else if (s == Configuration.ORIENTATION_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);//xoay qua xoay lai landscape
            //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);//ko thay gi xay ra
            //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);//xoay tum lum, nhung khi nhan full thi no k xoay sang landscape
            //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER);//co ve nhu gan giong full sensor
            //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//chi xoay dc 1 landscape ben trai
            //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);//hinh nhu la no lock luon cai orientation hien tai
            //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//xoay portrait
            //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);//xoay landscape ben phai
            //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);//xoay portrait ben tren
            //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);//xoay dc landscape trai phai va portrait duoi, ko xoay dc portrait tren
            //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//ko con sensor
            //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);//xoay tum lum, nhung khi nhan full thi no k xoay sang landscape
            //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            return false;
        }
        return false;
    }

    public static void changeScreenPortrait(Activity activity) {
        if (activity == null) {
            return;
        }
        int s = getScreenOrientation(activity);
        if (s == Configuration.ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }

    public static void changeScreenLandscape(Activity activity) {
        if (activity == null) {
            return;
        }
        int s = getScreenOrientation(activity);
        if (s == Configuration.ORIENTATION_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
    }

    public static void changeScreenLandscape(Activity activity, int orientation) {
        if (activity == null) {
            return;
        }
        int s = getScreenOrientation(activity);
        if (s == Configuration.ORIENTATION_PORTRAIT) {
            if (orientation == 90) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            } else if (orientation == 270) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
    }

    public static int getScreenOrientation(Activity activity) {
        return activity.getResources().getConfiguration().orientation;
    }
}
