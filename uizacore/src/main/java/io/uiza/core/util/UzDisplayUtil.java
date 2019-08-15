package io.uiza.core.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class UzDisplayUtil {

    private static final String DIMEN = "dimen";
    private static final String STATUS_BAR_HEIGHT = "status_bar_height";
    private static final String ANDROID = "android";

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier(STATUS_BAR_HEIGHT, DIMEN, ANDROID);
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
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
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        final Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        if (Build.VERSION.SDK_INT >= 19) {
            // include navigation bar
            display.getRealSize(outPoint);
        } else {
            // exclude navigation bar
            display.getSize(outPoint);
        }
        int realHeight;
        if (outPoint.y > outPoint.x) {
            realHeight = outPoint.y;
            //mRealSizeWidth = outPoint.x;
        } else {
            realHeight = outPoint.x;
            //mRealSizeWidth = outPoint.y;
        }
        return realHeight;
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
        final Window window = ((Activity) context).getWindow();
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
        final Window window = ((Activity) context).getWindow();
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
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getOrientation();
        return rotation != Surface.ROTATION_0 && rotation != Surface.ROTATION_180;
    }

    public static boolean toggleScreenOrientation(final Activity activity) {
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

    public static int getScreenOrientation(Context context) {
        return context.getResources().getConfiguration().orientation;
    }

    public interface DelayCallback {

        void doAfter(int mls);
    }

    public static void setDelay(final int mls, final UzDisplayUtil.DelayCallback delayCallback) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (delayCallback != null) {
                    delayCallback.doAfter(mls);
                }
            }
        }, mls);
    }

    public static void setLastCursorEditText(EditText editText) {
        if (editText == null) {
            return;
        }
        if (!editText.getText().toString().isEmpty()) {
            editText.setSelection(editText.getText().length());
        }
    }

    public static void setTextShadow(TextView textView) {
        setTextShadow(textView, Color.BLACK);
    }

    public static void setTextShadow(TextView textView, int color) {
        if (textView == null) {
            return;
        }
        textView.setShadowLayer(
                1f, // radius
                1f, // dx
                1f, // dy
                color // shadow color
        );
    }

    public static void printBeautyJson(Object o, TextView textView) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(o);
        textView.setText(json);
    }

    public static void setColorProgressBar(ProgressBar progressBar, int color) {
        if (progressBar == null) {
            return;
        }
        progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    public static void showProgressBar(ProgressBar progressBar) {
        if (progressBar != null && progressBar.getVisibility() != View.VISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public static void hideProgressBar(ProgressBar progressBar) {
        if (progressBar != null && progressBar.getVisibility() != View.GONE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    public static void setTextViewUnderLine(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public static void setMarginDimen(View view, int dpL, int dpT, int dpR, int dpB) {
        if (view == null) {
            return;
        }
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        mlp.setMargins(UzConvertUtils.dp2px(dpL), UzConvertUtils.dp2px(dpT),
                UzConvertUtils.dp2px(dpR), UzConvertUtils.dp2px(dpB));
        view.requestLayout();
    }

    public static void setMarginPx(View view, int l, int t, int r, int b) {
        if (view == null) {
            return;
        }
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        mlp.setMargins(l, t, r, b);
        view.requestLayout();
    }

    //return pixel
    public static int getHeightOfView(View view) {
        if (view == null) {
            return 0;
        }
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    public static void visibleViews(View... views) {
        for (View v : views) {
            if (v != null && v.getVisibility() != View.VISIBLE) {
                v.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void goneViews(View... views) {
        for (View v : views) {
            if (v != null && v.getVisibility() != View.GONE) {
                v.setVisibility(View.GONE);
            }
        }
    }

    public static void invisibleViews(View... views) {
        for (View v : views) {
            if (v != null && v.getVisibility() != View.INVISIBLE) {
                v.setVisibility(View.INVISIBLE);
            }
        }
    }

    public static void setVisibilityViews(int visibility, View... views) {
        for (View v : views) {
            if (v != null && v.getVisibility() != visibility) {
                v.setVisibility(visibility);
            }
        }
    }

    public static void setFocusableViews(boolean focusable, View... views) {
        for (View v : views) {
            if (v != null && !v.isFocusable()) {
                v.setFocusable(focusable);
            }
        }
    }

    public static void performClick(View view) {
        if (view != null) {
            view.performClick();
        }
    }
}
