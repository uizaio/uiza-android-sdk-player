package uizacoresdk.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

import uizacoresdk.R;
import uizacoresdk.chromecast.Casty;
import uizacoresdk.model.UZCustomLinkPlay;
import uizacoresdk.view.ComunicateMng;
import uizacoresdk.view.dlg.hq.UZItem;
import uizacoresdk.view.floatview.FUZVideoService;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LConnectivityUtil;
import vn.uiza.core.utilities.LDeviceUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.restapi.uiza.model.v2.auth.Auth;
import vn.uiza.restapi.uiza.model.v2.listallentity.Subtitle;
import vn.uiza.utils.CallbackGetDetailEntity;
import vn.uiza.utils.UZUtilBase;
import vn.uiza.utils.util.ConvertUtils;
import vn.uiza.utils.util.Utils;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;
import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by loitp on 4/11/2018.
 */

public class UZUtil {
    private final static String TAG = UZUtil.class.getSimpleName();

    public static void setUIFullScreenIcon(Context context, ImageButton imageButton, boolean isFullScreen) {
        if (imageButton == null) {
            return;
        }
        if (isFullScreen) {
            imageButton.setImageResource(R.drawable.baseline_fullscreen_exit_white_48);
        } else {
            imageButton.setImageResource(R.drawable.baseline_fullscreen_white_48);
        }
    }

