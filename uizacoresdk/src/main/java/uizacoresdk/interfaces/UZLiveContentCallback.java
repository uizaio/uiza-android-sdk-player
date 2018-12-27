package uizacoresdk.interfaces;

public interface UZLiveContentCallback {
    public void onUpdateLiveInfoTimeStartLive(long duration, String hhmmss);

    public void onUpdateLiveInfoCurrentView(long watchnow);
}
