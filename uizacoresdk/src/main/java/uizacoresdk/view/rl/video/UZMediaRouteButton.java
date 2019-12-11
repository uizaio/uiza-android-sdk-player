package uizacoresdk.view.rl.video;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.mediarouter.app.MediaRouteButton;
import android.util.AttributeSet;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.exception.UZException;

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
        DrawableCompat.setTint(wrapDrawable, color);
    }

    private void checkChromeCastAvailable() {
        if (!UZUtil.isDependencyAvailable("com.google.android.gms.cast.framework.OptionsProvider")
                || !UZUtil.isDependencyAvailable("androidx.mediarouter.app.MediaRouteButton")) {
            throw new NoClassDefFoundError(UZException.ERR_505);
        }
    }
}