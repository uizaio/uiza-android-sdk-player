package io.uiza.player.interfaces;

import com.github.rubensousa.previewseekbar.PreviewView;

public interface UzTimeBarPreviewListener {

    void onStartPreview(PreviewView previewView, int progress);

    void onStopPreview(PreviewView previewView, int progress);

    void onPreview(PreviewView previewView, int progress, boolean fromUser);
}
