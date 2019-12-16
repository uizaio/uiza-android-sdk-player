package io.uiza.live.util;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class LiveUtil {

    private LiveUtil() {
    }

    public static void blinking(View v) {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        v.startAnimation(anim);
    }
}
