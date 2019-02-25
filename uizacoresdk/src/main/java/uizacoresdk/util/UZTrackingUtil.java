package uizacoresdk.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by loitp on 7/19/2018.
 */

public class UZTrackingUtil {
    private final static String PREFERENCES_FILE_NAME = "UZTrackingUtil";

    public static final String E_DISPLAY = "E_DISPLAY";
    public static final String E_PLAYS_REQUESTED = "E_PLAYS_REQUESTED";
    public static final String E_VIDEO_STARTS = "E_VIDEO_STARTS";
    public static final String E_VIEW = "E_VIEW";
    public static final String E_REPLAY = "E_REPLAY";
    public static final String E_PLAY_THROUGHT_25 = "PLAY_THROUGHT_25";
    public static final String E_PLAY_THROUGHT_50 = "PLAY_THROUGHT_50";
    public static final String E_PLAY_THROUGHT_75 = "PLAY_THROUGHT_75";
    public static final String E_PLAY_THROUGHT_100 = "PLAY_THROUGHT_100";

    public static void clearAllValues(Context context) {
        setTrackingDoneWithEventTypeDisplay(context, false);
        setTrackingDoneWithEventTypePlaysRequested(context, false);
        setTrackingDoneWithEventTypeVideoStarts(context, false);
        setTrackingDoneWithEventTypeView(context, false);
        setTrackingDoneWithEventTypeReplay(context, false);
        setTrackingDoneWithEventTypePlayThrought25(context, false);
        setTrackingDoneWithEventTypePlayThrought50(context, false);
        setTrackingDoneWithEventTypePlayThrought75(context, false);
        setTrackingDoneWithEventTypePlayThrought100(context, false);
    }

    //Event type display
    public static boolean isTrackedEventTypeDisplay(Context context) {
        if (context == null) {
            return false;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getBoolean(E_DISPLAY, false);
    }

    public static void setTrackingDoneWithEventTypeDisplay(Context context, boolean isDone) {
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putBoolean(E_DISPLAY, isDone);
        editor.apply();
    }

    //Event type plays_requested
    public static boolean isTrackedEventTypePlaysRequested(Context context) {
        if (context == null) {
            return false;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getBoolean(E_PLAYS_REQUESTED, false);
    }

    public static void setTrackingDoneWithEventTypePlaysRequested(Context context, boolean isDone) {
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putBoolean(E_PLAYS_REQUESTED, isDone);
        editor.apply();
    }

    //Event type video_starts
    public static boolean isTrackedEventTypeVideoStarts(Context context) {
        if (context == null) {
            return false;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getBoolean(E_VIDEO_STARTS, false);
    }

    public static void setTrackingDoneWithEventTypeVideoStarts(Context context, boolean isDone) {
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putBoolean(E_VIDEO_STARTS, isDone);
        editor.apply();
    }

    //Event type view
    public static boolean isTrackedEventTypeView(Context context) {
        if (context == null) {
            return false;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getBoolean(E_VIEW, false);
    }

    public static void setTrackingDoneWithEventTypeView(Context context, boolean isDone) {
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putBoolean(E_VIEW, isDone);
        editor.apply();
    }

    //Event type replay
    public static boolean isTrackedEventTypeReplay(Context context) {
        if (context == null) {
            return false;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getBoolean(E_REPLAY, false);
    }

    public static void setTrackingDoneWithEventTypeReplay(Context context, boolean isDone) {
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putBoolean(E_REPLAY, isDone);
        editor.apply();
    }

    //Event type play_through 25
    public static boolean isTrackedEventTypePlayThrought25(Context context) {
        if (context == null) {
            return false;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getBoolean(E_PLAY_THROUGHT_25, false);
    }

    public static void setTrackingDoneWithEventTypePlayThrought25(Context context, boolean isDone) {
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putBoolean(E_PLAY_THROUGHT_25, isDone);
        editor.apply();
    }

    //Event type play_through 50
    public static boolean isTrackedEventTypePlayThrought50(Context context) {
        if (context == null) {
            return false;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getBoolean(E_PLAY_THROUGHT_50, false);
    }

    public static void setTrackingDoneWithEventTypePlayThrought50(Context context, boolean isDone) {
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putBoolean(E_PLAY_THROUGHT_50, isDone);
        editor.apply();
    }

    //Event type play_through 75
    public static boolean isTrackedEventTypePlayThrought75(Context context) {
        if (context == null) {
            return false;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getBoolean(E_PLAY_THROUGHT_75, false);
    }

    public static void setTrackingDoneWithEventTypePlayThrought75(Context context, boolean isDone) {
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putBoolean(E_PLAY_THROUGHT_75, isDone);
        editor.apply();
    }

    //Event type play_through 100
    public static boolean isTrackedEventTypePlayThrought100(Context context) {
        if (context == null) {
            return false;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getBoolean(E_PLAY_THROUGHT_100, false);
    }

    public static void setTrackingDoneWithEventTypePlayThrought100(Context context, boolean isDone) {
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putBoolean(E_PLAY_THROUGHT_100, isDone);
        editor.apply();
    }

    public interface UizaTrackingCallback {
        public void onTrackingSuccess();
    }
}
