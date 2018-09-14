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
    //for flide
    public static void load(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }

    public static void load(Context context, String url, ImageView imageView, int resPlaceHolder) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                                .placeholder(resPlaceHolder)
                        //.fitCenter()
                )
                .into(imageView);
    }

    /*public static void load(Activity activity, String url, ImageView imageView, RequestListener<String, GlideDrawable> glideDrawableRequestListener) {
        Glide.with(activity).load(url)
                .listener(glideDrawableRequestListener)
                .into(imageView);
    }*/

    /*public static void loadNoEffect(Activity activity, String url, String oldImage, ImageView imageView) {
        Glide.with(activity).load(url)
                .thumbnail(
                        Glide // this thumbnail request has to have the same RESULT cache key
                        .with(activity) // as the outer request, which usually simply means
                        .load(oldImage) // same size/transformation(e.g. centerCrop)/format(e.g. asBitmap)
                        .centerCrop() // have to be explicit here to match outer load exactly
                )
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (isFirstResource) {
                            return false; // thumbnail was not shown, do as usual
                        }
                        return new DrawableCrossFadeFactory<Drawable>(customize animation here)
                                .build(false, false) // force crossFade() even if coming from memory cache
                                .animate(resource, (GlideAnimation.ViewAdapter) target);
                    }
                })
                .dontAnimate()
                .dontTransform()
                .into(imageView);
    }*/

    public static void load(Activity activity, String url, ImageView imageView, int resPlaceHolder, int resError, int sizeW, int sizeH, RequestListener<Drawable> drawableRequestListener) {
        Glide.with(activity).load(url)
                .apply(new RequestOptions()
                        .placeholder(resPlaceHolder)
                        //.fitCenter()
                        .override(sizeW, sizeH)
                        .error(resError)
                )
                .listener(drawableRequestListener)
                .into(imageView);
    }

    public static void load(Activity activity, String url, ImageView imageView, int resPlaceHolder, int resError, RequestListener<Drawable> drawableRequestListener) {
        Glide.with(activity).load(url)
                .apply(new RequestOptions()
                        .placeholder(resPlaceHolder)
                        //.fitCenter()
                        //.override(sizeW, sizeH)
                        .error(resError)
                )
                .listener(drawableRequestListener)
                .into(imageView);
    }

    public static void load(Activity activity, String url, ImageView imageView, final ProgressBar progressBar, int resPlaceHolder, int resError) {
        Glide.with(activity).load(url)
                .apply(new RequestOptions()
                        .placeholder(resPlaceHolder)
                        //.fitCenter()
                        //.override(sizeW, sizeH)
                        .error(resError)
                )
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
                .apply(new RequestOptions()
                        //.placeholder(resPlaceHolder)
                        //.fitCenter()
                        .override(sizeW, sizeH)
                ).into(imageView);
    }

    public static void load(Activity activity, final String[] url, ImageView imageView, final ProgressBar progressBar) {
        String u = null;
        for (int i = 0; i < url.length; i++) {
            if (url[i] != null) {
                u = url[i];
                break;
            }
        }
        if (u != null) {
            load(activity, u, imageView, progressBar);
        }
    }
}
