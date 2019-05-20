package uizacoresdk.view.rl.video;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.decoder.DecoderCounters;

import java.util.Locale;

import vn.uiza.core.common.Constants;

final class UZPlayerHelper {
    private static final String PLAYER_STATE_FORMAT = "playWhenReady:%s playbackState:%s window:%s";
    private static final String BUFFERING = "buffering";
    private static final String ENDED = "ended";
    private static final String IDLE = "idle";
    private static final String READY = "ready";
    private static final String UNKNOWN = "unknown";
    private SimpleExoPlayer player;

    UZPlayerHelper(SimpleExoPlayer player) {
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
        if (isPlayerValid()) {
            return player.getVolume();
        }
        return Constants.NOT_FOUND;
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
        if (!isPlayerValid()) {
            return 0;
        }
        return player.getCurrentPosition();
    }

    long getDuration() {
        if (!isPlayerValid()) {
            return 0;
        }
        return player.getDuration();
    }

    boolean isVOD() {
        if (!isPlayerValid()) {
            return false;
        }
        return !player.isCurrentWindowDynamic();
    }

    boolean isLIVE() {
        if (!isPlayerValid()) {
            return false;
        }
        return player.isCurrentWindowDynamic();
    }

    /**
     * Returns a string containing player state debugging information.
     */
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
        if (!isPlayerValid()) {
            return 0;
        }
        return player.getContentPosition();
    }

    SimpleExoPlayer getPlayer() {
        return player;
    }
}
