package io.uiza.player.analytic.muiza;

import static io.uiza.player.analytic.muiza.MuizaEvent.MUIZA_EVENT_ERROR;
import static io.uiza.player.analytic.muiza.MuizaEvent.MUIZA_EVENT_FULLSCREENCHANGE;
import static io.uiza.player.analytic.muiza.MuizaEvent.MUIZA_EVENT_LOADSTART;
import static io.uiza.player.analytic.muiza.MuizaEvent.MUIZA_EVENT_PAUSE;
import static io.uiza.player.analytic.muiza.MuizaEvent.MUIZA_EVENT_PLAY;
import static io.uiza.player.analytic.muiza.MuizaEvent.MUIZA_EVENT_PLAYING;
import static io.uiza.player.analytic.muiza.MuizaEvent.MUIZA_EVENT_RATECHANGE;
import static io.uiza.player.analytic.muiza.MuizaEvent.MUIZA_EVENT_READY;
import static io.uiza.player.analytic.muiza.MuizaEvent.MUIZA_EVENT_REBUFFEREND;
import static io.uiza.player.analytic.muiza.MuizaEvent.MUIZA_EVENT_REBUFFERSTART;
import static io.uiza.player.analytic.muiza.MuizaEvent.MUIZA_EVENT_SEEKED;
import static io.uiza.player.analytic.muiza.MuizaEvent.MUIZA_EVENT_SEEKING;
import static io.uiza.player.analytic.muiza.MuizaEvent.MUIZA_EVENT_VIEWENDED;
import static io.uiza.player.analytic.muiza.MuizaEvent.MUIZA_EVENT_VIEWSTART;
import static io.uiza.player.analytic.muiza.MuizaEvent.MUIZA_EVENT_VOLUMECHANGE;
import static io.uiza.player.analytic.muiza.MuizaEvent.MUIZA_EVENT_WAITING;

import android.support.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({MUIZA_EVENT_READY, MUIZA_EVENT_LOADSTART, MUIZA_EVENT_VIEWSTART, MUIZA_EVENT_PAUSE,
        MUIZA_EVENT_PLAY, MUIZA_EVENT_PLAYING, MUIZA_EVENT_SEEKING, MUIZA_EVENT_SEEKED,
        MUIZA_EVENT_WAITING, MUIZA_EVENT_RATECHANGE, MUIZA_EVENT_REBUFFERSTART,
        MUIZA_EVENT_REBUFFEREND, MUIZA_EVENT_VOLUMECHANGE, MUIZA_EVENT_FULLSCREENCHANGE,
        MUIZA_EVENT_VIEWENDED, MUIZA_EVENT_ERROR})
@Retention(RetentionPolicy.SOURCE)
public @interface MuizaEvent {

    String MUIZA_EVENT_READY = "ready";//the player init time
    String MUIZA_EVENT_LOADSTART = "loadstart";//the load video start time
    String MUIZA_EVENT_VIEWSTART = "viewstart";//the played time of the first video frame
    String MUIZA_EVENT_PAUSE = "pause";//the time player is paused
    String MUIZA_EVENT_PLAY = "play";//the time player is played
    String MUIZA_EVENT_PLAYING = "playing";//the time video starts playing (after play time)
    String MUIZA_EVENT_SEEKING = "seeking";//the time user start click to seek
    String MUIZA_EVENT_SEEKED = "seeked";//the time player starts playing after seeking
    String MUIZA_EVENT_WAITING = "waiting";//the time player is paused for buffering
    String MUIZA_EVENT_RATECHANGE = "ratechange";//the time rate is changed
    String MUIZA_EVENT_REBUFFERSTART = "rebufferstart";//the time player finishes play all buffered data, then paused for new buffering
    String MUIZA_EVENT_REBUFFEREND = "rebufferend";//the time after rebufferstart, then play again
    String MUIZA_EVENT_VOLUMECHANGE = "volumechange";//the time user changes volume
    String MUIZA_EVENT_FULLSCREENCHANGE = "fullscreenchange";//the time user force fullscreen mode
    String MUIZA_EVENT_VIEWENDED = "viewended";//the time video is ended
    String MUIZA_EVENT_ERROR = "error";//the time player is completely stopped by an error
}
