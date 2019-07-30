package vn.uiza.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import vn.uiza.R;
import vn.uiza.core.utilities.LLog;
import vn.uiza.utils.util.SentryUtils;

public class LToast {
    private final static String TAG = LToast.class.getSimpleName();

    public static void show(Context context, String s) {
        show(context, s, 0);
    }

    public static void show(Context context, int resource) {
        show(context, resource, 0);
    }

    @SuppressLint("InflateParams")
    public static void show(Context context, int resource, int length) {
        show(context, context.getResources().getString(resource), length);
    }

    @SuppressLint("InflateParams")
    public static void show(Context context, String msg, int length) {
        if (context == null) {
            return;
        }
        clear();
        try {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inf.inflate(R.layout.view_l_toast, null);
            TextView textView = layout.findViewById(R.id.tv_loading);
            textView.setText(msg);
            Toast toast = new Toast(context);
            toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
            toast.setDuration(length);
            toast.setView(layout);
            toast.show();
            toastList.add(toast);
        } catch (Exception e) {
            LLog.d(TAG, "LToast" + e.toString());
            SentryUtils.captureException(e);
        }
    }

    private static List<Toast> toastList = new ArrayList<>();

    private static void clear() {
        for (int i = 0; i < toastList.size(); i++) {
            if (toastList.get(i) != null) {
                toastList.get(i).cancel();
            }
        }
    }
}
