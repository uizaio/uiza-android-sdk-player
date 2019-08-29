package io.uiza.core.view.autosize;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import io.uiza.core.R;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;

public class UzTextView extends AppCompatTextView {
    private boolean isUseDefault;
    private boolean isLandscape;

    public UzTextView(Context context) {
        super(context);
    }

    public UzTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public UzTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UzTextView);
        isUseDefault = a.getBoolean(R.styleable.UzTextView_useDefaultTV, true);
        UzDisplayUtil.setTextShadow(this);
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
