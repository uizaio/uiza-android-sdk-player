package vn.uiza.utils.glide;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class GlideThumbnailTransformationPB extends BitmapTransformation {
    public static final int MAX_LINES = 7;
    public static final int MAX_COLUMNS = 7;
    public static final int CAPACITY = 8;
    public static final int THUMBNAILS_EACH = 5000; // millis seconds

    private int x;
    private int y;

    public GlideThumbnailTransformationPB(long position) {
        int square = (int) position / THUMBNAILS_EACH;
        y = square / MAX_LINES;
        x = square % MAX_COLUMNS;
    }

    private int getX() {
        return x;
    }

    private int getY() {
        return y;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth() / MAX_COLUMNS;
        int height = toTransform.getHeight() / MAX_LINES;
        return Bitmap.createBitmap(toTransform, x * width, y * height, width, height);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        byte[] data = ByteBuffer.allocate(CAPACITY).putInt(x).putInt(y).array();
        messageDigest.update(data);
    }

    @Override
    public int hashCode() {
        return (String.valueOf(x) + y).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GlideThumbnailTransformationPB)) {
            return false;
        }
        GlideThumbnailTransformationPB transformation = (GlideThumbnailTransformationPB) obj;
        return transformation.getX() == x && transformation.getY() == y;
    }
}
