package uizacoresdk.listerner;

public interface UizaBufferListener {
    void onBufferChanged(long bufferedDurationUs, float playbackSpeed);
}
