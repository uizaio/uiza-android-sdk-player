package uizacoresdk.view.rl.video;

import com.github.rubensousa.previewseekbar.PreviewLoader;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import java.util.List;
import uizacoresdk.interfaces.UZBufferCallback;
import uizacoresdk.listerner.ProgressCallback;
import vn.uiza.restapi.uiza.model.v2.listallentity.Subtitle;
import vn.uiza.views.autosize.UZImageButton;

abstract class IUZPlayerManager implements PreviewLoader {
    abstract int getVideoW();

    abstract int getVideoH();

    abstract long getCurrentPosition();

    abstract int getVideoProfileW();

    abstract int getVideoProfileH();

    abstract void initWithoutReset();

    abstract void setRunnable();

    abstract void release();

    abstract void resumeVideo();

    abstract void pauseVideo();

    abstract boolean seekTo(long positionMs);

    abstract void seekToForward(long forward);

    abstract void seekToBackward(long backward);

    abstract String getLinkPlay();

    abstract SimpleExoPlayer getPlayer();

    abstract void toggleVolumeMute(UZImageButton exoVolume);

    abstract List<Subtitle> getSubtitleList();

    abstract void setVolume(float volume);

    abstract float getVolume();

    abstract boolean isPlayingAd();

    abstract DefaultTrackSelector getTrackSelector();

    abstract void hideProgress();

    abstract void showProgress();

    abstract void setProgressCallback(ProgressCallback progressCallback);

    abstract void setDebugCallback(DebugCallback debugCallback);

    abstract void setBufferCallback(UZBufferCallback bufferCallback);

    abstract void init();

    abstract ExoPlaybackException getExoPlaybackException();

    abstract void setResumeIfConnectionError();
}
