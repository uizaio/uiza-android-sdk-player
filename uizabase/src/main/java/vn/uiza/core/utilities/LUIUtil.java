package vn.uiza.core.utilities;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import vn.uiza.utils.util.ConvertUtils;

public class LUIUtil {

    public interface DelayCallback {
        void doAfter(int mls);
    }

    public static void setDelay(final int mls, final DelayCallback delayCallback) {
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
        progressBar.getIndeterminateDrawable().setColorFilter(color, android.graphics.PorterDuff.Mode.MULTIPLY);
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
        mlp.setMargins(ConvertUtils.dp2px(dpL), ConvertUtils.dp2px(dpT), ConvertUtils.dp2px(dpR), ConvertUtils.dp2px(dpB));
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
        for (View v: views) {
            if (v != null && v.getVisibility() != View.VISIBLE) {
                v.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void goneViews(View... views) {
        for (View v: views) {
            if (v != null && v.getVisibility() != View.GONE) {
                v.setVisibility(View.GONE);
            }
        }
    }

    public static void invisibleViews(View... views) {
        for (View v: views) {
            if (v != null && v.getVisibility() != View.INVISIBLE) {
                v.setVisibility(View.INVISIBLE);
            }
        }
    }

    public static void setVisibilityViews(int visibility, View... views) {
        for (View v: views) {
            if (v != null && v.getVisibility() != visibility) {
                v.setVisibility(visibility);
            }
        }
    }

    public static void setFocusableViews(boolean focusable, View... views) {
        for (View v: views) {
            if (v != null && !v.isFocusable()) {
                v.setFocusable(focusable);
            }
        }
    }

    public static void performClick(View view) {
        if (view != null) view.performClick();
    }
}
