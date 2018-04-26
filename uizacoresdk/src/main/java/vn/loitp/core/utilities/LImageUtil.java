package vn.loitp.core.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import vn.loitp.views.progressloadingview.avloadingindicatorview.lib.avi.AVLoadingIndicatorView;

/**
 * Created by www.muathu@gmail.com on 10/7/2017.
 */

public class LImageUtil {
    //for flide
    public static void load(Activity activity, String url, ImageView imageView) {
        Glide.with(activity).load(url).into(imageView);
    }

    public static void load(Activity activity, String url, ImageView imageView, int resPlaceHolder) {
        //Glide.with(activity).load(url).placeholder(resPlaceHolder).into(imageView);
        Glide.with(activity)
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

    public static void load(Activity activity, String url, ImageView imageView, final AVLoadingIndicatorView avLoadingIndicatorView, int resPlaceHolder, int resError) {
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
                        if (avLoadingIndicatorView != null) {
                            avLoadingIndicatorView.smoothToHide();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        if (avLoadingIndicatorView != null) {
                            avLoadingIndicatorView.smoothToHide();
                        }
                        return false;
                    }
                })
                .into(imageView);
    }

    public static void load(Activity activity, String url, ImageView imageView, final AVLoadingIndicatorView avLoadingIndicatorView, int resPlaceHolder) {
        load(activity, url, imageView, avLoadingIndicatorView, resPlaceHolder, Color.TRANSPARENT);
    }

    public static void load(Activity activity, String url, ImageView imageView, final AVLoadingIndicatorView avLoadingIndicatorView) {
        load(activity, url, imageView, avLoadingIndicatorView, Color.TRANSPARENT, Color.TRANSPARENT);
    }

    public static void load(Activity activity, String url, ImageView imageView, int sizeW, int sizeH) {
        Glide.with(activity).load(url)
                .apply(new RequestOptions()
                        //.placeholder(resPlaceHolder)
                        //.fitCenter()
                        .override(sizeW, sizeH)
                ).into(imageView);
    }
}
