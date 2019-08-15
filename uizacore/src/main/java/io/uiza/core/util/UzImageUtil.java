package io.uiza.core.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public final class UzImageUtil {

    public static void load(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }

    public static void load(Context context, String url, ImageView imageView, int resPlaceHolder) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().placeholder(resPlaceHolder))
                .into(imageView);
    }

    public static void load(Activity activity, String url, ImageView imageView,
            final ProgressBar progressBar, int resPlaceHolder, int resError) {
        Glide.with(activity).load(url)
                .apply(new RequestOptions().placeholder(resPlaceHolder).error(resError))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                            Target<Drawable> target, boolean isFirstResource) {
                        UzDisplayUtil.goneViews(progressBar);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                            Target<Drawable> target, DataSource dataSource,
                            boolean isFirstResource) {
                        UzDisplayUtil.goneViews(progressBar);
                        return false;
                    }
                })
                .into(imageView);
    }

    public static void load(Activity activity, String url, ImageView imageView,
            final ProgressBar progressBar) {
        load(activity, url, imageView, progressBar, Color.TRANSPARENT, Color.TRANSPARENT);
    }
}
