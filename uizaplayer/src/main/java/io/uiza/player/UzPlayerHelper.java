package io.uiza.player;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import io.uiza.core.util.constant.Constants;
import java.util.Locale;

final class UzPlayerHelper {

    private static final String PLAYER_STATE_FORMAT = "playWhenReady:%s playbackState:%s window:%s";
    private static final String BUFFERING = "buffering";
    private static final String ENDED = "ended";
    private static final String IDLE = "idle";
    private static final String READY = "ready";
    private static final String UNKNOWN = "unknown";
    private SimpleExoPlayer exoPlayer;

    UzPlayerHelper(SimpleExoPlayer exoPlayer) {
        this.exoPlayer = exoPlayer;
    }

    boolean isPlayerValid() {
        return exoPlayer != null;
    }

    void setPlayWhenReady(boolean ready) {
        if (isPlayerValid()) {
            exoPlayer.setPlayWhenReady(ready);
        }
    }

    void release() {
        if (isPlayerValid()) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    float getVolume() {
        if (isPlayerValid()) {
            return exoPlayer.getVolume();
        }
        return Constants.NOT_FOUND;
    }

    void setVolume(float value) {
        if (isPlayerValid()) {
            exoPlayer.setVolume(value);
        }
    }

    boolean seekTo(long positionMs) {
        if (isPlayerValid()) {
            exoPlayer.seekTo(positionMs);
            return true;
        }
        return false;
    }

    void seekToDefaultPosition() {
        if (isPlayerValid()) {
            exoPlayer.seekToDefaultPosition();
        }
    }

    void seekToForward(long forward) {
        if (!isPlayerValid()) {
            return;
        }
        if (exoPlayer.getCurrentPosition() + forward > exoPlayer.getDuration()) {
            exoPlayer.seekTo(exoPlayer.getDuration());
        } else {
            exoPlayer.seekTo(exoPlayer.getCurrentPosition() + forward);
        }
    }

    void seekToBackward(long backward) {
        if (!isPlayerValid()) {
            return;
        }
        if (exoPlayer.getCurrentPosition() - backward > 0) {
            exoPlayer.seekTo(exoPlayer.getCurrentPosition() - backward);
        } else {
            exoPlayer.seekTo(0);
        }
    }

    long getCurrentPosition() {
        if (!isPlayerValid()) {
            return 0;
        }
        return exoPlayer.getCurrentPosition();
    }

    long getDuration() {
        if (!isPlayerValid()) {
            return 0;
        }
        return exoPlayer.getDuration();
    }

    boolean isVod() {
        if (!isPlayerValid()) {
            return false;
        }
        return !exoPlayer.isCurrentWindowDynamic();
    }

    boolean isLive() {
        if (!isPlayerValid()) {
            return false;
        }
        return exoPlayer.isCurrentWindowDynamic();
    }

    /**
     * Returns a string containing player state debugging information.
     */
    String getPlayerStateString() {
        if (!isPlayerValid()) {
            return null;
        }
        String playbackStateString;
        switch (exoPlayer.getPlaybackState()) {
            case Player.STATE_BUFFERING:
                playbackStateString = BUFFERING;
                break;
            case Player.STATE_ENDED:
                playbackStateString = ENDED;
                break;
            case Player.STATE_IDLE:
                playbackStateString = IDLE;
                break;
            case Player.STATE_READY:
                playbackStateString = READY;
                break;
            default:
                playbackStateString = UNKNOWN;
                break;
        }
        return String.format(PLAYER_STATE_FORMAT, exoPlayer.getPlayWhenReady(), playbackStateString,
                exoPlayer.getCurrentWindowIndex());
    }

    /**
     * Returns a string containing video debugging information.
     */
    String getVideoString() {
        if (!isPlayerValid()) {
            return null;
        }
        Format format = exoPlayer.getVideoFormat();
        if (format == null) {
            return "";
        }
        return "\n" + format.sampleMimeType + "(id:" + format.id + " r:" + format.width + "x"
                + format.height + getPixelAspectRatioString(format.pixelWidthHeightRatio)
                + getDecoderCountersBufferCountString(exoPlayer.getVideoDecoderCounters()) + ")";
    }

    String getDecoderCountersBufferCountString(DecoderCounters counters) {
        if (counters == null) {
            return "";
        }
        counters.ensureUpdated();
        return " sib:" + counters.skippedInputBufferCount
                + " sb:" + counters.skippedOutputBufferCount
                + " rb:" + counters.renderedOutputBufferCount
                + " db:" + counters.droppedBufferCount
                + " mcdb:" + counters.maxConsecutiveDroppedBufferCount
                + " dk:" + counters.droppedToKeyframeCount;
    }

    String getPixelAspectRatioString(float pixelAspectRatio) {
        return pixelAspectRatio == Format.NO_VALUE || pixelAspectRatio == 1f ? ""
                : (" par:" + String.format(Locale.US, "%.02f", pixelAspectRatio));
    }

    int getVideoProfileW() {
        if (!isPlayerValid()) {
            return Constants.UNKNOWN;
        }
        Format format = exoPlayer.getVideoFormat();
        if (format == null) {
            return Constants.UNKNOWN;
        }
        return format.width;
    }

    int getVideoProfileH() {
        if (!isPlayerValid()) {
            return Constants.UNKNOWN;
        }
        Format format = exoPlayer.getVideoFormat();
        if (format == null) {
            return Constants.UNKNOWN;
        }
        return format.height;
    }

    /**
     * Returns a string containing audio debugging information.
     */
    String getAudioString() {
        if (exoPlayer == null) {
            return null;
        }
        Format format = exoPlayer.getAudioFormat();
        if (format == null) {
            return "";
        }
        return "\n" + format.sampleMimeType
                + "(id:" + format.id
                + " hz:" + format.sampleRate
                + " ch:" + format.channelCount
                + getDecoderCountersBufferCountString(exoPlayer.getAudioDecoderCounters()) + ")";
    }

    long getContentPosition() {
        if (!isPlayerValid()) {
            return 0;
        }
        return exoPlayer.getContentPosition();
    }

    SimpleExoPlayer getExoPlayer() {
        return exoPlayer;
    }
}
