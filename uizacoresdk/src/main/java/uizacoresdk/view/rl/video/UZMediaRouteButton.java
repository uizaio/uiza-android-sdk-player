package uizacoresdk.view.rl.video;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.MediaRouteButton;
import android.util.AttributeSet;
import io.uiza.core.exception.UzException;
import uizacoresdk.util.UZUtil;

public class UZMediaRouteButton extends MediaRouteButton {

    protected Drawable mRemoteIndicatorDrawable;

    {
        checkChromeCastAvailable();
    }

    public UZMediaRouteButton(Context context) {
        super(context);
    }

    public UZMediaRouteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UZMediaRouteButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setRemoteIndicatorDrawable(Drawable d) {
        mRemoteIndicatorDrawable = d;
        super.setRemoteIndicatorDrawable(d);
    }

    public void applyTint(int color) {
        Drawable wrapDrawable = DrawableCompat.wrap(mRemoteIndicatorDrawable);
        if (wrapDrawable != null) {
            DrawableCompat.setTint(wrapDrawable, color);
        }
    }

    private void checkChromeCastAvailable() {
        if (!UZUtil.isDependencyAvailable("com.google.android.gms.cast.framework.OptionsProvider")
                || !UZUtil.isDependencyAvailable("android.support.v7.app.MediaRouteButton")) {
            throw new NoClassDefFoundError(UzException.ERR_505);
        }
    }
}