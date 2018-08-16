package testlibuiza.sample.livestream;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import testlibuiza.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.uizavideov3.view.rl.livestream.UizaLivestream;

public class LiveVideoBroadcasterActivity extends BaseActivity {
    private UizaLivestream uizaLivestream;

    @Override
    protected boolean setFullScreen() {
        return true;
    }

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_live_video_broadcaster;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        uizaLivestream = (UizaLivestream) findViewById(R.id.uiza_livestream);
    }

    @Override
    protected void onDestroy() {
        uizaLivestream.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        uizaLivestream.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        uizaLivestream.onResume();
        super.onResume();
    }
}
