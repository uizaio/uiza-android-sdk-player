package testlibuiza.sample.v3.uizavideov3;

import android.os.Bundle;

import testlibuiza.R;
import vn.loitp.core.base.BaseActivity;

public class UizaSDKPlayerV3Activity extends BaseActivity {

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
        return R.layout.v3_player_activity;
    }
}
