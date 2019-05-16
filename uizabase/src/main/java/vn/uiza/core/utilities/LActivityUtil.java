package vn.uiza.core.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.View;
import android.view.WindowManager;

import vn.uiza.R;
import vn.uiza.core.common.Constants;
import vn.uiza.data.ActivityData;

/**
 * Created by www.muathu@gmail.com on 1/3/2018.
 */

public class LActivityUtil {
    private final static String TAG = LActivityUtil.class.getSimpleName();

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

    public static void tranIn(Context context) {
        int typeActivityTransition = ActivityData.getInstance().getType();
        if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_NO_ANIM) {
            LActivityUtil.transActivityNoAniamtion(context);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SYSTEM_DEFAULT) {
            //do nothing
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SLIDELEFT) {
            LActivityUtil.slideLeft(context);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SLIDERIGHT) {
            LActivityUtil.slideRight(context);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SLIDEDOWN) {
            LActivityUtil.slideDown(context);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SLIDEUP) {
            LActivityUtil.slideUp(context);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_FADE) {
            LActivityUtil.fade(context);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_ZOOM) {
            LActivityUtil.zoom(context);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_WINDMILL) {
            LActivityUtil.windmill(context);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_DIAGONAL) {
            LActivityUtil.diagonal(context);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SPIN) {
            LActivityUtil.spin(context);
        }
    }

    public static void tranOut(Context context) {
        int typeActivityTransition = ActivityData.getInstance().getType();
        if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_NO_ANIM) {
            LActivityUtil.transActivityNoAniamtion(context);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SYSTEM_DEFAULT) {
            //do nothing
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SLIDELEFT) {
            LActivityUtil.slideRight(context);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SLIDERIGHT) {
            LActivityUtil.slideLeft(context);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SLIDEDOWN) {
            LActivityUtil.slideUp(context);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SLIDEUP) {
            LActivityUtil.slideDown(context);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_FADE) {
            LActivityUtil.fade(context);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_ZOOM) {
            LActivityUtil.zoom(context);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_WINDMILL) {
            LActivityUtil.windmill(context);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_DIAGONAL) {
            LActivityUtil.diagonal(context);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SPIN) {
            LActivityUtil.spin(context);
        }
    }

    public static void transActivityNoAniamtion(Context context) {
        ((Activity) context).overridePendingTransition(0, 0);
    }

    public static void slideLeft(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
    }

    public static void slideRight(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public static void slideDown(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit);
    }

    public static void slideUp(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.slide_up_enter, R.anim.slide_up_exit);
    }

    public static void zoom(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
    }

    public static void fade(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.fade_enter, R.anim.fade_exit);
    }

    public static void windmill(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.windmill_enter, R.anim.windmill_exit);
    }

    public static void spin(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.spin_enter, R.anim.spin_exit);
    }

    public static void diagonal(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.diagonal_right_enter, R.anim.diagonal_right_exit);
    }

    public static void toggleFullScreen(Activity activity) {
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
