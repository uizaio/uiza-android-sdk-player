package vn.uiza.core.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ProgressBar;
import java.util.ArrayList;
import java.util.List;
import vn.uiza.R;

public class LDialogUtil {
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
