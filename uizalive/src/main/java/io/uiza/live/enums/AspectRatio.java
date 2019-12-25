package io.uiza.live.enums;

public enum AspectRatio {

    /**
     * 19:9 standard aspect ratio.
     * Ex: note 10
     */
    RATIO_19_9,

    /**
     * 18:9 standard aspect ratio.
     */
    RATIO_18_9,

    /**
     * default
     * 16:9 standard aspect ratio.
     */
    RATIO_16_9,

    /**
     * 4:3 standard aspect ratio.
     */
    RATIO_4_3;


    public double getAspectRatio() {
        switch (this) {
            case RATIO_4_3:
                return 4.0 / 3;
            case RATIO_19_9:
                return 19.0 / 9;
            case RATIO_18_9:
                return 18.0 / 9;
            case RATIO_16_9:
            default:
                return 16.0 / 9;
        }

    }
}
