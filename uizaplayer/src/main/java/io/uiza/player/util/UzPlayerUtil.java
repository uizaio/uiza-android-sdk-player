package io.uiza.player.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
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
import io.uiza.core.util.LLog;
import io.uiza.core.util.SentryUtil;
import io.uiza.core.util.UzCommonUtil;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.player.R;
import io.uiza.player.analytic.TmpParamData;
import io.uiza.player.UzTrackItem;

public class UzPlayerUtil {

    private static final String TAG = UzPlayerUtil.class.getSimpleName();

    public static void setUiFullScreenIcon(ImageButton imageButton, boolean isFullScreen) {
        if (imageButton == null) {
            return;
        }
        if (isFullScreen) {
            imageButton.setImageResource(R.drawable.baseline_fullscreen_exit_white_48);
        } else {
            imageButton.setImageResource(R.drawable.baseline_fullscreen_white_48);
        }
    }

    public static void resizeLayout(ViewGroup viewGroup, ImageView ivVideoCover, int pixelAdded,
            int videoW, int videoH, boolean isFreeSize) {
        if (viewGroup == null) {
            return;
        }
        int surfaceViewWidth;
        int surfaceViewHeight;
        boolean isFullScreen = UzDisplayUtil.isFullScreen(viewGroup.getContext());
        if (isFullScreen) {
            //landscape
            surfaceViewWidth = UzDisplayUtil
                    .getScreenHeightIncludeNavigationBar(viewGroup.getContext());
            surfaceViewHeight = UzDisplayUtil.getScreenHeight();
        } else {
            //portrait
            surfaceViewWidth = UzDisplayUtil.getScreenWidth();
            if (videoW == 0 || videoH == 0) {
                surfaceViewHeight = (int) (surfaceViewWidth * Constants.RATIO_9_16) + pixelAdded;
            } else {
                if (videoW >= videoH) {
                    if (isFreeSize) {
                        surfaceViewHeight = surfaceViewWidth * videoH / videoW + pixelAdded;
                    } else {
                        surfaceViewHeight =
                                (int) (surfaceViewWidth * Constants.RATIO_9_16) + pixelAdded;
                    }
                } else {
                    if (isFreeSize) {
                        surfaceViewHeight = surfaceViewWidth * videoH / videoW + pixelAdded;
                    } else {
                        surfaceViewHeight =
                                (int) (surfaceViewWidth * Constants.RATIO_9_16) + pixelAdded;
                    }
                }
            }
        }
        TmpParamData.getInstance().setPlayerWidth(surfaceViewWidth);
        TmpParamData.getInstance().setPlayerHeight(surfaceViewHeight);
        viewGroup.getLayoutParams().width = surfaceViewWidth;
        viewGroup.getLayoutParams().height = surfaceViewHeight;
        viewGroup.requestLayout();
        //set size of parent view group of viewGroup
        RelativeLayout parentViewGroup = (RelativeLayout) viewGroup.getParent();
        if (parentViewGroup != null) {
            parentViewGroup.getLayoutParams().width = surfaceViewWidth;
            parentViewGroup.getLayoutParams().height = surfaceViewHeight;
            parentViewGroup.requestLayout();
        }
        if (ivVideoCover != null) {
            ivVideoCover.getLayoutParams().width = surfaceViewWidth;
            ivVideoCover.getLayoutParams().height = surfaceViewHeight - pixelAdded;
            ivVideoCover.requestLayout();
        }
        //edit size of imageview thumbnail
        FrameLayout layoutThumbPreview = viewGroup.findViewById(R.id.preview_frame_layout);
        if (layoutThumbPreview != null) {
            if (isFullScreen) {
                layoutThumbPreview.getLayoutParams().width = surfaceViewWidth / 4;
                layoutThumbPreview.getLayoutParams().height = (int) (surfaceViewWidth / 4
                        * Constants.RATIO_9_16);
            } else {
                layoutThumbPreview.getLayoutParams().width = surfaceViewWidth / 5;
                layoutThumbPreview.getLayoutParams().height = (int) (surfaceViewWidth / 5
                        * Constants.RATIO_9_16);
            }
            layoutThumbPreview.requestLayout();
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
                if (((Button) childView).getText().toString()
                        .equalsIgnoreCase(debugRootView.getContext().getString(R.string.video))) {
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
                if (((Button) childView).getText().toString()
                        .equalsIgnoreCase(debugRootView.getContext().getString(R.string.audio))) {
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
                if (((Button) childView).getText().toString()
                        .equalsIgnoreCase(debugRootView.getContext().getString(R.string.text))) {
                    return childView;
                }
            }
        }
        return null;
    }

    public static void showUizaDialog(Context context, Dialog dialog) {
        if (context == null || dialog == null) {
            return;
        }
        boolean isFullScreen = UzDisplayUtil.isFullScreen(context);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        if (isFullScreen) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
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
            SentryUtil.captureException(e);
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
            textView.setText((min / 60) + ":" + minutes);
        } catch (Exception e) {
            LLog.e(TAG, "Error setTextDuration " + e.toString());
            textView.setText(" - ");
            SentryUtil.captureException(e);
        }
    }

