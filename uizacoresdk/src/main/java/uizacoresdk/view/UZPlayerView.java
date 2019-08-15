package uizacoresdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import io.uiza.core.util.SentryUtil;
import uizacoresdk.util.UZData;

/**
 * Created by loitp on 6/8/2018.
 */

//https://github.com/google/ExoPlayer/issues/4031
//I want to to show playback controls only when onTouch event is fired.
// How to prevent control buttons being showed up when on long pressing, dragging etc.?
public final class UZPlayerView extends PlayerView implements PlayerControlView.VisibilityListener {
    private final String TAG = getClass().getSimpleName();

    private boolean controllerVisible;

    public UZPlayerView(Context context) {
        this(context, null);
    }

    public UZPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private GestureDetector mDetector;

    private OnTouchEvent onTouchEvent;

    public UZPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setControllerVisibilityListener(this);
        mDetector = new GestureDetector(context, new UizaGestureListener());
    }

    public boolean isControllerVisible() {
        return controllerVisible;
    }

    public interface ControllerStateCallback {
        void onVisibilityChange(boolean isShow);
    }

    private ControllerStateCallback controllerStateCallback;

    public void setControllerStateCallback(ControllerStateCallback controllerStateCallback) {
        this.controllerStateCallback = controllerStateCallback;
    }

    @Override
    public void onVisibilityChange(int visibility) {
        controllerVisible = visibility == View.VISIBLE;
        //LLog.d(TAG, "onVisibilityChange visibility controllerVisible " + controllerVisible);
        if (controllerStateCallback != null) {
            controllerStateCallback.onVisibilityChange(controllerVisible);
        }
    }

    public void toggleShowHideController() {
        if (controllerVisible) {
            hideController();
        } else {
            showController();
        }
    }

    @Override
    public void showController() {
        if (!UZData.getInstance().isSettingPlayer()) {
            super.showController();
        }
    }

    @Override
    public void hideController() {
        if (!UZData.getInstance().isSettingPlayer()) {
            super.hideController();
        }
    }

    public void setOnTouchEvent(OnTouchEvent onTouchEvent) {
        this.onTouchEvent = onTouchEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (UZData.getInstance().isUseWithVDHView()) {
            return false;
        } else {
            mDetector.onTouchEvent(ev);
            return true;
        }
    }

    public interface OnTouchEvent {
        void onSingleTapConfirmed(float x, float y);

        void onLongPress(float x, float y);

        void onDoubleTap(float x, float y);

        void onSwipeRight();

        void onSwipeLeft();

        void onSwipeBottom();

        void onSwipeTop();
    }

    private class UizaGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent event) {
            // don't return false here or else none of the other
            // gestures will work
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (!controllerVisible) {
                showController();
            } else if (getControllerHideOnTouch()) {
                hideController();
            }
            if (onTouchEvent != null) {
                onTouchEvent.onSingleTapConfirmed(e.getX(), e.getY());
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (onTouchEvent != null) {
                onTouchEvent.onLongPress(e.getX(), e.getY());
            }
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (onTouchEvent != null) {
                onTouchEvent.onDoubleTap(e.getX(), e.getY());
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            //LLog.d(TAG, "onSwipeRight");
                            if (onTouchEvent != null) {
                                onTouchEvent.onSwipeRight();
                            }
                        } else {
                            //LLog.d(TAG, "onSwipeLeft");
                            if (onTouchEvent != null) {
                                onTouchEvent.onSwipeLeft();
                            }
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            //LLog.d(TAG, "onSwipeBottom");
                            if (onTouchEvent != null) {
                                onTouchEvent.onSwipeBottom();
                            }
                        } else {
                            //LLog.d(TAG, "onSwipeTop");
                            if (onTouchEvent != null) {
                                onTouchEvent.onSwipeTop();
                            }
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                SentryUtil.captureException(exception);
            }
            return true;
        }
    }

    public PlayerControlView getPlayerControlView() {
        for (int i = 0; i < this.getChildCount(); i++) {
            if (this.getChildAt(i) instanceof PlayerControlView) {
                return (PlayerControlView) getChildAt(i);
            }
        }
        return null;
    }
}
