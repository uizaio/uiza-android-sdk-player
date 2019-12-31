package vn.uiza.views.autosize;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

import vn.uiza.R;
import vn.uiza.core.common.Constants;
import vn.uiza.utils.LUIUtil;

/**
 * Created by loitp on 4/19/2018.
 */

public class UizaTextView extends AppCompatTextView {
    private boolean isUseDefault;
    private boolean isLandscape;

    public UizaTextView(Context context) {
        super(context);
    }

    public UizaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public UizaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UizaTextView, defStyleAttr, 0);
        isUseDefault = a.getBoolean(R.styleable.UizaTextView_useDefaultTV, true);
        LUIUtil.setTextShadow(this);
        updateSize();
        setSingleLine();
        a.recycle();
    }

    private void updateSize() {
        if (!isUseDefault) {
            return;
        }
        if (isLandscape) {
            int textSize = getTextSizeLand();
            setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        } else {
            int textSize = getTextSizePortrait();
            setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        isLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        updateSize();
    }

    private int textSizeLand = Constants.NOT_FOUND;
    private int textSizePortrait = Constants.NOT_FOUND;

    public int getTextSizeLand() {
        return textSizeLand == Constants.NOT_FOUND ? 15 : textSizeLand;
    }

    //sp
    public void setTextSizeLand(int textSizeLand) {
        this.textSizeLand = textSizeLand;
    }

    public int getTextSizePortrait() {
        return textSizePortrait == Constants.NOT_FOUND ? 10 : textSizePortrait;
    }

    //sp
    public void setTextSizePortrait(int textSizePortrait) {
        this.textSizePortrait = textSizePortrait;
    }
}
