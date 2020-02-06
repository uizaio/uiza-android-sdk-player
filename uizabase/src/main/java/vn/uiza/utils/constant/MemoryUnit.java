package vn.uiza.utils.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({MemoryConstants.BYTE, MemoryConstants.KB, MemoryConstants.MB, MemoryConstants.GB})
@Retention(RetentionPolicy.SOURCE)
public @interface MemoryUnit {
}
