package uizacoresdk.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import androidx.annotation.NonNull;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import uizacoresdk.R;
import vn.uiza.core.common.Constants;

public class AnimationUtils {
    private AnimationUtils() {

    }

    public interface Callback {
        void onCancel();

        void onEnd();

        void onRepeat();

        void onStart();
    }

    public static void play(@NonNull View view, int duration, int repeatCount, Techniques techniques, int delayInMls, final AnimationUtils.Callback callback) {
        view.clearAnimation();
        YoYo.with(techniques)
                .duration(duration)
                .repeat(repeatCount)
                .onCancel(animator -> {
                    if (callback != null) {
                        callback.onCancel();
                    }
                })
                .onEnd(animator -> {
                    if (callback != null) {
                        callback.onEnd();
                    }
                })
                .onRepeat(animator -> {
                    if (callback != null) {
                        callback.onRepeat();
                    }
                })
                .onStart(animator -> {
                    if (callback != null) {
                        callback.onStart();
                    }
                })
                .delay(delayInMls)
                .playOn(view);
    }

    public static void play(@NonNull View view, Techniques techniques) {
        play(view, 200, 1, techniques, 0, null);
    }

    public static void playRepeatCount(@NonNull View view, Techniques techniques, int count) {
        play(view, 200, count, techniques, 0, null);
    }

    public static void play(@NonNull View view, Techniques techniques, int delayInMls) {
        play(view, 200, 1, techniques, delayInMls, null);
    }

    public static void play(@NonNull View view, Techniques techniques, AnimationUtils.Callback callback) {
        play(view, 200, 1, techniques, 0, callback);
    }

    public static void playDuration(@NonNull View view, Techniques techniques, int duration) {
        play(view, duration, 1, techniques, 0, null);
    }

    public static void playDuration(@NonNull View view, Techniques techniques, int duration, AnimationUtils.Callback callback) {
        play(view, duration, 1, techniques, 0, callback);
    }

    public static void playRotate(@NonNull View view, Animation.AnimationListener animationListener) {
        RotateAnimation anim = new RotateAnimation(0.0f, 90.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setFillAfter(true);
        //anim.setRepeatCount(Animation.INFINITE); //Repeat animation indefinitely
        anim.setDuration(200); //Put desired duration per anim cycle here, in milliseconds
        anim.setAnimationListener(animationListener);
        view.startAnimation(anim);
    }

    public static void slideInDown(@NonNull Context context, @NonNull View view) {
        Animation slideDown = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.slide_down);
        view.startAnimation(slideDown);
    }

    public static void slideInUp(@NonNull Context context, @NonNull View view) {
        Animation slideDown = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.slide_up);
        view.startAnimation(slideDown);
    }

    //This will make your View pulsate up to 1.2 its size and back, repeatedly.
    public static void pulse(@NonNull View view) {
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
    public static void fade(@NonNull View v) {
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

    public static void blinking(@NonNull View view) {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        view.startAnimation(anim);
    }

    public static void tranIn(@NonNull Activity activity) {
        ActivityData.TransitionType typeActivityTransition = ActivityData.getInstance().getType();
        switch (typeActivityTransition) {
            case TYPE_ACTIVITY_TRANSITION_NO_ANIM:
                transActivityNoAnimation(activity);
                break;
            case TYPE_ACTIVITY_TRANSITION_SLIDE_LEFT:
                slideLeft(activity);
                break;
            case TYPE_ACTIVITY_TRANSITION_SLIDE_RIGHT:
                slideRight(activity);
                break;
            case TYPE_ACTIVITY_TRANSITION_SLIDE_DOWN:
                slideDown(activity);
                break;
            case TYPE_ACTIVITY_TRANSITION_SLIDE_UP:
                slideUp(activity);
                break;
            case TYPE_ACTIVITY_TRANSITION_FADE:
                fade(activity);
                break;
            case TYPE_ACTIVITY_TRANSITION_ZOOM:
                zoom(activity);
                break;
            case TYPE_ACTIVITY_TRANSITION_WINDMILL:
                windmill(activity);
                break;
            case TYPE_ACTIVITY_TRANSITION_DIAGONAL:
                diagonal(activity);
                break;
            case TYPE_ACTIVITY_TRANSITION_SPIN:
                spin(activity);
                break;
            default: // TYPE_ACTIVITY_TRANSITION_SYSTEM_DEFAULT
                // nothing todo
                break;
        }
    }

    public static void tranOut(@NonNull Activity activity) {
        ActivityData.TransitionType typeActivityTransition = ActivityData.getInstance().getType();
        switch (typeActivityTransition) {
            case TYPE_ACTIVITY_TRANSITION_NO_ANIM:
                transActivityNoAnimation(activity);
                break;
            case TYPE_ACTIVITY_TRANSITION_SLIDE_LEFT:
                slideRight(activity);
                break;
            case TYPE_ACTIVITY_TRANSITION_SLIDE_RIGHT:
                slideLeft(activity);
                break;
            case TYPE_ACTIVITY_TRANSITION_SLIDE_DOWN:
                slideUp(activity);
                break;
            case TYPE_ACTIVITY_TRANSITION_SLIDE_UP:
                slideDown(activity);
                break;
            case TYPE_ACTIVITY_TRANSITION_FADE:
                fade(activity);
                break;
            case TYPE_ACTIVITY_TRANSITION_ZOOM:
                zoom(activity);
                break;
            case TYPE_ACTIVITY_TRANSITION_WINDMILL:
                windmill(activity);
                break;
            case TYPE_ACTIVITY_TRANSITION_DIAGONAL:
                diagonal(activity);
                break;
            case TYPE_ACTIVITY_TRANSITION_SPIN:
                spin(activity);
            default: //TYPE_ACTIVITY_TRANSITION_SYSTEM_DEFAULT
                // nothing todo
                break;
        }
    }


    public static void transActivityNoAnimation(@NonNull Activity activity) {
        activity.overridePendingTransition(0, 0);
    }

    public static void slideLeft(@NonNull Activity activity) {
        activity.overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
    }

    public static void slideRight(@NonNull Activity activity) {
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public static void slideDown(@NonNull Activity activity) {
        activity.overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit);
    }

    public static void slideUp(@NonNull Activity activity) {
        activity.overridePendingTransition(R.anim.slide_up_enter, R.anim.slide_up_exit);
    }

    public static void zoom(@NonNull Activity activity) {
        activity.overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
    }

    public static void fade(@NonNull Activity activity) {
        activity.overridePendingTransition(R.anim.fade_enter, R.anim.fade_exit);
    }

    public static void windmill(@NonNull Activity activity) {
        activity.overridePendingTransition(R.anim.windmill_enter, R.anim.windmill_exit);
    }

    public static void spin(@NonNull Activity activity) {
        activity.overridePendingTransition(R.anim.spin_enter, R.anim.spin_exit);
    }

    public static void diagonal(@NonNull Activity activity) {
        activity.overridePendingTransition(R.anim.diagonal_right_enter, R.anim.diagonal_right_exit);
    }
}
