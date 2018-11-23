package uizacoresdk.view.rl.video;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.util.Assertions;

import java.util.Locale;

import uizacoresdk.interfaces.UZCallbackHelper;
import vn.uiza.core.common.Constants;

/**
 * A helper class for periodically updating a {@link TextView} with debug information obtained from
 * a {@link SimpleExoPlayer}.
 */
public class UZHelper implements Player.EventListener, Runnable {
    private final String TAG = getClass().getSimpleName();
    private static final int REFRESH_INTERVAL_MS = 1000;
    private final SimpleExoPlayer player;
    private boolean started;
    private Handler handler = new Handler();
    private UZCallbackHelper uzCallbackHelper;

    public void setUzCallbackHelper(UZCallbackHelper uzCallbackHelper) {
        this.uzCallbackHelper = uzCallbackHelper;
    }

    public UZHelper(SimpleExoPlayer player) {
        Assertions.checkArgument(player.getApplicationLooper() == Looper.getMainLooper());
        this.player = player;
    }

    /**
     * Starts periodic updates of the {@link TextView}. Must be called from the application's main
     * thread.
     */
    public final void start() {
        if (started) {
            return;
        }
        started = true;
        player.addListener(this);
        updateAndPost();
    }

    /**
     * Stops periodic updates of the {@link TextView}. Must be called from the application's main
     * thread.
     */
    public final void stop() {
        if (!started) {
            return;
        }
        started = false;
        player.removeListener(this);
        handler.removeCallbacks(this);
    }

    // Player.EventListener implementation.
    @Override
    public final void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        updateAndPost();
    }

    @Override
    public final void onPositionDiscontinuity(@Player.DiscontinuityReason int reason) {
        updateAndPost();
    }

    // Runnable implementation.

    @Override
    public final void run() {
        updateAndPost();
    }

    // Protected methods.

    @SuppressLint("SetTextI18n")
    protected final void updateAndPost() {
        //LLog.d(TAG, "updateAndPost: " + getDebugString());
        if (uzCallbackHelper != null) {
            uzCallbackHelper.onDebug(getDebugString());
            handler.postDelayed(this, REFRESH_INTERVAL_MS);
        }
    }

    /**
     * Returns the debugging information string to be shown by the target {@link TextView}.
     */
    protected String getDebugString() {
        return getPlayerStateString() + getVideoString() + getAudioString();
    }

    /**
     * Returns a string containing player state debugging information.
     */
    protected String getPlayerStateString() {
        String playbackStateString;
        switch (player.getPlaybackState()) {
            case Player.STATE_BUFFERING:
                playbackStateString = "buffering";
                break;
            case Player.STATE_ENDED:
                playbackStateString = "ended";
                break;
            case Player.STATE_IDLE:
                playbackStateString = "idle";
                break;
            case Player.STATE_READY:
                playbackStateString = "ready";
                break;
            default:
                playbackStateString = "unknown";
                break;
        }
        return String.format(
                "playWhenReady:%s playbackState:%s window:%s",
                player.getPlayWhenReady(), playbackStateString, player.getCurrentWindowIndex());
    }

    /**
     * Returns a string containing video debugging information.
     */
    protected String getVideoString() {
        Format format = player.getVideoFormat();
        if (format == null) {
            return "";
        }
        return "\n" + format.sampleMimeType + "(id:" + format.id + " r:" + format.width + "x"
                + format.height + getPixelAspectRatioString(format.pixelWidthHeightRatio)
                + getDecoderCountersBufferCountString(player.getVideoDecoderCounters()) + ")";
    }

    protected int getVideoProfileW() {
        Format format = player.getVideoFormat();
        if (format == null) {
            return Constants.UNKNOW;
        }
        return format.width;
    }

    protected int getVideoProfileH() {
        Format format = player.getVideoFormat();
        if (format == null) {
            return Constants.UNKNOW;
        }
        return format.height;
    }

    /**
     * Returns a string containing audio debugging information.
     */
    protected String getAudioString() {
        Format format = player.getAudioFormat();
        if (format == null) {
            return "";
        }
        return "\n" + format.sampleMimeType + "(id:" + format.id + " hz:" + format.sampleRate + " ch:"
                + format.channelCount
                + getDecoderCountersBufferCountString(player.getAudioDecoderCounters()) + ")";
    }

    private static String getDecoderCountersBufferCountString(DecoderCounters counters) {
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

    private static String getPixelAspectRatioString(float pixelAspectRatio) {
        return pixelAspectRatio == Format.NO_VALUE || pixelAspectRatio == 1f ? ""
                : (" par:" + String.format(Locale.US, "%.02f", pixelAspectRatio));
    }
}