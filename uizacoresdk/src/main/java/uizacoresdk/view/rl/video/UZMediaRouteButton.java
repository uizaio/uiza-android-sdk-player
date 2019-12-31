package uizacoresdk.view.rl.video;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.mediarouter.app.MediaRouteButton;

import vn.uiza.core.exception.UizaException;
import vn.uiza.utils.AppUtils;

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
        if (!AppUtils.checkChromeCastAvailable()) {
            throw new NoClassDefFoundError(UizaException.ERR_505);
        }
    }
}