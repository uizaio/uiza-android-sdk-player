package vn.uiza.core.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class LScreenUtil {
    private static final String DIMEN = "dimen";
    private static final String STATUS_BAR_HEIGHT = "status_bar_height";
    private static final String ANDROID = "android";

    public static int getStatusBarHeight(Context mContext) {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier(STATUS_BAR_HEIGHT, DIMEN, ANDROID);
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int getScreenHeightIncludeNavigationBar(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        if (Build.VERSION.SDK_INT >= 19) {
            // include navigation bar
            display.getRealSize(outPoint);
        } else {
            // exclude navigation bar
            display.getSize(outPoint);
        }
        int mRealSizeHeight;
        if (outPoint.y > outPoint.x) {
            mRealSizeHeight = outPoint.y;
            //mRealSizeWidth = outPoint.x;
        } else {
            mRealSizeHeight = outPoint.x;
            //mRealSizeWidth = outPoint.y;
        }
        return mRealSizeHeight;
    }

    public static void toggleFullscreen(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags ^= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(attrs);
    }

    public static void toggleFullscreen(Activity activity, boolean isFullScreen) {
        int flags;
        if (isFullScreen) {
            flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                flags = flags | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
        } else {
            flags = View.SYSTEM_UI_FLAG_VISIBLE;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    @SuppressLint("ObsoleteSdkInt")
    public static void hideDefaultControls(@NonNull final Context context) {
        final Window window = ((Activity)context).getWindow();
        if (window == null) {
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final View decorView = window.getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= 14) {
            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        decorView.setSystemUiVisibility(uiOptions);
    }

    @SuppressLint("ObsoleteSdkInt")
    public static void showDefaultControls(@NonNull final Context context) {
        final Window window = ((Activity)context).getWindow();
        if (window == null) {
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        final View decorView = window.getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= 14) {
            uiOptions &= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            uiOptions &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        decorView.setSystemUiVisibility(uiOptions);
    }

    public static boolean isFullScreen(Context context) {
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        return rotation != Surface.ROTATION_0 && rotation != Surface.ROTATION_180;
    }
}
