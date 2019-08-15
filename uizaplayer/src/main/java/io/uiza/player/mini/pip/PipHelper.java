package io.uiza.player.mini.pip;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import io.uiza.core.util.SharedPreferenceUtil;
import io.uiza.core.util.UzConvertUtils;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.player.R;
import io.uiza.player.util.ComunicateMsg;

public final class PipHelper {

    private static final String PREFERENCES_FILE_NAME = "MiniPlayerSettings";
    private static final String CLICKED_PIP = "CLICKED_PIP";
    private static final String VIDEO_WIDTH = "VIDEO_WIDTH";
    private static final String VIDEO_HEIGHT = "VIDEO_HEIGHT";
    private static final String MINI_PLAYER_COLOR_VIEW_DESTROY = "MINI_PLAYER_COLOR_VIEW_DESTROY";
    private static final String MINI_PLAYER_TAP_TO_FULL_PLAYER = "MINI_PLAYER_TAP_TO_FULL_PLAYER";
    private static final String MINI_PLAYER_EZ_DESTROY = "MINI_PLAYER_EZ_DESTROY";
    private static final String MINI_PLAYER_ENABLE_VIBRATION = "MINI_PLAYER_ENABLE_VIBRATION";
    private static final String MINI_PLAYER_ENABLE_SMOOTH_SWITCH = "MINI_PLAYER_ENABLE_SMOOTH_SWITCH";
    private static final String MINI_PLAYER_AUTO_SIZE = "MINI_PLAYER_AUTO_SIZE";
    private static final String MINI_PLAYER_FIRST_POSITION_X = "MINI_PLAYER_FIRST_POSITION_X";
    private static final String MINI_PLAYER_FIRST_POSITION_Y = "MINI_PLAYER_FIRST_POSITION_Y";
    private static final String MINI_PLAYER_MARGIN_L = "MINI_PLAYER_MARGIN_L";
    private static final String MINI_PLAYER_MARGIN_T = "MINI_PLAYER_MARGIN_T";
    private static final String MINI_PLAYER_MARGIN_R = "MINI_PLAYER_MARGIN_R";
    private static final String MINI_PLAYER_MARGIN_B = "MINI_PLAYER_MARGIN_B";
    private static final String MINI_PLAYER_SIZE_WIDTH = "MINI_PLAYER_SIZE_WIDTH";
    private static final String MINI_PLAYER_SIZE_HEIGHT = "MINI_PLAYER_SIZE_HEIGHT";
    private static final String STABLE_PIP_TOP_POSITION = "STABLE_PIP_TOP_POSITION";


