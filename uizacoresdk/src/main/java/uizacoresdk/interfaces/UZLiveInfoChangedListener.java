package uizacoresdk.interfaces;

public interface UZLiveInfoChangedListener {
    void onUpdateLiveInfoTimeStartLive(long duration, String elapsedTime);

    void onUpdateLiveInfoCurrentView(long watchNow);

    void onLivestreamUnAvailable();

    void onLivestreamEnded();
}
