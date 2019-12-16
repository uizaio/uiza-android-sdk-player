package io.uiza.extensions;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({MediaExtension.TYPE_HLS, MediaExtension.TYPE_DASH})
@Retention(RetentionPolicy.SOURCE)
public @interface MediaExtension {
    String TYPE_HLS = "m3u8";
    String TYPE_DASH = "mpd";
}
