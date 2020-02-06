package uizacoresdk.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.decoder.DecoderCounters;

import java.util.Locale;

import vn.uiza.core.common.Constants;

final class UizaPlayerHelper {
    private static final String PLAYER_STATE_FORMAT = "playWhenReady:%s playbackState:%s window:%s";
    private static final String BUFFERING = "buffering";
    private static final String ENDED = "ended";
    private static final String IDLE = "idle";
    private static final String READY = "ready";
    private static final String UNKNOWN = "unknown";
    private SimpleExoPlayer player;

    UizaPlayerHelper(SimpleExoPlayer player) {
        this.player = player;
    }

    boolean isPlayerValid() {
        return player != null;
    }

    void setPlayWhenReady(boolean ready) {
        if (isPlayerValid()) {
            player.setPlayWhenReady(ready);
        }
    }

    void release() {
        if (isPlayerValid()) {
            player.release();
            player = null;
        }
    }

    float getVolume() {
        return isPlayerValid() ? player.getVolume() : -1;
    }

    void setVolume(float value) {
        if (isPlayerValid()) {
            player.setVolume(value);
        }
    }

    boolean seekTo(long positionMs) {
        if (isPlayerValid()) {
            player.seekTo(positionMs);
            return true;
        }
        return false;
    }

    void seekToDefaultPosition() {
        if (isPlayerValid()) {
            player.seekToDefaultPosition();
        }
    }

    void seekToForward(long forward) {
        if (!isPlayerValid()) {
            return;
        }
        if (player.getCurrentPosition() + forward > player.getDuration()) {
            player.seekTo(player.getDuration());
        } else {
            player.seekTo(player.getCurrentPosition() + forward);
        }
    }

    void seekToBackward(long backward) {
        if (!isPlayerValid()) {
            return;
        }
        if (player.getCurrentPosition() - backward > 0) {
            player.seekTo(player.getCurrentPosition() - backward);
        } else {
            player.seekTo(0);
        }
    }

    long getCurrentPosition() {
        return isPlayerValid() ? player.getCurrentPosition() : 0;
    }

    long getDuration() {
        return isPlayerValid() ? player.getDuration() : 0;
    }

    boolean isVOD() {
        return isPlayerValid() && !player.isCurrentWindowDynamic();
    }

    boolean isLIVE() {
        return isPlayerValid() && player.isCurrentWindowDynamic();
    }

    /**
     * Returns a string containing player state debugging information.
     */
    @Nullable
    String getPlayerStateString() {
        if (!isPlayerValid()) {
            return null;
        }
        String playbackStateString;
        switch (player.getPlaybackState()) {
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
        return String.format(PLAYER_STATE_FORMAT, player.getPlayWhenReady(), playbackStateString, player.getCurrentWindowIndex());
    }

    /**
     * Returns a string containing video debugging information.
     */
    @Nullable
    String getVideoString() {
        if (!isPlayerValid()) {
            return null;
        }
        Format format = player.getVideoFormat();
        if (format == null) {
            return "";
        }
        return "\n" + format.sampleMimeType + "(id:" + format.id + " r:" + format.width + "x"
                + format.height + getPixelAspectRatioString(format.pixelWidthHeightRatio)
                + getDecoderCountersBufferCountString(player.getVideoDecoderCounters()) + ")";
    }

    @Nullable
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
        return pixelAspectRatio == Format.NO_VALUE || pixelAspectRatio == 1f ? "" : (" par:" + String.format(Locale.US, "%.02f", pixelAspectRatio));
    }

    int getVideoProfileW() {
        if (!isPlayerValid()) {
            return Constants.UNKNOW;
        }
        Format format = player.getVideoFormat();
        if (format == null) {
            return Constants.UNKNOW;
        }
        return format.width;
    }

    int getVideoProfileH() {
        if (!isPlayerValid()) {
            return Constants.UNKNOW;
        }
        Format format = player.getVideoFormat();
        if (format == null) {
            return Constants.UNKNOW;
        }
        return format.height;
    }

    /**
     * Returns a string containing audio debugging information.
     */
    String getAudioString() {
        if (player == null) {
            return null;
        }
        Format format = player.getAudioFormat();
        if (format == null) {
            return "";
        }
        return "\n" + format.sampleMimeType
                + "(id:" + format.id
                + " hz:" + format.sampleRate
                + " ch:" + format.channelCount
                + getDecoderCountersBufferCountString(player.getAudioDecoderCounters()) + ")";
    }

    long getContentPosition() {
        return isPlayerValid() ? player.getContentPosition() : 0;
    }

    SimpleExoPlayer getPlayer() {
        return player;
    }
}
