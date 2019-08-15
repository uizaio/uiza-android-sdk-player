package io.uiza.core.util;

import android.support.annotation.NonNull;
import io.sentry.Sentry;
import io.sentry.event.Event;
import io.sentry.event.EventBuilder;

public final class SentryUtil {

    public static void captureException(@NonNull Throwable ex) {
        Sentry.capture(ex);
    }

    public static void captureEvent(@NonNull String message) {
        Event event = new EventBuilder().withMessage(message).build();
        Sentry.capture(event);
    }
}
