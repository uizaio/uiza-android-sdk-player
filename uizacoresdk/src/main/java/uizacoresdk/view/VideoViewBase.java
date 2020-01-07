package uizacoresdk.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.exoplayer2.SimpleExoPlayer;

import vn.uiza.models.PlaybackInfo;


public abstract class VideoViewBase extends RelativeLayout {

    public VideoViewBase(Context context) {
        super(context);
    }

    public VideoViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoViewBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VideoViewBase(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Call one time from {@link #onAttachedToWindow}
     * Note: you must call inflate in this method
     */
    public abstract void onCreateView();


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onCreateView();
    }

    public abstract boolean play(@NonNull PlaybackInfo playback);

    public abstract boolean play(@NonNull String entityId, boolean isLive);

    public abstract long getCurrentPosition();

    public abstract void seekTo(long positionMs);

    public abstract void pause();

    public abstract void resume();

    public abstract int getVideoWidth();

    public abstract int getVideoHeight();

    public abstract SimpleExoPlayer getPlayer();

    public interface ProgressListener {
        void onAdProgress(int s, int duration, int percent);

        void onAdEnded();

        void onVideoProgress(long currentMls, int s, long duration, int percent);

        void onPlayerStateChanged(boolean playWhenReady, int playbackState);

        void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration);
    }
}
