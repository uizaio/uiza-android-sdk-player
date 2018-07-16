package testlibuiza.sample.v3.uizavideov3;

import testlibuiza.R;
import vn.loitp.core.base.BaseActivity;

/**
 * Created by loitp on 7/16/2018.
 */

public class V3CannotSlidePlayer extends BaseActivity {
    @Override
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v3_cannot_slide_player;
    }
}
