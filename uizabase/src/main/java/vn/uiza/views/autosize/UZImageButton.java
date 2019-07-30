package vn.uiza.views.autosize;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import vn.uiza.R;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LDeviceUtil;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.utils.util.ConvertUtils;

/**
 * Created by loitp on 2/27/2019.
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

    private Drawable drawableEnabled;
    private Drawable drawableDisabled;
    private int screenWPortrait;
    private int screenWLandscape;
    private boolean isUseDefault;
    private boolean isSetSrcDrawableEnabled;

    public boolean isSetSrcDrawableEnabled() {
        return isSetSrcDrawableEnabled;
    }

    public void setSrcDrawableEnabled() {
        if (drawableEnabled != null) {
            setClickable(true);
            setFocusable(true);
            setImageDrawable(drawableEnabled);
        }
        clearColorFilter();
        invalidate();
        isSetSrcDrawableEnabled = true;
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
        isSetSrcDrawableEnabled = false;
    }

    public void setSrcDrawableDisabledCanTouch() {
        setClickable(true);
        setFocusable(true);
        if (drawableDisabled == null) {
            setColorFilter(Color.GRAY);
        } else {
            setImageDrawable(drawableDisabled);
            clearColorFilter();
        }
        invalidate();
        isSetSrcDrawableEnabled = false;
    }

    private void initSizeScreenW(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UZImageButton);
        isUseDefault = a.getBoolean(R.styleable.UZImageButton_useDefaultIB, true);
        drawableDisabled = a.getDrawable(R.styleable.UZImageButton_srcDisabled);
        //disable click sound of a particular button in android app
        setSoundEffectsEnabled(false);
        if (!isUseDefault) {
            a.recycle();
            drawableEnabled = getDrawable();
            return;
        }
        boolean isTablet = LDeviceUtil.isTablet(getContext());
        if (isTablet) {
            ratioLand = Constants.RATIO_LAND_TABLET;
            ratioPort = Constants.RATIO_PORTRAIT_TABLET;
        } else {
            ratioLand = Constants.RATIO_LAND_MOBILE;
            ratioPort = Constants.RATIO_PORTRAIT_MOBILE;
        }
        screenWPortrait = LScreenUtil.getScreenWidth();
        screenWLandscape = LScreenUtil.getScreenHeightIncludeNavigationBar(this.getContext());
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
            return;
        }
        size = screenWPortrait / ratioPort;
        this.getLayoutParams().width = size;
        this.getLayoutParams().height = size;
        this.requestLayout();
    }

    private void updateSizeLandscape() {
        if (!isUseDefault) {
            return;
        }
        size = screenWLandscape / ratioLand;
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

    public void setUIVisible(final boolean isVisible) {
        setClickable(isVisible);
        setFocusable(isVisible);
        if (isVisible) {
            setSrcDrawableEnabled();
        } else {
            setImageResource(0);
        }
    }
}
