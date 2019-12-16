package io.uiza.live.enums;

import com.pedro.encoder.utils.gl.TranslateTo;

public enum Translate {
    CENTER(TranslateTo.CENTER),
    LEFT(TranslateTo.LEFT),
    RIGHT(TranslateTo.RIGHT),
    TOP(TranslateTo.TOP),
    BOTTOM(TranslateTo.BOTTOM),
    TOP_LEFT(TranslateTo.TOP_LEFT),
    TOP_RIGHT(TranslateTo.TOP_RIGHT),
    BOTTOM_LEFT(TranslateTo.BOTTOM_LEFT),
    BOTTOM_RIGHT(TranslateTo.BOTTOM_RIGHT);

    private final TranslateTo translateTo;

    private Translate(TranslateTo translateTo) {
        this.translateTo = translateTo;
    }

    public TranslateTo getTranslateTo() {
        return translateTo;
    }

}
