package uizacoresdk.interfaces;

/**
 * @deprecated use {@link UZLiveInfoChangedListener} instead
 */
@Deprecated
public interface UZLiveContentCallback {
    @Deprecated
    void onUpdateLiveInfoTimeStartLive(long duration, String hhmmss);

    @Deprecated
    void onUpdateLiveInfoCurrentView(long watchnow);
}
