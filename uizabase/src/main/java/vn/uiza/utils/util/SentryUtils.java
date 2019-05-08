package vn.uiza.utils.util;

import android.support.annotation.NonNull;
import io.sentry.Sentry;
import io.sentry.event.Event;
import io.sentry.event.EventBuilder;

public class SentryUtils {

    public static void captureException(@NonNull Throwable ex) {
        Sentry.capture(ex);
    }

    public static void captureEvent(@NonNull String message) {
        Event event = new EventBuilder().withMessage(message).build();
        Sentry.capture(event);
    }
}
