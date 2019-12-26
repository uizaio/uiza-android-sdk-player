package vn.uiza.utils.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.Target;

public class ViewUtils {
    public static void visibleViews(View... views) {
        for (View v : views) {
            if (v != null && v.getVisibility() != View.VISIBLE) {
                v.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void goneViews(View... views) {
        for (View v : views) {
            if (v != null && v.getVisibility() != View.GONE) {
                v.setVisibility(View.GONE);
            }
        }
    }

    public static void invisibleViews(View... views) {
        for (View v : views) {
            if (v != null && v.getVisibility() != View.INVISIBLE) {
                v.setVisibility(View.INVISIBLE);
            }
        }
    }

    public static void setVisibilityViews(int visibility, View... views) {
        for (View v : views) {
            if (v != null && v.getVisibility() != visibility) {
                v.setVisibility(visibility);
            }
        }
    }

    public static void setFocusableViews(boolean focusable, View... views) {
        for (View v : views) {
            if (v != null && !v.isFocusable()) {
                v.setFocusable(focusable);
            }
        }
    }

    public static void performClick(View view) {
        if (view != null) view.performClick();
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
    public static void loadImage(@NonNull ImageView imageView, @NonNull String imageUrl, int placeholder, TransformationType transformationType) {
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
    public static void loadImage(@NonNull ImageView imageView, @NonNull String imageUrl, @DrawableRes int placeholder) {
        loadImage(imageView, imageUrl, placeholder, TransformationType.NONE);
    }

    /**
     * Load image into imageView with Glide and centerCrop with {@link TransformationType#CIRCLE }
     *
     * @param imageView: target view
     * @param imageUrl   : image url
     */
    public static void loadCircleImage(@NonNull ImageView imageView, @NonNull String imageUrl, @DrawableRes int placeholder) {
        loadImage(imageView, imageUrl, placeholder, TransformationType.CIRCLE);
    }

    /**
     * Load image into imageView with Glide and centerCrop with {@link TransformationType#ROUND }
     *
     * @param imageView: target view
     * @param imageUrl   : image url
     */
    public static void loadRoundImage(@NonNull ImageView imageView, @NonNull String imageUrl, @DrawableRes int placeholder) {
        loadImage(imageView, imageUrl, placeholder, TransformationType.ROUND);
    }

    /**
     * Load image into imageView with Glide and centerCrop with {@link TransformationType#BLUR }
     *
     * @param imageView: target view
     * @param imageUrl   : image url
     */
    public static void loadBlurImage(@NonNull ImageView imageView, @NonNull String imageUrl, @DrawableRes int placeholder) {
        loadImage(imageView, imageUrl, placeholder, TransformationType.BLUR);
    }

    /**
     * Load image into imageView with Glide and centerCrop, no placeholder with {@link TransformationType#NONE }
     *
     * @param imageView : target view
     * @param imageUrl  : image url
     */
    public static void loadImage(@NonNull ImageView imageView, @NonNull String imageUrl) {
        loadImage(imageView, imageUrl, 0, TransformationType.NONE);
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
