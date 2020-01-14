package vn.uiza.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayout;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import timber.log.Timber;
import vn.uiza.R;

/**
 * File created on 11/3/2016.
 *
 * @author loitp
 */
public class LUIUtil {
    private static final String IMG_FOLDER = "img/";

    public static void setMarquee(TextView tv, String text) {
        tv.setText(text);
        setMarquee(tv);
    }

    public static void setMarquee(TextView tv) {
        tv.setSelected(true);
        tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tv.setSingleLine(true);
        tv.setMarqueeRepeatLimit(-1);//no limit loop
    }

    public static GradientDrawable createGradientDrawableWithRandomColor() {
        int color = LStoreUtil.getRandomColor();
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(0f);
        gradientDrawable.setStroke(1, color);
        return gradientDrawable;
    }

    public static GradientDrawable createGradientDrawableWithColor(int colorMain, int colorStroke) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(colorMain);
        gradientDrawable.setCornerRadius(90f);
        gradientDrawable.setStroke(3, colorStroke);
        return gradientDrawable;
    }

    @SuppressWarnings("deprecation")
    public static void setCircleViewWithColor(View view, int colorMain, int colorStroke) {
        try {
            view.setBackgroundDrawable(createGradientDrawableWithColor(colorMain, colorStroke));
        } catch (Exception e) {
            Timber.e(e, "setCircleViewWithColor setBkgColor:");
        }
    }

    public static void setGradientBackground(View v) {
        final View view = v;
        Drawable[] layers = new Drawable[1];

        ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                return new LinearGradient(0, 0, 0, view.getHeight(), new int[]{LStoreUtil.getRandomColor(), LStoreUtil.getRandomColor(), LStoreUtil.getRandomColor(), LStoreUtil
                        .getRandomColor()}, new float[]{0, 0.49f, 0.50f, 1}, Shader.TileMode.CLAMP);
            }
        };
        PaintDrawable p = new PaintDrawable();
        p.setShape(new RectShape());
        p.setShaderFactory(sf);
        p.setCornerRadii(new float[]{5, 5, 5, 5, 0, 0, 0, 0});
        layers[0] = p;
        LayerDrawable composite = new LayerDrawable(layers);
        view.setBackground(composite);
    }

    public static void setTextFromHTML(TextView textView, String bodyData) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(bodyData, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(bodyData));
        }
    }

    public static void setImageFromAsset(Context context, String fileName, ImageView imageView) {
        {
            Drawable drawable;
            InputStream stream = null;
            try {
                stream = context.getAssets().open(IMG_FOLDER + fileName);
                drawable = Drawable.createFromStream(stream, null);
                if (drawable != null) {
                    imageView.setImageDrawable(drawable);
                }
            } catch (Exception e) {
                Timber.e(e, "setImageFromAsset:");
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (Exception e) {
                    Timber.e(e, "setImageFromAsset:");
                }
            }
        }
    }

    public static void fixSizeTabLayout(TabLayout tabLayout, String[] titleList) {
        if (titleList.length > 3) {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            tabLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
    }

    public static void setTextAppearance(Context context, TextView textView, int resId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            textView.setTextAppearance(context, resId);
        } else {
            textView.setTextAppearance(resId);
        }
    }

    public static void setSoftInputMode(Activity activity, int mode) {
        activity.getWindow().setSoftInputMode(mode);
    }

    public static void setLastCursorEditText(@NonNull EditText editText) {
        if (!editText.getText().toString().isEmpty()) {
            editText.setSelection(editText.getText().length());
        }
    }

    public static void removeUnderlineOfSearchView(SearchView searchView) {
        View v = searchView.findViewById(R.id.search_plate);
        v.setBackgroundColor(Color.TRANSPARENT);
    }

    public static void setColorForSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        if (swipeRefreshLayout == null) {
            return;
        }
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.vip1,
                R.color.vip2,
                R.color.vip3,
                R.color.vip4,
                R.color.vip5);
    }

    public static void setTextShadow(TextView textView) {
        setTextShadow(textView, Color.BLACK);
    }

    public static void setTextShadow(@NonNull TextView textView, int color) {
        textView.setShadowLayer(
                1f, // radius
                1f, // dx
                1f, // dy
                color // shadow color
        );
    }

    public static void setTextBold(TextView textBold) {
        textBold.setTypeface(null, Typeface.BOLD);
    }

    private static int[] colors = {
            R.color.light_blue,
            R.color.light_coral,
            R.color.light_cyan,
            R.color.light_goldenrod_yellow,
            R.color.light_green,
            R.color.light_grey,
            R.color.light_pink,
            R.color.light_salmon,
            R.color.light_sea_green,
            R.color.light_slate_gray,
            R.color.light_steel_blue,
            R.color.light_yellow,
            R.color.light_sky_blue
    };

    public static int getColor(Context context) {
        Random random = new Random();
        int c = random.nextInt(colors.length);
        return ContextCompat.getColor(context, colors[c]);
    }

    public interface CallbackSearch {
        void onSearch();
    }

    public static void setImeiActionSearch(@NonNull EditText editText, final CallbackSearch callbackSearch) {
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (callbackSearch != null) {
                    callbackSearch.onSearch();
                }
                return true;
            }
            return false;
        });
    }

    public static void setColorProgressBar(@NonNull ProgressBar progressBar, int color) {
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

    public static void setColorSeekBar(@NonNull SeekBar seekBar, int color) {
        seekBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    public static void setTextViewUnderLine(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public static void setMarginDimen(@NonNull View view, int dpL, int dpT, int dpR, int dpB) {
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        mlp.setMargins(ConvertUtils.dp2px(dpL), ConvertUtils.dp2px(dpT), ConvertUtils.dp2px(dpR), ConvertUtils.dp2px(dpB));
        view.requestLayout();
    }

    public static void setMarginPx(@NonNull View view, int l, int t, int r, int b) {
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        mlp.setMargins(l, t, r, b);
        view.requestLayout();
    }

    public static void setTintSeekbar(@NonNull SeekBar seekBar, int color) {
        seekBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    public static void setTint(View view, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        } else {
            Drawable wrapDrawable = DrawableCompat.wrap(view.getBackground());
            DrawableCompat.setTint(wrapDrawable, color);
            view.setBackground(DrawableCompat.unwrap(wrapDrawable));
        }
    }

    //return pixel
    public static int getHeightOfView(@NonNull View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    public static ArrayList<View> getAllChildren(View v) {
        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }
        ArrayList<View> result = new ArrayList<View>();
        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));
            result.addAll(viewArrayList);
        }
        return result;
    }
}