package uizalivestream.view;

import android.annotation.TargetApi;
import android.hardware.Camera;
import android.os.Build;
import android.util.Size;

public class UizaSize {
    private int mWidth;
    private int mHeight;

    private UizaSize(int w, int h) {
        this.mWidth = w;
        this.mHeight = h;
    }

    public static UizaSize fromSize(Camera.Size size) {
        return new UizaSize(size.width, size.height);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static UizaSize fromSize(Size size) {
        return new UizaSize(size.getWidth(), size.getHeight());
    }

    /**
     * Check if this size is equal to another size.
     * <p>
     * Two sizes are equal if and only if both their widths and heights are
     * equal.
     * </p>
     * <p>
     * A size object is never equal to any other type of object.
     * </p>
     *
     * @return {@code true} if the objects were equal, {@code false} otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof UizaSize) {
            UizaSize other = (UizaSize) obj;
            return mWidth == other.mWidth && mHeight == other.mHeight;
        }
        return false;
    }

    /**
     * Return the size represented as a string with the format {@code "WxH"}
     *
     * @return string representation of the size
     */
    @Override
    public String toString() {
        return mWidth + "x" + mHeight;
    }

    private static NumberFormatException invalidSize(String s) {
        throw new NumberFormatException("Invalid Size: \"" + s + "\"");
    }

    /**
     * Parses the specified string as a size value.
     * <p>
     * The ASCII characters {@code \}{@code u002a} ('*') and
     * {@code \}{@code u0078} ('x') are recognized as separators between
     * the width and height.</p>
     * <p>
     * For any {@code Size s}: {@code Size.parseSize(s.toString()).equals(s)}.
     * However, the method also handles sizes expressed in the
     * following forms:</p>
     * <p>
     * "<i>width</i>{@code x}<i>height</i>" or
     * "<i>width</i>{@code *}<i>height</i>" {@code => new Size(width, height)},
     * where <i>width</i> and <i>height</i> are string integers potentially
     * containing a sign, such as "-10", "+7" or "5".</p>
     *
     * <pre>{@code
     * Size.parseSize("3*+6").equals(new Size(3, 6)) == true
     * Size.parseSize("-3x-6").equals(new Size(-3, -6)) == true
     * Size.parseSize("4 by 3") => throws NumberFormatException
     * }</pre>
     *
     * @param string the string representation of a size value.
     * @return the size value represented by {@code string}.
     * @throws NumberFormatException if {@code string} cannot be parsed
     *                               as a size value.
     * @throws NullPointerException  if {@code string} was {@code null}
     */
    public static UizaSize parseSize(String string)
            throws NumberFormatException {
        int sep_ix = string.indexOf('*');
        if (sep_ix < 0) {
            sep_ix = string.indexOf('x');
        }
        if (sep_ix < 0) {
            throw invalidSize(string);
        }
        try {
            return new UizaSize(Integer.parseInt(string.substring(0, sep_ix)),
                    Integer.parseInt(string.substring(sep_ix + 1)));
        } catch (NumberFormatException e) {
            throw invalidSize(string);
        }
    }

    public int getHeight() {
        return mHeight;
    }

    public int getWidth() {
        return mWidth;
    }
}
