package vn.uiza.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import vn.uiza.utils.glide.BlurTransformation;
import vn.uiza.utils.glide.GlideThumbnailTransformationPB;

/**
 * Created by namdinh@uiza.io on 11/12/2019.
 */

public class ImageUtil {
    private ImageUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * @param imageView       : target view
     * @param url             : image url
     * @param resPlaceHolder: placeholder
     * @param progressBar     : ProgressBar
     */
    public static void load(ImageView imageView, String url, int resPlaceHolder, final ProgressBar progressBar) {
        Glide.with(imageView.getContext()).load(url)
                .apply(new RequestOptions().placeholder(resPlaceHolder))
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

    /**
     * @param imageView   : target view
     * @param url         : image url
     * @param progressBar : ProgressBar
     */
    public static void load(ImageView imageView, String url, final ProgressBar progressBar) {
        load(imageView, url, Color.TRANSPARENT, progressBar);
    }

    /**
     * Load image into imageView with Glide and centerCrop
     *
     * @param imageView           : target view
     * @param imageUrl            : image url
     * @param placeholder         : place holder, = 0 No place Holder
     * @param transformationType: One of {@link TransformationType#NONE},
     *                            {@link TransformationType#CIRCLE}, {@link TransformationType#ROUND}
     *                            and {@link TransformationType#BLUR}
     */
    public static void load(@NonNull ImageView imageView, @NonNull String imageUrl, int placeholder, TransformationType transformationType) {
        RequestBuilder<Drawable> builder = Glide.with(imageView.getContext())
                .load(imageUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        if (placeholder > 0) {
            builder = builder.placeholder(placeholder);
        }
        if (transformationType != TransformationType.NONE) {
            builder = builder.transform(transformationType.getTransformation(imageView.getContext()));
        }
        builder.into(imageView);
    }

    /**
     * Load image into imageView with Glide and centerCrop with {@link TransformationType#NONE }
     *
     * @param imageView: target view
     * @param imageUrl   : image url
     */
    public static void load(@NonNull ImageView imageView, @NonNull String imageUrl, @DrawableRes int placeholder) {
        load(imageView, imageUrl, placeholder, TransformationType.NONE);
    }

    /**
     * Load image into imageView with Glide and centerCrop with {@link TransformationType#CIRCLE }
     *
     * @param imageView: target view
     * @param imageUrl   : image url
     */
    public static void loadCircle(@NonNull ImageView imageView, @NonNull String imageUrl, @DrawableRes int placeholder) {
        load(imageView, imageUrl, placeholder, TransformationType.CIRCLE);
    }

    /**
     * Load image into imageView with Glide and centerCrop with {@link TransformationType#ROUND }
     *
     * @param imageView: target view
     * @param imageUrl   : image url
     */
    public static void loadRound(@NonNull ImageView imageView, @NonNull String imageUrl, @DrawableRes int placeholder) {
        load(imageView, imageUrl, placeholder, TransformationType.ROUND);
    }

    /**
     * Load image into imageView with Glide and centerCrop with {@link TransformationType#BLUR }
     *
     * @param imageView: target view
     * @param imageUrl   : image url
     */
    public static void loadBlur(@NonNull ImageView imageView, @NonNull String imageUrl, @DrawableRes int placeholder) {
        load(imageView, imageUrl, placeholder, TransformationType.BLUR);
    }

    /**
     * Load image into imageView with Glide and centerCrop, no placeholder with {@link TransformationType#NONE }
     *
     * @param imageView : target view
     * @param imageUrl  : image url
     */
    public static void load(@NonNull ImageView imageView, @NonNull String imageUrl) {
        load(imageView, imageUrl, 0, TransformationType.NONE);
    }

    public static void loadThumbnail(@NonNull ImageView imageView, @NonNull String imageUrl, long position) {
        Glide.with(imageView)
                .load(imageUrl)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .transform(new GlideThumbnailTransformationPB(position))
                .into(imageView);
    }


    enum TransformationType {
        CIRCLE,
        ROUND,
        BLUR,
        THUMB,
        NONE;

        @NonNull
        Transformation<Bitmap> getTransformation(Context context) {
            switch (this) {
                case CIRCLE:
                    return new CircleCrop();
                case BLUR:
                    return new BlurTransformation(context);
                case ROUND:
                    return new RoundedCorners(20);
                case THUMB:
                    return new GlideThumbnailTransformationPB(1000);
                default:
                    return new RoundedCorners(0);
            }
        }
    }
}
