package io.uiza.player.analytic.event;

import static io.uiza.player.analytic.event.VideoTrackingEvent.EVENT_TYPE_DISPLAY;
import static io.uiza.player.analytic.event.VideoTrackingEvent.EVENT_TYPE_PLAYS_REQUESTED;
import static io.uiza.player.analytic.event.VideoTrackingEvent.EVENT_TYPE_PLAY_THROUGH;
import static io.uiza.player.analytic.event.VideoTrackingEvent.EVENT_TYPE_VIDEO_STARTS;
import static io.uiza.player.analytic.event.VideoTrackingEvent.EVENT_TYPE_VIEW;

import android.support.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({EVENT_TYPE_DISPLAY, EVENT_TYPE_PLAYS_REQUESTED, EVENT_TYPE_VIDEO_STARTS,
        EVENT_TYPE_VIEW, EVENT_TYPE_PLAY_THROUGH})
@Retention(RetentionPolicy.SOURCE)
public @interface VideoTrackingEvent {

    String EVENT_TYPE_DISPLAY = "display";
    String EVENT_TYPE_PLAYS_REQUESTED = "plays_requested";
    String EVENT_TYPE_VIDEO_STARTS = "video_starts";
    String EVENT_TYPE_VIEW = "view";
    String EVENT_TYPE_PLAY_THROUGH = "play_through";
}
