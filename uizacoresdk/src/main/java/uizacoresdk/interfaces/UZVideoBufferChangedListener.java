package uizacoresdk.interfaces;

public interface UZVideoBufferChangedListener {
    void onBufferChanged(long bufferedDurationUs, float playbackSpeed);
}