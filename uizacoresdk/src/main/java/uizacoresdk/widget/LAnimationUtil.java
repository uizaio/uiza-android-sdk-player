package uizacoresdk.widget;

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
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import androidx.annotation.NonNull;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import uizacoresdk.R;
import vn.uiza.core.common.Constants;
import vn.uiza.data.ActivityData;

public class LAnimationUtil {
    private LAnimationUtil() {

    }

    public interface Callback {
        void onCancel();

        void onEnd();

        void onRepeat();

        void onStart();
    }

    public static void play(@NonNull View view, int duration, int repeatCount, Techniques techniques, int delayInMls, final LAnimationUtil.Callback callback) {
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

    public static void play(View view, Techniques techniques) {
        play(view, 200, 1, techniques, 0, null);
    }

    public static void playRepeatCount(View view, Techniques techniques, int count) {
        play(view, 200, count, techniques, 0, null);
    }

    public static void play(View view, Techniques techniques, int delayInMls) {
        play(view, 200, 1, techniques, delayInMls, null);
    }

    public static void play(View view, Techniques techniques, LAnimationUtil.Callback callback) {
        play(view, 200, 1, techniques, 0, callback);
    }

    public static void playDuration(View view, Techniques techniques, int duration) {
        play(view, duration, 1, techniques, 0, null);
    }

    public static void playDuration(View view, Techniques techniques, int duration, LAnimationUtil.Callback callback) {
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

    public static void slideInDown(@NonNull Context context,@NonNull View view) {
        Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        view.startAnimation(slideDown);
    }

    public static void slideInUp(@NonNull Context context, @NonNull View view) {
        Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_up);
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
        int typeActivityTransition = ActivityData.getInstance().getType();
        if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_NO_ANIM) {
            transActivityNoAniamtion(activity);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SYSTEM_DEFAULT) {
            //do nothing
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SLIDELEFT) {
            slideLeft(activity);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SLIDERIGHT) {
            slideRight(activity);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SLIDEDOWN) {
            slideDown(activity);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SLIDEUP) {
            slideUp(activity);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_FADE) {
            fade(activity);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_ZOOM) {
            zoom(activity);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_WINDMILL) {
            windmill(activity);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_DIAGONAL) {
            diagonal(activity);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SPIN) {
            spin(activity);
        }
    }

    public static void tranOut(@NonNull Activity activity) {
        int typeActivityTransition = ActivityData.getInstance().getType();
        if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_NO_ANIM) {
            transActivityNoAniamtion(activity);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SYSTEM_DEFAULT) {
            //do nothing
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SLIDELEFT) {
            slideRight(activity);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SLIDERIGHT) {
            slideLeft(activity);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SLIDEDOWN) {
            slideUp(activity);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SLIDEUP) {
            slideDown(activity);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_FADE) {
            fade(activity);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_ZOOM) {
            zoom(activity);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_WINDMILL) {
            windmill(activity);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_DIAGONAL) {
            diagonal(activity);
        } else if (typeActivityTransition == Constants.TYPE_ACTIVITY_TRANSITION_SPIN) {
            spin(activity);
        }
    }

    public static void transActivityNoAniamtion(@NonNull Activity activity) {
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
