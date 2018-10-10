package vn.uiza.views.autosize;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

import loitp.core.R;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LDeviceUtil;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.utils.util.ConvertUtils;

/**
 * Created by loitp on 4/19/2018.
 */

public class UZImageButton extends AppCompatImageButton {
    private final String TAG = getClass().getSimpleName();

    public UZImageButton(Context context) {
        super(context);
    }

    public UZImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSizeScreenW(attrs);
    }

    public UZImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSizeScreenW(attrs);
    }

    /*public UZImageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initSizeScreenW(attrs);
    }*/
    private Drawable drawableEnabled;
    private Drawable drawableDisabled;
    private int screenWPortrait;
    private int screenWLandscape;

    private boolean isTablet;
    private boolean isUseDefault;

    public void setSrcDrawableEnabled() {
        if (drawableEnabled != null) {
            setClickable(true);
            setFocusable(true);
            setImageDrawable(drawableEnabled);
        }
        clearColorFilter();
        invalidate();
    }

    public void setSrcDrawableDisabled() {
        setClickable(false);
        setFocusable(false);
        if (drawableDisabled == null) {
            setColorFilter(Color.GRAY);
        } else {
            setImageDrawable(drawableDisabled);
            clearColorFilter();
        }
        invalidate();
    }

    private void initSizeScreenW(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UZImageButton);
        isUseDefault = a.getBoolean(R.styleable.UZImageButton_useDefaultIB, true);
        drawableDisabled = a.getDrawable(R.styleable.UZImageButton_srcDisabled);
        //LLog.d(TAG, "initSizeScreenW isUseDefault " + isUseDefault);
        if (!isUseDefault) {
            //LLog.d(TAG, "initSizeScreenW -> return");
            a.recycle();
            return;
        }
        isTablet = LDeviceUtil.isTablet(getContext());
        if (isTablet) {
            ratioLand = Constants.RATIO_LAND_TABLET;
            ratioPort = Constants.RATIO_PORTRAIT_TABLET;
        } else {
            ratioLand = Constants.RATIO_LAND_MOBILE;
            ratioPort = Constants.RATIO_PORTRAIT_MOBILE;
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
        a.recycle();
        drawableEnabled = getDrawable();
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

    private int ratioLand = 7;
    private int ratioPort = 5;

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

    private int size;

    private void updateSizePortrait() {
        if (!isUseDefault) {
            //LLog.d(TAG, "updateSizePortrait isUseDefault false -> return");
            return;
        }
        size = screenWPortrait / ratioPort;
        //LLog.d(TAG, "updateSizePortrait sizePortrait " + size);

        this.getLayoutParams().width = size;
        this.getLayoutParams().height = size;
        this.requestLayout();
    }

    private void updateSizeLandscape() {
        if (!isUseDefault) {
            //LLog.d(TAG, "updateSizeLandscape isUseDefault false -> return");
            return;
        }
        size = screenWLandscape / ratioLand;
        //LLog.d(TAG, "updateSizeLandscape sizeLandscape " + size);

        this.getLayoutParams().width = size;
        this.getLayoutParams().height = size;
        this.requestLayout();
    }

    public int getSize() {
        return size;
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
                this.setBackgroundResource(R.drawableEnabled.circle_effect);
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
            this.setBackgroundResource(R.drawableEnabled.bt_rate);
        } else {
            this.setBackgroundResource(0);
        }
    }*/

    public void setUIVisible(final boolean isVisible) {
        setClickable(isVisible);
        setFocusable(isVisible);
        if (isVisible) {
            setSrcDrawableEnabled();

            //setBackgroundColor(Color.TRANSPARENT);
            /*getLayoutParams().width = size;
            getLayoutParams().height = size;
            invalidate();*/
        } else {
            setImageResource(0);

            //setBackgroundColor(Color.RED);

            /*getLayoutParams().width = 0;
            getLayoutParams().height = 0;
            invalidate();*/
        }
    }
}
