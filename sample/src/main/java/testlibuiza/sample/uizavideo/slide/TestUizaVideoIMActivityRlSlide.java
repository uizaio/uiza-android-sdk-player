package testlibuiza.sample.uizavideo.slide;

import android.os.Bundle;

import testlibuiza.R;
import vn.loitp.core.base.BaseActivity;

public class TestUizaVideoIMActivityRlSlide extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        return R.layout.test_uiza_ima_video_activity_rl_slide;
    }
}
