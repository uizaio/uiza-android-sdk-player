package vn.uiza.core.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.view.View;
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
        public void onClick1();

        public void onCancel();
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
                //LLog.d(TAG, "setPositiveButton");
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
                //LLog.d(TAG, "setOnCancelListener");
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
        //LLog.d(TAG, "showDialog1");
        clearAll();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.warning));
        builder.setMessage(msg);
        builder.setPositiveButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //LLog.d(TAG, "setPositiveButton");
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
                //LLog.d(TAG, "setOnCancelListener");
                if (callback1 != null) {
                    callback1.onCancel();
                }
            }
        });
        boolean isFullScreen = LScreenUtil.isFullScreen(context);
        if (isFullScreen) {
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            dialog.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        dialog.show();
        if (isFullScreen) {
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
        int color = ContextCompat.getColor(context, R.color.colorPrimary);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
        alertDialogList.add(dialog);
    }

    /*public static void showDialog1(Context context, String title, String msg, String button1, final Callback1 callback1) {
        showDialog1(context, title, msg, button1, false, callback1, null);
    }*/

    /*public interface Callback2 {
        public void onClick1();

        public void onClick2();
    }

    public static void showDialog2(Context context, String title, String msg, String button1, String button2, final Callback2 callback2) {
        //LLog.d(TAG, "showDialog2");
        clearAll();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null && !title.isEmpty()) {
            builder.setTitle(title);
        }
        builder.setMessage(msg);
        if (button1 != null && !button1.isEmpty()) {
            //LLog.d(TAG, "button1");
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
            //LLog.d(TAG, "button2");
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
    }

    public interface Callback3 {
        public void onClick1();

        public void onClick2();

        public void onClick3();
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
        public void onClick(int position);
    }

    public static void showDialogList(Context context, String title, String[] arr, final CallbackList callbackList) {
        clearAll();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null && !title.isEmpty()) {
            builder.setTitle(title);
        }
        //String[] arr = {"horse", "cow", "camel", "sheep", "goat"};
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
    }*/

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

    /*public static void showIOSDialog1(Activity activity, String title, String subtitle, String label1, boolean isBold, final Callback1 callback1) {
        final iOSDialog iOSDialog = new iOSDialog(activity);
        iOSDialog.setTitle(title);
        iOSDialog.setSubtitle(subtitle);
        iOSDialog.setPositiveLabel(label1);
        iOSDialog.setBoldPositiveLabel(isBold);
        iOSDialog.setPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOSDialog.dismiss();
                if (callback1 != null) {
                    callback1.onClick1();
                }
            }
        });
        iOSDialog.show();
    }

    public static void showIOSDialog2(Activity activity, String title, String subtitle, String label1, String label2, boolean isBold, final Callback2 callback2) {
        final iOSDialog iOSDialog = new iOSDialog(activity);
        iOSDialog.setTitle(title);
        iOSDialog.setSubtitle(subtitle);
        iOSDialog.setNegativeLabel(label1);
        iOSDialog.setPositiveLabel(label2);
        iOSDialog.setBoldPositiveLabel(isBold);
        iOSDialog.setNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOSDialog.dismiss();
                if (callback2 != null) {
                    callback2.onClick1();
                }
            }
        });
        iOSDialog.setPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOSDialog.dismiss();
                if (callback2 != null) {
                    callback2.onClick2();
                }
            }
        });
        iOSDialog.show();
    }*/
}
