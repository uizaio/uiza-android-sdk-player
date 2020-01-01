package uizacoresdk.widget.previewseekbar;

import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;

public interface PreviewView {

    int getProgress();

    int getMax();

    int getThumbOffset();

    int getDefaultColor();

    boolean isShowingPreview();

    void showPreview();

    void hidePreview();

    void setPreviewLoader(PreviewLoader previewLoader);

    void setPreviewColorTint(@ColorInt int color);

    void setPreviewColorResourceTint(@ColorRes int color);

    void attachPreviewFrameLayout(@Nullable FrameLayout frameLayout);

    void addOnPreviewChangeListener(OnPreviewChangeListener listener);

    void removeOnPreviewChangeListener(@Nullable OnPreviewChangeListener listener);

    interface OnPreviewChangeListener {
        void onStartPreview(@Nullable PreviewView previewView, int progress);

        void onStopPreview(@Nullable PreviewView previewView, int progress);

        void onPreview(@Nullable PreviewView previewView, int progress, boolean fromUser);
    }
}
