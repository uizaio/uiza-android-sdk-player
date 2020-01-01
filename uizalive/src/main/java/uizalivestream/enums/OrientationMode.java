package uizalivestream.enums;

public enum OrientationMode {

    /**
     * Default value
     * The output video always follows the orientation of the captured video,
     * because the receiver takes the rotational information passed on from the video encoder.
     * If the captured video is in landscape mode, the output video is in landscape mode.
     * If the captured video is in portrait mode, the output video is in portrait mode.
     */
    ADAPTIVE(0),
    /**
     * The output video is always in landscape mode.
     * If the captured video is in portrait mode,
     * the video encoder crops it to fit the output.
     * Applies to situations where the receiving end cannot process the rotational information
     */
    FIXED_LANDSCAPE(1),
    /**
     * The output video is always in portrait mode.
     * If the captured video is in landscape mode,
     * the video encoder crops it to fit the output.
     * Applies to situations where the receiving end cannot process the rotational information
     */
    FIXED_PORTRAIT(2);

    private final int value;

    OrientationMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OrientationMode fromValue(int id) {
        for (OrientationMode o : values()) {
            if (o.value == id) return o;
        }
        throw new IllegalArgumentException();
    }
}
