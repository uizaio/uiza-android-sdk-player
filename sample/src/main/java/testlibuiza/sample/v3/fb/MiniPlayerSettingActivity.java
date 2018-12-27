package testlibuiza.sample.v3.fb;

import android.os.Bundle;

import testlibuiza.R;
import vn.uiza.core.base.BaseActivity;

public class MiniPlayerSettingActivity extends BaseActivity {

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
        return "TAG" + getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_fb_list_video;
    }
}
