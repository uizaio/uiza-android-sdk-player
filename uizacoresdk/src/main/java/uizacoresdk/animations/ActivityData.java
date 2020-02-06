package uizacoresdk.animations;

import uizacoresdk.util.UizaData;
import vn.uiza.core.common.Constants;

/**
 * Created by www.muathu@gmail.com on 1/4/2018.
 */

public class ActivityData {

    public enum TransitionType {
        TYPE_ACTIVITY_TRANSITION_NO_ANIM(-1),
        TYPE_ACTIVITY_TRANSITION_SYSTEM_DEFAULT(0),
        TYPE_ACTIVITY_TRANSITION_SLIDE_LEFT(1),
        TYPE_ACTIVITY_TRANSITION_SLIDE_RIGHT(2),
        TYPE_ACTIVITY_TRANSITION_SLIDE_DOWN(3),
        TYPE_ACTIVITY_TRANSITION_SLIDE_UP(4),
        TYPE_ACTIVITY_TRANSITION_FADE(5),
        TYPE_ACTIVITY_TRANSITION_ZOOM(6),
        TYPE_ACTIVITY_TRANSITION_WINDMILL(7),
        TYPE_ACTIVITY_TRANSITION_DIAGONAL(8),
        TYPE_ACTIVITY_TRANSITION_SPIN(9);

        private int value;

        private TransitionType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    // Bill Pugh Singleton Implementation
    private static class ActivityDataHelper {
        private static final ActivityData INSTANCE = new ActivityData();
    }

    public static ActivityData getInstance() {
        return ActivityDataHelper.INSTANCE;
    }

    private ActivityData() {
    }

    private TransitionType type = TransitionType.TYPE_ACTIVITY_TRANSITION_SYSTEM_DEFAULT;

    public TransitionType getType() {
        return type;
    }

    public void setType(TransitionType type) {
        this.type = type;
    }
}
