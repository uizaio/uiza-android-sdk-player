package vn.uiza.core.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

/**
 * Created by www.muathu@gmail.com on 10/7/2017.
 */

public class LImageUtil {
    public static void load(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }

    public static void load(Context context, String url, ImageView imageView, int resPlaceHolder) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().placeholder(resPlaceHolder))
                .into(imageView);
    }

    public static void load(Activity activity, String url, ImageView imageView, int resPlaceHolder, int resError, int sizeW, int sizeH, RequestListener<Drawable> drawableRequestListener) {
        Glide.with(activity).load(url)
                .apply(new RequestOptions()
                        .placeholder(resPlaceHolder)
                        .override(sizeW, sizeH)
                        .error(resError)
                )
                .listener(drawableRequestListener)
                .into(imageView);
    }

    public static void load(Activity activity, String url, ImageView imageView, int resPlaceHolder, int resError, RequestListener<Drawable> drawableRequestListener) {
        Glide.with(activity).load(url)
                .apply(new RequestOptions().placeholder(resPlaceHolder).error(resError))
                .listener(drawableRequestListener)
                .into(imageView);
    }

    public static void load(Activity activity, String url, ImageView imageView, final ProgressBar progressBar, int resPlaceHolder, int resError) {
        Glide.with(activity).load(url)
                .apply(new RequestOptions().placeholder(resPlaceHolder).error(resError))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (progressBar != null && progressBar.getVisibility() != View.GONE) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        if (progressBar != null && progressBar.getVisibility() != View.GONE) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }
                })
                .into(imageView);
    }

    public static void load(Activity activity, String url, ImageView imageView, final ProgressBar progressBar, int resPlaceHolder) {
        load(activity, url, imageView, progressBar, resPlaceHolder, Color.TRANSPARENT);
    }

    public static void load(Activity activity, String url, ImageView imageView, final ProgressBar progressBar) {
        load(activity, url, imageView, progressBar, Color.TRANSPARENT, Color.TRANSPARENT);
    }

    public static void load(Activity activity, String url, ImageView imageView, int sizeW, int sizeH) {
        Glide.with(activity).load(url)
                .apply(new RequestOptions().override(sizeW, sizeH)).into(imageView);
    }

    public static void load(Activity activity, final String[] url, ImageView imageView, final ProgressBar progressBar) {
        String u = null;
        for (String s : url) {
            if (s != null) {
                u = s;
                break;
            }
        }
        if (u != null) {
            load(activity, u, imageView, progressBar);
        }
    }
}
