package uizacoresdk.interfaces;

/**
 * @deprecated use {@link UZVideoBufferChangedListener} instead
 */
@Deprecated
public interface UZBufferCallback {
    @Deprecated
    void onBufferChanged(long bufferedDurationUs, float playbackSpeed);
}
