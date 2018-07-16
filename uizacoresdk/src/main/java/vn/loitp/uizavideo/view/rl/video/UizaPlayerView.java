package vn.loitp.uizavideo.view.rl.video;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;

/**
 * Created by loitp on 6/8/2018.
 */

//https://github.com/google/ExoPlayer/issues/4031
//I want to to show playback controls only when onTouch event is fired. How to prevent control buttons being showed up when on long pressing, dragging etc.?

public final class UizaPlayerView extends PlayerView implements PlayerControlView.VisibilityListener {
    private final String TAG = getClass().getSimpleName();
    private static final float DRAG_THRESHOLD = 10;
    private static final long LONG_PRESS_THRESHOLD_MS = 500;

    private boolean controllerVisible;
    private long tapStartTimeMs;
    private float tapPositionX;
    private float tapPositionY;

    public UizaPlayerView(Context context) {
        this(context, null);
    }

    public UizaPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UizaPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setControllerVisibilityListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                tapStartTimeMs = SystemClock.elapsedRealtime();
                tapPositionX = ev.getX();
                tapPositionY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (tapStartTimeMs != 0
                        && (Math.abs(ev.getX() - tapPositionX) > DRAG_THRESHOLD
                        || Math.abs(ev.getY() - tapPositionY) > DRAG_THRESHOLD)) {
                    tapStartTimeMs = 0;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (tapStartTimeMs != 0) {
                    if (SystemClock.elapsedRealtime() - tapStartTimeMs < LONG_PRESS_THRESHOLD_MS) {
                        if (!controllerVisible) {
                            //LLog.d(TAG, "showController");
                            showController();
                        } else if (getControllerHideOnTouch()) {
                            //LLog.d(TAG, "hideController");
                            hideController();
                        }
                    }
                    tapStartTimeMs = 0;
                    controllerVisible = !controllerVisible;
                }
        }
        return true;
    }

    @Override
    public void onVisibilityChange(int visibility) {
        //do nothing
        controllerVisible = visibility == View.VISIBLE;
        //LLog.d(TAG, "onVisibilityChange visibility controllerVisible " + controllerVisible);
    }

    public boolean isControllerVisible() {
        return controllerVisible;
    }
}