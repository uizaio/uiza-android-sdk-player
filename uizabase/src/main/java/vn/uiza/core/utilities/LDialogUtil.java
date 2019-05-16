package vn.uiza.core.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import vn.uiza.R;

/**
 * Created by www.muathu@gmail.com on 12/29/2017.
 */

public class LDialogUtil {
    private final static String TAG = LDialogUtil.class.getSimpleName();
    private static List<AlertDialog> alertDialogList = new ArrayList<>();

    public static void clearAll() {
        if (alertDialogList == null) {
            return;
        }
        for (int i = 0; i < alertDialogList.size(); i++) {
            if (alertDialogList.get(i) != null) {
                hide(alertDialogList.get(i));
            }
        }
    }

    public interface Callback1 {
        void onClick1();

        void onCancel();
    }

    public static void showDialog1(Context context, String msg, Callback1 callback1) {
        showDialog1(context, context.getString(R.string.warning), msg, context.getString(R.string.confirm), callback1);
    }

    public static void showDialog1(Context context, String title, String msg, String button1, final Callback1 callback1) {
        clearAll();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null && !title.isEmpty()) {
            builder.setTitle(title);
        }
        builder.setMessage(msg);
        builder.setPositiveButton(button1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (callback1 != null) {
                    callback1.onClick1();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (callback1 != null) {
                    callback1.onCancel();
                }
            }
        });
        dialog.show();
        int color = ContextCompat.getColor(context, R.color.colorPrimary);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
        alertDialogList.add(dialog);
    }

    public static void showDialog1Immersive(Context context, String msg, final Callback1 callback1) {
        clearAll();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.warning));
        builder.setMessage(msg);
        builder.setPositiveButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (callback1 != null) {
                    callback1.onClick1();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (callback1 != null) {
                    callback1.onCancel();
                }
            }
        });
        boolean isFullScreen = LScreenUtil.isFullScreen(context);
        Window window = dialog.getWindow();
        if (window == null) return;
        if (isFullScreen) {
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        dialog.show();
        if (isFullScreen) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
        int color = ContextCompat.getColor(context, R.color.colorPrimary);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
        alertDialogList.add(dialog);
    }

    public interface Callback2 {
        void onClick1();

        void onClick2();
    }

    public static AlertDialog showDialog2(Context context, String title, String msg, String button1, String button2, final Callback2 callback2) {
        clearAll();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null && !title.isEmpty()) {
            builder.setTitle(title);
        }
        builder.setMessage(msg);
        if (button1 != null && !button1.isEmpty()) {
            builder.setNegativeButton(button1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (callback2 != null) {
                        callback2.onClick1();
                    }
                }
            });
        }
        if (button2 != null && !button2.isEmpty()) {
            builder.setPositiveButton(button2, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (callback2 != null) {
                        callback2.onClick2();
                    }
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.show();
        int color = ContextCompat.getColor(context, R.color.colorPrimary);
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
        alertDialogList.add(dialog);
        return dialog;
    }

    public interface Callback3 {
        void onClick1();

        void onClick2();

        void onClick3();
    }

    public static void showDialog3(Context context, String title, String msg, String button1, String button2, String button3, final Callback3 callback3) {
        clearAll();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null && !title.isEmpty()) {
            builder.setTitle(title);
        }
        builder.setMessage(msg);
        if (button1 != null && !button1.isEmpty()) {
            builder.setNegativeButton(button1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (callback3 != null) {
                        callback3.onClick1();
                    }
                }
            });
        }
        if (button2 != null && !button2.isEmpty()) {
            builder.setPositiveButton(button2, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (callback3 != null) {
                        callback3.onClick2();
                    }
                }
            });
        }
        if (button3 != null && !button3.isEmpty()) {
            builder.setNeutralButton(button3, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (callback3 != null) {
                        callback3.onClick3();
                    }
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.show();
        int color = ContextCompat.getColor(context, R.color.colorPrimary);
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(color);
        alertDialogList.add(dialog);
    }

    public interface CallbackList {
        void onClick(int position);
    }

    public static void showDialogList(Context context, String title, String[] arr, final CallbackList callbackList) {
        clearAll();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null && !title.isEmpty()) {
            builder.setTitle(title);
        }
        builder.setItems(arr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (callbackList != null) {
                    callbackList.onClick(which);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        alertDialogList.add(dialog);
    }

    public static ProgressDialog getSpinnerProgressDialog(Context context, String title, String msg) {
        return getProgressDialog(context, 100, title, msg, false, ProgressDialog.STYLE_SPINNER, null, null);
    }

    //style ex ProgressDialog.STYLE_HORIZONTAL
    public static ProgressDialog getProgressDialog(Context context, int max, String title, String msg, boolean isCancelAble, int style, String buttonTitle, final Callback1 callback1) {
        clearAll();
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMax(max);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(isCancelAble);
        progressDialog.setTitle(title);
        progressDialog.setProgressStyle(style);
        if (buttonTitle != null) {
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, buttonTitle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (callback1 != null) {
                        callback1.onClick1();
                    }
                }
            });
        }
        alertDialogList.add(progressDialog);
        return progressDialog;
    }

    public static void show(Dialog dialog) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public static void hide(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    public static void show(ProgressBar progressBar) {
        if (progressBar != null && progressBar.getVisibility() != View.VISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public static void hide(ProgressBar progressBar) {
        if (progressBar != null && progressBar.getVisibility() != View.GONE) {
            progressBar.setVisibility(View.GONE);
        }
    }
}
