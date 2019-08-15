package io.uiza.player.analytic.event;

import static io.uiza.player.analytic.event.TrackingEvent.E_DISPLAY;
import static io.uiza.player.analytic.event.TrackingEvent.E_PLAYS_REQUESTED;
import static io.uiza.player.analytic.event.TrackingEvent.E_PLAY_THROUGH_100;
import static io.uiza.player.analytic.event.TrackingEvent.E_PLAY_THROUGH_25;
import static io.uiza.player.analytic.event.TrackingEvent.E_PLAY_THROUGH_50;
import static io.uiza.player.analytic.event.TrackingEvent.E_PLAY_THROUGH_75;
import static io.uiza.player.analytic.event.TrackingEvent.E_VIDEO_STARTS;
import static io.uiza.player.analytic.event.TrackingEvent.E_VIEW;

import android.support.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({E_DISPLAY, E_PLAYS_REQUESTED, E_VIDEO_STARTS, E_VIEW, E_PLAY_THROUGH_25,
        E_PLAY_THROUGH_50, E_PLAY_THROUGH_75, E_PLAY_THROUGH_100})
@Retention(RetentionPolicy.SOURCE)
public @interface TrackingEvent {

    String E_DISPLAY = "E_DISPLAY";
    String E_PLAYS_REQUESTED = "E_PLAYS_REQUESTED";
    String E_VIDEO_STARTS = "E_VIDEO_STARTS";
    String E_VIEW = "E_VIEW";
    String E_PLAY_THROUGH_25 = "PLAY_THROUGH_25";
    String E_PLAY_THROUGH_50 = "PLAY_THROUGH_50";
    String E_PLAY_THROUGH_75 = "PLAY_THROUGH_75";
    String E_PLAY_THROUGH_100 = "PLAY_THROUGH_100";
}