    public static void resizeLayout(ViewGroup viewGroup, ImageView ivVideoCover, int pixelAdded, int videoW, int videoH) {
        if (viewGroup == null) {
            return;
        }
        int widthSurfaceView = 0;
        int heightSurfaceView = 0;
        boolean isFullScreen = LScreenUtil.isFullScreen(viewGroup.getContext());
        if (isFullScreen) {
            //landscape
            widthSurfaceView = LScreenUtil.getScreenHeightIncludeNavigationBar(viewGroup.getContext());
            heightSurfaceView = LScreenUtil.getScreenHeight();
        } else {
            //portrait
            widthSurfaceView = LScreenUtil.getScreenWidth();
            //heightSurfaceView = (int) (widthSurfaceView * Constants.RATIO_9_16) + pixelAdded;
            if (videoW == 0 || videoH == 0) {
                heightSurfaceView = (int) (widthSurfaceView * Constants.RATIO_9_16) + pixelAdded;
            } else {
                if (videoW >= videoH) {
                    //LLog.d(TAG, "video source is landscape -> scale depend on videoW, videoH");
                    heightSurfaceView = widthSurfaceView * videoH / videoW + pixelAdded;
                } else {
                    //LLog.d(TAG, "video source is portrait -> scale 9-16");
                    //heightSurfaceView = (int) (widthSurfaceView * Constants.RATIO_9_16) + pixelAdded;
                    heightSurfaceView = widthSurfaceView;
                }
            }
        }
        //LLog.d(TAG, "resizeLayout isFullScreen " + isFullScreen + ", widthSurfaceView x heightSurfaceView: " + widthSurfaceView + "x" + heightSurfaceView + ", pixelAdded: " + pixelAdded + ", videoW: " + videoW + ", videoH: " + videoH);
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
            ivVideoCover.getLayoutParams().height = heightSurfaceView;
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
    public static View getBtVideo(LinearLayout debugRootView) {
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

    public static List<Subtitle> createDummySubtitle(Gson gson) {
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
        Subtitle[] subtitles = gson.fromJson(json, new TypeToken<Subtitle[]>() {
        }.getType());
        //LLog.d(TAG, "createDummySubtitle subtitles " + gson.toJson(subtitles));
        List subtitleList = Arrays.asList(subtitles);
        //LLog.d(TAG, "createDummySubtitle subtitleList " + gson.toJson(subtitleList));
        return subtitleList;
    }

    public static void showUizaDialog(Activity activity, Dialog dialog) {
        if (activity == null || dialog == null) {
            return;
        }
        boolean isFullScreen = LScreenUtil.isFullScreen(activity);
        if (isFullScreen) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                dialog.getWindow().getDecorView().setSystemUiVisibility(
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
            dialog.getWindow().getAttributes().windowAnimations = R.style.uiza_dialog_animation;
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog_uiza);
            //set dialog position
            WindowManager.LayoutParams wlp = dialog.getWindow().getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            //wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            wlp.dimAmount = 0.65f;
            dialog.getWindow().setAttributes(wlp);

            int width = 0;
            int height = 0;
            if (isFullScreen) {
                width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 1.0);
                height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.5);
            } else {
                width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 1.0);
                height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.3);
            }
            dialog.getWindow().setLayout(width, height);
        } catch (Exception e) {
            //do nothing
        }
        if (isFullScreen) {
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
    }

    public static void setTextDuration(TextView textView, String duration) {
        //LLog.d(TAG, "duration: " + duration);
        if (textView == null || duration == null || duration.isEmpty()) {
            return;
        }
        try {
            int min = (int) Double.parseDouble(duration) + 1;
            String minutes = Integer.toString(min % 60);
            minutes = minutes.length() == 1 ? "0" + minutes : minutes;
            textView.setText((min / 60) + ":" + minutes);
        } catch (Exception e) {
            LLog.e(TAG, "Error setTextDuration " + e.toString());
            textView.setText(" - ");
        }
    }

    /*
     **Hàm này set size của MediaRouteButton giống với size default của ImageButtonWithSize
     */
    /*public static void setUICastButton(MediaRouteButton uiCastButton) {
        if (uiCastButton == null) {
            return;
        }
        boolean isTablet = LDeviceUtil.isTablet(uiCastButton.getContext());
        int ratioPort;
        int ratioLand;
        if (isTablet) {
            ratioLand = Constants.RATIO_LAND_TABLET;
            ratioPort = Constants.RATIO_PORTRAIT_TABLET;
        } else {
            ratioLand = Constants.RATIO_LAND_MOBILE;
            ratioPort = Constants.RATIO_PORTRAIT_MOBILE;
        }
        int size;
        if (LScreenUtil.isFullScreen(uiCastButton.getContext())) {
            int screenWLandscape = LScreenUtil.getScreenHeightIncludeNavigationBar(uiCastButton.getContext());
            size = screenWLandscape / (ratioLand + 1);
        } else {
            int screenWPortrait = LScreenUtil.getScreenWidth();
            size = screenWPortrait / (ratioPort + 1);
        }
        LLog.d(TAG, "setUICastButton size: " + size + ", ratioPort: " + ratioPort + ", ratioLand: " + ratioLand);
        uiCastButton.getLayoutParams().width = size;
        uiCastButton.getLayoutParams().height = size;
        uiCastButton.requestLayout();
    }*/

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

    public static boolean checkServiceRunning(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            //LLog.d(TAG, "checkServiceRunning: " + FUZVideoService.class.getName());
            //LLog.d(TAG, "checkServiceRunning: " + service.service.getClassName());
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /*public static void setupRestClientV2(Activity activity) {
        if (RestClientV2.getRetrofit() == null && RestClientTracking.getRetrofit() == null) {
            String currentApi = getApiEndPoint(activity);
            if (currentApi == null || currentApi.isEmpty()) {
                LLog.e(TAG, "setupRestClientV2 trackUiza currentApi == null || currentApi.isEmpty()");
                return;
            }
            String token = getToken(activity);
            if (token == null || token.isEmpty()) {
                LLog.e(TAG, "setupRestClientV2 trackUiza token==null||token.isEmpty()");
                return;
            }
            String currentTrackApi = getApiTrackEndPoint(activity);
            if (currentTrackApi == null || currentTrackApi.isEmpty()) {
                LLog.e(TAG, "setupRestClientV2 currentTrackApi == null || currentTrackApi.isEmpty()");
                return;
            }

            RestClientV2.init(currentApi);
            RestClientV2.addAuthorization(token);
            RestClientTracking.init(currentTrackApi);

            if (Constants.IS_DEBUG) {
                LToast.show(activity, "setupRestClientV2 with currentApi: " + currentApi + "\ntoken:" + token + "\ncurrentTrackApi: " + currentTrackApi);
            }
        }
    }*/

    //stop service pip FUZVideoService
    public static void stopMiniPlayer(Activity activity) {
        if (activity == null) {
            return;
        }
        //LLog.d(TAG, "stopMiniPlayer");
        boolean isSvPipRunning = isMiniPlayerRunning(activity);
        //LLog.d(TAG, "isSvPipRunning " + isSvPipRunning);
        if (isSvPipRunning) {
            //stop service if running
            Intent intent = new Intent(activity, FUZVideoService.class);
            activity.stopService(intent);
        }
    }

    public static boolean isMiniPlayerRunning(Context context) {
        if (context == null) {
            return false;
        }
        return UZUtil.checkServiceRunning(context, FUZVideoService.class.getName());
    }

    //=============================================================================START FOR UIZA V3
    /*public static void initUizaWorkspace(Context context, UizaWorkspaceInfo uizaWorkspaceInfo) {
        if (uizaWorkspaceInfo == null || uizaWorkspaceInfo.getUsername() == null || uizaWorkspaceInfo.getPassword() == null || uizaWorkspaceInfo.getUrlApi() == null) {
            throw new NullPointerException("UizaWorkspaceInfo cannot be null or empty!");
        }
        setUizaWorkspaceInfo(context, uizaWorkspaceInfo);
        UZRestClient.init(Constants.PREFIXS + uizaWorkspaceInfo.getUrlApi());
    }*/

    /*public static UizaWorkspaceInfo getUizaWorkspace(Context context) {
        if (context == null) {
            return null;
        }
        return getUizaWorkspaceInfo(context);
    }*/

    /*public static void setResultGetToken(Context context, ResultGetToken resultGetToken) {
        setResultGetToken(context, resultGetToken);
    }

    public static ResultGetToken getResultGetToken(Context context) {
        return getResultGetToken(context);
    }*/

    /*public static String getToken(Context context) {
        if (context == null) {
            return null;
        }
        ResultGetToken resultGetToken = getResultGetToken(context);
        if (resultGetToken == null) {
            return null;
        }
        return resultGetToken.getData().getToken();
    }*/

    /*public static String getAppId(Context context) {
        if (context == null) {
            return null;
        }
        ResultGetToken resultGetToken = getResultGetToken(context);
        if (resultGetToken == null) {
            return null;
        }
        return resultGetToken.getData().getAppId();
    }*/

    public static void getDetailEntity(final BaseActivity activity, final String entityId, final CallbackGetDetailEntity callback) {
        UZUtilBase.getDetailEntity(activity, entityId, callback);
    }

    public static boolean initLinkPlay(Activity activity, UZVideo uzVideo) {
        if (activity == null) {
            throw new NullPointerException(UZException.ERR_12);
        }
        if (uzVideo == null) {
            throw new NullPointerException(UZException.ERR_13);
        }
        if (UZDataCLP.getInstance().getUzCustomLinkPlay() == null) {
            LLog.e(TAG, UZException.ERR_14);
            return false;
        }
        if (!LConnectivityUtil.isConnected(activity)) {
            LLog.e(TAG, UZException.ERR_0);
            return false;
        }
        if (UZUtil.getClickedPip(activity)) {
            LLog.d(TAG, "miniplayer STEP 6 initLinkPlay");
            UZUtil.playLinkPlay(uzVideo, UZDataCLP.getInstance().getUzCustomLinkPlay());
        } else {
            UZUtil.stopMiniPlayer(activity);
            UZUtil.playLinkPlay(uzVideo, UZDataCLP.getInstance().getUzCustomLinkPlay());
        }
        UZUtil.setIsInitPlaylistFolder(activity, false);
        return true;
    }

    public static void initEntity(Activity activity, UZVideo uzVideo, String entityId) {
        if (activity == null) {
            throw new NullPointerException(UZException.ERR_12);
        }
        if (uzVideo == null) {
            throw new NullPointerException(UZException.ERR_13);
        }
        if (entityId != null) {
            UZUtil.setClickedPip(activity, false);
        }
        if (!LConnectivityUtil.isConnected(activity)) {
            LLog.e(TAG, UZException.ERR_0);
            return;
        }
        if (UZUtil.getClickedPip(activity)) {
            LLog.d(TAG, "miniplayer STEP 6 initEntity");
            UZUtil.play(uzVideo, null);
        } else {
            //check if play entity
            UZUtil.stopMiniPlayer(activity);
            if (entityId != null) {
                //LLog.d(TAG, "initEntity entityId: " + entityId);
                UZUtil.play(uzVideo, entityId);
            }
        }
        UZUtil.setIsInitPlaylistFolder(activity, false);
        UZDataCLP.getInstance().clearData();
    }

    public static void initPlaylistFolder(Activity activity, UZVideo uzVideo, String metadataId) {
        if (activity == null) {
            throw new NullPointerException(UZException.ERR_12);
        }
        if (uzVideo == null) {
            throw new NullPointerException(UZException.ERR_13);
        }
        if (metadataId != null) {
            UZUtil.setClickedPip(activity, false);
        }
        if (!LConnectivityUtil.isConnected(activity)) {
            LLog.e(TAG, UZException.ERR_0);
            return;
        }
        //LLog.d(TAG, "initPlaylistFolder getClickedPip: " + UZUtil.getClickedPip(activity));
        if (UZUtil.getClickedPip(activity)) {
            //LLog.d(TAG, "called from pip enter fullscreen");
            if (UZData.getInstance().isPlayWithPlaylistFolder()) {
                LLog.d(TAG, "miniplayer STEP 6 initPlaylistFolder");
                playPlaylist(uzVideo, null);
            }
        } else {
            //check if play entity
            UZUtil.stopMiniPlayer(activity);
            //setMetadataId(activity, metadataId);
            playPlaylist(uzVideo, metadataId);
        }
        UZUtil.setIsInitPlaylistFolder(activity, true);
        UZDataCLP.getInstance().clearData();
    }

    private static void playLinkPlay(final UZVideo uzVideo, final UZCustomLinkPlay uzCustomLinkPlay) {
        UZData.getInstance().setSettingPlayer(false);
        uzVideo.post(new Runnable() {
            @Override
            public void run() {
                uzVideo.initLinkPlay(uzCustomLinkPlay.getLinkPlay(), uzCustomLinkPlay.isLivestream());
            }
        });
    }

    private static void play(final UZVideo uzVideo, final String entityId) {
        UZData.getInstance().setSettingPlayer(false);
        uzVideo.post(new Runnable() {
            @Override
            public void run() {
                uzVideo.init(entityId);
            }
        });
    }

    private static void playPlaylist(final UZVideo uzVideo, final String metadataId) {
        //LLog.d(TAG, "playPlaylist metadataId " + metadataId);
        UZData.getInstance().setSettingPlayer(false);
        uzVideo.post(new Runnable() {
            @Override
            public void run() {
                uzVideo.initPlaylistFolder(metadataId);
            }
        });
    }

    public static void initWorkspace(Context context, String domainApi, String token, String appId, int env, int currentPlayerId) {
        if (context == null) {
            throw new NullPointerException(UZException.ERR_15);
        }
        if (domainApi == null || domainApi.isEmpty()) {
            throw new NullPointerException(UZException.ERR_16);
        }
        if (token == null || token.isEmpty()) {
            throw new NullPointerException(UZException.ERR_17);
        }
        if (appId == null || appId.isEmpty()) {
            throw new NullPointerException(UZException.ERR_18);
        }
        Utils.init(context.getApplicationContext());
        UZUtil.setCurrentPlayerId(currentPlayerId);
        //UZData.getInstance().setCurrentPlayerId(currentPlayerId);
        UZData.getInstance().initSDK(domainApi, token, appId, env);
    }

    public static void initWorkspace(Context context, String domainApi, String token, String appId, int currentPlayerId) {
        initWorkspace(context, domainApi, token, appId, Constants.ENVIRONMENT_PROD, currentPlayerId);
    }

    public static void initWorkspace(Context context, String domainApi, String token, String appId) {
        initWorkspace(context, domainApi, token, appId, Constants.ENVIRONMENT_PROD, R.layout.uz_player_skin_1);
    }

    public static void setCasty(Activity activity) {
        if (activity == null) {
            throw new NullPointerException(UZException.ERR_12);
        }
        if (LDeviceUtil.isTV(activity)) {
            return;
        }
        UZData.getInstance().setCasty(Casty.create(activity));
    }

    public static void setCurrentPlayerId(int resLayoutMain) {
        UZData.getInstance().setCurrentPlayerId(resLayoutMain);
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
    public static UZItem.Format getFormatVideo(String description) {
        String format = UZItem.Format.F_UNKNOW;
        String profile = UZItem.Format.P_UNKNOW;
        if (description.contains(",")) {
            String resolution = description.split(",")[0];
            //LLog.d(TAG, "resolution " + resolution);
            if (resolution.contains(" ")) {
                String s[] = resolution.split(" ");
                if (s.length >= 3) {
                    String s0 = s[0];
                    String s1 = s[2];
                    //LLog.d(TAG, "s0 x s1: " + s0 + " x " + s1);

                    int w;
                    int h;
                    try {
                        w = Integer.parseInt(s0);
                        h = Integer.parseInt(s1);
                    } catch (Exception e) {
                        return new UZItem.Format();
                    }
                    if (w < h) {
                        w = h;
                    }

                    //set profile
                    //https://docs.google.com/spreadsheets/d/13lIsH711GJjttmZzFixph3RZwvP7a7vZhppSFnvsEl8/edit#gid=1297908801
                    if (w <= 480) {
                        profile = UZItem.Format.P_270;
                        format = UZItem.Format.F_SD;
                    } else if (w <= 640) {
                        profile = UZItem.Format.P_360;
                        format = UZItem.Format.F_SD;
                    } else if (w <= 854) {
                        profile = UZItem.Format.P_480;
                        format = UZItem.Format.F_SD;
                    } else if (w <= 1280) {
                        profile = UZItem.Format.P_720;
                        format = UZItem.Format.F_HD;
                    } else if (w <= 1920) {
                        profile = UZItem.Format.P_1080;
                        format = UZItem.Format.F_FHD;
                    } else if (w <= 2560) {
                        profile = UZItem.Format.P_1440;
                        format = UZItem.Format.F_2K;
                    } else if (w <= 3840) {
                        profile = UZItem.Format.P_2160;
                        format = UZItem.Format.F_4K;
                    } else {
                        profile = UZItem.Format.P_UNKNOW;
                        format = UZItem.Format.F_UNKNOW;
                    }
                }
            }
        }
        //LLog.d(TAG, "format: " + format + ", profile: " + profile);
        UZItem.Format f = new UZItem.Format();
        f.setFormat(format);
        f.setProfile(profile);
        return f;
    }

    //description
    //return SD, HD, FHD, QHD...
    public static UZItem.Format getFormatVideo(int width, int height) {
        //LLog.d(TAG, "getFormatVideo " + width + "x" + height);
        String format;
        String profile;
        //set profile
        //https://docs.google.com/spreadsheets/d/13lIsH711GJjttmZzFixph3RZwvP7a7vZhppSFnvsEl8/edit#gid=1297908801
        if (width < height) {
            width = height;
        }
        if (width <= 480) {
            profile = UZItem.Format.P_270;
            format = UZItem.Format.F_SD;
        } else if (width <= 640) {
            profile = UZItem.Format.P_360;
            format = UZItem.Format.F_SD;
        } else if (width <= 854) {
            profile = UZItem.Format.P_480;
            format = UZItem.Format.F_SD;
        } else if (width <= 1280) {
            profile = UZItem.Format.P_720;
            format = UZItem.Format.F_HD;
        } else if (width <= 1920) {
            profile = UZItem.Format.P_1080;
            format = UZItem.Format.F_FHD;
        } else if (width <= 2560) {
            profile = UZItem.Format.P_1440;
            format = UZItem.Format.F_2K;
        } else if (width <= 3840) {
            profile = UZItem.Format.P_2160;
            format = UZItem.Format.F_4K;
        } else {
            profile = UZItem.Format.P_UNKNOW;
            format = UZItem.Format.F_UNKNOW;
        }
        //LLog.d(TAG, "format: " + format + ", profile: " + profile);
        UZItem.Format f = new UZItem.Format();
        f.setFormat(format);
        f.setProfile(profile);
        return f;
    }

    //=============================================================================END FOR UIZA V3

    //=============================================================================START PREF
    private final static String PREFERENCES_FILE_NAME = "loitp";
    private final static String AUTH = "AUTH";
    private final static String API_TRACK_END_POINT = "API_TRACK_END_POINT";
    private final static String TOKEN = "TOKEN";
    private final static String CLICKED_PIP = "CLICKED_PIP";
    private final static String CLASS_NAME_OF_PLAYER = "CLASS_NAME_OF_PLAYER";
    private final static String IS_INIT_PLAYLIST_FOLDER = "IS_INIT_PLAYLIST_FOLDER";
    private final static String VIDEO_WIDTH = "VIDEO_WIDTH";
    private final static String VIDEO_HEIGHT = "VIDEO_HEIGHT";
    private final static String MINI_PLAYER_COLOR_VIEW_DESTROY = "MINI_PLAYER_COLOR_VIEW_DESTROY";
    private final static String MINI_PLAYER_EZ_DESTROY = "MINI_PLAYER_EZ_DESTROY";
    private final static String MINI_PLAYER_ENABLE_VIBRATION = "MINI_PLAYER_ENABLE_VIBRATION";
    private final static String MINI_PLAYER_ENABLE_SMOOTH_SWITCH = "MINI_PLAYER_ENABLE_SMOOTH_SWITCH";
    private final static String MINI_PLAYER_CONTENT_POSITION_WHEN_SWITCH_TO_FULL_PLAYER = "MINI_PLAYER_CONTENT_POSITION_WHEN_SWITCH_TO_FULL_PLAYER";
    private final static String MINI_PLAYER_FIRST_POSITION_X = "MINI_PLAYER_FIRST_POSITION_X";
    private final static String MINI_PLAYER_FIRST_POSITION_Y = "MINI_PLAYER_FIRST_POSITION_Y";
    private final static String MINI_PLAYER_MARGIN_L = "MINI_PLAYER_MARGIN_L";
    private final static String MINI_PLAYER_MARGIN_T = "MINI_PLAYER_MARGIN_T";
    private final static String MINI_PLAYER_MARGIN_R = "MINI_PLAYER_MARGIN_R";
    private final static String MINI_PLAYER_MARGIN_B = "MINI_PLAYER_MARGIN_B";

    /////////////////////////////////STRING
    public static String getApiTrackEndPoint(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return pref.getString(API_TRACK_END_POINT, null);
    }

    public static void setApiTrackEndPoint(Context context, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putString(API_TRACK_END_POINT, value);
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return pref.getString(TOKEN, null);
    }

    public static void setToken(Context context, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putString(TOKEN, value);
        editor.apply();
    }

    public static String getClassNameOfPlayer(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return pref.getString(CLASS_NAME_OF_PLAYER, null);
    }

    public static void setClassNameOfPlayer(Context context, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putString(CLASS_NAME_OF_PLAYER, value);
        editor.apply();
    }

    /////////////////////////////////BOOLEAN
    public static Boolean isInitPlaylistFolder(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getBoolean(IS_INIT_PLAYLIST_FOLDER, false);
    }

    public static void setIsInitPlaylistFolder(Context context, Boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putBoolean(IS_INIT_PLAYLIST_FOLDER, value);
        editor.apply();
    }

    public static Boolean getClickedPip(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getBoolean(CLICKED_PIP, false);
    }

    public static void setClickedPip(Context context, Boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putBoolean(CLICKED_PIP, value);
        editor.apply();
    }

    public static Boolean getMiniPlayerEzDestroy(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getBoolean(MINI_PLAYER_EZ_DESTROY, false);
    }

    public static void setMiniPlayerEzDestroy(Context context, Boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putBoolean(MINI_PLAYER_EZ_DESTROY, value);
        editor.apply();
    }

    public static Boolean getMiniPlayerEnableVibration(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getBoolean(MINI_PLAYER_ENABLE_VIBRATION, true);
    }

    public static void setMiniPlayerEnableVibration(Context context, Boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putBoolean(MINI_PLAYER_ENABLE_VIBRATION, value);
        editor.apply();
    }

    public static Boolean getMiniPlayerEnableSmoothSwitch(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getBoolean(MINI_PLAYER_ENABLE_SMOOTH_SWITCH, true);
    }

    public static void setMiniPlayerEnableSmoothSwitch(Context context, Boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putBoolean(MINI_PLAYER_ENABLE_SMOOTH_SWITCH, value);
        editor.apply();
    }

    /////////////////////////////////INT
    public static int getVideoWidth(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getInt(VIDEO_WIDTH, 16);
    }

    public static void setVideoWidth(Context context, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putInt(VIDEO_WIDTH, value);
        editor.apply();
    }

    public static int getVideoHeight(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getInt(VIDEO_HEIGHT, 9);
    }

    public static void setVideoHeight(Context context, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putInt(VIDEO_HEIGHT, value);
        editor.apply();
    }

    public static int getMiniPlayerColorViewDestroy(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getInt(MINI_PLAYER_COLOR_VIEW_DESTROY, ContextCompat.getColor(context, R.color.black_65));
    }

    public static void setMiniPlayerColorViewDestroy(Context context, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putInt(MINI_PLAYER_COLOR_VIEW_DESTROY, value);
        editor.apply();
    }

    public static int getMiniPlayerFirstPositionX(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getInt(MINI_PLAYER_FIRST_POSITION_X, Constants.NOT_FOUND);
    }

    private static void setMiniPlayerFirstPositionX(Context context, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putInt(MINI_PLAYER_FIRST_POSITION_X, value);
        editor.apply();
    }

    public static int getMiniPlayerFirstPositionY(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getInt(MINI_PLAYER_FIRST_POSITION_Y, Constants.NOT_FOUND);
    }

    private static void setMiniPlayerFirstPositionY(Context context, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putInt(MINI_PLAYER_FIRST_POSITION_Y, value);
        editor.apply();
    }

    public static void setMiniPlayerFirstPosition(Context context, int firstPositionX, int firstPositionY) {
        setMiniPlayerFirstPositionX(context, firstPositionX);
        setMiniPlayerFirstPositionY(context, firstPositionY);
    }

    public static int getMiniPlayerMarginL(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getInt(MINI_PLAYER_MARGIN_L, 0);
    }

    private static void setMiniPlayerMarginL(Context context, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putInt(MINI_PLAYER_MARGIN_L, value);
        editor.apply();
    }

    public static int getMiniPlayerMarginT(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getInt(MINI_PLAYER_MARGIN_T, 0);
    }

    private static void setMiniPlayerMarginT(Context context, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putInt(MINI_PLAYER_MARGIN_T, value);
        editor.apply();
    }

    public static int getMiniPlayerMarginR(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getInt(MINI_PLAYER_MARGIN_R, 0);
    }

    private static void setMiniPlayerMarginR(Context context, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putInt(MINI_PLAYER_MARGIN_R, value);
        editor.apply();
    }

    public static int getMiniPlayerMarginB(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getInt(MINI_PLAYER_MARGIN_B, 0);
    }

    private static void setMiniPlayerMarginB(Context context, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putInt(MINI_PLAYER_MARGIN_B, value);
        editor.apply();
    }

    public static boolean setMiniPlayerMarginDp(Context context, float marginL, float marginT, float marginR, float marginB) {
        if (context == null) {
            return false;
        }
        int pxL = ConvertUtils.dp2px(marginL);
        int pxT = ConvertUtils.dp2px(marginT);
        int pxR = ConvertUtils.dp2px(marginR);
        int pxB = ConvertUtils.dp2px(marginB);
        return setMiniPlayerMarginPixel(context, pxL, pxT, pxR, pxB);
    }

    public static boolean setMiniPlayerMarginPixel(Context context, int marginL, int marginT, int marginR, int marginB) {
        if (context == null) {
            return false;
        }
        int screenW = LScreenUtil.getScreenWidth();
        int screenH = LScreenUtil.getScreenHeight();
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

    /////////////////////////////////OBJECT
    public static Auth getAuth(Context context, Gson gson) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        String json = pref.getString(AUTH, null);
        return gson.fromJson(json, Auth.class);
    }

    public static void setAuth(Context context, Auth auth, Gson gson) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putString(AUTH, gson.toJson(auth));
        editor.apply();
    }

    /////////////////////////////////LONG
    public static long getMiniPlayerContentPositionWhenSwitchToFullPlayer(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getLong(MINI_PLAYER_CONTENT_POSITION_WHEN_SWITCH_TO_FULL_PLAYER, Constants.UNKNOW);
    }

    public static void setMiniPlayerContentPositionWhenSwitchToFullPlayer(Context context, long value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putLong(MINI_PLAYER_CONTENT_POSITION_WHEN_SWITCH_TO_FULL_PLAYER, value);
        editor.apply();
    }

    //=============================================================================END PREF

    public static void showMiniPlayerController(Context context) {
        if (isMiniPlayerRunning(context)) {
            ComunicateMng.MsgFromActivity msgFromActivity = new ComunicateMng.MsgFromActivity(ComunicateMng.SHOW_MINI_PLAYER_CONTROLLER);
            ComunicateMng.postFromActivity(msgFromActivity);
        }
    }

    public static void hideMiniPlayerController(Context context) {
        if (isMiniPlayerRunning(context)) {
            ComunicateMng.MsgFromActivity msgFromActivity = new ComunicateMng.MsgFromActivity(ComunicateMng.HIDE_MINI_PLAYER_CONTROLLER);
            ComunicateMng.postFromActivity(msgFromActivity);
        }
    }

    public static void toggleMiniPlayerController(Context context) {
        if (isMiniPlayerRunning(context)) {
            ComunicateMng.MsgFromActivity msgFromActivity = new ComunicateMng.MsgFromActivity(ComunicateMng.TOGGLE_MINI_PLAYER_CONTROLLER);
            ComunicateMng.postFromActivity(msgFromActivity);
        }
    }

    public static void pauseVideo(Context context) {
        if (isMiniPlayerRunning(context)) {
            ComunicateMng.MsgFromActivity msgFromActivity = new ComunicateMng.MsgFromActivity(ComunicateMng.PAUSE_MINI_PLAYER);
            ComunicateMng.postFromActivity(msgFromActivity);
        }
    }

    public static void resumeVideo(Context context) {
        if (isMiniPlayerRunning(context)) {
            ComunicateMng.MsgFromActivity msgFromActivity = new ComunicateMng.MsgFromActivity(ComunicateMng.RESUME_MINI_PLAYER);
            ComunicateMng.postFromActivity(msgFromActivity);
        }
    }

    public static void toggleResumePauseVideo(Context context) {
        if (isMiniPlayerRunning(context)) {
            ComunicateMng.MsgFromActivity msgFromActivity = new ComunicateMng.MsgFromActivity(ComunicateMng.TOGGLE_RESUME_PAUSE_MINI_PLAYER);
            ComunicateMng.postFromActivity(msgFromActivity);
        }
    }

    public static void openAppFromMiniPlayer(Context context) {
        if (isMiniPlayerRunning(context)) {
            ComunicateMng.MsgFromActivity msgFromActivity = new ComunicateMng.MsgFromActivity(ComunicateMng.OPEN_APP_FROM_MINI_PLAYER);
            ComunicateMng.postFromActivity(msgFromActivity);
        }
    }
}
