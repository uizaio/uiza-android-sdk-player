package uizacoresdk.interfaces;

import com.github.rubensousa.previewseekbar.PreviewView;

public interface CallbackUZTimebar {
    public void onStartPreview(PreviewView previewView, int progress);

    public void onStopPreview(PreviewView previewView, int progress);

    public void onPreview(PreviewView previewView, int progress, boolean fromUser);
}