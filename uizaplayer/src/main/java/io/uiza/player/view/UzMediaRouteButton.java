package io.uiza.player.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.MediaRouteButton;
import android.util.AttributeSet;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.UzCommonUtil;

public class UzMediaRouteButton extends MediaRouteButton {

    {
        checkChromeCastAvailable();
    }

    protected Drawable remoteIndicatorDrawable;

    public UzMediaRouteButton(Context context) {
        super(context);
    }

    public UzMediaRouteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UzMediaRouteButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setRemoteIndicatorDrawable(Drawable drawable) {
        remoteIndicatorDrawable = drawable;
        super.setRemoteIndicatorDrawable(drawable);
    }

    public void applyTint(int color) {
        Drawable wrapDrawable = DrawableCompat.wrap(remoteIndicatorDrawable);
        DrawableCompat.setTint(wrapDrawable, color);
    }

    private void checkChromeCastAvailable() {
        if (!UzCommonUtil.isCastDependencyAvailable()) {
            throw new NoClassDefFoundError(UzException.ERR_505);
        }
    }
}
