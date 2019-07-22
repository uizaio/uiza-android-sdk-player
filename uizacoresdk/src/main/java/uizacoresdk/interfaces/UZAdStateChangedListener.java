package uizacoresdk.interfaces;

public interface UZAdStateChangedListener {
    void onAdProgress(int s, int duration, int percent);

    void onAdEnded();
}