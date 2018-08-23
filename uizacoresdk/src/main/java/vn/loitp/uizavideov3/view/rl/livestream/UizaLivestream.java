package vn.loitp.uizavideov3.view.rl.livestream;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import loitp.core.R;

/**
 * Created by loitp on 7/26/2017.
 */

public class UizaLivestream extends RelativeLayout {
    private final String TAG = "TAG" + getClass().getSimpleName();

    public UizaLivestream(Context context) {
        super(context);
        onCreate();
    }

    public UizaLivestream(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public UizaLivestream(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    public UizaLivestream(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
    }

    private void onCreate() {
        inflate(getContext(), R.layout.v3_uiza_livestream, this);

    }
}