    public static boolean checkServiceRunning(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void stopMiniPlayer(Context context) {
        if (context == null) {
            return;
        }
        boolean isSvPipRunning = isMiniPlayerRunning(context);
        if (isSvPipRunning) {
            Intent intent = new Intent(context, UzPipService.class);
            context.stopService(intent);
        }
    }

    public static boolean isMiniPlayerRunning(Context context) {
        if (context == null) {
            return false;
        }
        return checkServiceRunning(context, UzPipService.class.getName());
    }

    public static Boolean getClickedPip(Context context) {
        return (Boolean) SharedPreferenceUtil
                .get(getPrivatePreference(context), CLICKED_PIP, false);
    }

    public static void setClickedPip(Context context, Boolean value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), CLICKED_PIP, value);
    }

    public static Boolean getMiniPlayerEzDestroy(Context context) {
        return (Boolean) SharedPreferenceUtil
                .get(getPrivatePreference(context), MINI_PLAYER_EZ_DESTROY, false);
    }

    public static void setMiniPlayerEzDestroy(Context context, Boolean value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_EZ_DESTROY, value);
    }

    public static Boolean getMiniPlayerEnableVibration(Context context) {
        return (Boolean) SharedPreferenceUtil
                .get(getPrivatePreference(context), MINI_PLAYER_ENABLE_VIBRATION, false);
    }

    public static void setMiniPlayerEnableVibration(Context context, Boolean value) {
        SharedPreferenceUtil
                .put(getPrivatePreference(context), MINI_PLAYER_ENABLE_VIBRATION, value);
    }

    public static Boolean getMiniPlayerTapToFullPlayer(Context context) {
        return (Boolean) SharedPreferenceUtil
                .get(getPrivatePreference(context), MINI_PLAYER_TAP_TO_FULL_PLAYER, true);
    }

    public static void setMiniPlayerTapToFullPlayer(Context context, Boolean value) {
        SharedPreferenceUtil
                .put(getPrivatePreference(context), MINI_PLAYER_TAP_TO_FULL_PLAYER, value);
    }

    public static Boolean getMiniPlayerEnableSmoothSwitch(Context context) {
        return (Boolean) SharedPreferenceUtil
                .get(getPrivatePreference(context), MINI_PLAYER_ENABLE_SMOOTH_SWITCH, true);
    }

    public static void setMiniPlayerEnableSmoothSwitch(Context context, Boolean value) {
        SharedPreferenceUtil
                .put(getPrivatePreference(context), MINI_PLAYER_ENABLE_SMOOTH_SWITCH, value);
    }

    public static Boolean getMiniPlayerAutoSize(Context context) {
        return (Boolean) SharedPreferenceUtil
                .get(getPrivatePreference(context), MINI_PLAYER_AUTO_SIZE, true);
    }

    private static void setMiniPlayerAutoSize(Context context, Boolean value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_AUTO_SIZE, value);
    }

    /////////////////////////////////INT
    public static int getVideoWidth(Context context) {
        return (Integer) SharedPreferenceUtil.get(
                getPrivatePreference(context), VIDEO_WIDTH, 16);
    }

    public static void setVideoWidth(Context context, int value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), VIDEO_WIDTH, value);
    }

    public static int getVideoHeight(Context context) {
        return (Integer) SharedPreferenceUtil.get(
                getPrivatePreference(context), VIDEO_HEIGHT, 9);
    }

    public static void setVideoHeight(Context context, int value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), VIDEO_HEIGHT, value);
    }

    public static int getMiniPlayerColorViewDestroy(Context context) {
        return (Integer) SharedPreferenceUtil
                .get(getPrivatePreference(context), MINI_PLAYER_COLOR_VIEW_DESTROY,
                        ContextCompat.getColor(context, R.color.black_65));
    }

    public static void setMiniPlayerColorViewDestroy(Context context, int value) {
        SharedPreferenceUtil
                .put(getPrivatePreference(context), MINI_PLAYER_COLOR_VIEW_DESTROY, value);
    }

    public static int getMiniPlayerFirstPositionX(Context context) {
        return (Integer) SharedPreferenceUtil
                .get(getPrivatePreference(context), MINI_PLAYER_FIRST_POSITION_X,
                        Constants.NOT_FOUND);
    }

    private static void setMiniPlayerFirstPositionX(Context context, int value) {
        SharedPreferenceUtil
                .put(getPrivatePreference(context), MINI_PLAYER_FIRST_POSITION_X, value);
    }

    public static int getMiniPlayerFirstPositionY(Context context) {
        return (Integer) SharedPreferenceUtil
                .get(getPrivatePreference(context), MINI_PLAYER_FIRST_POSITION_Y,
                        Constants.NOT_FOUND);
    }

    private static void setMiniPlayerFirstPositionY(Context context, int value) {
        SharedPreferenceUtil
                .put(getPrivatePreference(context), MINI_PLAYER_FIRST_POSITION_Y, value);
    }

    public static void setMiniPlayerFirstPosition(Context context, int firstPositionX,
            int firstPositionY) {
        setMiniPlayerFirstPositionX(context, firstPositionX);
        setMiniPlayerFirstPositionY(context, firstPositionY);
    }

    public static int getMiniPlayerMarginL(Context context) {
        return (Integer) SharedPreferenceUtil
                .get(getPrivatePreference(context), MINI_PLAYER_MARGIN_L, 0);
    }

    private static void setMiniPlayerMarginL(Context context, int value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_MARGIN_L, value);
    }

    public static int getMiniPlayerMarginT(Context context) {
        return (Integer) SharedPreferenceUtil
                .get(getPrivatePreference(context), MINI_PLAYER_MARGIN_T, 0);
    }

    private static void setMiniPlayerMarginT(Context context, int value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_MARGIN_T, value);
    }

    public static int getMiniPlayerMarginR(Context context) {
        return (Integer) SharedPreferenceUtil
                .get(getPrivatePreference(context), MINI_PLAYER_MARGIN_R, 0);
    }

    private static void setMiniPlayerMarginR(Context context, int value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_MARGIN_R, value);
    }

    public static int getMiniPlayerMarginB(Context context) {
        return (Integer) SharedPreferenceUtil
                .get(getPrivatePreference(context), MINI_PLAYER_MARGIN_B, 0);
    }

    private static void setMiniPlayerMarginB(Context context, int value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_MARGIN_B, value);
    }

    public static int getMiniPlayerSizeWidth(Context context) {
        return (Integer) SharedPreferenceUtil
                .get(getPrivatePreference(context), MINI_PLAYER_SIZE_WIDTH, Constants.W_320);
    }

    private static void setMiniPlayerSizeWidth(Context context, int value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_SIZE_WIDTH, value);
    }

    public static int getMiniPlayerSizeHeight(Context context) {
        return (Integer) SharedPreferenceUtil
                .get(getPrivatePreference(context), MINI_PLAYER_SIZE_HEIGHT, Constants.W_180);
    }

    private static void setMiniPlayerSizeHeight(Context context, int value) {
        SharedPreferenceUtil.put(
                getPrivatePreference(context), MINI_PLAYER_SIZE_HEIGHT, value);
    }

    public static boolean setMiniPlayerMarginDp(Context context, float marginL, float marginT,
            float marginR, float marginB) {
        int pxL = UzConvertUtils.dp2px(marginL);
        int pxT = UzConvertUtils.dp2px(marginT);
        int pxR = UzConvertUtils.dp2px(marginR);
        int pxB = UzConvertUtils.dp2px(marginB);
        return setMiniPlayerMarginPixel(context, pxL, pxT, pxR, pxB);
    }

    public static boolean setMiniPlayerMarginPixel(Context context, int marginL, int marginT,
            int marginR, int marginB) {
        int screenW = UzDisplayUtil.getScreenWidth();
        int screenH = UzDisplayUtil.getScreenHeight();
        int rangeMarginW = screenW / 5;
        int rangeMarginH = screenH / 5;
        if (marginL < 0 || marginL > rangeMarginW) {
            throw new IllegalArgumentException(
                    "Error: marginL is invalid, the right value must from 0px to " + rangeMarginW
                            + "px or 0dp to " + UzConvertUtils.px2dp(rangeMarginW) + "dp");
        }
        if (marginT < 0 || marginT > rangeMarginH) {
            throw new IllegalArgumentException(
                    "Error: marginT is invalid, the right value must from 0px to " + rangeMarginH
                            + "px or 0dp to " + UzConvertUtils.px2dp(rangeMarginH) + "dp");
        }
        if (marginR < 0 || marginR > rangeMarginW) {
            throw new IllegalArgumentException(
                    "Error: marginR is invalid, the right value must from 0px to " + rangeMarginW
                            + "px or 0dp to " + UzConvertUtils.px2dp(rangeMarginW) + "dp");
        }
        if (marginB < 0 || marginB > rangeMarginH) {
            throw new IllegalArgumentException(
                    "Error: marginB is invalid, the right value must from 0px to " + rangeMarginH
                            + "px or 0dp to " + UzConvertUtils.px2dp(rangeMarginH) + "dp");
        }
        setMiniPlayerMarginL(context, marginL);
        setMiniPlayerMarginT(context, marginT);
        setMiniPlayerMarginR(context, marginR);
        setMiniPlayerMarginB(context, marginB);
        return true;
    }

    public static boolean setMiniPlayerSizeDp(Context context, boolean isAutoSize, int videoWidthDp,
            int videoHeightDp) {
        int pxW = UzConvertUtils.dp2px(videoWidthDp);
        int pxH = UzConvertUtils.dp2px(videoHeightDp);
        return setMiniPlayerSizePixel(context, isAutoSize, pxW, pxH);
    }

    public static boolean setMiniPlayerSizePixel(Context context, boolean isAutoSize,
            int videoWidthPx, int videoHeightPx) {
        setMiniPlayerAutoSize(context, isAutoSize);
        if (isAutoSize) {
            setMiniPlayerSizeWidth(context, Constants.W_320);
            setMiniPlayerSizeHeight(context, Constants.W_180);
            return true;
        }
        int screenWPx = UzDisplayUtil.getScreenWidth();
        int screenHPx = UzDisplayUtil.getScreenHeight();
        if (videoWidthPx < 0 || videoWidthPx > screenWPx) {
            throw new IllegalArgumentException(
                    "Error: videoWidthPx is invalid, the right value must from 0px to " + screenWPx
                            + "px or 0dp to " + UzConvertUtils.px2dp(screenWPx) + "dp");
        }
        if (videoHeightPx < 0 || videoHeightPx > screenHPx) {
            throw new IllegalArgumentException(
                    "Error: videoHeightPx is invalid, the right value must from 0px to " + screenHPx
                            + "px or 0dp to " + UzConvertUtils.px2dp(screenHPx) + "dp");
        }
        setMiniPlayerSizeWidth(context, videoWidthPx);
        setMiniPlayerSizeHeight(context, videoHeightPx);
        return true;
    }

    public static void setStablePipTopPosition(Context context, int value) {
        SharedPreferenceUtil.put(
                getPrivatePreference(context), STABLE_PIP_TOP_POSITION, value);
    }

    public static int getStablePipTopPosition(Context context) {
        return (Integer) SharedPreferenceUtil
                .get(getPrivatePreference(context), STABLE_PIP_TOP_POSITION, 0);
    }

    public static void showMiniPlayerController(Context context) {
        communicateContext(context, ComunicateMsg.SHOW_MINI_PLAYER_CONTROLLER);
    }

    public static void hideMiniPlayerController(Context context) {
        communicateContext(context, ComunicateMsg.HIDE_MINI_PLAYER_CONTROLLER);
    }

    public static void toggleMiniPlayerController(Context context) {
        communicateContext(context, ComunicateMsg.TOGGLE_MINI_PLAYER_CONTROLLER);
    }

    public static void pauseVideo(Context context) {
        communicateContext(context, ComunicateMsg.PAUSE_MINI_PLAYER);
    }

    public static void resumeVideo(Context context) {
        communicateContext(context, ComunicateMsg.RESUME_MINI_PLAYER);
    }

    public static void toggleResumePauseVideo(Context context) {
        communicateContext(context, ComunicateMsg.TOGGLE_RESUME_PAUSE_MINI_PLAYER);
    }

    public static void openAppFromMiniPlayer(Context context) {
        communicateContext(context, ComunicateMsg.OPEN_APP_FROM_MINI_PLAYER);
    }

    public static void disappearMiniplayer(Context context) {
        communicateContext(context, ComunicateMsg.DISAPPEAR);
    }

    public static void appearMiniplayer(Context context) {
        communicateContext(context, ComunicateMsg.APPEAR);
    }

    private static void communicateContext(Context context, String event) {
        if (isMiniPlayerRunning(context)) {
            ComunicateMsg.MsgFromActivity msgFromActivity = new ComunicateMsg.MsgFromActivity(
                    event);
            ComunicateMsg.postFromActivity(msgFromActivity);
        }
    }

    public static void moveTaskToFront(Activity activity, boolean restoredToTop) {
        if (activity == null) {
            return;
        }
        if (android.os.Build.VERSION.SDK_INT >= 19 && !activity.isTaskRoot() && restoredToTop) {
            // 4.4.2 platform issues for FLAG_ACTIVITY_REORDER_TO_FRONT,
            // reordered activity back press will go to home unexpectedly,
            // Workaround: move reordered activity current task to front when it's finished.
            ActivityManager tasksManager = (ActivityManager) activity
                    .getSystemService(ACTIVITY_SERVICE);
            tasksManager.moveTaskToFront(activity.getTaskId(),
                    ActivityManager.MOVE_TASK_NO_USER_ACTION);
        }
    }

    private static SharedPreferences getPrivatePreference(Context context) {
        return context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
    }
}
