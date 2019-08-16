package uizacoresdk.interfaces;

import com.github.rubensousa.previewseekbar.PreviewView;

/**
 * @deprecated use {@link UZTimeBarChangedListener} instead
 */
@Deprecated
public interface CallbackUZTimebar {
    @Deprecated
    void onStartPreview(PreviewView previewView, int progress);

    @Deprecated
    void onStopPreview(PreviewView previewView, int progress);

    @Deprecated
    void onPreview(PreviewView previewView, int progress, boolean fromUser);
}
