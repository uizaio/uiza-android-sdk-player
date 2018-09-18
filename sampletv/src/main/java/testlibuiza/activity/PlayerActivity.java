package testlibuiza.activity;

import android.os.Bundle;

import testlibuiza.app.R;
import vn.uiza.core.base.BaseActivity;

public class PlayerActivity extends BaseActivity {
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
        return R.layout.activity_player;
    }
}
