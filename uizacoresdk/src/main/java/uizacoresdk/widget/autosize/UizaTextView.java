package uizacoresdk.widget.autosize;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

import uizacoresdk.R;
import vn.uiza.core.common.Constants;

/**
 * Created by loitp on 4/19/2018.
 */

public class UizaTextView extends AppCompatTextView {
    private boolean isUseDefault;
    private boolean isLandscape;

    public UizaTextView(Context context) {
        super(context);
        init(null, 0);
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
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UizaTextView, defStyleAttr, 0);
            try {
                isUseDefault = a.getBoolean(R.styleable.UizaTextView_useDefaultTV, true);
            } finally {
                a.recycle();
            }
        } else {
            isUseDefault = true;
        }
        setShadowLayer(
                1f, // radius
                1f, // dx
                1f, // dy
                Color.BLACK // shadow color
        );
        updateSize();
        setSingleLine();
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

    private int textSizeLand = -1;
    private int textSizePortrait = -1;

    public int getTextSizeLand() {
        return textSizeLand == -1 ? 15 : textSizeLand;
    }

    //sp
    public void setTextSizeLand(int textSizeLand) {
        this.textSizeLand = textSizeLand;
    }

    public int getTextSizePortrait() {
        return textSizePortrait == -1 ? 10 : textSizePortrait;
    }

    //sp
    public void setTextSizePortrait(int textSizePortrait) {
        this.textSizePortrait = textSizePortrait;
    }
}
