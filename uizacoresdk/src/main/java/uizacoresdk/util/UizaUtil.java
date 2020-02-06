package uizacoresdk.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Locale;

import timber.log.Timber;
import uizacoresdk.R;
import uizacoresdk.dialog.hq.UizaItem;
import uizacoresdk.floatview.FloatUizaVideoService;
import uizacoresdk.view.ComunicateMng;
import vn.uiza.core.common.Constants;
import vn.uiza.models.Subtitle;
import vn.uiza.models.auth.Auth;
import vn.uiza.utils.ConvertUtils;
import vn.uiza.utils.ScreenUtils;
import vn.uiza.utils.SharedPreferenceUtil;
import vn.uiza.utils.StringUtils;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;
import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by loitp on 16/1/2019.
 */

public class UizaUtil {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private UizaUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(Context context) {
        UizaUtil.context = context.getApplicationContext();
    }

    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }


    public static void setUIFullScreenIcon(ImageButton imageButton, boolean isFullScreen) {
        if (imageButton == null) {
            return;
        }
        if (isFullScreen) {
            imageButton.setImageResource(R.drawable.ic_fullscreen_exit_white_48);
        } else {
            imageButton.setImageResource(R.drawable.ic_fullscreen_white_48);
        }
    }

    public static void resizeLayout(ViewGroup viewGroup, ImageView ivVideoCover, int pixelAdded, int videoW, int videoH, boolean isFreeSize) {
        if (viewGroup == null) {
            return;
        }
        int widthSurfaceView = 0;
        int heightSurfaceView = 0;
        boolean isFullScreen = ScreenUtils.isFullScreen(viewGroup.getContext());
        if (isFullScreen) {//landscape
            widthSurfaceView = ScreenUtils.getScreenHeightIncludeNavigationBar(viewGroup.getContext());
            heightSurfaceView = ScreenUtils.getScreenHeight();
        } else {//portrait
            widthSurfaceView = ScreenUtils.getScreenWidth();
            if (videoW == 0 || videoH == 0) {
                heightSurfaceView = (int) (widthSurfaceView * Constants.RATIO_9_16) + pixelAdded;
            } else {
                if (videoW >= videoH) {
                    if (isFreeSize) {
                        heightSurfaceView = widthSurfaceView * videoH / videoW + pixelAdded;
                    } else {
                        heightSurfaceView = (int) (widthSurfaceView * Constants.RATIO_9_16) + pixelAdded;
                    }
                } else {
                    if (isFreeSize) {
                        heightSurfaceView = widthSurfaceView * videoH / videoW + pixelAdded;
                    } else {
                        //heightSurfaceView = widthSurfaceView;
                        heightSurfaceView = (int) (widthSurfaceView * Constants.RATIO_9_16) + pixelAdded;
                    }
                }
            }
        }
        //LLog.d(TAG, "resizeLayout isFullScreen " + isFullScreen + ", widthSurfaceView x heightSurfaceView: " + widthSurfaceView + "x" + heightSurfaceView + ", pixelAdded: " + pixelAdded + ", videoW: " + videoW + ", videoH: " + videoH);
        TmpParamData.getInstance().setPlayerWidth(widthSurfaceView);
        TmpParamData.getInstance().setPlayerHeight(heightSurfaceView);
        viewGroup.getLayoutParams().width = widthSurfaceView;
        viewGroup.getLayoutParams().height = heightSurfaceView;
        viewGroup.requestLayout();
        //set size of parent view group of viewGroup
        RelativeLayout parentViewGroup = (RelativeLayout) viewGroup.getParent();
        if (parentViewGroup != null) {
            parentViewGroup.getLayoutParams().width = widthSurfaceView;
            parentViewGroup.getLayoutParams().height = heightSurfaceView;
            parentViewGroup.requestLayout();
        }
        if (ivVideoCover != null) {
            ivVideoCover.getLayoutParams().width = widthSurfaceView;
            ivVideoCover.getLayoutParams().height = heightSurfaceView - pixelAdded;
            ivVideoCover.requestLayout();
        }
        //edit size of imageview thumnail
        FrameLayout flImgThumnailPreviewSeekbar = viewGroup.findViewById(R.id.preview_frame_layout);
        if (flImgThumnailPreviewSeekbar != null) {
            if (isFullScreen) {
                flImgThumnailPreviewSeekbar.getLayoutParams().width = widthSurfaceView / 4;
                flImgThumnailPreviewSeekbar.getLayoutParams().height = (int) (widthSurfaceView / 4 * Constants.RATIO_9_16);
            } else {
                flImgThumnailPreviewSeekbar.getLayoutParams().width = widthSurfaceView / 5;
                flImgThumnailPreviewSeekbar.getLayoutParams().height = (int) (widthSurfaceView / 5 * Constants.RATIO_9_16);
            }
            flImgThumnailPreviewSeekbar.requestLayout();
        }
    }

    //return button video in debug layout
    @Nullable
    public static View getBtVideo(@Nullable LinearLayout debugRootView) {
        if (debugRootView == null) {
            return null;
        }
        for (int i = 0; i < debugRootView.getChildCount(); i++) {
            View childView = debugRootView.getChildAt(i);
            if (childView instanceof Button) {
                if (((Button) childView).getText().toString().equalsIgnoreCase(debugRootView.getContext().getString(R.string.video))) {
                    return childView;
                }
            }
        }
        return null;
    }

    //return button audio in debug layout
    public static View getBtAudio(LinearLayout debugRootView) {
        if (debugRootView == null) {
            return null;
        }
        for (int i = 0; i < debugRootView.getChildCount(); i++) {
            View childView = debugRootView.getChildAt(i);
            if (childView instanceof Button) {
                if (((Button) childView).getText().toString().equalsIgnoreCase(debugRootView.getContext().getString(R.string.audio))) {
                    return childView;
                }
            }
        }
        return null;
    }

    //return button text in debug layout
    public static View getBtText(LinearLayout debugRootView) {
        if (debugRootView == null) {
            return null;
        }
        for (int i = 0; i < debugRootView.getChildCount(); i++) {
            View childView = debugRootView.getChildAt(i);
            if (childView instanceof Button) {
                if (((Button) childView).getText().toString().equalsIgnoreCase(debugRootView.getContext().getString(R.string.text))) {
                    return childView;
                }
            }
        }
        return null;
    }

    public static List<Subtitle> createDummySubtitle() {
        String json = "[\n" +
                "                {\n" +
                "                    \"id\": \"18414566-c0c8-4a51-9d60-03f825bb64a9\",\n" +
                "                    \"name\": \"\",\n" +
                "                    \"type\": \"subtitle\",\n" +
                "                    \"url\": \"//dev-static.uiza.io/subtitle_56a4f990-17e6-473c-8434-ef6c7e40bba1_en_1522812430080.vtt\",\n" +
                "                    \"mine\": \"vtt\",\n" +
                "                    \"language\": \"en\",\n" +
                "                    \"isDefault\": \"0\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"271787a0-5d23-4a35-a10a-5c43fdcb71a8\",\n" +
                "                    \"name\": \"\",\n" +
                "                    \"type\": \"subtitle\",\n" +
                "                    \"url\": \"//dev-static.uiza.io/subtitle_56a4f990-17e6-473c-8434-ef6c7e40bba1_vi_1522812445904.vtt\",\n" +
                "                    \"mine\": \"vtt\",\n" +
                "                    \"language\": \"vi\",\n" +
                "                    \"isDefault\": \"0\"\n" +
                "                }\n" +
                "            ]";
        return StringUtils.toList(json);
    }

    public static void showUizaDialog(Context context, Dialog dialog) {
        if (context == null || dialog == null) {
            return;
        }
        boolean isFullScreen = ScreenUtils.isFullScreen(context);
        Window window = dialog.getWindow();
        if (window == null) return;
        if (isFullScreen) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            } else {
                //TODO cần làm ở sdk thấp, thanh navigation ko chịu ẩn
            }
        }
        dialog.show();
        try {
            window.getAttributes().windowAnimations = R.style.uiza_dialog_animation;
            window.setBackgroundDrawableResource(R.drawable.background_dialog_uiza);
            //set dialog position
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            //wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            wlp.dimAmount = 0.65f;
            window.setAttributes(wlp);

            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height;
            if (isFullScreen) {
                height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.5);
            } else {
                height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.3);
            }
            window.setLayout(width, height);
        } catch (Exception e) {
            //do nothing
            Timber.e(e);
        }
        if (isFullScreen) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
    }

    public static void setTextDuration(TextView textView, String duration) {
        if (textView == null || duration == null || duration.isEmpty()) {
            return;
        }
        try {
            int min = (int) Double.parseDouble(duration) + 1;
            String minutes = Integer.toString(min % 60);
            minutes = minutes.length() == 1 ? "0" + minutes : minutes;
            textView.setText(String.format(Locale.getDefault(), "%d:%s", (min / 60), minutes));
        } catch (Exception e) {
            Timber.e(e, "Error setTextDuration");
            textView.setText(" - ");
        }
    }

    //return true if app is in foreground
    public static boolean isAppInForeground(Context context) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
            String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
            return foregroundTaskPackageName.toLowerCase().equals(context.getPackageName().toLowerCase());
        } else {
            ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(appProcessInfo);
            if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
                return true;
            }
            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            // App is foreground, but screen is locked, so show notification
            return km.inKeyguardRestrictedInputMode();
        }
    }

    public static boolean checkServiceRunning(@NonNull Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    //stop service pip FloatUizaVideoService
    public static void stopMiniPlayer(@NonNull Context context) {
        //LLog.d(TAG, "stopMiniPlayer");
        boolean isSvPipRunning = isMiniPlayerRunning(context);
        //LLog.d(TAG, "isSvPipRunning " + isSvPipRunning);
        if (isSvPipRunning) {
            //stop service if running
            Intent intent = new Intent(context, FloatUizaVideoService.class);
            context.stopService(intent);
        }
    }

    public static boolean isMiniPlayerRunning(@NonNull Context context) {
        return checkServiceRunning(context, FloatUizaVideoService.class.getName());
    }

    public static void updateUIFocusChange(View view, boolean isFocus) {
        updateUIFocusChange(view, isFocus, R.drawable.bkg_has_focus, R.drawable.bkg_no_focus);
    }

    public static void updateUIFocusChange(View view, boolean isFocus, int resHasFocus, int resNoFocus) {
        if (view == null) {
            return;
        }
        if (isFocus) {
            view.setBackgroundResource(resHasFocus);
        } else {
            view.setBackgroundResource(resNoFocus);
        }
    }

    //description
    //return SD, HD, FHD, QHD...
    public static UizaItem.Format getFormatVideo(String description) {
        String format = UizaItem.Format.F_UNKNOW;
        String profile = UizaItem.Format.P_UNKNOW;
        if (description.contains(",")) {
            String resolution = description.split(",")[0];
            //LLog.d(TAG, "resolution " + resolution);
            if (resolution.contains(" ")) {
                String[] s = resolution.split(" ");
                if (s.length >= 3) {
                    String s0 = s[0];
                    String s1 = s[2];

                    int w;
                    int h;
                    try {
                        w = Integer.parseInt(s0);
                        h = Integer.parseInt(s1);
                    } catch (Exception e) {
                        Timber.e(e);
                        return new UizaItem.Format();
                    }
                    if (w < h) {
                        w = h;
                    }
                    //set profile
                    //https://docs.google.com/spreadsheets/d/13lIsH711GJjttmZzFixph3RZwvP7a7vZhppSFnvsEl8/edit#gid=1297908801
                    if (w <= 480) {
                        profile = UizaItem.Format.P_270;
                        format = UizaItem.Format.F_SD;
                    } else if (w <= 640) {
                        profile = UizaItem.Format.P_360;
                        format = UizaItem.Format.F_SD;
                    } else if (w <= 854) {
                        profile = UizaItem.Format.P_480;
                        format = UizaItem.Format.F_SD;
                    } else if (w <= 1280) {
                        profile = UizaItem.Format.P_720;
                        format = UizaItem.Format.F_HD;
                    } else if (w <= 1920) {
                        profile = UizaItem.Format.P_1080;
                        format = UizaItem.Format.F_FHD;
                    } else if (w <= 2560) {
                        profile = UizaItem.Format.P_1440;
                        format = UizaItem.Format.F_2K;
                    } else if (w <= 3840) {
                        profile = UizaItem.Format.P_2160;
                        format = UizaItem.Format.F_4K;
                    } else {
                        profile = UizaItem.Format.P_UNKNOW;
                        format = UizaItem.Format.F_UNKNOW;
                    }
                }
            }
        }
        UizaItem.Format f = new UizaItem.Format();
        f.setFormat(format);
        f.setProfile(profile);
        return f;
    }

    //description
    //return SD, HD, FHD, QHD...
    public static UizaItem.Format getFormatVideo(int width, int height) {
        String format;
        String profile;
        //set profile
        //https://docs.google.com/spreadsheets/d/13lIsH711GJjttmZzFixph3RZwvP7a7vZhppSFnvsEl8/edit#gid=1297908801
        if (width < height) {
            width = height;
        }
        if (width <= 480) {
            profile = UizaItem.Format.P_270;
            format = UizaItem.Format.F_SD;
        } else if (width <= 640) {
            profile = UizaItem.Format.P_360;
            format = UizaItem.Format.F_SD;
        } else if (width <= 854) {
            profile = UizaItem.Format.P_480;
            format = UizaItem.Format.F_SD;
        } else if (width <= 1280) {
            profile = UizaItem.Format.P_720;
            format = UizaItem.Format.F_HD;
        } else if (width <= 1920) {
            profile = UizaItem.Format.P_1080;
            format = UizaItem.Format.F_FHD;
        } else if (width <= 2560) {
            profile = UizaItem.Format.P_1440;
            format = UizaItem.Format.F_2K;
        } else if (width <= 3840) {
            profile = UizaItem.Format.P_2160;
            format = UizaItem.Format.F_4K;
        } else {
            profile = UizaItem.Format.P_UNKNOW;
            format = UizaItem.Format.F_UNKNOW;
        }
        UizaItem.Format f = new UizaItem.Format();
        f.setFormat(format);
        f.setProfile(profile);
        return f;
    }

    //=============================================================================END FOR UIZA V3

    //=============================================================================START PREF
    private final static String PREFERENCES_FILE_NAME = "loitp";
    private final static String AUTH = "AUTH";
    private final static String API_TRACK_END_POINT = "API_TRACK_END_POINT";
    private final static String APP_ID = "APP_ID";
    private final static String CLICKED_PIP = "CLICKED_PIP";
    //private final static String CLASS_NAME_OF_PLAYER = "CLASS_NAME_OF_PLAYER";
    private final static String IS_INIT_PLAYLIST_FOLDER = "IS_INIT_PLAYLIST_FOLDER";
    private final static String VIDEO_WIDTH = "VIDEO_WIDTH";
    private final static String VIDEO_HEIGHT = "VIDEO_HEIGHT";
    private final static String MINI_PLAYER_COLOR_VIEW_DESTROY = "MINI_PLAYER_COLOR_VIEW_DESTROY";
    private final static String MINI_PLAYER_TAP_TO_FULL_PLAYER = "MINI_PLAYER_TAP_TO_FULL_PLAYER";
    private final static String MINI_PLAYER_EZ_DESTROY = "MINI_PLAYER_EZ_DESTROY";
    private final static String MINI_PLAYER_ENABLE_VIBRATION = "MINI_PLAYER_ENABLE_VIBRATION";
    private final static String MINI_PLAYER_ENABLE_SMOOTH_SWITCH = "MINI_PLAYER_ENABLE_SMOOTH_SWITCH";
    private final static String MINI_PLAYER_AUTO_SIZE = "MINI_PLAYER_AUTO_SIZE";
    //private final static String MINI_PLAYER_CONTENT_POSITION_WHEN_SWITCH_TO_FULL_PLAYER = "MINI_PLAYER_CONTENT_POSITION_WHEN_SWITCH_TO_FULL_PLAYER";
    private final static String MINI_PLAYER_FIRST_POSITION_X = "MINI_PLAYER_FIRST_POSITION_X";
    private final static String MINI_PLAYER_FIRST_POSITION_Y = "MINI_PLAYER_FIRST_POSITION_Y";
    private final static String MINI_PLAYER_MARGIN_L = "MINI_PLAYER_MARGIN_L";
    private final static String MINI_PLAYER_MARGIN_T = "MINI_PLAYER_MARGIN_T";
    private final static String MINI_PLAYER_MARGIN_R = "MINI_PLAYER_MARGIN_R";
    private final static String MINI_PLAYER_MARGIN_B = "MINI_PLAYER_MARGIN_B";
    private final static String MINI_PLAYER_SIZE_WIDTH = "MINI_PLAYER_SIZE_WIDTH";
    private final static String MINI_PLAYER_SIZE_HEIGHT = "MINI_PLAYER_SIZE_HEIGHT";
    private final static String LAST_SYNCED_SERVER_TIME = "LAST_SYNCED_SERVER_TIME";
    private final static String LAST_ELAPSED_TIME = "LAST_ELAPSED_TIME";
    private final static String STABLE_PIP_TOP_POSITION = "STABLE_PIP_TOP_POSITION";

    private static SharedPreferences getPrivatePreference(Context context) {
        return context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
    }

    /////////////////////////////////STRING
    public static String getApiTrackEndPoint(Context context) {
        return (String) SharedPreferenceUtil.get(getPrivatePreference(context), API_TRACK_END_POINT, "");
    }

    public static void setApiTrackEndPoint(String value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), API_TRACK_END_POINT, value);
    }

    public static String getAppId() {
        return (String) SharedPreferenceUtil.get(getPrivatePreference(context), APP_ID, "");
    }

    public static void setAppId(String value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), APP_ID, value);
    }

    /////////////////////////////////BOOLEAN
    public static Boolean isInitPlaylistFolder(Context context) {
        return (Boolean) SharedPreferenceUtil.get(getPrivatePreference(context), IS_INIT_PLAYLIST_FOLDER, false);
    }

    public static void setIsInitPlaylistFolder(Context context, Boolean value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), IS_INIT_PLAYLIST_FOLDER, value);
    }

    public static Boolean getClickedPip(Context context) {
        return (Boolean) SharedPreferenceUtil.get(getPrivatePreference(context), CLICKED_PIP, false);
    }

    public static void setClickedPip(Context context, Boolean value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), CLICKED_PIP, value);
    }

    public static Boolean getMiniPlayerEzDestroy(Context context) {
        return (Boolean) SharedPreferenceUtil.get(getPrivatePreference(context), MINI_PLAYER_EZ_DESTROY, false);
    }

    public static void setMiniPlayerEzDestroy(Context context, Boolean value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_EZ_DESTROY, value);
    }

    public static Boolean getMiniPlayerEnableVibration(Context context) {
        return (Boolean) SharedPreferenceUtil.get(getPrivatePreference(context), MINI_PLAYER_ENABLE_VIBRATION, false);
    }

    public static void setMiniPlayerEnableVibration(Context context, Boolean value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_ENABLE_VIBRATION, value);
    }

    public static Boolean getMiniPlayerTapToFullPlayer(Context context) {
        return (Boolean) SharedPreferenceUtil.get(getPrivatePreference(context), MINI_PLAYER_TAP_TO_FULL_PLAYER, true);
    }

    public static void setMiniPlayerTapToFullPlayer(Context context, Boolean value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_TAP_TO_FULL_PLAYER, value);
    }

    public static Boolean getMiniPlayerEnableSmoothSwitch(Context context) {
        return (Boolean) SharedPreferenceUtil.get(getPrivatePreference(context), MINI_PLAYER_ENABLE_SMOOTH_SWITCH, true);
    }

    public static void setMiniPlayerEnableSmoothSwitch(Context context, Boolean value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_ENABLE_SMOOTH_SWITCH, value);
    }

    public static Boolean getMiniPlayerAutoSize(Context context) {
        return (Boolean) SharedPreferenceUtil.get(getPrivatePreference(context), MINI_PLAYER_AUTO_SIZE, true);
    }

    private static void setMiniPlayerAutoSize(Context context, Boolean value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_AUTO_SIZE, value);
    }

    /////////////////////////////////INT
    public static int getVideoWidth(Context context) {
        return (Integer) SharedPreferenceUtil.get(getPrivatePreference(context), VIDEO_WIDTH, 16);
    }

    public static void setVideoWidth(Context context, int value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), VIDEO_WIDTH, value);
    }

    public static int getVideoHeight(Context context) {
        return (Integer) SharedPreferenceUtil.get(getPrivatePreference(context), VIDEO_HEIGHT, 9);
    }

    public static void setVideoHeight(Context context, int value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), VIDEO_HEIGHT, value);
    }

    public static int getMiniPlayerColorViewDestroy(Context context) {
        return (Integer) SharedPreferenceUtil.get(getPrivatePreference(context), MINI_PLAYER_COLOR_VIEW_DESTROY, ContextCompat.getColor(context, R.color.black_65));
    }

    public static void setMiniPlayerColorViewDestroy(Context context, int value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_COLOR_VIEW_DESTROY, value);
    }

    public static int getMiniPlayerFirstPositionX(Context context) {
        return (Integer) SharedPreferenceUtil.get(getPrivatePreference(context), MINI_PLAYER_FIRST_POSITION_X, -1);
    }

    private static void setMiniPlayerFirstPositionX(Context context, int value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_FIRST_POSITION_X, value);
    }

    public static int getMiniPlayerFirstPositionY(Context context) {
        return (Integer) SharedPreferenceUtil.get(getPrivatePreference(context), MINI_PLAYER_FIRST_POSITION_Y, -1);
    }

    private static void setMiniPlayerFirstPositionY(Context context, int value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_FIRST_POSITION_Y, value);
    }

    public static void setMiniPlayerFirstPosition(Context context, int firstPositionX, int firstPositionY) {
        setMiniPlayerFirstPositionX(context, firstPositionX);
        setMiniPlayerFirstPositionY(context, firstPositionY);
    }

    public static int getMiniPlayerMarginL(Context context) {
        return (Integer) SharedPreferenceUtil.get(getPrivatePreference(context), MINI_PLAYER_MARGIN_L, 0);
    }

    private static void setMiniPlayerMarginL(Context context, int value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_MARGIN_L, value);
    }

    public static int getMiniPlayerMarginT(Context context) {
        return (Integer) SharedPreferenceUtil.get(getPrivatePreference(context), MINI_PLAYER_MARGIN_T, 0);
    }

    private static void setMiniPlayerMarginT(Context context, int value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_MARGIN_T, value);
    }

    public static int getMiniPlayerMarginR(Context context) {
        return (Integer) SharedPreferenceUtil.get(getPrivatePreference(context), MINI_PLAYER_MARGIN_R, 0);
    }

    private static void setMiniPlayerMarginR(Context context, int value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_MARGIN_R, value);
    }

    public static int getMiniPlayerMarginB(Context context) {
        return (Integer) SharedPreferenceUtil.get(getPrivatePreference(context), MINI_PLAYER_MARGIN_B, 0);
    }

    private static void setMiniPlayerMarginB(Context context, int value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_MARGIN_B, value);
    }

    public static int getMiniPlayerSizeWidth(Context context) {
        return (Integer) SharedPreferenceUtil.get(getPrivatePreference(context), MINI_PLAYER_SIZE_WIDTH, Constants.W_320);
    }

    private static void setMiniPlayerSizeWidth(Context context, int value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_SIZE_WIDTH, value);
    }

    public static int getMiniPlayerSizeHeight(Context context) {
        return (Integer) SharedPreferenceUtil.get(getPrivatePreference(context), MINI_PLAYER_SIZE_HEIGHT, Constants.W_180);
    }

    private static void setMiniPlayerSizeHeight(Context context, int value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), MINI_PLAYER_SIZE_HEIGHT, value);
    }

    public static boolean setMiniPlayerMarginDp(Context context, float marginL, float marginT, float marginR, float marginB) {
        int pxL = ConvertUtils.dp2px(marginL);
        int pxT = ConvertUtils.dp2px(marginT);
        int pxR = ConvertUtils.dp2px(marginR);
        int pxB = ConvertUtils.dp2px(marginB);
        return setMiniPlayerMarginPixel(context, pxL, pxT, pxR, pxB);
    }

    public static boolean setMiniPlayerMarginPixel(Context context, int marginL, int marginT, int marginR, int marginB) {
        int screenW = ScreenUtils.getScreenWidth();
        int screenH = ScreenUtils.getScreenHeight();
        int rangeMarginW = screenW / 5;
        int rangeMarginH = screenH / 5;
        if (marginL < 0 || marginL > rangeMarginW) {
            throw new IllegalArgumentException("Error: marginL is invalid, the right value must from 0px to " + rangeMarginW + "px or 0dp to " + ConvertUtils.px2dp(rangeMarginW) + "dp");
        }
        if (marginT < 0 || marginT > rangeMarginH) {
            throw new IllegalArgumentException("Error: marginT is invalid, the right value must from 0px to " + rangeMarginH + "px or 0dp to " + ConvertUtils.px2dp(rangeMarginH) + "dp");
        }
        if (marginR < 0 || marginR > rangeMarginW) {
            throw new IllegalArgumentException("Error: marginR is invalid, the right value must from 0px to " + rangeMarginW + "px or 0dp to " + ConvertUtils.px2dp(rangeMarginW) + "dp");
        }
        if (marginB < 0 || marginB > rangeMarginH) {
            throw new IllegalArgumentException("Error: marginB is invalid, the right value must from 0px to " + rangeMarginH + "px or 0dp to " + ConvertUtils.px2dp(rangeMarginH) + "dp");
        }
        setMiniPlayerMarginL(context, marginL);
        setMiniPlayerMarginT(context, marginT);
        setMiniPlayerMarginR(context, marginR);
        setMiniPlayerMarginB(context, marginB);
        return true;
    }

    public static boolean setMiniPlayerSizeDp(Context context, boolean isAutoSize, int videoWidthDp, int videoHeightDp) {
        int pxW = ConvertUtils.dp2px(videoWidthDp);
        int pxH = ConvertUtils.dp2px(videoHeightDp);
        return setMiniPlayerSizePixel(context, isAutoSize, pxW, pxH);
    }

    public static boolean setMiniPlayerSizePixel(Context context, boolean isAutoSize, int videoWidthPx, int videoHeightPx) {
        setMiniPlayerAutoSize(context, isAutoSize);
        if (isAutoSize) {
            setMiniPlayerSizeWidth(context, Constants.W_320);
            setMiniPlayerSizeHeight(context, Constants.W_180);
            return true;
        }
        int screenWPx = ScreenUtils.getScreenWidth();
        int screenHPx = ScreenUtils.getScreenHeight();
        if (videoWidthPx < 0 || videoWidthPx > screenWPx) {
            throw new IllegalArgumentException("Error: videoWidthPx is invalid, the right value must from 0px to " + screenWPx + "px or 0dp to " + ConvertUtils.px2dp(screenWPx) + "dp");
        }
        if (videoHeightPx < 0 || videoHeightPx > screenHPx) {
            throw new IllegalArgumentException("Error: videoHeightPx is invalid, the right value must from 0px to " + screenHPx + "px or 0dp to " + ConvertUtils.px2dp(screenHPx) + "dp");
        }
        setMiniPlayerSizeWidth(context, videoWidthPx);
        setMiniPlayerSizeHeight(context, videoHeightPx);
        return true;
    }

    public static void setStablePipTopPosition(Context context, int value) {
        SharedPreferenceUtil.put(getPrivatePreference(context), STABLE_PIP_TOP_POSITION, value);
    }

    public static int getStablePipTopPosition(Context context) {
        return (Integer) SharedPreferenceUtil.get(getPrivatePreference(context), STABLE_PIP_TOP_POSITION, 0);
    }

    /////////////////////////////////OBJECT
    public static Auth getAuth(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        String json = pref.getString(AUTH, null);
        if (!TextUtils.isEmpty(json))
            return StringUtils.toObject(json, Auth.class);
        return null;
    }

    public static void setAuth(Context context, Auth auth) {
        SharedPreferenceUtil.put(getPrivatePreference(context), AUTH, StringUtils.toJson(auth));
    }

    //=============================================================================END PREF

    public static void showMiniPlayerController(Context context) {
        communicateContext(context, ComunicateMng.SHOW_MINI_PLAYER_CONTROLLER);
    }

    public static void hideMiniPlayerController(Context context) {
        communicateContext(context, ComunicateMng.HIDE_MINI_PLAYER_CONTROLLER);
    }

    public static void toggleMiniPlayerController(Context context) {
        communicateContext(context, ComunicateMng.TOGGLE_MINI_PLAYER_CONTROLLER);
    }

    public static void pauseVideo(Context context) {
        communicateContext(context, ComunicateMng.PAUSE_MINI_PLAYER);
    }

    public static void resumeVideo(Context context) {
        communicateContext(context, ComunicateMng.RESUME_MINI_PLAYER);
    }

    public static void toggleResumePauseVideo(Context context) {
        communicateContext(context, ComunicateMng.TOGGLE_RESUME_PAUSE_MINI_PLAYER);
    }

    public static void openAppFromMiniPlayer(Context context) {
        communicateContext(context, ComunicateMng.OPEN_APP_FROM_MINI_PLAYER);
    }

    public static void disappearMiniplayer(Context context) {
        communicateContext(context, ComunicateMng.DISAPPEAR);
    }

    public static void appearMiniplayer(Context context) {
        communicateContext(context, ComunicateMng.APPEAR);
    }

    private static void communicateContext(Context context, String event) {
        if (isMiniPlayerRunning(context)) {
            ComunicateMng.MsgFromActivity msgFromActivity = new ComunicateMng.MsgFromActivity(event);
            ComunicateMng.postFromActivity(msgFromActivity);
        }
    }

    public static void moveTaskToFront(@NonNull Activity activity, boolean mIsRestoredToTop) {
        if (android.os.Build.VERSION.SDK_INT >= 19 && !activity.isTaskRoot() && mIsRestoredToTop) {
            // 4.4.2 platform issues for FLAG_ACTIVITY_REORDER_TO_FRONT,
            // reordered activity back press will go to home unexpectedly,
            // Workaround: move reordered activity current task to front when it's finished.
            ActivityManager tasksManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            tasksManager.moveTaskToFront(activity.getTaskId(), ActivityManager.MOVE_TASK_NO_USER_ACTION);
        }
    }

    //===================================================================================
    public static void saveLastServerTime(long currentTimeMillis) {
        SharedPreferenceUtil.put(getPrivatePreference(context), LAST_SYNCED_SERVER_TIME, currentTimeMillis);
    }

    public static long getLastServerTime() {
        return (long) SharedPreferenceUtil.get(getPrivatePreference(context), LAST_SYNCED_SERVER_TIME,
                System.currentTimeMillis());
    }

    public static void saveLastElapsedTime(long elapsedTime) {
        SharedPreferenceUtil.put(getPrivatePreference(context), LAST_ELAPSED_TIME, elapsedTime);
    }

    public static long getLastElapsedTime() {
        return (long) SharedPreferenceUtil.get(getPrivatePreference(context), LAST_ELAPSED_TIME,
                SystemClock.elapsedRealtime());
    }
}
