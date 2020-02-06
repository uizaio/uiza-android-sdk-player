package vn.uiza.utils.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({TimeConstants.MSEC, TimeConstants.SEC, TimeConstants.MIN, TimeConstants.HOUR, TimeConstants.DAY})
@Retention(RetentionPolicy.SOURCE)
public @interface TimeUnit {
}