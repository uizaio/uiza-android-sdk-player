package uizacoresdk.interfaces;


import uizacoresdk.view.rl.previewseekbar.PreviewView;

public interface CallbackUZTimebar {
    void onStartPreview(PreviewView previewView, int progress);

    void onStopPreview(PreviewView previewView, int progress);

    void onPreview(PreviewView previewView, int progress, boolean fromUser);
}