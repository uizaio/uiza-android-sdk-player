package io.uiza.samplelive;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Checkable;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageButton;

public class UizaMediaButton extends AppCompatImageButton implements Checkable {

    private int activeDrawableId = -1, inActiveDrawableId = -1;
    private boolean checked = false;


    public UizaMediaButton(Context context) {
        this(context, null);
    }

    public UizaMediaButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs, 0);
    }

    public UizaMediaButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs, defStyleAttr);
    }

    private void initView(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.UizaMediaButton, defStyleAttr, 0);
            activeDrawableId = a.getResourceId(R.styleable.UizaMediaButton_srcActive, -1);
            inActiveDrawableId = a.getResourceId(R.styleable.UizaMediaButton_srcInactive, -1);
        } else {
            activeDrawableId = -1;
            inActiveDrawableId = -1;
        }
        updateDrawable();
    }

    private void updateDrawable() {
        setImageDrawable(AppCompatResources.getDrawable(getContext(), checked ? activeDrawableId : inActiveDrawableId));
    }

    @Override
    public void setChecked(boolean checked) {
        if (this.checked != checked) {
            this.checked = checked;
            updateDrawable();
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        checked = !checked;
    }
}
