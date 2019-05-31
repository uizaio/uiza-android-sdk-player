package uizacoresdk.interfaces;

public interface UZLiveInfoChangedListener {
    void onLiveTimeChanged(long duration, String elapsedTime);

    void onCurrentViewChanged(long watchNow);
}
