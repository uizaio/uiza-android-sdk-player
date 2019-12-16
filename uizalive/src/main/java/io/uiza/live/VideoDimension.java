package io.uiza.live;

import java.util.Objects;

public class VideoDimension {
    private final int width;
    private final int height;

    /**
     * Create a new immutable VideoDimension instance.
     *
     * @param width  The width of the size, in pixels
     * @param height The height of the size, in pixels
     */
    public VideoDimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Get the width of the size (in pixels).
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the size (in pixels).
     *
     * @return height
     */
    public int getHeight() {
        return height;
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
        if (obj instanceof VideoDimension) {
            VideoDimension other = (VideoDimension) obj;
            return width == other.width && height == other.height;
        }
        return false;
    }

    /**
     * Parses the specified string as a size value.
     * <p>
     * The ASCII characters {@code \}{@code u002a} ('*') and
     * {@code \}{@code u0078} ('x') are recognized as separators between
     * the width and height.</p>
     * <p>
     * For any {@code VideoDimension s}: {@code VideoDimension.parseSize(s.toString()).equals(s)}.
     * However, the method also handles sizes expressed in the
     * following forms:</p>
     * <p>
     * "<i>width</i>{@code x}<i>height</i>" or
     * "<i>width</i>{@code *}<i>height</i>" {@code => new VideoDimension(width, height)},
     * where <i>width</i> and <i>height</i> are string integers potentially
     * containing a sign, such as "-10", "+7" or "5".</p>
     *
     * <pre>{@code
     * VideoDimension.parseSize("3*+6").equals(new VideoDimension(3, 6)) == true
     * VideoDimension.parseSize("-3x-6").equals(new VideoDimension(-3, -6)) == true
     * VideoDimension.parseSize("4 by 3") => throws NumberFormatException
     * }</pre>
     *
     * @param string the string representation of a size value.
     * @return the size value represented by {@code string}.
     * @throws NumberFormatException if {@code string} cannot be parsed
     *                               as a size value.
     * @throws NullPointerException  if {@code string} was {@code null}
     */
    public static VideoDimension parseSize(String string)
            throws NumberFormatException {
        Objects.requireNonNull(string, "string must not be null");

        int sep_ix = string.indexOf('*');
        if (sep_ix < 0) {
            sep_ix = string.indexOf('x');
        }
        if (sep_ix < 0) {
            throw invalidSize(string);
        }
        try {
            return new VideoDimension(Integer.parseInt(string.substring(0, sep_ix)),
                    Integer.parseInt(string.substring(sep_ix + 1)));
        } catch (NumberFormatException e) {
            throw invalidSize(string);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        // assuming most sizes are <2^16, doing a rotate will give us perfect hashing
        return height ^ ((width << (Integer.SIZE / 2)) | (width >>> (Integer.SIZE / 2)));
    }

    /**
     * Return the size represented as a string with the format {@code "WxH"}
     *
     * @return string representation of the dimension
     */
    @Override
    public String toString() {
        return width + "x" + height;
    }

    private static NumberFormatException invalidSize(String s) {
        throw new NumberFormatException("Invalid VideoDimension: \"" + s + "\"");
    }

}
