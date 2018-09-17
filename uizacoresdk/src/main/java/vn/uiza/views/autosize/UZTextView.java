package vn.uiza.views.autosize;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import loitp.core.R;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LUIUtil;

/**
 * Created by loitp on 4/19/2018.
 */

public class UZTextView extends TextView {
    private final String TAG = getClass().getSimpleName();

    public UZTextView(Context context) {
        super(context);
    }

    public UZTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public UZTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public UZTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private boolean isUseDefault;

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UZTextView);
        isUseDefault = a.getBoolean(R.styleable.UZTextView_useDefaultTV, true);
        //LLog.d(TAG, "init isUseDefault " + isUseDefault);
        LUIUtil.setTextShadow(this);
        a.recycle();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        if (!isUseDefault) {
            //LLog.d(TAG, "onConfigurationChanged !isUseDefault -> return");
            return;
        }
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
}
