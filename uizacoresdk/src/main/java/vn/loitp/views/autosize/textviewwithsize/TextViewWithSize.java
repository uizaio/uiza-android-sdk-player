package vn.loitp.views.autosize.textviewwithsize;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LUIUtil;

/**
 * Created by LENOVO on 4/19/2018.
 */

public class TextViewWithSize extends TextView {
    private final String TAG = getClass().getSimpleName();

    public TextViewWithSize(Context context) {
        super(context);
        //initSizeScreenW();
        init();
    }

    public TextViewWithSize(Context context, AttributeSet attrs) {
        super(context, attrs);
        //initSizeScreenW();
        init();
    }

    public TextViewWithSize(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //initSizeScreenW();
        init();
    }

    public TextViewWithSize(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //initSizeScreenW();
        init();
    }

    private void init() {
        LUIUtil.setTextShadow(this);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            int textSize = getTextSizeLand();
            //LLog.d(TAG, "textSize " + textSize);
            setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        } else {
            int textSize = getTextSizePortrait();
            //LLog.d(TAG, "textSize " + textSize);
            setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }

    private int textSizeLand = Constants.NOT_FOUND;
    private int textSizePortrait = Constants.NOT_FOUND;

    public int getTextSizeLand() {
        return textSizeLand == Constants.NOT_FOUND ? 17 : textSizeLand;
    }

    //sp
    public void setTextSizeLand(int textSizeLand) {
        this.textSizeLand = textSizeLand;
    }

    public int getTextSizePortrait() {
        return textSizePortrait == Constants.NOT_FOUND ? 12 : textSizePortrait;
    }

    //sp
    public void setTextSizePortrait(int textSizePortrait) {
        this.textSizePortrait = textSizePortrait;
    }
    /*private int screenWPortrait;
    private int screenWLandscape;

    private void initSizeScreenW() {
        screenWPortrait = LScreenUtil.getScreenWidth();
        screenWLandscape = LScreenUtil.getScreenHeightIncludeNavigationBar(this.getContext());

        int px = ConvertUtils.dp2px(5);
        setPadding(px, px, px, px);
    }*/


    //SET SIZE
    /*private boolean isFullScreen;
    private boolean isSetSize;
    private int screenWidth;
    private int size;

    public void onMeasure(int widthSpec, int heightSpec) {
        //super.onMeasure(widthSpec, heightSpec);
        if (isSetSize && isFullScreen == LScreenUtil.isFullScreen(this.getContext())) {
            LLog.d(TAG, "return isSetSize: " + isSetSize + " -> " + size + "x" + size);
            setMeasuredDimension(size, size);
            return;
        }
        isSetSize = false;
        isFullScreen = LScreenUtil.isFullScreen(this.getContext());

        LLog.d(TAG, "isFullScreen " + isFullScreen);
        if (isFullScreen) {
            screenWidth = screenWLandscape;
        } else {
            screenWidth = screenWPortrait;
        }
        LLog.d(TAG, "screenWidth " + screenWidth);
        if (isFullScreen) {
            size = screenWidth / 16;
        } else {
            size = screenWidth / 12;
        }
        LLog.d(TAG, size + "x" + size);
        setMeasuredDimension(size, size);
        isSetSize = true;
    }*/
}