    //description
    //return SD, HD, FHD, QHD...
    public static UzTrackItem.Format getFormatVideo(String description) {
        String format = UzTrackItem.Format.F_UNKNOWN;
        String profile = UzTrackItem.Format.P_UNKNOWN;
        if (description.contains(",")) {
            String resolution = description.split(",")[0];
            if (UzCommonUtil.hasSpace(resolution)) {
                String[] s = resolution.split(" ");
                if (s.length >= 3) {
                    String s0 = s[0];
                    String s1 = s[2];

                    int width;
                    int height;
                    try {
                        width = Integer.parseInt(s0);
                        height = Integer.parseInt(s1);
                    } catch (Exception e) {
                        SentryUtil.captureException(e);
                        return new UzTrackItem.Format();
                    }
                    return getFormatVideo(width, height);
                }
            }
        }
        UzTrackItem.Format f = new UzTrackItem.Format();
        f.setFormat(format);
        f.setProfile(profile);
        return f;
    }

    //description
    //return SD, HD, FHD, QHD...
    public static UzTrackItem.Format getFormatVideo(int width, int height) {
        String format;
        String profile;
        //set profile
        //https://docs.google.com/spreadsheets/d/13lIsH711GJjttmZzFixph3RZwvP7a7vZhppSFnvsEl8/edit#gid=1297908801
        if (width < height) {
            width = height;
        }
        if (width <= 480) {
            profile = UzTrackItem.Format.P_270;
            format = UzTrackItem.Format.F_SD;
        } else if (width <= 640) {
            profile = UzTrackItem.Format.P_360;
            format = UzTrackItem.Format.F_SD;
        } else if (width <= 854) {
            profile = UzTrackItem.Format.P_480;
            format = UzTrackItem.Format.F_SD;
        } else if (width <= 1280) {
            profile = UzTrackItem.Format.P_720;
            format = UzTrackItem.Format.F_HD;
        } else if (width <= 1920) {
            profile = UzTrackItem.Format.P_1080;
            format = UzTrackItem.Format.F_FHD;
        } else if (width <= 2560) {
            profile = UzTrackItem.Format.P_1440;
            format = UzTrackItem.Format.F_2K;
        } else if (width <= 3840) {
            profile = UzTrackItem.Format.P_2160;
            format = UzTrackItem.Format.F_4K;
        } else {
            profile = UzTrackItem.Format.P_UNKNOWN;
            format = UzTrackItem.Format.F_UNKNOWN;
        }
        UzTrackItem.Format f = new UzTrackItem.Format();
        f.setFormat(format);
        f.setProfile(profile);
        return f;
    }
}
