package vn.loitp.views.autosize.imagebuttonwithsize;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

import loitp.core.R;
import vn.loitp.core.common.Constants;
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

    private void initSizeScreenW() {
        screenWPortrait = LScreenUtil.getScreenWidth();
        screenWLandscape = LScreenUtil.getScreenHeightIncludeNavigationBar(this.getContext());

        int px = ConvertUtils.dp2px(5);
        setPadding(px, px, px, px);
    }

    private boolean isFullScreen;
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
    }

    private int ratioLand = Constants.RATIO_LAND;
    private int ratioPort = Constants.RATIO_PORT;

    public int getRatioLand() {
        return ratioLand;
    }

    public void setRatioLand(int ratioLand) {
        this.ratioLand = ratioLand;
        requestLayout();
        invalidate();
    }

    public int getRatioPort() {
        return ratioPort;
    }

    public void setRatioPort(int ratioPort) {
        this.ratioPort = ratioPort;
        requestLayout();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int maskedAction = event.getActionMasked();
        if (maskedAction == MotionEvent.ACTION_DOWN) {
            //setColorTint(ContextCompat.getColor(getContext(), R.color.Gray));
            this.setBackgroundResource(R.drawable.circle_effect);
        } else if (maskedAction == MotionEvent.ACTION_UP) {
            //clearColorTint();
            this.setBackgroundResource(0);
        }
        //LAnimationUtil.play(this, Techniques.Pulse);
        return super.onTouchEvent(event);
    }

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
