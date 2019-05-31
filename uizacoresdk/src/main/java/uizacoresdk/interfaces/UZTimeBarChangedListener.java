package uizacoresdk.interfaces;

import com.github.rubensousa.previewseekbar.PreviewView;

public interface UZTimeBarChangedListener {
    void onStartPreview(PreviewView previewView, int progress);

    void onStopPreview(PreviewView previewView, int progress);

    void onPreview(PreviewView previewView, int progress, boolean fromUser);
}
