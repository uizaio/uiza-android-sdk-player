package uizacoresdk.interfaces;

public interface UZLiveContentCallback {
    void onUpdateLiveInfoTimeStartLive(long duration, String hhmmss);

    void onUpdateLiveInfoCurrentView(long watchnow);
}
