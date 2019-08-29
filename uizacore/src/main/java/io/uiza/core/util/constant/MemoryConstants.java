package io.uiza.core.util.constant;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class MemoryConstants {

    public static final int BYTE = 1;

    public static final int KB = 1024;

    public static final int MB = 1048576;

    public static final int GB = 1073741824;

    @IntDef({BYTE, KB, MB, GB})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Unit {
    }
}
