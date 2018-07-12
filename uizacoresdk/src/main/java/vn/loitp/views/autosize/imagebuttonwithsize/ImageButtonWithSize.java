package vn.loitp.views.autosize.imagebuttonwithsize;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.ImageButton;

import vn.loitp.core.utilities.LDeviceUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.utils.util.ConvertUtils;

/**
 * Created by LENOVO on 4/19/2018.
 */

public class ImageButtonWithSize extends ImageButton {
    private final String TAG = getClass().getSimpleName();

    public ImageButtonWithSize(Context context) {
        super(context);
        initSizeScreenW();
    }

    public ImageButtonWithSize(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSizeScreenW();
    }

    public ImageButtonWithSize(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSizeScreenW();
    }

    public ImageButtonWithSize(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initSizeScreenW();
    }

    private int screenWPortrait;
    private int screenWLandscape;

    private boolean isTablet;

    private void initSizeScreenW() {
        isTablet = LDeviceUtil.isTablet(getContext());
        if (isTablet) {
            ratioLand = 24;
            ratioPort = 20;
        } else {
            ratioLand = 18;
            ratioPort = 14;
        }

        screenWPortrait = LScreenUtil.getScreenWidth();
        screenWLandscape = LScreenUtil.getScreenHeightIncludeNavigationBar(this.getContext());

        //LLog.d(TAG, ">>>screenWPortrait " + screenWPortrait);
        //LLog.d(TAG, ">>>screenWLandscape " + screenWLandscape);

        //set padding 5dp
        int px = ConvertUtils.dp2px(5);
        setPadding(px, px, px, px);

        post(new Runnable() {
            @Override
            public void run() {
                if (LScreenUtil.isFullScreen(getContext())) {
                    updateSizeLandscape();
                } else {
                    updateSizePortrait();
                }
            }
        });
    }

    /*private boolean isFullScreen;
    private boolean isSetSize;
    private int screenWidth;
    private int size;

    public void onMeasure(int widthSpec, int heightSpec) {
        //super.onMeasure(widthSpec, heightSpec);
        if (isSetSize && isFullScreen == LScreenUtil.isFullScreen(this.getContext())) {
            //LLog.d(TAG, "return isSetSize: " + isSetSize + " -> " + size + "x" + size);
            setMeasuredDimension(size, size);
            return;
        }
        isSetSize = false;
        isFullScreen = LScreenUtil.isFullScreen(this.getContext());

        //LLog.d(TAG, "isFullScreen " + isFullScreen);
        if (isFullScreen) {
            screenWidth = screenWLandscape;
        } else {
            screenWidth = screenWPortrait;
        }
        //LLog.d(TAG, "screenWidth " + screenWidth);
        if (isFullScreen) {
            size = screenWidth / getRatioLand();
        } else {
            size = screenWidth / getRatioPort();
        }
        //LLog.d(TAG, "onMeasure: " + size + "x" + size);
        setMeasuredDimension(size, size);
        isSetSize = true;
    }*/

    private int ratioLand;
    private int ratioPort;

    public int getRatioLand() {
        return ratioLand;
    }

    public void setRatioLand(int ratioLand) {
        this.ratioLand = ratioLand;
        if (LScreenUtil.isFullScreen(getContext())) {
            updateSizeLandscape();
        } else {
            updateSizePortrait();
        }
    }

    public int getRatioPort() {
        return ratioPort;
    }

    public void setRatioPort(int ratioPort) {
        this.ratioPort = ratioPort;
        if (LScreenUtil.isFullScreen(getContext())) {
            updateSizeLandscape();
        } else {
            updateSizePortrait();
        }
    }

    private void updateSizePortrait() {
        int sizePortrait = screenWPortrait / ratioPort;
        LLog.d(TAG, "updateSizePortrait sizePortrait " + sizePortrait);

        this.getLayoutParams().width = sizePortrait;
        this.getLayoutParams().height = sizePortrait;
        this.requestLayout();
    }

    private void updateSizeLandscape() {
        int sizeLandscape = screenWLandscape / ratioLand;
        LLog.d(TAG, "updateSizeLandscape sizeLandscape " + sizeLandscape);

        this.getLayoutParams().width = sizeLandscape;
        this.getLayoutParams().height = sizeLandscape;
        this.requestLayout();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            updateSizeLandscape();
        } else {
            updateSizePortrait();
        }
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isClickable()) {
            int maskedAction = event.getActionMasked();
            if (maskedAction == MotionEvent.ACTION_DOWN) {
                //setColorTint(ContextCompat.getColor(getContext(), R.color.Gray));
                this.setBackgroundResource(R.drawable.circle_effect);
            } else if (maskedAction == MotionEvent.ACTION_UP) {
                //clearColorTint();
                this.setBackgroundResource(0);
            }
            //LDeviceUtil.vibrate(getContext(), 100);
        }
        return super.onTouchEvent(event);
    }*/

    /*public void setColorTint(int color) {
        getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    public void clearColorTint() {
        getDrawable().clearColorFilter();
    }*/

    /*@Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            this.setBackgroundResource(R.drawable.bt_rate);
        } else {
            this.setBackgroundResource(0);
        }
    }*/
}
