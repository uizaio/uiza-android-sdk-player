package vn.uiza.core.utilities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import vn.uiza.R;
import vn.uiza.core.common.Constants;

/**
 * Created by www.muathu@gmail.com on 6/9/2017.
 */

public class LAnimationUtil {
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

    public static void playRepeatCount(View view, Techniques techniques, int count) {
        play(view, 200, count, techniques, 0, null);
    }

    public static void play(View view, Techniques techniques, int delayInMls) {
        play(view, 200, 1, techniques, delayInMls, null);
    }

    public static void play(View view, Techniques techniques, Callback callback) {
        play(view, 200, 1, techniques, 0, callback);
    }

    public static void playDuration(View view, Techniques techniques, int duration) {
        play(view, duration, 1, techniques, 0, null);
    }

    public static void playDuration(View view, Techniques techniques, int duration, Callback callback) {
        play(view, duration, 1, techniques, 0, callback);
    }

    public static void playRotate(View view, Animation.AnimationListener animationListener) {
        RotateAnimation anim = new RotateAnimation(0.0f, 90.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setFillAfter(true);
        //anim.setRepeatCount(Animation.INFINITE); //Repeat animation indefinitely
        anim.setDuration(200); //Put desired duration per anim cycle here, in milliseconds
        anim.setAnimationListener(animationListener);
        view.startAnimation(anim);
    }

    public static void slideInDown(Context context, View view) {
        Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        view.startAnimation(slideDown);
    }

    public static void slideInUp(Context context, View view) {
        Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        view.startAnimation(slideDown);
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

    //https://stackoverflow.com/questions/14156837/animation-fade-in-and-out
    public static void fade(View v) {
        if (v == null) {
            return;
        }
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(v, View.ALPHA, 1f, .3f);
        fadeOut.setDuration(1000);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(v, View.ALPHA, .3f, 1f);
        fadeIn.setDuration(1000);
        final AnimatorSet mAnimationSet = new AnimatorSet();
        mAnimationSet.play(fadeIn).after(fadeOut);
        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimationSet.start();
            }
        });
        mAnimationSet.start();
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
