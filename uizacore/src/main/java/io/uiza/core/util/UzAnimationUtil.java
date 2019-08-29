package io.uiza.core.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import io.uiza.core.util.constant.Constants;

public final class UzAnimationUtil {
    public interface Callback {
        void onCancel();

        void onEnd();

        void onRepeat();

        void onStart();
    }

    public static void play(View view, int duration, int repeatCount, Techniques techniques, int delayInMls, final Callback callback) {
        if (view == null) {
            return;
        }

        view.clearAnimation();
        YoYo.with(techniques)
                .duration(duration)
                .repeat(repeatCount)
                .onCancel(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        if (callback != null) {
                            callback.onCancel();
                        }
                    }
                })
                .onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        if (callback != null) {
                            callback.onEnd();
                        }
                    }
                })
                .onRepeat(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        if (callback != null) {
                            callback.onRepeat();
                        }
                    }
                })
                .onStart(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        if (callback != null) {
                            callback.onStart();
                        }
                    }
                })
                .delay(delayInMls)
                .playOn(view);
    }

    public static void play(View view, Techniques techniques) {
        play(view, 200, 1, techniques, 0, null);
    }

    public static void play(View view, Techniques techniques, int delayInMls) {
        play(view, 200, 1, techniques, delayInMls, null);
    }

    public static void play(View view, Techniques techniques, Callback callback) {
        play(view, 200, 1, techniques, 0, callback);
    }


    //This will make your View pulsate up to 1.2 its size and back, repeatedly.
    public static void pulse(View view) {
        if (view == null) {
            return;
        }
        view.clearAnimation();
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                view,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1.5f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.5f));
        scaleDown.setDuration(Constants.ANIMATION_DURATION);
        //scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatCount(2);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.start();
    }

    public static void blinking(View view) {
        if (view == null) {
            return;
        }
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        view.startAnimation(anim);
    }
}
