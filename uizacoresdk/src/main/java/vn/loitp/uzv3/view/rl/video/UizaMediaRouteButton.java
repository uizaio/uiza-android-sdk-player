package vn.loitp.uzv3.view.rl.video;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.MediaRouteButton;
import android.util.AttributeSet;

public class UizaMediaRouteButton extends MediaRouteButton {

    protected Drawable mRemoteIndicatorDrawable;

    public UizaMediaRouteButton(Context context) {
        super(context);
    }

    public UizaMediaRouteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UizaMediaRouteButton(Context context, AttributeSet attrs, int defStyleAttr) {
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
}