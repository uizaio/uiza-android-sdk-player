package vn.uiza.views.autosize;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import vn.uiza.R;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LUIUtil;

/**
 * Created by loitp on 4/19/2018.
 */

public class UZTextView extends AppCompatTextView {
    private final String TAG = getClass().getSimpleName();
    private boolean isUseDefault;
    private boolean isLandscape;

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

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UZTextView);
        isUseDefault = a.getBoolean(R.styleable.UZTextView_useDefaultTV, true);
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
