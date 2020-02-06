package vn.uiza.core.common;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({Constants.ENVIRONMENT.DEV, Constants.ENVIRONMENT.STAG, Constants.ENVIRONMENT.PROD})
@Retention(RetentionPolicy.SOURCE)
public @interface EnvironmentValues {
}
