package io.uiza.player.cast;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;

public class CastyPlayer {

    private RemoteMediaClient remoteMediaClient;
    private OnMediaLoadedListener onMediaLoadedListener;
    private MediaInfo currentMedia;

    //Needed for NoOp instance
    CastyPlayer() {
        //no-op
    }

    CastyPlayer(OnMediaLoadedListener onMediaLoadedListener) {
        this.onMediaLoadedListener = onMediaLoadedListener;
    }

    public RemoteMediaClient getRemoteMediaClient() {
        return remoteMediaClient;
    }

    void setRemoteMediaClient(RemoteMediaClient remoteMediaClient) {
        this.remoteMediaClient = remoteMediaClient;
    }

    /**
     * Plays the current media file if it is paused.
     */
    public void play() {
        if (isPaused()) {
            remoteMediaClient.play();
        }
    }

    /**
     * Pauses the current media file if it is playing.
     */
    public void pause() {
        if (isPlaying()) {
            remoteMediaClient.pause();
        }
    }

    /**
     * Seeks the current media file.
     *
     * @param time the number of milliseconds to seek by
     */
    public void seek(long time) {
        if (remoteMediaClient != null) {
            if (remoteMediaClient.hasMediaSession()) {
                remoteMediaClient.seek(time);
            } else {
                loadMediaAndPlayInBackground(currentMedia, true, time);
            }
        }
    }

    public void seekToForward(long forward) {
        if (remoteMediaClient == null) {
            return;
        }
        if (remoteMediaClient.getMediaStatus().getStreamPosition() + forward > remoteMediaClient
                .getMediaStatus().getMediaInfo().getStreamDuration()) {
            seek(remoteMediaClient.getMediaStatus().getMediaInfo().getStreamDuration());
        } else {
            if (remoteMediaClient.hasMediaSession()) {
                seek(remoteMediaClient.getMediaStatus().getStreamPosition() + forward);
            }
        }
    }

    public void seekToBackward(long backward) {
        if (remoteMediaClient == null) {
            return;
        }
        if (remoteMediaClient.getMediaStatus().getStreamPosition() - backward > 0) {
            seek(remoteMediaClient.getMediaStatus().getStreamPosition() - backward);
        } else {
            if (!remoteMediaClient.hasMediaSession()) {
                seek(currentMedia.getStreamDuration() - backward);
            } else {
                seek(0);
            }
        }
    }

    /**
     * Tries to play or pause the current media file, depending of the current state.
     */
    public void togglePlayPause() {
        if (remoteMediaClient == null) {
            return;
        }
        if (remoteMediaClient.isPlaying()) {
            remoteMediaClient.pause();
        } else if (remoteMediaClient.isPaused()) {
            remoteMediaClient.play();
        }
    }

    /**
     * Checks if the media file is playing.
     *
     * @return true if the media file is playing, false otherwise
     */
    public boolean isPlaying() {
        return remoteMediaClient != null && remoteMediaClient.isPlaying();
    }

    /**
     * Checks if the media file is paused.
     *
     * @return true if the media file is paused, false otherwise
     */
    public boolean isPaused() {
        return remoteMediaClient != null && remoteMediaClient.isPaused();
    }

    /**
     * Checks if the media file is buffering.
     *
     * @return true if the media file is buffering, false otherwise
     */
    public boolean isBuffering() {
        return remoteMediaClient != null && remoteMediaClient.isBuffering();
    }

    /**
     * Tries to load the media file and play it in the {@link ExpandedControlsActivity}.
     *
     * @param mediaData Information about the media
     * @return true if attempt was successful, false otherwise
     * @see MediaData
     */
    @MainThread
    public boolean loadMediaAndPlay(@NonNull MediaData mediaData) {
        return loadMediaAndPlay(mediaData.createMediaInfo(), mediaData.autoPlay,
                mediaData.position);
    }

    /**
     * Tries to load the media file and play it in the {@link ExpandedControlsActivity}.
     *
     * @param mediaInfo Information about the media
     * @return true if attempt was successful, false otherwise
     * @see MediaInfo
     */
    @MainThread
    public boolean loadMediaAndPlay(@NonNull MediaInfo mediaInfo) {
        return loadMediaAndPlay(mediaInfo, true, 0);
    }

    /**
     * Tries to load the media file and play it in the {@link ExpandedControlsActivity}.
     *
     * @param mediaInfo Information about the media
     * @param autoPlay True if the media file should start automatically
     * @param position Start position of video in milliseconds
     * @return true if attempt was successful, false otherwise
     * @see MediaInfo
     */
    @MainThread
    public boolean loadMediaAndPlay(@NonNull MediaInfo mediaInfo, boolean autoPlay, long position) {
        return playMediaBaseMethod(mediaInfo, autoPlay, position, false);
    }

    /**
     * Tries to load the media file and play in background.
     *
     * @param mediaData Information about the media
     * @return true if attempt was successful, false otherwise
     * @see MediaData
     */
    @MainThread
    public boolean loadMediaAndPlayInBackground(@NonNull MediaData mediaData) {
        return loadMediaAndPlayInBackground(mediaData.createMediaInfo(), mediaData.autoPlay,
                mediaData.position);
    }

    /**
     * Tries to load the media file and play in background.
     *
     * @param mediaInfo Information about the media
     * @return true if attempt was successful, false otherwise
     * @see MediaInfo
     */
    @MainThread
    public boolean loadMediaAndPlayInBackground(@NonNull MediaInfo mediaInfo) {
        return loadMediaAndPlayInBackground(mediaInfo, true, 0);
    }

    /**
     * Tries to load the media file and play in background.
     *
     * @param mediaInfo Information about the media
     * @param autoPlay True if the media file should start automatically
     * @param position Start position of video in milliseconds
     * @return true if attempt was successful, false otherwise
     * @see MediaInfo
     */
    @MainThread
    public boolean loadMediaAndPlayInBackground(@NonNull MediaInfo mediaInfo, boolean autoPlay,
            long position) {
        return playMediaBaseMethod(mediaInfo, autoPlay, position, true);
    }

    private boolean playMediaBaseMethod(MediaInfo mediaInfo, boolean autoPlay, long position,
            boolean inBackground) {
        if (remoteMediaClient == null) {
            return false;
        }
        if (!inBackground) {
            remoteMediaClient.addListener(createRemoteMediaClientListener());
        }
        if (!mediaInfo.equals(currentMedia)) {
            currentMedia = mediaInfo;
        }
        remoteMediaClient.load(mediaInfo, autoPlay, position);
        return true;
    }

    private RemoteMediaClient.Listener createRemoteMediaClientListener() {
        return new RemoteMediaClient.Listener() {
            @Override
            public void onStatusUpdated() {
                onMediaLoadedListener.onMediaLoaded();
                remoteMediaClient.removeListener(this);
            }

            @Override
            public void onMetadataUpdated() {
                //no-op
            }

            @Override
            public void onQueueStatusUpdated() {
                //no-op
            }

            @Override
            public void onPreloadStatusUpdated() {
                //no-op
            }

            @Override
            public void onSendingRemoteMediaRequest() {
                //no-op
            }

            @Override
            public void onAdBreakStatusUpdated() {
                //no-op
            }
        };
    }

    interface OnMediaLoadedListener {

        void onMediaLoaded();
    }
}
